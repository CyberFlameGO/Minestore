const request = require('request');

/**
 * This function is calling a get request from Mojang's api to verify if a player
 * with the given username exists.
 * The function then calls the callback function.
 */
async function userExists(username, callback) {
  if(typeof username !== "string")
    return callback(new Error('username is not a string'));

  var unixTimestamp = new Date().getTime();

  await get('https://api.mojang.com/users/profiles/minecraft/' + encodeURIComponent(username) + '?at=' + unixTimestamp, callback);
}

async function get(url, callback) {
  request(url, { json: true }, (err, res, body) => {
    if(typeof body === 'undefined')
      return callback(new Error('body is undefined'));

    if(body.error)
      return callback(new Error(body.error + ": " + body.errorMessage));

    return callback(null, body);
  });
}

exports.userExists = userExists;
