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

    	RegisterCtrl.$inject = ['RegisterService', '$state'];

		function RegisterCtrl(RegisterService, $state) {
			var context = this;
			context.aluno = {nome: "", login: "", senha: ""};
			context.register = register;

			function register(){
				return RegisterService.register(context.aluno)
					.then(function (success) {
						console.info("[RegisterCtrl] register >>> ", success);
                    })
					.catch(function (error) {
                        console.error("[RegisterCtrl] register >>> ", error);
                    });
			}
		}
})();
