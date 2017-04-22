(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:authService
	 * @description
	 * # authService
	 * Service responsible for authenticate the user
	 */

  	angular
		.module('auth')
		.factory('authService', Auth);

		Auth.$inject = ['ServiceAPI'];

		function Auth (ServiceAPI) {

			return {
				login: login,
                lista: lista
			};

			function login(usuario){
				return ServiceAPI.post(usuario, '/login/autentica');
			}

			function lista(){
                return ServiceAPI.get('/login/listaFases');
			}
		}
})();
