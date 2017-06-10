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

    	ClassesDeEquivalenciaCtrl.$inject = ['$mdDialog','alunoDesafio', 'ClassesDeEquivalenciaService', '$timeout',
				'$interval', '$state', 'SalaTestadoresService'];

		function ClassesDeEquivalenciaCtrl($mdDialog, alunoDesafio, ClassesDeEquivalenciaService, $timeout,
                   $interval, $state, SalaTestadoresService) {

			var context = this;
            // context.classesAluno = [];
            context.count = 1;
            context.cerebrosDisponiveis = [];
            for(var i = 0; i < alunoDesafio.cerebrosDisponiveis; i++){
                context.cerebrosDisponiveis.push({imageSrc: 'app/assets/images/geral/cerebro_0.png'});
            }
            context.desafio = alunoDesafio.desafio;
            context.finalizar = _finalizar;

			ClassesDeEquivalenciaService.getValoresAluno(context.desafio.id)
				.then(function (success) {
					context.valoresAluno = success.data.valores;
                    context.valorSelecionado = context.valoresAluno[0];
                })
				.catch(function (error) {
					console.error("classesEquivalencia >>> ", error);
                });


            ClassesDeEquivalenciaService.getClassesAluno(context.desafio.id)
                .then(function (success) {
                    context.classesAluno = success.data.classesAluno;
                })
                .catch(function () {
                    context.classesAluno = [];
                });


			context.validate = _validate;

			function _validate(){
				var testDTO = {
					login: localStorage.getItem("login"),
					entradaAluno: context.valorPreenchido,
                    valorDeEntrada: context.valorSelecionado,
                    idDesafio: context.desafio.id,
                    classesAluno: context.classesAluno
				};
				ClassesDeEquivalenciaService.validate(testDTO)
					.then(function (successResponse) {
                        angular.forEach(successResponse.data.testResult.alunoEncontraClasseEquivalencia, function(classe){
                            context.classesAluno.push(classe);
                        });
                        // context.classesAluno = successResponse.data.testResult.alunoEncontraClasseEquivalencia;
                    })
					.catch(function (errorResponse) {
						context.errorMessage = errorResponse.data;
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

            function _finalizar(){
                ClassesDeEquivalenciaService.finalizaDesafio(context.desafio.id, context.classesAluno)
                    .then(function (response) {
                        var alunoInSession = JSON.parse(sessionStorage.getItem("aluno"));
                        alunoInSession.pontos = response.data.resultadoDesafio.pontosAluno;
                        sessionStorage.setItem("aluno", JSON.stringify(alunoInSession));
                        $mdDialog.show({
                            controller: 'ConcluiDesafioCtrl',
                            controllerAs: '$concluiDesafio',
                            templateUrl: 'app/modules/sala-testadores/desafio/tmpl/conclui-desafio-dialog.html',
                            clickOutsideToClose: false,
                            locals: { desempenhoInfo: response.data.resultadoDesafio }
                        }).then(function(){
                            SalaTestadoresService.restartaDesafio(context.desafio.id)
                                .then(function(response){
                                    $state.go('authenticated.valoresDeEntrada');
                                })
                                .catch(function (errorResponse) {
                                    console.error(" finaliza >>> ", errorResponse);
                                });
                        }).catch(function () {
                            $state.go('authenticated.salaTestadores');
                        });
                    })
                    .catch(function (response) {
                        console.error("error >>> ", response);
                    });
            }
        }
})();
