(function() {
	'use strict';

	/**
	* @ngdoc index
	* @name app
	* @description
	* # app
	*
	* Main module of the application.
	*/

	angular.module('under-attack', [
		'ngResource',
		'ngAria',
		'ngAnimate',
		'ngMaterial',
		'ngMessages',
		'ui.router',
		'angular-loading-bar',

		'login-validator',
		// 'admin',
		'auth',
		'register',
		'home'
	]);

})();
