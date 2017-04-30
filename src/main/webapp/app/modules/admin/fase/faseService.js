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
		.module('fase')
		.factory('FaseService', FaseService);

		FaseService.$inject = ['ServiceAPI'];

		function FaseService (ServiceAPI) {

			return {
				getAll: getAll
			};

			function getAll(){
                return ServiceAPI.get('/fase/listAll');
			}

		}
})();
