// ===============================================
// routes/pages.js is a hub we use to redirect calls
// for the different pages such as the index page
// ===============================================
const databasehandler = require("../handlers/databasehandler.js");
const express = require("express");

// ========== [Variables] ========== \\
const router = express.Router();

// ========== [Initialization] ========== \\
router.get("/", async(req, res) => {
  res.render("pages/index");
});

router.get("/support", async(req, res) => {
  res.render("pages/support");
});

router.get("/thankyou", async(req, res) => {
  res.render("pages/thankyou");
});

router.get("/authenticate", async(req, res) => {
  var error = req.cookies["error"];
  res.clearCookie("error", { httpOnly: true });

  // If there is an IGN stored as a cookie, redirect the user to
  // the checkout automatically
  var player = req.cookies["player"];
  if(player == null) {
    res.render("pages/authenticate", { error: error });
  } else {
    res.redirect("/checkout");
  }
});

router.get("/checkout", async(req, res) => {
  var error = req.cookies["error"];
  res.clearCookie("error", { httpOnly: true });

  var package_id = req.cookies["package_id"];

  // Connect to database with package.name
  // Recreate a package object from the given package_name
  // package_id | package_name | description | price | commands in game

  var player = req.cookies["player"];

  // If there is no IGN stored as a cookie, redirect the user
  // to the authenticate page
  if(player == null) {
    res.redirect("/authenticate");
  } else if(package_id == null) {
    res.redirect("/");
  } else {
    var package_object = databasehandler.getpackageobject(package_id, (error, package_object) => {
      if(error) {
        res.redirect("/");
        throw error;
      }

      var new_package = { package_id: package_id, package_name: package_object.package_name, package_description: package_object.package_description, package_price: package_object.package_price }

      res.render("pages/checkout", { error: error, player: player, package: new_package });
    });
  }
});

router.get("/category/ranks", async(req, res) => {
  res.render("pages/category/ranks");
});

router.get("/category/coins", async(req, res) => {
  res.render("pages/category/coins");
});

router.get("/category/pets", async(req, res) => {
  res.render("pages/category/pets");
});

module.exports = router;
