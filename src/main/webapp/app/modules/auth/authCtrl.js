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

			context.usuario = {login: null, senha: null};
			context.register = register;
			context.authenticate = authenticate;
			context.adminMode = adminMode;

            /**
			 * Autentica um usuário
             */
			function authenticate(){
				authService.login(context.usuario)
					.then(function (response) {
						console.log("Success >>> ", response);
						// $state.go('authenticated.home.dashboard');
					})
					.catch(function (errorResponse) {
						console.log(errorResponse.data.errors[0].message);
					});
			}

            /**
			 * Manda pro estado de registro
             */
			function register(){
				$state.go("register");
			}

			function adminMode(){
				$state.go("login");
			}

		}
})();
