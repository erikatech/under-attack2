(function () {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:FaseCtrl
	* @description
	* # FaseCtrl
	* Controller of the app
	*/

	angular
		.module('fase')
		.controller('FaseCtrl', Fase);

	Fase.$inject = ['$stateParams', 'faseService'];

	function Fase($stateParams, faseService) {
		var context = this;

		onOpenPage();

        context.cadastrandoDesafio = true;
		context.desafio = {nivel: null,  programa: {titulo: null, descricao: null, valoresEntrada: []}};
		context.valorDeEntradaSelected = [];
		context.selectedDesafios = [];

		/*context.fase = $stateParams.fase;

		context.fase.desafios = [];*/

		context.niveis = faseService.getNiveis();
		context.tipos = faseService.getTipos();
		context.dificuldades = faseService.getDificuldades();
		context.tiposClasses = faseService.getTiposClasses();

		context.addClasse = addClasse;
		context.addValorDeEntrada = addValorDeEntrada;
		context.addDesafio = addDesafio;


		function onOpenPage(){
			faseService.getIngredientes()
				.then(function (successResponse) {
					context.ingredientes = successResponse.data.ingredientes;
                })
				.catch(function (errorResponse) {
					console.log("ERROR WHILE GETTING INGREDIENTS >>> ", errorResponse);
                });
		}

		function addClasse(){
			context.valorDeEntrada.classesEquivalencia.push(context.classeEquivalencia);
			context.classeEquivalencia = {descricao: null, expressaoRegular: null,
				bugExistente: false, tipo: null, dificuldade: null, ingrediente: null,
				resultadoEsperado: null, saida: null};
		}

		function addValorDeEntrada(){
            $mdDialog.show({
                controller: 'ValorDeEntradaCtrl',
                templateUrl: 'app/modules/sheet-upload/tmpl/upload-dialog.tmpl.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true,
                locals: { uploadSheetInfo: uploadSheetInfo }
            });/*
			context.desafio.programa.valoresEntrada.push(context.valorDeEntrada);
			context.valorDeEntrada = {descricao: null, tipo: null, dificuldade: null, classesEquivalencia: []};*/
		}

		function addDesafio(){
			context.fase.desafios.push(context.desafio);
			console.log(context.fase);
		}

	}
})();
