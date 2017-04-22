(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:adminService
	 * @description
	 * # adminService
	 * Service of the app
	 */

  	angular
		.module('dashboard')
		.factory('dashboardService', DashboardService);

	DashboardService.$inject = ['$http', '$q'];

		function DashboardService ($http, $q) {
			return {



			};
		}
})();
