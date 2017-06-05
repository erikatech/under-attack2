(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:SalaTestadoresService
	 * @description
	 * # SalaTestadoresService
	 * Service of the module sala-testadores
	 */

  	angular
		.module('sala-testadores')
		.factory('SalaTestadoresService', SalaTestadores);

    	SalaTestadores.$inject = ['ServiceAPI'];

		function SalaTestadores (ServiceAPI) {

			return {
				getItemsPocaoMagica: _getItemsPocaoMagica
			};

			function _getItemsPocaoMagica(){
				var config = { params: { login: localStorage.getItem("login")}};
				return ServiceAPI.get('/salaTestadores/getPocaoMagicaItems', config);
			}

		}
})();
