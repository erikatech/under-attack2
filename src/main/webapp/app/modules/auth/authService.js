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
				logout: logout,
				getUser: getUser
			};

			function login(usuario){
				return ServiceAPI.post(usuario, '/login/autentica')
					.then(function (success) {
                        var usuarioLogado = success.data;
                        localStorage.setItem("token", usuarioLogado.token);
                        localStorage.setItem("login", usuarioLogado.login);
                    });
			}

			function logout(){
                localStorage.removeItem("token");
                localStorage.removeItem("login");
                location.href = '#/';
			}

			function getUser(){
				return ServiceAPI.get('/login/getUser');
			}
		}
})();
