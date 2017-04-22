(function () {

	angular.module("login-validator")
		.directive("loginValidator", loginValidator);

	loginValidator.$inject = ['loginService'];

	function loginValidator(loginService) {
		return {
			restrict: 'A',
			require: 'ngModel',
			link: link
		};

		function link(scope, element, attrs, ngModel){
				element.bind('blur', function () {
					// Validando se o campo está vazio
					if (!ngModel || element.val() === "") return;
					loginService.checkLoginAvailability(attrs.loginValidator, element.val())
						.then(function (successResponse) {
							var isLoginAvailable = successResponse.data.boolean;
							ngModel.$setValidity('unavailable', isLoginAvailable);
						})
						.catch(function (errorResponse) {
							console.log("[loginValidator] >>> ", errorResponse);
						})
				});
		}
	}
})();
