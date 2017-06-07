'use strict';

/**
 * @ngdoc function
 * @name app.route:salaTestadoresRoute
 * @description
 * # salaTestadoresRoute
 * Route of the module sala-testadores
 */

angular.module('sala-testadores')
	.config(['$stateProvider', function ($stateProvider) {
		$stateProvider
            .state('authenticated.salaTestadores', {
                url:'/fases/testadores',
                templateUrl: 'app/modules/sala-testadores/sala-testadores.html',
                controller: 'SalaTestadoresCtrl',
                controllerAs: '$testadores'
            })
            .state('authenticated.classesEquivalencia', {
                url:'/fases/testadores/classes-de-equivalencia',
                templateUrl: 'app/modules/sala-testadores/desafio/classes-equivalencia/classes-de-equivalencia.html',
                controller: 'ClassesDeEquivalenciaCtrl',
                controllerAs: '$classes',
                resolve: {
                    alunoDesafio:
                        ['$q', '$state', 'ClassesDeEquivalenciaService',
                            function ($q, $state, ClassesDeEquivalenciaService) {
                                return ClassesDeEquivalenciaService.getDesafios(localStorage.getItem("desafioId"))
                                    .then(function (response) {
                                        return response.data.alunoDesafio;
                                    }).catch(function () {
                                        $state.go('authenticated.valoresDeEntrada');
                                    });
                            }]
                }

            })
            .state('authenticated.valoresDeEntrada', {
                url:'/fases/testadores/valores-de-entrada',
                templateUrl: 'app/modules/sala-testadores/desafio/valores-de-entrada/valores-de-entrada.html',
                controller: 'ValoresDeEntradaCtrl',
                controllerAs: '$valores',
                resolve: {
                    alunoDesafio:
                        ['$q', '$state', 'SalaTestadoresService',
                            function ($q, $state, SalaTestadoresService) {
                                return SalaTestadoresService.iniciaDesafio()
                                    .then(function (response) {
                                        return response.data.alunoDesafio;
                                    }).catch(function () {
                                        $state.go('authenticated.classesEquivalencia');
                                        // console.error("Catch valor de entrada >>> ", errorResponse);
                                    });
                            }]
                }

            })
	}]);
