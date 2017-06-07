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

    	PageHeader.$inject = ['$rootScope', 'authService', 'PageHeaderService'];

		function PageHeader($rootScope, authService, PageHeaderService) {
			var context = this;

			context.logout = _logout;

            if(sessionStorage.getItem("aluno") === null){
                PageHeaderService.getAlunoInfo()
                    .then(function (response) {
                        sessionStorage.setItem("aluno", JSON.stringify(response.data.aluno));
                        $rootScope.aluno = JSON.parse(sessionStorage.getItem("aluno"));
                    })
                    .catch(function (error) {
                        console.error(error);
                    });
			} else {
                $rootScope.aluno = JSON.parse(sessionStorage.getItem("aluno"));
			}

			function _logout(){
                authService.logout();
			}
		}

})();
