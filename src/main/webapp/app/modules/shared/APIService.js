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
		.module('under-attack')
		.factory('ServiceAPI', Service);

    	Service.$inject = ['$http', '$q'];

		function Service($http, $q) {

			var baseEndPoint = "http://localhost:8080/under-attack";

			return {
				post: post,
				get: get
			};

            function post(entity, endPoint){
            	var finalEndpoint = baseEndPoint + endPoint;

            	console.info("[ServiceAPI] finalEndpoint >>> ", finalEndpoint);

                return $http.post(finalEndpoint, entity)
                    .then(function (response) {
                        return $q.resolve(response);
                    })
                    .catch(function (errorResponse) {
                        return $q.reject(errorResponse);
                    });
            }

            function get(endPoint){
                var finalEndpoint = baseEndPoint + endPoint;

                console.info("[ServiceAPI] finalEndpoint >>> ", finalEndpoint);

                return $http.get(finalEndpoint)
                    .then(function (response) {
                        return $q.resolve(response);
                    })
                    .catch(function (errorResponse) {
                        return $q.reject(errorResponse);
                    });
            }
		}
})();
