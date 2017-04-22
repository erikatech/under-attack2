(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:authService
	 * @description
	 * # authService
	 * Service of the app
	 */

  	angular
		.module('auth')
		.factory('authService', Auth);

		Auth.$inject = ['$http', '$q'];

		function Auth ($http, $q) {
			return {
				login: function(login, senha){
					var data = {login: login, senha: senha};
					return $http.post("http://localhost:8080/under-attack/professor", data)
						.then(function (response) {
							return $q.resolve(response);

						})
						.catch(function (errorResponse) {
							return $q.reject(errorResponse);

						})
				}
			}
		}
})();
