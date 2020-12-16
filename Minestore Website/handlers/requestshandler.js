// ========================================================
// handlers/requestshandler.js is the handler of api requests
// such as: authentication, communicating with the database, etc.
// =======================================================

const databasehandler = require('./databasehandler.js');
const httphandler     = require('./httphandler.js');
const paypal          = require('paypal-rest-sdk');
const dotenv          = require('dotenv');
const net             = require("net");

dotenv.config({ path: "./.env"});
const minecraftMinestorePort = process.env.MINECRAFT_PORT || 1804;

paypal.configure({
  'mode': 'sandbox',
  'client_id': 'AQjowo7MNT3jJT_VM-060xIBpfgWAGtVhSdBrM4NGOfw9Y9o_P9Xm7TtEZtYr35YUeH7UjMDmIHZqSWh',
  'client_secret': 'EI8Knuk9jRVtAvC5frls_MWl21MAakkMYEikQjCSmtS6Sq34SKVhmBUiY_NJYOkXKr6SCrchx5zfdUks'
});

/**
 * An async method for communicating with the Minecraft Server.
 *
 * This method is receiving an In-Game name and sending a request to the
 * Minecraft server, for the matching player to authenticate their purchase
 * Once a response is received, we forward the user to the correct page.
 */
exports.authenticate = async(req, res, next) => {
  try {
    const { ign } = req.body;

    if(!ign || ign == 'underfiend') {
      res.cookie("error", 1, { httpOnly: true });
      res.redirect("/authenticate");
      return;
    }

    httphandler.userExists(ign, (error, body) => {
      if(error != null) {
        res.cookie("error", 2, { httpOnly: true });
        res.redirect("/authenticate");
      } else {
        const client = net.connect(minecraftMinestorePort, "localhost");
        var request = {
          "protocol_version":1,
          "request_id":0,
          "data": {
            "player_name":ign,
            "player_uuid":body.id
          }
        };

        client.write(JSON.stringify(request));

        client.on("data", (data) => {
          var response = JSON.parse(data.toString());

          if(response.data.authenticated === true) {
            res.cookie("player", { ign: ign, uuid: body.id }, { expires: new Date(Date.now() + 900000), httpOnly: true });
            res.redirect("/checkout");
          } else {
            res.cookie("error", 3, { httpOnly: true });
            res.redirect("/authenticate");
          }

          client.end();
          return;
        });

        client.on("error", () => {
          console.log("[Minestore Debug] Couldn't connect to the server");
          res.cookie("error", 4, { httpOnly: true });
          res.redirect("/authenticate");

          client.end();
          return;
        });

        client.on("end", () => {
          console.log("[Minestore Debug] Disconnected from the server");
        });

        client.end();
      }
    });
  } catch(error) {
    console.log(error);
  }
}

/**
 * An async method for logging out (removing the stored ign).
 */
exports.logout = async(req, res) => {
  res.clearCookie("player", { httpOnly: true });
  res.redirect('/authenticate');
}

/**
 * An async method for redirecting to checkout.
 *
 * This method is receiving a package and saves it as a cookie.
 * Then, it redirects the user to the authenticate page
 */
exports.savecart = async(req, res, next) => {
  try {
    const { package_id } = req.body;

    if(!package_id || package_id == 'underfiend') {
      res.redirect("/");
      return;
    }

    res.cookie("package_id", package_id, { expires: new Date(Date.now() + 900000), httpOnly: true });
    res.redirect("/authenticate");
  } catch(error) {
    console.log(error);
  }
}

/**
 * An async method for checking a user out. Building an object of the package
 * and redirecting the user to the paypal checkout page.
 */
