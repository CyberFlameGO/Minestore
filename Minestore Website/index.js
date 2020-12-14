const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');

// ========== [Variables] ========== \\
const app = express();

const publicDir = '/public';
const viewsDir = '/views';

// ========== [Set-ups] ========== \\
// Setting Static Files
app.use(express.static(path.join(__dirname, publicDir)));
app.use('/assets', express.static(path.join(__dirname, publicDir, '/assets')));
app.use('/css', express.static(path.join(__dirname, publicDir, '/css')));
app.use('/js', express.static(path.join(__dirname, publicDir, '/js')));

// Setting View Engine
app.set("view engine", "ejs");
app.set('views', path.join(__dirname, viewsDir));

// Other stuff
app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(cookieParser());

// Setting Routes
app.use("/", require("./routes/pages"));
app.use("/requests", require("./routes/requests"));

// ========== [Initialization] ========== \\
// Initializing the web server
const port = process.env.PORT || 5000;
app.listen(port, () => console.log(`Web Server is running on port ${port}`));
