'use strict';

	/**
	* @ngdoc function
	* @name app.route:AdminRoute
	* @description
	* # AdminRout
	* Route for the admin module
	*/

angular.module('dashboard')
	.config(['$stateProvider', function ($stateProvider) {
		$stateProvider
			.state('adminHome.dashboard', {
				templateUrl: 'app/modules/admin/dashboard/dashboard.html',
				url: "/dashboard",
				controller: 'DashboardCtrl',
				controllerAs: '$dashCtrl'
			});
	}]);
