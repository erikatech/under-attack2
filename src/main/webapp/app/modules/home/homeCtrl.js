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

    	HomeCtrl.$inject = ['checkIfUnblocked', 'HomeService', '$state', '$mdDialog'];

		function HomeCtrl(checkIfUnblocked, HomeService, $state, $mdDialog) {
			var context = this;
            context.login = localStorage.getItem("login");

            if(checkIfUnblocked.desbloqueou){
                $mdDialog.show({
                    controller: 'DesbloqueioFaseCtrl',
                    controllerAs: '$desbloqueioDialog',
                    templateUrl: 'app/modules/sala-testadores/desbloqueio/desbloqueio-fase-dialog.html',
                    clickOutsideToClose: true
                });
            }

            HomeService.getFases(context.login)
                .then(function (successResponse) {
                	console.log("FASES >>> ",successResponse);
                    context.fasesAluno = successResponse.data.fasesAluno;
                })
                .catch(function (errorResponse) {
                    console.error(errorResponse)
                });

			context.iniciarFase = _iniciarFase;

			function _iniciarFase (fase){
				localStorage.setItem("fase", JSON.stringify(fase));
                $state.go('authenticated.salaTestadores');
			};

			context.hideIniciarButton = function(){
				if(context.fasesAluno !== undefined){
                    var faseTestadores = context.fasesAluno[0];
                    var objetivosFase = faseTestadores.faseObjetivo.length;
                    var objetivosRealizados = faseTestadores.faseObjetivo.filter(function(item){
                    	return item.realizado;
					});
                    return objetivosFase === objetivosRealizados.length;
				}
            }
		}
})();
