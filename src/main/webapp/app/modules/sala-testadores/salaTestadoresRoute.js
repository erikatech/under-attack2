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
            .state('authenticated.valoresDeEntrada', {
                url:'/fases/testadores/valores-de-entrada',
                templateUrl: 'app/modules/sala-testadores/desafio/valores-de-entrada/valores-de-entrada.html',
                controller: 'ValoresDeEntradaCtrl',
                controllerAs: '$valores',
                resolve: {
                    alunoDesafio:
                        ['$q', 'ValoresDeEntradaService',
                            function ($q, ValoresDeEntradaService) {
                                return ValoresDeEntradaService.iniciarDesafio()
                                    .then(function (response) {
                                        return response.data.alunoDesafio;
                                    }).catch(function (errorResponse) {
                                        console.error("Resolve valor de entrada >>> ", errorResponse);
                                    });
                            }]
                }

            })
	}]);
