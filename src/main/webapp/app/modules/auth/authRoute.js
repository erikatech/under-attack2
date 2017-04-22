'use strict';

/**
 * @ngdoc function
 * @name app.route:authRoute
 * @description
 * # authRoute
 * Route of the app
 */

angular.module('auth')
	.config(['$stateProvider', function ($stateProvider) {

		$stateProvider
            .state('authenticated', {
                abstract: true,
                templateUrl: 'app/modules/auth/authenticated.html',
                resolve: {
                    user: ['$rootScope', '$q', 'authService', function ($rootScope, $q, AuthService) {
                        return  AuthService.getUser()
                            .then(function (response) {
                                return $q.resolve(response);
                            })
                            .catch(function (err) {
                                console.error('Unauthorized >>> ', err);
                                location.href = '#/';
                            });
                    }]
                }
            })
			.state('auth', {
				url:'/',
				templateUrl: 'app/modules/auth/auth.html',
				controller: 'AuthCtrl',
				controllerAs: '$authCtrl'
			});
	}]);
