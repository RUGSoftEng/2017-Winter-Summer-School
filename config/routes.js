module.exports = function(app, passport) {
	
	
	app.get('/', function(req, res) {
	//	connection.query('SELECT * FROM anouncement',function(err,rows){
			//if(err) throw err;
			
			req.flash('loginFail', "Invalid username or password");
			res.render('file', { message: req.flash('loginFail') });
	//	});
	});
	
	app.get('/main', isLoggedIn, function(req, res) {
        res.render('main.ejs', {
            user : req.user 
        });
    });
	
	
	app.get('/logout', function(req, res) {
        req.logout();
        res.redirect('/');
    });
    
     app.post('/', passport.authenticate('local-login', {
        successRedirect : '/main', 
        failureRedirect : '/',
        failureFlash : true     }));

};

function isLoggedIn(req, res, next) {

    if (req.isAuthenticated())
        return next();

    res.redirect('/');
}