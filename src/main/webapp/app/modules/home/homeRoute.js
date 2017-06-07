'use strict';

/**
 * @ngdoc function
 * @name app.route:homeRoute
 * @description
 * # homeRoute
 * Route of the module Home
 */

angular.module('home')
	.config(['$stateProvider', function ($stateProvider) {
		$stateProvider
            .state('authenticated.home', {
                url:'/home',
                templateUrl: 'app/modules/home/home.html',
                controller: 'HomeCtrl',
                controllerAs: '$home',
                resolve: {
                    checkIfUnblocked:
                        ['$q', '$state', 'SalaTestadoresService',
                            function ($q, $state, SalaTestadoresService) {
                                return SalaTestadoresService.verificaSeDesbloqueouFase()
                                    .then(function (response) {
                                        return response.data;
                                    }).catch(function (errorResponse) {
                                        console.error(errorResponse);
                                    });
                            }]


                }

            })
	}]);
