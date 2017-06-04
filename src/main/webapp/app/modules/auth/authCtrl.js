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

		AuthCtrl.$inject = ['authService', '$state', 'CustomToastService'];

		function AuthCtrl(authService, $state, CustomToastService) {
			var context = this;

			context.usuario = {login: null, senha: null};
			context.register = register;
			context.authenticate = authenticate;

            /**
			 * Autentica um usuário
             */
			function authenticate(){
				authService.login(context.usuario)
					.then(function () {
						$state.go('authenticated.home');
					})
					.catch(function (errorResponse) {
                        CustomToastService.show(errorResponse.data.errors[0].message, "top right", 2000);
					});
			}

            /**
			 * Manda pro estado de registro
             */
			function register(){
				$state.go("register");
			}

		}
})();
