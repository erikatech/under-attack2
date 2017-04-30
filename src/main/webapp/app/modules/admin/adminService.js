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
		.module('admin')
		.factory('AdminService', Admin);

    	Admin.$inject = ['ServiceAPI'];

		function Admin (ServiceAPI) {
			return {
				login: login,
                getNiveis: getNiveis,
                getDificuldades: getDificuldades
			};

			function login(professor){
				return ServiceAPI.post(professor, '/login/autenticaProfessor');
			}

            function getNiveis(){
                return [
                    {value: 'FACIL', label: 'Fácil'},
                    {value: 'INTERMEDIARIO', label: 'Intermediário'},
                    {value: 'DIFICIL', label: 'Difícil'}
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
		}
})();
