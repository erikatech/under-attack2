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
			'CustomToastService', '$timeout', '$mdDialog', '$rootScope'];

		function ValoresDeEntradaCtrl(alunoDesafio, ValoresDeEntradaService, $interval, $state, CustomToastService,
    			$timeout, $mdDialog, $rootScope) {
			var context = this;

            context.count = 1;
			context.selecionados = [];
			context.cerebrosDisponiveis = [];
			for(var i = 0; i < alunoDesafio.cerebrosDisponiveis; i++){
				context.cerebrosDisponiveis.push({imageSrc: 'app/assets/images/geral/cerebro_0.png'});
			}
			context.desafio = alunoDesafio.desafio;

			ValoresDeEntradaService.getValoresEncontados(context.desafio.id)
				.then(function (response) {
                    context.selecionados = response.data.valores;
                    var ids = context.selecionados.map(function (item) {
						return item.id;
                    });
                    angular.forEach(ids, function(id){
                    	angular.forEach(context.desafio.programa.valoresEntrada, function(valorDeEntrada,index){
							if(valorDeEntrada.id === id ){
								context.desafio.programa.valoresEntrada.splice(index, 1);
							}
						});
					});
                })
				.catch(function (response) {
                    console.error("ValorDeEntrada >>> ", response);
                });

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
                    // $interval.cancel(context.brainsInterval);
				}
			}

			context.goToNextStage = function(){
                ValoresDeEntradaService.goToNextStage(context.selecionados)
					.then(function (response) {
                        var alunoInSession = JSON.parse(sessionStorage.getItem("aluno"));
                        alunoInSession.pontos = response.data.aluno.pontos;
                        sessionStorage.setItem("aluno", JSON.stringify(alunoInSession));
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
