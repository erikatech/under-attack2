(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:PageHeaderCtrl
	* @description
	* # PageHeaderCtrl
	* Controller of the page header directive
	*/

	angular
		.module('under-attack')
		.controller('PageHeaderCtrl', PageHeader );

    	PageHeader.$inject = ['authService'];

		function PageHeader(authService) {
			var context = this;

			context.logout = _logout;

			function _logout(){
                authService.logout();
			}
		}

})();
