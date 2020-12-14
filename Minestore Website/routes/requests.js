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
// router.post("/givepackage", requestHandler.givepackage);

module.exports = router;
