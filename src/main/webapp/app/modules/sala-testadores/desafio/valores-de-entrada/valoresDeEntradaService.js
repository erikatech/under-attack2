(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:ValoresDeEntradaService
	 * @description
	 * # ValoresDeEntradaService
	 * Service of the module sala-testadores
	 */

  	angular
		.module('sala-testadores')
		.factory('ValoresDeEntradaService', ValoresDeEntradaService);

    	ValoresDeEntradaService.$inject = ['ServiceAPI'];

		function ValoresDeEntradaService (ServiceAPI) {
			return {
				iniciarDesafio: _iniciarDesafio
			};

			function _iniciarDesafio(){
				var requestData = { login: localStorage.getItem("login"), idDesafio: localStorage.getItem("desafioId")};
				return ServiceAPI.post(requestData, '/salaTestadores/iniciaDesafio');
			}

		}
})();
