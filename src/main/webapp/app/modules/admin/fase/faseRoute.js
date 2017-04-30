'use strict';

	/**
	* @ngdoc function
	* @name app.route:FaseRoute
	* @description
	* # FaseRoute
	* Route for the fase module
	*/

angular.module('fase')
	.config(['$stateProvider', function ($stateProvider) {
		$stateProvider
			.state('authenticated.adminHome.fase', {
				templateUrl: 'app/modules/admin/fase/fase.html',
				params: {
					fase: null
				},
				url: "/admin/fase",
				controller: 'FaseCtrl',
				controllerAs: '$faseCtrl'
			});
	}]);
