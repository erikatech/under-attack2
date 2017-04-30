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
		.factory('faseService', FaseService);

		FaseService.$inject = ['ServiceAPI'];

		function FaseService (ServiceAPI) {

			return {
				getAll: getAll,
                getNiveis: getNiveis,
				getTipos: getTipos,
				getDificuldades: getDificuldades,
				getTiposClasses: getTiposClasses,
				getIngredientes: getIngredientes
			};

			function getAll(){
                return ServiceAPI.get('/fase/listAll');
			}

			function getNiveis(){
                return [
                    {value: 'FACIL', label: 'Fácil'},
                    {value: 'INTERMEDIARIO', label: 'Intermediário'},
                    {value: 'DIFICIL', label: 'Difícil'}
                ];
			}

			function getTipos(){
                return [
                    {value: "DISTRATIVO", label: "Distrativo"},
                    {value: "CORRETO", label: "Correto"}
                ];
			}

			function getDificuldades(){
                return [
                    {value: "FACIL", label: "Fácil"},
                    {value: "NORMAL", label: "Normal"},
                    {value: "DIFICIL", label: "Fácil"},
                    {value: "CHUCK_NORRIS", label: "Chuck Norris"}
                ];
			}

			function getTiposClasses(){
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
