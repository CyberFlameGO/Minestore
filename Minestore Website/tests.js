var net = require("net");

var client = net.connect(5001, "localhost");

var request = {
  "protocol_version":1,
  "request_id":2,
  "data": {
    "player_name":"LielAmar"
  }
}

client.write(JSON.stringify(request));
console.log("CLIENT: " + JSON.stringify(request))

client.on("data", (data) => {
  console.log("SERVER: " + data.toString());
});

client.on("end", () => {
  console.log("DEBUG: Disconnected from the server");
});

client.end();
// const httphandler = require('./handlers/httphandler.js');
//
// httphandler.isUserExists("LielAmar", (error, body) => {
//   if(error == null) {
//     console.log("User exists!");
//     return true;
//   }
//   console.log("User doesn't exists!");
//   return false;
// });
