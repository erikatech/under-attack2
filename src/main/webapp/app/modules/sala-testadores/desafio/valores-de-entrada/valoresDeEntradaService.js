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
                validateValorDeEntrada: _validateValorDeEntrada,
				goToNextStage: _gotoNextStage,
				getValoresEncontados: _getValoresEncontrados
			};


            function _validateValorDeEntrada(idValorDeEntrada, idDesafio){
                var config = { params: {idValorDeEntrada: idValorDeEntrada, login: localStorage.getItem("login"),
                    desafioId: idDesafio}};
                return ServiceAPI.get('/salaTestadores/isValorValido', config);
            }

			function _gotoNextStage(valoresSelecionados){
                var requestData = { login: localStorage.getItem("login"), valoresSelecionados: valoresSelecionados};
                return ServiceAPI.post(requestData, '/salaTestadores/goToNextStage');
			}

			function _getValoresEncontrados(){
                var config = { params: {login: localStorage.getItem("login")}};
                return ServiceAPI.get('/salaTestadores/getValoresAluno', config);
			}

		}
})();
