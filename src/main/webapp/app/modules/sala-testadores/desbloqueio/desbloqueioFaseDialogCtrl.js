(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:DesbloqueioFaseCtrl
	* @description
	* # DesbloqueioFaseCtrl
	* Controller of the dialog desbloqueio-fase
	*/

  	angular
		.module('sala-testadores')
		.controller('DesbloqueioFaseCtrl', DesbloqueioFaseCtrl);

    	DesbloqueioFaseCtrl.$inject = ['$mdDialog'];

		function DesbloqueioFaseCtrl($mdDialog) {
			var context = this;

            context.backToRoom = function(){
            	$mdDialog.cancel();
			};

			context.continue = function(){
            	$mdDialog.hide();
			}
        }
})();
