// ========================================================
// handlers/databasehandler.js is the handler of our database
// =======================================================

const globalvariables = require("./globalvariables.js");
const mysql = require("mysql");
const dotenv = require("dotenv");

dotenv.config({ path: "./.env"});

var connection = mysql.createConnection({
  host:     process.env.MYSQL_HOST,
  user:     process.env.MYSQL_USER,
  password: process.env.MYSQL_PASSWORD,
  database: process.env.MYSQL_DATABASE
});

connection.connect((error) => {
  if(error) throw error;
  console.log("[Minestore Debug] Connected to the MySQL database!");

  var main_table = "CREATE TABLE IF NOT EXISTS packages (id VARCHAR(128) PRIMARY KEY, name VARCHAR(255), description VARCHAR(255), price DOUBLE(20,2), unit_limit INT)";
  connection.query(main_table, (error, result) => {
    if(error) throw error;
    console.log("[Minestore Debug] Loaded the main MySQL table!");
  });

  var purchases_table = "CREATE TABLE IF NOT EXISTS purchases (id INT AUTO_INCREMENT PRIMARY KEY, purchase_id VARCHAR(255), package_id VARCHAR(128), price DOUBLE(20,2), player_ign VARCHAR(16), player_uuid VARCHAR(36), buyer_name VARCHAR(255), buyer_email VARCHAR(255), "
                      + "buyer_address1 VARCHAR(255), buyer_address2 VARCHAR(255), buyer_city VARCHAR(255), buyer_zipcode INT(10), buyer_state VARCHAR(255), buyer_country VARCHAR(255), delivered INT(1))";
  connection.query(purchases_table, (error, result) => {
    if(error) throw error;
    console.log("[Minestore Debug] Loaded the purchases MySQL table!");
  });

  var commands_table = "CREATE TABLE IF NOT EXISTS commands (package_id VARCHAR(128), command VARCHAR(255), server VARCHAR(255))";
  connection.query(commands_table, (error, result) => {
    if(error) throw error;
    console.log("[Minestore Debug] Loaded the commands MySQL table!");
  });
});

exports.connection = connection;


exports.getpackageobject = (package_id, callback) => {
  var create_payment_json = null;

  if(!globalvariables.haspackage(package_id)) {
    const select_query = "SELECT * FROM packages WHERE id=?";
    connection.query(select_query, [package_id], (error, results) => {
      if(error) {
        return callback(new Exception("Package with the given package_id was not found!"), null);
      }

      var package_price = results[0].price;
      var package_name = results[0].name;
      var package_description = results[0].description;

      if(!package_price || !package_name || !package_description) {
        return callback(new Exception("Package with the given package_id was not found!"), null);
      }

      return callback(null, { package_name: package_name, package_price: package_price, package_description: package_description });
    });
  } else {
    return callback(null, { package_name: package_object.package_name, package_price: package_object.package_price, package_description: package_object.package_description });
  }
}
