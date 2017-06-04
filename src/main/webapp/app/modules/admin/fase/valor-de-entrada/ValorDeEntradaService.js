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
		.factory('ValorDeEntradaService', valorDeEntradaService);

		function valorDeEntradaService () {

			return {
				getTipos: getTipos,
				getTiposClasses: getTiposClasses
			};

			function getTipos(){
                return [
                    {value: "DISTRATIVO", label: "Distrativo"},
                    {value: "CORRETO", label: "Correto"}
                ];
			}

			function getTiposClasses(){
				return [
                    {value: 'VALIDA', label: "Válida"},
                    {value: 'INVALIDA', label: "Inválida"}
                ];
			}

		}
})();
