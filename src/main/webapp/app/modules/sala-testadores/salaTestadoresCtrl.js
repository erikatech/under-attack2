(function() {
	'use strict';

	/**
	* @ngdoc function
	* @name app.controller:SalaTestadoresCtrl
	* @description
	* # SalaTestadoresCtrl
	* Controller of the module sala-testadores
	*/

  	angular
		.module('sala-testadores')
		.controller('SalaTestadoresCtrl', SalaTestadoresCtrl);

    	SalaTestadoresCtrl.$inject = ['SalaTestadoresService', 'FaseService', '$mdDialog', '$state', '$window'];

		function SalaTestadoresCtrl(SalaTestadoresService, FaseService, $mdDialog, $state, $window) {

			var context = this;

            var fase = JSON.parse(localStorage.getItem("fase"));

			context.desafios = fase.desafios;

            context.desafioCSS = context.desafios.map(function(item){
            	return {
                    'content': "url(app/assets/images/geral/"+item.imagem+".png)",
                    top: item.top + 'px',
                    left: item.leftPos + 'px'
				}
			});

			context.openDesafio = _openDesafio;

            SalaTestadoresService.getItemsPocaoMagica()
				.then(function (successResponse) {
                    context.itensAluno = successResponse.data.ingredientes.pocaoIngredienteList;
                })
				.catch(function (errorResponse) {
					console.error(errorResponse);
                });

            function _openDesafio(idDesafio){
                FaseService.getDesafio(idDesafio)
					.then(function (successResponse) {
                        $mdDialog.show({
                            controller: 'DesafioDialogCtrl',
                            controllerAs: '$desafioDialog',
                            templateUrl: 'app/modules/sala-testadores/desafio/tmpl/desafio-dialog.tmpl.html',
                            clickOutsideToClose: true,
                            locals: { desafioDialogInfo: successResponse.data.desafio }
                        }).then(function(desafioId){
                        	localStorage.setItem("desafioId", desafioId);
                        	$state.go('authenticated.valoresDeEntrada');
						})/*.catch(function(desafioId){
							console.log("restarta")
                            if(desafioId !== undefined){
                                SalaTestadoresService.restartaDesafio(desafioId)
                                    .then(function(){
                                        localStorage.setItem("desafioId", desafioId);
                                        $state.go('authenticated.valoresDeEntrada');
                                    })
                                    .catch(function (errorResponse) {
                                        console.error("Restarta desafio >>> ", errorResponse)
                                    });
							}
						});*/

                    })
					.catch(function (errorResponse) {
						console.error("openDesafio >>> ", errorResponse);
                    })
			}

			context.getImageSrc = function(item){
            	var rootFolder = "app/assets/images/geral/".concat(item.ingrediente.nomeImagem);
            	return rootFolder.concat(item.situacaoIngrediente === 'ESCONDIDO' ? '-cinza.png' : '.png');
			};

			context.setItemHover = function(item, desafio){
                item.content = "url(app/assets/images/geral/"+desafio.imagem+"-iluminado.png)";
			};

            context.removeItemHover = function(item, desafio){
                item.content = "url(app/assets/images/geral/"+desafio.imagem+".png)";
            }
		}
})();
