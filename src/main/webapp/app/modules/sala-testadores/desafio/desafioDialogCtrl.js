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
		.controller('DesafioDialogCtrl', DesafioDialogCtrl);

    	DesafioDialogCtrl.$inject = ['desafioDialogInfo', '$mdDialog'];

		function DesafioDialogCtrl(desafioDialogInfo, $mdDialog) {
			var context = this;
			context.desafio = desafioDialogInfo;

			context.startDesafio = _startDesafio;

			function _startDesafio(){
                $mdDialog.hide(context.desafio.id);
			}
        }
})();
