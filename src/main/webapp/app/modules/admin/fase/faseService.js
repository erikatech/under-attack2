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

		FaseService.$inject = ['$http', '$q'];

		function FaseService ($http, $q) {
			return {
				getAll: getAll,
				atualiza: atualiza
			};

			function getAll(){
				return $http.get("http://localhost:8080/under-attack/fase")
					.then(function (response) {
						return $q.resolve(response);

					})
					.catch(function (errorResponse) {
						return $q.reject(errorResponse);

					});
			}

			function atualiza(fase){
				return $http.put("http://localhost:8080/under-attack/fase", fase)
					.then(function (response) {
						return $q.resolve(response);

					})
					.catch(function (errorResponse) {
						return $q.reject(errorResponse);

					});
			}
		}
})();
