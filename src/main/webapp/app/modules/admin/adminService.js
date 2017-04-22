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
		.module('admin')
		.factory('AdminService', Admin);

    	Admin.$inject = ['ServiceAPI'];

		function Admin (ServiceAPI) {
			return {
				login: login
			};

			function login(professor){
				return ServiceAPI.post(professor, '/login/autenticaProfessor');
			}
		}
})();