exports.checkout = async(req, res, next) => {
  try {
    const { package_id, full_name, email_address, address_1, address_2, city, zip_code, state, country } = req.body;

    var player_ign = req.cookies["player"];

    if(!package_id || !full_name || !email_address || !address_1 || !address_2 || !city || !zip_code || !state || !country || !player_ign) {
      res.cookie("error", 1, { httpOnly: true });
      res.redirect("/checkout");
      return;
    }

    databasehandler.getpackageobject(package_id, (error, package_object) => {
      if(error) {
        res.cookie("error", 5, { httpOnly: true });
        res.redirect("/checkout");
        throw error;
      }

      var create_payment_json = createPaymentJson(package_object.package_name, package_object.package_price, package_object.package_description);

      paypal.payment.create(create_payment_json, (error, payment) => {
        if(error) {
          res.cookie("error", 5, { httpOnly: true });
          res.redirect("/checkout");
          throw error;
        }

        // create an encrypted cookie with the id.
        res.cookie("purchase_information", { payment: payment, package_id: package_id, package_price: package_object.package_price, player_ign: player_ign.ign, buyer_name: full_name, buyer_email: email_address,
                                             buyer_address1: address_1, buyer_address2: address_2, buyer_city: city, buyer_zipcode: zip_code, buyer_state: state, buyer_country: country },
                                           { expires: new Date(Date.now() + 900000), httpOnly: true });
        res.redirect(payment.links[1].href);
      });
    });
  } catch(error) {
    console.log(error);
  }
}

/**
 * Creating a json object from the package_name, price and description variables, to
 * send to paypal.
 */
function createPaymentJson(package_name, package_price, package_description) {
  return {
    "intent": "sale",
    "payer": {
        "payment_method": "paypal"
    },
    "redirect_urls": {
        "return_url": process.env.BASE_URL + "/requests/confirmpurchase",
        "cancel_url": process.env.BASE_URL + "/requests/cancelpurchase" // REMEMBER TO DESTORY ALL COOKIES WHEN CALLING THIS
    },
    "transactions": [{
        "item_list": {
            "items": [{
                "name": package_name,
                "sku": package_name,
                "price": package_price,
                "currency": "USD",
                "quantity": 1
            }]
        },
        "amount": {
            "currency": "USD",
            "total": package_price
        },
        "description": package_description
    }]
  };
}

/**
 * An async method for confirming a purchase. Inserting the data into the database and then sending
 * a socket to the minecraft server, to give the player their items.
 */
exports.confirmpurchase = async(req, res, next) => {
  try {
    var purchase = req.cookies["purchase_information"];

    const insert_query = "INSERT INTO purchases (payment_id, package_id, price, player_ign, buyer_name, buyer_email, buyer_address1, buyer_address2, buyer_city, buyer_zipcode, buyer_state, buyer_country,delivered) "
                       + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    databasehandler.connection.query(insert_query, [purchase.payment.id, purchase.package_id, purchase.package_price, purchase.player_ign, purchase.buyer_name, purchase.buyer_email,
                                                    purchase.buyer_address1, purchase.buyer_address2, purchase.buyer_city, purchase.buyer_zipcode, purchase.buyer_state, purchase.buyer_country, 0],
                                                    (error, result) => {
      if(error) throw error;
      console.log("[Minestore Debug] A new purchase inserted to the database! Purchase Id: " + result.insertId + " of IGN: " + purchase.player_ign + " bought " + purchase.package_id);
      // TODO: send to the minecraft server with the payment_id

      res.clearCookie("purchase_information");
      res.clearCookie("package");
      res.clearCookie("player");

      const client = net.connect(minecraftMinestorePort, "localhost");
      var request = {
        "protocol_version":1,
        "request_id":1,
        "data": {
          "purchase_id":purchase.payment.id
        }
      };

      client.write(JSON.stringify(request));

      client.on("error", () => {
        console.log("[Minestore Debug] Couldn't connect to the server");
        res.cookie("error", 4, { httpOnly: true });
        res.redirect("/checkout");

        client.end();
        return;
      });

      client.on("end", () => {
        console.log("[Minestore Debug] Disconnected from the server");
        res.redirect("/thankyou");

        client.end();
        return;
      });

      client.end();
    });
  } catch(error) {
    console.log(error);
  }
}
