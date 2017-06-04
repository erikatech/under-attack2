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
				post: _post,
				get: _get,
                // getWithParameters: _getWithParameters,
                put: _put
			};

            function _post(entity, endPoint){
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

            function _put(entity, endPoint){
                var finalEndpoint = baseEndPoint + endPoint;

                console.info("[ServiceAPI] finalEndpoint >>> ", finalEndpoint);

                return $http.put(finalEndpoint, entity)
                    .then(function (response) {
                        return $q.resolve(response);
                    })
                    .catch(function (errorResponse) {
                        return $q.reject(errorResponse);
                    });
            }

            function _get(endPoint, config){
                console.info("[ServiceAPI] config >>>", config);
                var finalEndpoint = baseEndPoint + endPoint;
                console.info("[ServiceAPI] finalEndpoint >>> ", finalEndpoint);

                return $http.get(finalEndpoint, config || null)
                    .then(function (response) {
                        return $q.resolve(response);
                    })
                    .catch(function (errorResponse) {
                        return $q.reject(errorResponse);
                    });
            }

            /*function _getWithParameters(endPoint, config){
                var finalEndpoint = baseEndPoint + endPoint;

                console.info("[ServiceAPI] finalEndpoint >>> ", finalEndpoint);

                return $http.get(finalEndpoint)
                    .then(function (response) {
                        return $q.resolve(response);
                    })
                    .catch(function (errorResponse) {
                        return $q.reject(errorResponse);
                    });
            }*/
		}
})();
