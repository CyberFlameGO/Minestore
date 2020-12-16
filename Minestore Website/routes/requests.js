// ========================================================
// routes/requests.js is a hub we use to handle requests
// such as: authentication, giving packages in game, etc.
// =======================================================
const express = require("express");
const requestHandler = require("../handlers/requestshandler");

// ========== [Variables] ========== \\
const router = express.Router();

// ========== [Initialization] ========== \\
router.post("/authenticate", requestHandler.authenticate);
router.post("/logout", requestHandler.logout);
router.post("/savecart", requestHandler.savecart);
router.post("/checkout", requestHandler.checkout);
router.get("/confirmpurchase", requestHandler.confirmpurchase);
// router.post("/givepackage", requestHandler.givepackage);

module.exports = router;
