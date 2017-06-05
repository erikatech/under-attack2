(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:ValoresDeEntradaCtrl
	* @description
	* # ValoresDeEntradaCtrl
	* Controller of the module sala-testadores
	*/

  	angular
		.module('sala-testadores')
		.controller('ValoresDeEntradaCtrl', ValoresDeEntradaCtrl);

    	ValoresDeEntradaCtrl.$inject = ['alunoDesafio'];

		function ValoresDeEntradaCtrl(alunoDesafio) {
			var context = this;

			context.selecionados = [];
			context.valorEntrada = {};

			context.cerebrosDisponiveis = [];
			for(var i = 1; i < 4; i++){
				context.cerebrosDisponiveis.push(i);
			}
			context.desafio = alunoDesafio.desafio;

			context.validate = _validate;

			function _validate(){
                console.log(context.valorSelecionado )
			}

			context.startCallback = function(event, ui, item){
				context.valorSelecionado = item;
			}



		}
})();
