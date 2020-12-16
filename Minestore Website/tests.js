const tls = require('tls');
const fs = require('fs');

const options = {
    key: fs.readFileSync('key.pem'),
    cert: fs.readFileSync('cert.pem'),
    host: 'localhost',
    port: 1804,
    rejectUnauthorized:true,
    requestCert:true
};

const socket = tls.connect(options, () => {
    console.log('client connected',
        socket.authorized ? 'authorized' : 'unauthorized');
    process.stdin.pipe(socket);
    process.stdin.resume();
});

socket.setEncoding('utf8');

socket.on('data', (data) => {
    console.log(data);
});

socket.on('error', (error) => {
    console.log(error);
});

socket.on('end', (data) => {
    console.log('Socket end event');
});

// var client = net.connect(5001, "localhost");
//
// var request = {
//   "protocol_version":1,
//   "request_id":2,
//   "data": {
//     "player_name":"LielAmar"
//   }
// }
//
// client.write(JSON.stringify(request));
// console.log("CLIENT: " + JSON.stringify(request))
//
// client.on("data", (data) => {
//   console.log("SERVER: " + data.toString());
// });
//
// client.on("end", () => {
//   console.log("DEBUG: Disconnected from the server");
// });
//
// client.end();
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
