(function() {
	'use strict';

	/**
	 * @ngdoc function
	 * @name app.service:PageHeaderService
	 * @description
	 * # PageHeaderService
	 * Service for operations related to registering a new student
	 */

  	angular
		.module('under-attack')
		.factory('PageHeaderService', PageHeaderService);

    	PageHeaderService.$inject = ['ServiceAPI'];

		function PageHeaderService(ServiceAPI) {

			return {
				getAlunoInfo: _getAlunoInfo
			};

            function _getAlunoInfo(){
                var config = { params: { login: localStorage.getItem("login")}};
                return ServiceAPI.get('/aluno/getAlunoInfo', config);
            }
		}
})();
