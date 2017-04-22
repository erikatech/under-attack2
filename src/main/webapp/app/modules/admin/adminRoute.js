'use strict';

	/**
	* @ngdoc function
	* @name app.route:AdminRoute
	* @description
	* # AdminRout
	* Route for the admin module
	*/

angular.module('admin')
	.config(['$stateProvider', function ($stateProvider) {
		$stateProvider
			.state('authenticated.adminHome', {
				url: '',
				abstract: true,
				templateUrl: 'app/modules/admin/admin-home.html'
			})
			.state('login', {
				templateUrl: 'app/modules/admin/admin.html',
				url: "/admin",
				controller: 'AdminCtrl',
				controllerAs: '$adminCtrl'
			})
	}]);
