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
		.module('admin-fase')
		.factory('FaseService', FaseService);

		FaseService.$inject = ['ServiceAPI'];

		function FaseService (ServiceAPI) {

			return {
				getAll: getAll,
                updateFase: _updateFase
			};

			function getAll(){
                return ServiceAPI.get('/fase/listAll');
			}

			function _updateFase(fase){
				return ServiceAPI.put(fase, '/fase/updateFase');
			}

		}
})();
