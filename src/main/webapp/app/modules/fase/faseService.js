(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:FaseService
	 * @description
	 * # FaseService
	 * Service of the app
	 */

  	angular
		.module('under-attack')
		.factory('FaseService', FaseService);

		FaseService.$inject = ['ServiceAPI'];

		function FaseService (ServiceAPI) {

			return {
				getDesafio: _getDesafio
			};

			function _getDesafio(idDesafio){
				var config = {params: {idDesafio: idDesafio}};
				return ServiceAPI.get('/fase/getDesafio', config);
			}

		}
})();
