(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:authInterceptor
	 * @description
	 * # authInterceptor
	 * Service responsible for dealing with request and response authorization
	 */

  	angular
		.module('auth')
		.factory('AuthInterceptor', AuthInterceptor);

    	AuthInterceptor.$inject = ['$q', '$location'];

		function AuthInterceptor ($q, $location) {

            function responseError(responseError){
            	if(responseError.status === 401)
                    location.href = '#/';
                return $q.reject(responseError);
            };

            function request(config){
                var _token = localStorage.getItem("token");
                var _login = localStorage.getItem("login");
                var _isLogado = _token != null;

                config.headers = config.headers || {};

                if(_isLogado){
                    config.headers.Authorization = _login + ' ' + _token;
                }
                return config;

			};

			return {
                responseError: responseError,
                request: request
			};
		}
})();
