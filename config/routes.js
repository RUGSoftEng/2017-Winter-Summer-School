module.exports = function(app, passport) {
var mongojs = require('mongojs')
//this is the database connected to the app the first part of the parenthesis
//is the mongodb database and the second part the collections you use
var db = mongojs('mongodb://admin:summerwinter@ds119370.mlab.com:19370/summerwinter',['announcements','generalinfo'])



    app.get('/', function(req, res) {
        if(req.isAuthenticated()) {
	   		res.redirect('/main');
   		} else {
	        res.render('file', {
	            message: req.flash('error')
	        });
        }
    });

    app.get('/main', isLoggedIn, function(req, res) {
        db.announcements.find(function (err,docs){
          db.generalinfo.find(function(err,docs2){
            res.render('loggedIn.ejs', {
                user: req.user,
                announcements: docs,
                generalinfo: docs2
          })
          });
        });
    });


    app.get('/logout', function(req, res) {
        req.logout();
        res.redirect('/');
    });

    app.post('/', passport.authenticate('login', {
        successRedirect: '/main',
        failureRedirect: '/',
        badRequestMessage : 'Invalid username or password',
        failureFlash: true
    }));

    // sends 404 for random urls
    app.use(function(req, res) {
        res.render('notFound.ejs');
    });

};

function isLoggedIn(req, res, next) {

    if (req.isAuthenticated())
        return next();

    res.redirect('/');
}
