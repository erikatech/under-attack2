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
                updateFase: _updateFase,
				getDesafio: _getDesafio
			};

			function getAll(){
                return ServiceAPI.get('/fase/listAll');
			}

			function _updateFase(fase){
				return ServiceAPI.put(fase, '/fase/updateFase');
			}

			function _getDesafio(idDesafio){
				var config = {params: {idDesafio: idDesafio}};
				return ServiceAPI.get('/fase/getDesafio', config);
			}

		}
})();
