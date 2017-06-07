(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:ConcluiDesafioCtrl
	* @description
	* # ConcluiDesafioCtrl
	* Controller of the dialog conclui-desafio
	*/

  	angular
		.module('sala-testadores')
		.controller('ConcluiDesafioCtrl', ConcluiDesafioCtrl);

    	ConcluiDesafioCtrl.$inject = ['desempenhoInfo', '$mdDialog'];

		function ConcluiDesafioCtrl(desempenhoInfo, $mdDialog) {
			var context = this;
			context.desempenho = desempenhoInfo;

			var rootPath = "app/assets/images/geral/";

			context.primeiraEstrelaImg = rootPath.concat(context.desempenho.totalEstrelas === 1
				? 'estrela.png' : 'estrela.png');

            context.segundaEstrelaImg = rootPath.concat(context.desempenho.totalEstrelas === 2
                ? 'estrela.png' : 'estrela.png');

            context.terceiraEstrelaImg = rootPath.concat(context.desempenho.totalEstrelas === 3
                ? 'estrela.png' : 'estrela.png');

            context.backToRoom = function(){
            	$mdDialog.cancel();
			};

			context.tryAgain = function(){
            	$mdDialog.hide();
			}
        }
})();
