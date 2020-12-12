// ===============================================
// routes/pages.js is a hub we use to redirect calls
// for the different pages such as the index page
// ===============================================
const express = require("express");

// ========== [Variables] ========== \\
const router = express.Router();

// ========== [Initialization] ========== \\
router.get("/", async(request, response) => {
  response.render("pages/index");
  response.end();
});

router.get("/store", async(request, response) => {
  response.render("pages/store");
  response.end();
});

module.exports = router;
