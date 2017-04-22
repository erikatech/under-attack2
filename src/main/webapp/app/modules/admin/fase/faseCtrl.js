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

	Fase.$inject = ['$stateParams', '$http', 'faseService'];

	function Fase($stateParams, $http, faseService) {
		var context = this;
		onOpenPage();

		context.desafio = {nivel: null,  programa: {titulo: null, descricao: null, valoresEntrada: []}};

		context.valorDeEntradaSelected = [];
		context.classesEquivalenciaSelected = [];

		context.valorDeEntrada = {classesEquivalencia: [] };

		context.fase = $stateParams.fase;

		context.fase.desafios = [];

		context.niveis = [
			{value: 'FACIL', label: 'Fácil'},
			{value: 'INTERMEDIARIO', label: 'Intermediário'},
			{value: 'DIFICIL', label: 'Difícil'}
		];
		context.tipos = [
			{value: "DISTRATIVO", label: "Distrativo"},
			{value: "CORRETO", label: "Correto"}
		];
		context.dificuldades = [
			{value: "FACIL", label: "Fácil"},
			{value: "NORMAL", label: "Normal"},
			{value: "DIFICIL", label: "Fácil"},
			{value: "CHUCK_NORRIS", label: "Chuck Norris"}
		];

		context.tiposClasse = [
			{value: 'VALIDA', label: "Válida"},
			{value: 'INVALIDA', label: "Inválida"}
		];

		context.addClasse = addClasse;
		context.addValorEntrada = addValorEntrada;
		context.addDesafio = addDesafio;
		context.saveFase = saveFase;

		function onOpenPage(){
			$http.get("http://localhost:8080/under-attack/ingrediente")
				.then(function (successResponse) {
					context.ingredientes = successResponse.data.list;
				})
				.catch(function (error) {
					console.log("Erro ingredientes >>> ",error);
				})
		}

		function addClasse(){
			context.valorDeEntrada.classesEquivalencia.push(context.classeEquivalencia);
			context.classeEquivalencia = {descricao: null, expressaoRegular: null,
				bugExistente: false, tipo: null, dificuldade: null, ingrediente: null,
				resultadoEsperado: null, saida: null};
		}

		function addValorEntrada(){
			context.desafio.programa.valoresEntrada.push(context.valorDeEntrada);
			context.valorDeEntrada = {descricao: null, tipo: null, dificuldade: null, classesEquivalencia: []};
		}

		function addDesafio(){
			context.fase.desafios.push(context.desafio);
			console.log(context.fase);
		}


		function saveFase(){
			faseService.atualiza(context.fase)
				.then(function (success) {
					console.log("Sucess >>> ", success);
				})
				.catch(function (error) {
					console.log("Error >>> ", error);
				})
		}

	}
})();
