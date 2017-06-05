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

    	ValoresDeEntradaCtrl.$inject = ['alunoDesafio', 'ValoresDeEntradaService', '$mdDialog', '$state',
			'CustomToastService'];

		function ValoresDeEntradaCtrl(alunoDesafio, ValoresDeEntradaService, $mdDialog, $state, CustomToastService) {
			var context = this;

			context.selecionados = [];
			context.cerebrosDisponiveis = [];
			for(var i = 0; i < alunoDesafio.cerebrosDisponiveis; i++){
				context.cerebrosDisponiveis.push(i);
			}
			context.desafio = alunoDesafio.desafio;

			context.validate = _validate;

			function _validate(){
                ValoresDeEntradaService.validateValorDeEntrada(context.valorSelecionado.id, alunoDesafio.desafio.id)
					.then(function(response){
						context.valorSelecionado.pontos = response.data;
					})
					.catch(function () {
						context.cerebrosDisponiveis.pop();
						if(!context.cerebrosDisponiveis.length){
                            $mdDialog.show({
                                controller: 'GameOverDialogCtrl',
                                controllerAs: '$gameOver',
                                templateUrl: 'app/modules/shared/game-over-dialog/tmpl/game-over.html',
                                clickOutsideToClose: false
                            }).then(function(){
                            	$state.reload();
                            }).catch(function () {
                                $state.go('authenticated.salaTestadores');
                            })
						}
                    });
			}

			context.goToNextStage = function(){
                ValoresDeEntradaService.goToNextStage(context.selecionados)
					.then(function () {
						$state.go('authenticated.classesEquivalencia');
                    })
					.catch(function (errorResponse) {
						CustomToastService.show(errorResponse.data.errors[0].message, "top right", 2000);
                    });
			}

			context.startCallback = function(event, ui, item){
				context.valorSelecionado = item;
			}
		}
})();
