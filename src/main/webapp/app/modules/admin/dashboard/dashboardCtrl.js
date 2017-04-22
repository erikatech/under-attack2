(function () {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:DashboardCtrl
	* @description
	* # DashboardCtrl
	* Controller of the app
	*/

	angular
		.module('dashboard')
		.controller('DashboardCtrl', Dashboard);

	Dashboard.$inject = ['faseService', '$state', 'authService'];

	function Dashboard(faseService, $state, authService) {
		var context = this;
		onOpenPage();

		context.configuraFase = configuraFase;

		function onOpenPage(){
			/*faseService.getAll()
				.then(function (successResponse) {
					context.fases = successResponse.data.list;
				})
				.catch(function (errorResponse) {
					console.log("Error >>> ", errorResponse);
				})*/
            // listaFases();
		}

        function listaFases(){
            authService.lista()
                .then(function(success){
                    console.info(success)
                })
                .catch(function (error) {
                    console.error(error);
                })
        }

		function configuraFase(fase){
			$state.go('adminHome.fase', { fase: fase });
		}
	}
})();
