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

	Fase.$inject = ['$stateParams', 'FaseService', '$mdDialog', 'AdminService'];

	function Fase($stateParams, FaseService, $mdDialog, AdminService) {
		var context = this;

        context.cadastrandoDesafio = true;
		context.desafio = {nivel: null,  programa: {titulo: null, descricao: null, valoresEntrada: []}};
		context.valorDeEntradaSelected = [];
		context.selectedDesafios = [];

		/*context.fase = $stateParams.fase;

		context.fase.desafios = [];*/

        context.fase = {
        	desafio: {
        		programa:
					{
						valoresEntrada: []
					}
        	}
        };

		context.niveis = AdminService.getNiveis();
		context.dificuldades = AdminService.getDificuldades();

		context.addClasses = addClasses;
		context.addValorDeEntrada = addValorDeEntrada;
		context.addDesafio = addDesafio;


		function addClasses(valorDeEntrada){
            $mdDialog.show({
				locals: {valorEntrada: valorDeEntrada},
                controller: 'ClasseEquivalenciaCtrl',
                controllerAs: '$classeCtrl',
                templateUrl: 'app/modules/admin/fase/classe-de-equivalencia/add-classes-equivalencia.html',
                clickOutsideToClose: true
            }).then(function (response) {
                if(response.status === 'ok'){
                    console.log(context.fase.desafio.programa.valoresEntrada);
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
                    context.fase.desafio.programa.valoresEntrada.push(response.valorEntrada);
                    console.log("onHide>>> ",response.valorEntrada);
                }
            });
		}

		function addDesafio(){
			context.fase.desafios.push(context.desafio);
			console.log(context.fase);
		}

	}
})();
