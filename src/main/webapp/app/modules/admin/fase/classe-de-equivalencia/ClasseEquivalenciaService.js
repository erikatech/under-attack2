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
		.factory('ClasseEquivalenciaService', classeEquivalenciaService);

  		classeEquivalenciaService().$inject = ['ServiceAPI'];

		function classeEquivalenciaService (ServiceAPI) {

			return {
				getTipos: getTipos,
                getIngredientes: getIngredientes
			};

			function getTipos(){
				return [
                    {value: 'VALIDA', label: "Válida"},
                    {value: 'INVALIDA', label: "Inválida"}
                ];
			}

            function getIngredientes(){
                return ServiceAPI.get("/fase/listIngredientes");
            }
		}
})();
