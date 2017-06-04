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

			function _getFases(){
				return ServiceAPI.get('/aluno/login')
					.then(function (successResponse) {
                        console.info(successResponse)
                    })
					.catch(function(errorResponse){
						console.error(errorResponse);
					});
			}

		}
})();
