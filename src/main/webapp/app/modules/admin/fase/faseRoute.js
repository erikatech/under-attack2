'use strict';

	/**
	* @ngdoc function
	* @name app.route:FaseRoute
	* @description
	* # FaseRoute
	* Route for the fase module
	*/

angular.module('admin-fase')
	.config(['$stateProvider', function ($stateProvider) {
		$stateProvider
			.state('adminHome.fase', {
				templateUrl: 'app/modules/admin/fase/fase.html',
				params: {
					fase: null
				},
				url: "/admin/fase",
				controller: 'FaseCtrl',
				controllerAs: '$faseCtrl'
			});
	}]);
