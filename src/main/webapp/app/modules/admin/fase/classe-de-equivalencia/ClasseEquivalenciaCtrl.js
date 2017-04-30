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
		.controller('ClasseEquivalenciaCtrl', ClasseEquivalencia);

    ClasseEquivalencia.$inject = ['valorEntrada', '$mdDialog', 'ClasseEquivalenciaService', 'AdminService'];

	function ClasseEquivalencia(valorEntrada, $mdDialog, ClasseEquivalenciaService, AdminService) {
		var context = this;

		onOpenPage();

		context.valorEntrada = valorEntrada;
        context.valorEntrada.classesEquivalencia = valorEntrada.classesEquivalencia !== undefined ?
			valorEntrada.classesEquivalencia : [];

        context.classesEquivalenciaSelected = [];
        context.tipos = ClasseEquivalenciaService.getTipos();
        context.dificuldades = AdminService.getDificuldades();

        context.hide = hide;
        context.add = add;
		context.finalizar = finalizar;

        function onOpenPage(){
            ClasseEquivalenciaService.getIngredientes()
                .then(function (successResponse) {
                    context.ingredientes = successResponse.data.ingredientes;
                })
                .catch(function (errorResponse) {
                    console.log("ERROR WHILE GETTING INGREDIENTS >>> ", errorResponse);
                });
        }

		function hide(){
			$mdDialog.hide({status: 'cancel'});
		}

		function add(){
			context.valorEntrada.classesEquivalencia.push(context.classe);
			delete context.classe;
		}

		function finalizar(){
			$mdDialog.hide({status: 'ok', valorEntrada: context.valorEntrada});
		}
	}
})();
