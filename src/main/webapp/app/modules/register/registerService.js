(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:registerService
	 * @description
	 * # registerService
	 * Service for operations related to registering a new student
	 */

  	angular
		.module('register')
		.factory('RegisterService', Register);

		Register.$inject = ['ServiceAPI'];

		function Register(ServiceAPI) {

			return {
				register: register
			};

            function register(user){
                return ServiceAPI.post("/usuario", user)
                    .then(function (response) {
                        return $q.resolve(response);
                    })
                    .catch(function (errorResponse) {
                        return $q.reject(errorResponse);
                    })
            }
		}
})();
