(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:authCtrl
	* @description
	* # authCtrl
	* Controller of the app
	*/

  	angular
		.module('auth')
		.controller('AuthCtrl', AuthCtrl);

		AuthCtrl.$inject = ['authService', '$state'];

		function AuthCtrl(authService, $state) {
			var context = this;

			context.newStudent = {login: null, password: null};
			context.register = register;
			context.login = login;
			context.adminMode = adminMode;

			function login(){
				authService.login("erika", "erika")
					.then(function (response) {
						console.log("Success >>> ", response);
						$state.go('authenticated.home.dashboard');
					})
					.catch(function (errorResponse) {
						console.log(errorResponse.data.errors[0].message);
					});
			}

			function register(){
				authService.register(context.newStudent)
					.then(function (successResponse) {
						console.log("Register goes ok! ", successResponse);
					})
					.catch(function(errorResponse){
						console.error("Register gone wrong! ", errorResponse);
					});
			}

			function adminMode(){
				$state.go('adminHome.login');
			}

		}
})();
