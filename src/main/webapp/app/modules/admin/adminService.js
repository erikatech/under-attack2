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
		.factory('adminService', AdminService);

		AdminService.$inject = ['$http', '$q'];

		function AdminService ($http, $q) {
			return {
				login: login,
				register: register
			};

			function login(professor){
				return $http.post("http://localhost:8080/under-attack/professor", professor)
					.then(function (response) {
						return $q.resolve(response);

					})
					.catch(function (errorResponse) {
						return $q.reject(errorResponse);

					})
			}

			function register(professor){
				return $http.post("http://localhost:8080/under-attack/professor/register", professor)
					.then(function (response) {
						return $q.resolve(response);

					})
					.catch(function (errorResponse) {
						return $q.reject(errorResponse);

					})
			}
		}
})();
