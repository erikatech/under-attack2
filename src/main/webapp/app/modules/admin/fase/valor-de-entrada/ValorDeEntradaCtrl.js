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
		.controller('ValorDeEntradaCtrl', ValorDeEntrada);

    ValorDeEntrada.$inject = ['valorEntrada', '$mdDialog', 'ValorDeEntradaService', 'AdminService'];

	function ValorDeEntrada(valorEntrada, $mdDialog, ValorDeEntradaService, AdminService) {
		var context = this;

        context.classesEquivalenciaSelected = [];
        context.tipos = ValorDeEntradaService.getTipos();
        context.dificuldades = AdminService.getDificuldades();

        context.valorDeEntrada = angular.copy(valorEntrada);
        console.log(context.valorDeEntrada);

        context.hide = hide;
        context.add = add;

		function hide(){
			$mdDialog.hide({status: 'cancel'});
		}

		function add(){
			var response = { status: 'ok', valorEntrada: context.valorDeEntrada };
			$mdDialog.hide(response);
		}
	}
})();
