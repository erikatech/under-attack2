(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:SalaTestadoresCtrl
	* @description
	* # SalaTestadoresCtrl
	* Controller of the module sala-testadores
	*/

  	angular
		.module('sala-testadores')
		.controller('SalaTestadoresCtrl', SalaTestadoresCtrl);

    	SalaTestadoresCtrl.$inject = ['checkIfUnblocked', 'SalaTestadoresService', 'FaseService', '$mdDialog', '$state'];

		function SalaTestadoresCtrl(checkIfUnblocked, SalaTestadoresService, FaseService, $mdDialog, $state) {
			var context = this;
			var fase = JSON.parse(localStorage.getItem("fase"));

            if(checkIfUnblocked.desbloqueou){
                $mdDialog.show({
                    controller: 'DesbloqueioFaseCtrl',
                    controllerAs: '$desbloqueioDialog',
                    templateUrl: 'app/modules/sala-testadores/desbloqueio/desbloqueio-fase-dialog.html',
                    clickOutsideToClose: true
                }).catch(function(){
                    $state.go('authenticated.home');
                })
			}

			context.desafios = fase.desafios;
			context.openDesafio = _openDesafio;

            SalaTestadoresService.getItemsPocaoMagica()
				.then(function (successResponse) {
					console.log("Items poção >>> ", successResponse);
                })
				.catch(function (errorResponse) {
					console.error(errorResponse);
                });

            function _openDesafio(idDesafio){
                FaseService.getDesafio(idDesafio)
					.then(function (successResponse) {
                        $mdDialog.show({
                            controller: 'DesafioDialogCtrl',
                            controllerAs: '$desafioDialog',
                            templateUrl: 'app/modules/sala-testadores/desafio/tmpl/desafio-dialog.tmpl.html',
                            clickOutsideToClose: true,
                            locals: { desafioDialogInfo: successResponse.data.desafio }
                        }).then(function(desafioId){
                        	localStorage.setItem("desafioId", desafioId);
                        	$state.go('authenticated.valoresDeEntrada');
						})

                    })
					.catch(function (errorResponse) {
						console.error("openDesafio >>> ", errorResponse);
                    })
			}
		}
})();
