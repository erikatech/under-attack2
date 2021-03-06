(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:homeService
	 * @description
	 * # HomeService
	 * Service of the module home
	 */

  	angular
		.module('home')
		.factory('HomeService', Home);

    	Home.$inject = ['ServiceAPI'];

		function Home (ServiceAPI) {

			return {
				getFases: _getFases
			};

			function _getFases(login){
				var config = { params: { login: login}};
				return ServiceAPI.get('/fase/fasesFromAluno', config);
			}

		}
})();
