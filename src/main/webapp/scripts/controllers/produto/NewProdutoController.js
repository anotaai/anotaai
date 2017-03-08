'use-strict'

angular.module('anotaai').controller('NewProdutoController', function (dataTransferObject, $scope, $rootScope, $state, $http, $timeout, locationParser, flash, ProdutoResource, EnumResource, constant, Base64Service) {
	
	var object = this;
	object.dataTransferObject = dataTransferObject;
	$scope.disabled = false;
	$scope.produto = $scope.produto || {};
	$scope.produto.estoque = {};
	$scope.produto.itensReceita = [];
	$scope.produto.diasDisponibilidade = [];
	$scope.action = 'new';
	$scope.itemReceita = {};
	$scope.itemReceita.ingrediente = undefined;
	$scope.selecionado = false;
	$scope.diasSemanaList = undefined;
	
	$timeout(function() {
		EnumResource.load('unidadesmedida',
			function(response) {
				$scope.unidadesMedida = response.data;
			},
			$rootScope.defaultErrorCallback
		);
		var handlerSuccess = function(response) {
			var dias = response.data;
			$scope.diasSemanaList = dias;
			var data = [];
			data.push({id: 'TODOS', text: 'Todos os dias'});
			angular.forEach(dias, function(dia) {
				data.push({id: dia.type, text: dia.descricao, selected: true});
				$scope.produto.diasDisponibilidade.push({dia: dia.type});
			});
			jQuery('#disponibilidades').select2({
				placeholder: $rootScope.translateMessage('msg.dias.disponibilidade.produto', null),
				allowClear: true,
				data: data,
				closeOnSelect: false,
			});
			jQuery('#disponibilidades').change(function (e) {
				$scope.produto.diasDisponibilidade = [];
				var selects = jQuery('#disponibilidades').select2('data');
				//respeita a ordem que os elementos foram inseridos, se selecionou a opcao todos os dias
				if (selects.length > 0 && selects[0].id == 'TODOS') {
					selects = [];
					selectAllDisponibilidades();
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
	}, 0);

	function selectAllDisponibilidades() {
		var itensSelecionados = [];
		angular.forEach($scope.diasSemanaList, function(dia) {
			itensSelecionados.push(dia.type);
		});
		jQuery('#disponibilidades').val(itensSelecionados).change();
	}
	
	$scope.save = function() {
		var elements = ['codigo', 'descricao', 'descricaoResumida', 'unidadeMedida', 'precoVenda', 'autoComplete', 'qtdItemReceita', 'saveProduto', 'cancel', 'cancel'];
		$rootScope.disableElements(elements);
		var successCallback = function(response) {
			flash.destroyAllMessages();
			if (response.isValid) {
				flash.setMessages(response.messages);
				object.dataTransferObject.produto = response.entity;
				$state.go('app.produto-edit', {ProdutoId: Base64Service.encode(object.dataTransferObject.produto.id.toString())});
			} else {
				flash.setExceptionMessage(response.exception);
			}
			$rootScope.enableElements(elements);
		};
		ProdutoResource.save($scope.produto, successCallback, $rootScope.defaultErrorCallback);
	};
	
	$scope.cancel = function() {
		$state.go("app.produto");
	};
	
	$scope.editarItemReceita = function(itemReceita, index) {
		$scope.itemReceita = itemReceita;
		$scope.selecionado = true;
		$scope.produto.itensReceita.splice(index, 1);
		$timeout(function() {
			angular.element('#qtdItemReceita').focus();
		}, 0);
	};
	
	$scope.excluirItemReceita = function(itemReceita, index) {
		$scope.produto.itensReceita.splice(index, 1);
	};
	
	$scope.codigoAutomatico = function() {
		if ($scope.produto.codigoGerado) {
			$scope.produto.codigo = '';
			angular.element('#codigo').attr('disabled', 'disabled');
		} else {
			angular.element('#codigo').removeAttr('disabled');
		}
	};
	
	//---------------- auto complete produto -----------------
	
	$scope.addItemReceita = function() {
		
		var jaIncluido = $scope.produto.itensReceita.some(function(element, index) {
			return element.ingrediente.id === $scope.itemReceita.ingrediente.id;
		});
		if (!jaIncluido) {
			$scope.produto.itensReceita.push($scope.itemReceita);
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