module.exports = function(app, passport) {


    app.get('/', function(req, res) {
        res.render('file', {
            message: req.flash('failureFlash')
        });
    });

    app.get('/main', isLoggedIn, function(req, res) {
        res.render('loggedIn.ejs', {
            user: req.user
        });
    });


    app.get('/logout', function(req, res) {
        req.logout();
        res.redirect('/');
    });

    app.post('/', passport.authenticate('login', {
        successRedirect: '/main',
        failureRedirect: '/',
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