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

    	SalaTestadoresCtrl.$inject = ['SalaTestadoresService', 'FaseService', '$mdDialog'];

		function SalaTestadoresCtrl(SalaTestadoresService, FaseService, $mdDialog) {
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
						console.info("openDesafio >>> ", successResponse);
                        $mdDialog.show({
                            controller: 'DesafioDialogCtrl',
                            controllerAs: '$desafioDialog'
                            templateUrl: 'app/modules/shared/sheet-upload/tmpl/upload-dialog.tmpl.html',
                            clickOutsideToClose: true,
                            locals: { uploadSheetInfo: locals }
                        })


                    })
					.catch(function (errorResponse) {
						console.error("openDesafio >>> ", errorResponse);
                    })
			}
		}
})();
