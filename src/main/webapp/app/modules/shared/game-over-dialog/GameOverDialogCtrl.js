(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:GameOverDialogCtrl
	* @description
	* # GameOverDialogCtrl
	* Controller of the module sala-testadores
	*/

  	angular
		.module('sala-testadores')
		.controller('GameOverDialogCtrl', GameOverDialogCtrl);

    	GameOverDialogCtrl.$inject = ['$mdDialog'];

		function GameOverDialogCtrl($mdDialog) {
			var context = this;

			context.back = _back;
			context.tryAgain = _tryAgain;


			function _back(){
                $mdDialog.cancel();
			}

			function _tryAgain(){
				$mdDialog.hide();
			}
		}
})();
