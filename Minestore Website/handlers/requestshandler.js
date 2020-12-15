// ========================================================
// handlers/requestshandler.js is the handler of api requests
// such as: authentication, communicating with the database, etc.
// =======================================================

const net = require("net");
const httphandler = require('./httphandler.js');
const dotenv = require('dotenv');

dotenv.config({ path: "./.env"});
const minecraftMinestorePort = process.env.MINECRAFT_PORT || 1804;

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

    httphandler.isUserExists(ign, (error, body) => {
      if(error != null) {
        res.cookie("error", 2, { httpOnly: true });
        res.redirect("/authenticate");

      } else {
        const client = net.connect(minecraftMinestorePort, "localhost");
        var request = {
          "protocol_version":1,
          "request_id":2,
          "data": {
            "player_name":ign
          }
        };

        client.write(JSON.stringify(request));
        console.log("CLIENT: " + JSON.stringify(request))

        client.on("data", (data) => {
          console.log("SERVER: " + data.toString());
          var response = JSON.parse(data.toString());

          if(response.data.authenticated === true) {
            res.cookie("ign", ign, { expires: new Date(Date.now() + 900000), httpOnly: true });
            res.redirect("/checkout");
          } else {
            res.cookie("error", 3, { httpOnly: true });
            res.redirect("/authenticate");
          }

          client.end();
          return;
        });


        client.on("error", () => {
          console.log("[DEBUG] Note: Couldn't connect to the server");
          res.cookie("error", 4, { httpOnly: true });
          res.redirect("/authenticate");

          client.end();
          return;
        });

        client.on("end", () => {
          console.log("DEBUG: Disconnected from the server");
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
  res.clearCookie("ign", { httpOnly: true });
  res.redirect('/authenticate');
}

/**
 * An async method for redirecting to checkout.
 *
 * This method is receiving a package and saves it as a cookie.
 * Then, it redirects the user to the authenticate page
 */
exports.buyitem = async(req, res, next) => {
  try {
    const { package } = req.body;

    if(!package || package == 'underfiend') {
      res.redirect("/");
      return;
    }

    console.log("package: " + package);

    res.cookie("package", package, { expires: new Date(Date.now() + 900000), httpOnly: true });
    res.redirect("/authenticate");
  } catch(error) {
    console.log(error);
  }
}
