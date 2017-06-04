(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:RegisterCtrl
	* @description
	* # RegisterCtrl
	* Controller for registering a new student
	*/

  	angular
		.module('register')
		.controller('RegisterCtrl', RegisterCtrl);

    	RegisterCtrl.$inject = ['RegisterService', '$state', 'CustomToastService'];

		function RegisterCtrl(RegisterService, $state, CustomToastService) {
			var context = this;
			context.aluno = {nome: "", login: "", senha: ""};
			context.register = register;
			context.backToLogin = _backToLogin;

			function register(){
				return RegisterService.register(context.aluno)
					.then(function () {
                        $state.go('auth');
                    })
					.catch(function (errorResponse) {
                        CustomToastService.show(errorResponse.data.errors[0].message, "top right", 2000);
                        console.error("[RegisterCtrl] register >>> ", error);
                    });
			}

			function _backToLogin(){
                $state.go('auth');
			}
		}
})();
