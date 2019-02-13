app.config(function($routeProvider) {
			// route for the home page
			console.log('In route');
			$routeProvider.when('/', {
                templateUrl : 'templates/home.html',
                controller  : 'pdCtrl',
            }).when('/edit/:orgId', {
                templateUrl : 'templates/edit.html',
                controller  : 'editCtrl',
            }).when('/create', {
                templateUrl : 'templates/create.html',
                controller  : 'createCtrl',
            }).when('/nearest', {
                templateUrl : 'templates/nearest.html',
                controller  : 'nearestCtrl',
            })
			.otherwise({
				redirectTo : '/',
			});
});

