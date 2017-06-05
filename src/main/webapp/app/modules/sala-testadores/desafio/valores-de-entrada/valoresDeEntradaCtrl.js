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

    	SalaTestadoresCtrl.$inject = ['SalaTestadoresService', 'FaseService', '$mdDialog', '$state'];

		function SalaTestadoresCtrl(SalaTestadoresService, FaseService, $mdDialog, $state) {
			var context = this;
			var fase = JSON.parse(localStorage.getItem("fase"));

			context.desafios = fase.desafios;
			context.openDesafio = _openDesafio;

            SalaTestadoresService.getItemsPocaoMagica()
				.then(function (successResponse) {
					console.info(successResponse)
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
                        	console.log(desafioId);
                        	localStorage.setItem("desafioId", desafioId);
                        	// $state.go();
						})

                    })
					.catch(function (errorResponse) {
						console.error("openDesafio >>> ", errorResponse);
                    })
			}
		}
})();
