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
		.module('admin-fase')
		.controller('FaseCtrl', Fase);

	Fase.$inject = ['$stateParams', 'FaseService', '$mdDialog', 'AdminService'];

	function Fase($stateParams, FaseService, $mdDialog, AdminService) {
		var context = this;

		context.fase = $stateParams.fase;

        context.cadastrandoDesafio = true;
		context.desafio = {nivel: null,  programa: {titulo: null, descricao: null, valoresEntrada: []}};
		context.valorDeEntradaSelected = [];
		context.selectedDesafios = [];

		context.niveis = AdminService.getNiveis();
		context.dificuldades = AdminService.getDificuldades();

		context.addClasses = addClasses;
		context.addValorDeEntrada = addValorDeEntrada;
		context.update = _update;


		function addClasses(valorDeEntrada){
            $mdDialog.show({
				locals: {valorEntrada: valorDeEntrada},
                controller: 'ClasseEquivalenciaCtrl',
                controllerAs: '$classeCtrl',
                templateUrl: 'app/modules/admin/fase/classe-de-equivalencia/add-classes-equivalencia.html',
                clickOutsideToClose: true
            }).then(function (response) {
                if(response.status === 'ok'){
                    console.log(context.desafio.programa.valoresEntrada);
                }
            });
		}

		function addValorDeEntrada(valorDeEntrada){
            $mdDialog.show({
                locals: {valorEntrada: valorDeEntrada},
                controller: 'ValorDeEntradaCtrl',
                controllerAs: '$valorEntradaCtrl',
                templateUrl: 'app/modules/admin/fase/valor-de-entrada/add-valor-de-entrada.html',
                clickOutsideToClose: true
            }).then(function (response) {
            	if(response.status === 'ok'){
                    context.desafio.programa.valoresEntrada.push(response.valorEntrada);
                }
            });
		}

		function _update(){
            console.log("FASE AO ATUALIZAR >>> ",context.fase);
            context.fase.desafios = [context.desafio];
            /*context.desafio = {nivel: null,  programa: {titulo: null, descricao: null, valoresEntrada: []}};
            console.log(context.fase);*/
            FaseService.updateFase(context.fase)
				.then(function (response) {
					console.log("Success >>> ",  response);
                })
				.catch(function (error) {
					console.log("ERROR >>> ",error);
                });
        }

	}
})();
