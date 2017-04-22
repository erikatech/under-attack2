(function () {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:AdminCtrl
	* @description
	* # AdminCtrl
	* Controller of the app
	*/

	angular
		.module('admin')
		.controller('AdminCtrl', Admin);

	Admin.$inject = ['adminService', 'CustomToastService', '$state'];

	function Admin(adminService, CustomToastService, $state) {
		var context = this;

		context.newProfessor = {login: null, senha: null};
		context.professor = {login: null, senha: null};
		context.endPoint = "http://localhost:8080/under-attack/professor";

		context.register = register;
		context.login = login;

		function register(){
			adminService.register(context.newProfessor)
				.then(function () {
					CustomToastService.show("Professor cadastrado", "top right", 2000);
					context.newProfessor = {login: null, senha: null };
				})
				.catch(function (errorResponse) {
					CustomToastService.show("Erro ao cadastrar professor", "top right", 2000);
					console.error("Register gone wrong >>> ", errorResponse);
				})
		}
		function login(){
			adminService.login(context.professor)
				.then(function(){
					$state.go('adminHome.dashboard');
				})
				.catch(function (errorResponse) {
					console.log("[ADMIN LOGIN] >>> ",errorResponse);

				});
		}
	}
})();
