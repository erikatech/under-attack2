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
		.controller('ClassesDeEquivalenciaCtrl', ClassesDeEquivalenciaCtrl);

    	ClassesDeEquivalenciaCtrl.$inject = ['alunoDesafio', 'ClassesDeEquivalenciaService'];

		function ClassesDeEquivalenciaCtrl(alunoDesafio, ClassesDeEquivalenciaService) {
			var context = this;
            context.cerebrosDisponiveis = [];
            for(var i = 0; i < alunoDesafio.cerebrosDisponiveis; i++){
                context.cerebrosDisponiveis.push(i);
            }
            context.desafio = alunoDesafio.desafio;

			ClassesDeEquivalenciaService.getValoresAluno()
				.then(function (success) {
					context.alunoValores = success.data.valores;
                })
				.catch(function (error) {
					console.error("classesEquivalencia >>> ", error);
                })


        }
})();
