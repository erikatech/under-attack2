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

	Admin.$inject = ['CustomToastService', '$state', 'authService'];

	function Admin(CustomToastService, $state, authService) {
		var context = this;
		context.professor = {login: null, senha: null};

		context.authenticate = authenticate;
		context.backToHome = backToHome;
		context.isLogado = isLogado;
        context.logout = logout;

        /**
		 *
         */
		function authenticate(){
            authService.login(context.professor)
				.then(function(){
					$state.go('adminHome.dashboard');
				})
				.catch(function (errorResponse) {
					CustomToastService.show(errorResponse.data.errors[0].message, "top right", 2000);
					console.log("[ADMIN LOGIN] >>> ", errorResponse.data.errors[0].message);
				});
		}

        /**
		 *
         */
		function backToHome(){
			$state.go('auth');
		}

        /**
		 * Check if a user is logged
         * @returns {boolean}
         */
		function isLogado(){
			return localStorage.getItem("token") !== null;
		}

		function logout(){
            authService.logout();
		}
	}
})();
