var http = require('http');  
var fs = require('fs');
var url = require('url');
var express = require('express');
var passport = require('passport');
var app = express();
var flash    = require('connect-flash');
var morgan       = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser   = require('body-parser');
var session      = require('express-session');


require('./config/passport')(passport);

app.set('view engine', 'ejs');
app.use(morgan('dev'));
app.use(cookieParser());
app.use(session({ secret: 'summerwinter' }));
app.use(passport.initialize());
app.use(passport.session());
app.use(flash());

require('./config/routes.js')(app, passport);


app.listen(8080);
console.log('8080 is the magic port');