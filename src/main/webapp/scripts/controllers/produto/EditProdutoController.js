'use strict';

angular.module('anotaai').controller('EditProdutoController', function(dataTransferObject, $scope, $state, $stateParams, $http, $rootScope, $timeout, $routeParams, ProdutoResource, DisponibilidadeResource, ItemReceitaResource, EnumResource, $location, flash, constant, $log, Base64Service) {
	
	$scope.disabled = false;
	$scope.produto = $scope.produto || {};
	$scope.produto.estoque = {};
	$scope.produto.diasDisponibilidade = [];
	$scope.produto.itensReceita = [];
	$scope.action = 'edit';
	$scope.itemReceita = {};
	$scope.itemReceita.ingrediente = undefined;
	$scope.selecionado = false;
	var self = this;
	
	$scope.cancel = function() {
		$state.go('app.produto');
	};
	
	$timeout(function() {
		if ($stateParams.ProdutoId) {
			var idProduto = Base64Service.decode($stateParams.ProdutoId);
			var successCallback = function(response) {
				self.original = angular.copy(response.entity);
				$scope.produto = new ProdutoResource(response.entity);
				//os itens de receita sao incluidos e excluidos imediatamente portanto nao sao considerados 
				//criterios de diferenca entre os objetos
				self.original.itensReceita = $scope.produto.itensReceita;
				$scope.produto.diasDisponibilidade = $scope.produto.diasDisponibilidade || [];
				$scope.produto.itensReceita = $scope.produto.itensReceita || [];
			}
			ProdutoResource.get({ProdutoId: idProduto}, successCallback, $rootScope.defaultErrorCallback);
		} else {
			$scope.cancel();
		}
		//carrega a lista de unidades de mendia para selecao
		EnumResource.load('unidadesmedida',
			function(response) {
				$scope.unidadesMedida = response.data;
			},
			$rootScope.defaultErrorCallback
		);
		
		var handlerSuccess = function(response) {
			var dias = [];
			dias.push({id: 'TODOS', text: 'Todos os dias'});
			var item = undefined;
			angular.forEach(response.data, function(dia) {
				var selecionado = itemSelecionados(dia);
				console.log(selecionado);
				dias.push({id: dia.type, text: dia.descricao, selected: selecionado});
			});
			$scope.diasSemanaList = response.data;
			jQuery('#disponibilidades').select2({
				placeholder: $rootScope.translateMessage('msg.dias.disponibilidade.produto', null),
				allowClear: true,
				data: dias,
				closeOnSelect: false,
			});
			jQuery('#disponibilidades').change(function (e) {
				$scope.produto.diasDisponibilidade = [];
				var selects = jQuery('#disponibilidades').select2('data');
				//respeita a ordem que os elementos foram inseridos, se selecionou a opcao todos os dias
				if (selects.length > 0 && selects[0].id == 'TODOS') {
					selects = [];
					updateDisponibilidadeValues();
					jQuery('#disponibilidades').select2({
						placeholder: $rootScope.translateMessage('msg.dias.disponibilidade.produto', null),
						allowClear: true,
						closeOnSelect: false,
					});
					$timeout(function() {
						angular.element('#autoComplete').focus();
					}, 0);
				}
				angular.forEach(selects, function(dia) {
					$scope.produto.diasDisponibilidade.push({dia: dia.id});
				});
			});
		}
		EnumResource.load('diasemana', handlerSuccess, $rootScope.defaultErrorCallback);
		angular.element('#codigo').focus();
		
	}, 0);
	
	function itemSelecionados(dia) {
		var selecionado = $scope.produto.diasDisponibilidade.filter(function(diaSelecionado) {
			return diaSelecionado.dia.type === dia.type;
		});
		return selecionado.length > 0;
		
	}

	function updateDisponibilidadeValues() {
		var itensSelecionados = [];
		angular.forEach($scope.diasSemanaList, function(disponibilidade) {
			itensSelecionados.push(disponibilidade.type);
		});
		jQuery('#disponibilidades').val(itensSelecionados).change();
	}
	
	$scope.editarItemReceita = function(itemReceita, index) {
		$scope.itemReceita = itemReceita;
		$scope.selecionado = true;
		$scope.produto.itensReceita.splice(index, 1);
		$timeout(function() {
			angular.element('#qtdItemReceita').focus();
		}, 0);
	};
	
	$scope.numberOfPages = function() {
		if ($scope.grupoProduto && $scope.grupoProduto.produtos) {
			var result = Math.ceil($scope.grupoProduto.produtos.length / $scope.pageSize);
			var max = (result == 0) ? 1 : result;
			$scope.pageRange = [];
			for(var ctr = 0; ctr < max; ctr++) {
				$scope.pageRange.push(ctr);
			}
			return max;			
		}
	};

	$scope.isClean = function() {
		return $rootScope.equals(self.original, $scope.produto);
	};
	
	$scope.save = function() {
		var elements = ['cancel']
		$rootScope.disableElements(elements);
		var successCallback = function(response){
			flash.setMessages(response.messages)
			$state.go('app.produto');
		};
		var errorCallback = function(response) {
			$rootScope.enableElements(elements);
			flash.setExceptionMessage(response.data.exception);
		};
		$scope.produto.$update(successCallback, errorCallback);
	};

	$scope.remove = function(index, produtoGrupoProduto) {
		var successCallback = function(response) {
			$scope.grupoProduto.produtos.splice(index, 1);
			$scope.filteredResults = $filter('searchFilter')($scope.grupoProduto.produtos, $scope);
			flash.setMessages(response.messages);
		};
		var errorCallback = function(response) {
			$rootScope.enableElements(elements);
			flash.setExceptionMessage(response.data.exception);
		};
		var service = new ProdutoGrupoProdutoResource(produtoGrupoProduto);
		service.$remove(successCallback, errorCallback);
	};
	
	$scope.create = function(idProdutoGrupoProduto) {
		flash.destroyAllMessages();
		$state.go('app.grupo-produto-new');
	};
	
	$scope.excluirItemReceita = function(itemReceita, index) {
		var successCallback = function(response) {
			$scope.produto.itensReceita.splice(index, 1);
			flash.setMessages(response.data.messages);
		};
		ProdutoResource.deleteItemReceita(itemReceita, successCallback, $rootScope.defaultErrorCallback);
	};
	
	$scope.codigoAutomatico = function() {
		$scope.produto.codigo = '';
	};
	
	//---------------- auto complete produto -----------------
	
	$scope.addItemReceita = function() {
		
		var jaIncluido = $scope.produto.itensReceita.some(function(element, index) {
			return element.ingrediente.id === $scope.itemReceita.ingrediente.id;
		});
		if (!jaIncluido) {
			var successCallback = function(response) {
				$scope.produto.itensReceita.push(response.data.entity);
				flash.setMessages(response.data.messages)
			};
			if (!$scope.itemReceita.id) {
				ProdutoResource.addItemReceita($scope.itemReceita, successCallback, $rootScope.defaultErrorCallback);
			} else {
				ProdutoResource.editItemReceita($scope.itemReceita, successCallback, $rootScope.defaultErrorCallback);
			}
			$scope.itemReceita = {};
			$scope.itemReceita.ingrediente = undefined;
		} else {
			var descricao = $scope.itemReceita.ingrediente.descricao;
			$scope.itemReceita.ingrediente = descricao;
			flash.setMessage({
				isKey: true,
				key: 'msg.itemreceita.ja.incluido',
				time: constant.MESSAGE_TIME,
				type: {type: constant.TYPE_MESSAGE.ERROR},
				params: [descricao]
			});
		}
		$scope.selecionado = false;
		$timeout(function() {
			angular.element('#autoComplete').focus();
		}, 100);
		
	}
	
	$scope.produtoSelecionado = function(value) {
		if (arguments.length) {
			$scope.itemReceita.ingrediente = value;
			$scope.itemReceita.produto = $scope.produto;
			$scope.selecionado = angular.isObject(value);
			if ($scope.selecionado) {
				$scope.itemReceita.quantidade = null;
				$timeout(function() {
					angular.element('#qtdItemReceita').focus();
				}, 100);
			} else {
				if (value == '') {
					//TODO - remover o icone e a frase de nenhum registro encontrado
					//$scope.noResults = false;
				}
			}
		} else {
			return $scope.itemReceita.ingrediente;
		}
	};
	
	$scope.modelOptions = {
		debounce: {
			'default': 500,
			'blur': 250
		},
		getterSetter: true
	};

	$scope.getProdutos = function(val) {
		var ingredientes = [];
		ingredientes.push($scope.produto.id);
		angular.forEach($scope.produto.itensReceita, function(itemReceita) {
			ingredientes.push(itemReceita.ingrediente.id);
		});
		var promise = $http.get('rest/produto/searchProdutosParaReceita', {
			params: {
				query: val,
				start: 0,
				max: constant.AUTO_COMPLETE_MAX_RESULT,
				produtosDaReceita: ingredientes
			}
		});
		return promise.then(function(response){
			return response.data;
		});
	};
	
	$scope.itemProdutoPreenchido = function() {
		return $scope.selecionado && $scope.itemReceita.quantidade > 0;
	}
	
	//-------------------------------------------------------
});