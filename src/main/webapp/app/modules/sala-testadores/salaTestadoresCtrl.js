(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:HomeCtrl
	* @description
	* # HomeCtrl
	* Controller of the module home
	*/

  	angular
		.module('home')
		.controller('HomeCtrl', HomeCtrl);

    	HomeCtrl.$inject = ['HomeService', '$state'];

		function HomeCtrl(HomeService, $state) {
			var context = this;
            context.login = localStorage.getItem("login");

            HomeService.getFases(context.login)
                .then(function (successResponse) {
                    console.log(successResponse);
                    context.fasesAluno = successResponse.data.fasesAluno;
                })
                .catch(function (errorResponse) {
                    console.error(errorResponse)
                });

			context.iniciarFase = _iniciarFase;

			function _iniciarFase (fase){
                $state.go('fase', { fase: fase });
			};
		}
})();
