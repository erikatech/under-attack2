(function () {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:ValorDeEntradaCtrl
	* @description
	* # ValorDeEntradaCtrl
	* Controller of the app
	*/

	angular
		.module('fase')
		.controller('ClasseDeEquivalenciaCtrl', ClasseEquivalencia);

    ClasseEquivalencia.$inject = ['$stateParams', '$mdDialog', 'ValorDeEntradaService', 'AdminService'];

	function ClasseEquivalencia($stateParams, $mdDialog, ValorDeEntradaService, AdminService) {
		var context = this;

		context.classesEquivalenciaSelected = [];
        context.tipos = ValorDeEntradaService.getTipos();
        context.dificuldades = AdminService.getDificuldades();

        context.hide = hide;
        context.add = add;

		function hide(){
			$mdDialog.hide({status: 'cancel'});
		}

		function add(){
			var response = { status: 'ok', valorEntrada: context.valorEntrada };
			$mdDialog.hide(response);
		}
	}
})();
