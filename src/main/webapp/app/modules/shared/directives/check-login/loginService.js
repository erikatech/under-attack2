(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:loginService
	 * @description
	 * # loginService
	 * Service of the app
	 */

  	angular
		.module('login-validator')
		.factory('loginService', Login);

		Login.$inject = ['$http', '$q'];

		function Login ($http, $q) {
			return {
				checkLoginAvailability: checkLoginAvailability
			};

			/**
			 * Serviço que checa se o login do usuário está disponível
			 * @param endPoint
			 * @param login
			 * @returns {*}
			 */
			function checkLoginAvailability(endPoint, login){
				var finalEndpoint = endPoint + "?login="+login;
				return $http.get(finalEndpoint)
					.then(function (response) {
						return $q.resolve(response);
					})
					.catch(function (errorResponse) {
						return $q.reject(errorResponse);
					})
			}
		}
})();
