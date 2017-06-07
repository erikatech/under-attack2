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
				getItemsPocaoMagica: _getItemsPocaoMagica,
				iniciaDesafio: _iniciaDesafio,
				verificaSeDesbloqueouFase: _verificaSeDesbloqueouFase
			};

			function _getItemsPocaoMagica(){
				var config = { params: { login: localStorage.getItem("login")}};
				return ServiceAPI.get('/salaTestadores/getPocaoMagicaItems', config);
			}

            function _iniciaDesafio(){
                var requestData = { login: localStorage.getItem("login"), idDesafio: localStorage.getItem("desafioId")};
                return ServiceAPI.post(requestData, '/salaTestadores/iniciaDesafio');
            }

            function _verificaSeDesbloqueouFase(){
                var requestData = { login: localStorage.getItem("login")};
                return ServiceAPI.post(requestData, '/salaTestadores/checkIfUnblocked');
			}

		}
})();
