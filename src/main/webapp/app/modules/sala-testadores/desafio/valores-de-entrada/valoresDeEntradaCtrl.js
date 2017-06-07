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

    	ValoresDeEntradaCtrl.$inject = ['alunoDesafio', 'ValoresDeEntradaService', '$interval', '$state',
			'CustomToastService', '$timeout', '$mdDialog'];

		function ValoresDeEntradaCtrl(alunoDesafio, ValoresDeEntradaService, $interval, $state, CustomToastService,
    			$timeout, $mdDialog) {
			var context = this;

            context.count = 1;
			context.selecionados = [];
			context.cerebrosDisponiveis = [];
			for(var i = 0; i < alunoDesafio.cerebrosDisponiveis; i++){
				context.cerebrosDisponiveis.push({imageSrc: 'app/assets/images/geral/cerebro_0.png'});
			}
			context.desafio = alunoDesafio.desafio;

			context.validate = _validate;

			function _validate(){
                ValoresDeEntradaService.validateValorDeEntrada(context.valorSelecionado.id, alunoDesafio.desafio.id)
					.then(function(response){
						context.valorSelecionado.pontos = response.data;
					})
					.catch(function () {
                        context.brainsInterval = $interval( function(){ changeBackgroundImage(); }, 350, 0);
                    });
			}

			function changeBackgroundImage(){
                var cerebro = context.cerebrosDisponiveis[context.cerebrosDisponiveis.length - 1];
                cerebro.imageSrc = 'app/assets/images/geral/cerebro_'+context.count+'.png';
                context.cerebrosDisponiveis[context.cerebrosDisponiveis.length - 1] = cerebro;
                if(context.count === 3){
                	context.count = 1;
                    $interval.cancel(context.brainsInterval);
                    $timeout(function(){
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
					}, 350);
				} else {
                	context.count++;
				}
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
