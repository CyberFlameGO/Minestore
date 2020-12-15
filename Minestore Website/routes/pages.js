// ===============================================
// routes/pages.js is a hub we use to redirect calls
// for the different pages such as the index page
// ===============================================
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

router.get("/authenticate", async(req, res) => {
  var error = req.cookies["error"];
  res.clearCookie("error", { httpOnly: true });

  // If there is an IGN stored as a cookie, redirect the user to
  // the checkout automatically
  var ign = req.cookies["ign"];
  if(ign == null) {
    res.render("pages/authenticate", { error: error });
  } else {
    res.redirect("/checkout");
  }
});

router.get("/checkout", async(req, res) => {
  var ign = req.cookies["ign"];

  // If there is no IGN stored as a cookie, redirect the user
  // to the authenticate page
  if(ign == null) {
    res.redirect("/authenticate");
  } else {
    res.render("pages/checkout");
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
