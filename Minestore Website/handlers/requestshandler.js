// ========================================================
// handlers/requestshandler.js is the handler of api requests
// such as: authentication, communicating with the database, etc.
// =======================================================

// const database = require("../config/database");
const net = require("net");
const minecraftMinestorePort = process.env.MINECRAFT_PORT || 5001;

// ========== [Initializations] ========== \\
/*
An Async Login Method.
  req = The request the server receives
  res = The response the server gives

  * The Login Method is checking if the user exists in the center's data base.
*/
exports.authenticate = async(req, res, next) => {
  try {

    console.log(req.body);

    const { ign } = req.body;

    if(!ign || ign == 'underfiend') {
      res.cookie("error", 1, { httpOnly: true });
      res.redirect("/authenticate");
      return;
    }

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

      if(response.data.authenticated === true)
        res.redirect("/checkout");
      else {
        res.cookie("error", 2, { httpOnly: true });
        res.redirect("/authenticate");
      }

      client.end();
      return;
    });


    client.on("error", () => {
      console.log("[DEBUG] Note: Couldn't connect to the server");
      res.cookie("error", 3, { httpOnly: true });
      res.redirect("/authenticate");

      client.end();
      return;
    });

    client.on("end", () => {
      console.log("DEBUG: Disconnected from the server");
    });

    client.end();
  } catch(error) {
    console.log(error);
  }
}
