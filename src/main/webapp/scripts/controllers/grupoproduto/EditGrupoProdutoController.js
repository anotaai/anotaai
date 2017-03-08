'use-strict'

angular.module('anotaai').controller('EditGrupoProdutoController', function(dataTransferObject, $scope, $timeout, $rootScope, $state, flash, GrupoProdutoResource, ProdutoResource, ProdutoGrupoProdutoResource, $q, $http, constant, $stateParams, Base64Service) {

	var self = this;
	$scope.produto = undefined;
	$scope.disabled = false;
	$scope.action = 'edit';
	$scope.currentPage = 0;
	$scope.pageSize = 10;
	$scope.pageRange = [];

	$scope.cancel = function() {
		$state.go('app.grupo-produto');
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
	
	$scope.get = function() {
		if ($stateParams.GrupoProdutoId) {
			var successCallback = function(response) {
				$scope.grupoProduto = response.entity;
			};
			var param = {GrupoProdutoId:Base64Service.decode($stateParams.GrupoProdutoId)};
			GrupoProdutoResource.get(param, successCallback, $rootScope.defaultErrorCallback);
		} else {
			$scope.cancel();
		}
		$timeout(function() {
			angular.element('#nome').focus();
		}, 0);
	};
	
	$scope.get();

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.grupoProduto);
	};
	
	$scope.save = function() {
		var elements = ['nome', 'descricao', 'saveGrupoProduto', 'cancel', 'deleteGrupoProduto']
		$rootScope.disableElements(elements);
		var successCallback = function(response){
			flash.setMessages(response.messages)
			$state.go('app.grupo-produto');
		};
		var errorCallback = function(response) {
			$rootScope.enableElements(elements);
			flash.setExceptionMessage(response.data.exception);
		};
		var grupoProduto = angular.copy($scope.grupoProduto);
		delete grupoProduto.produtos;
		var service = new GrupoProdutoResource(grupoProduto);
		service.$update(successCallback, errorCallback);
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
	
	//---------------- auto complete produto -----------------
	
	$scope.produtoSelecionado = function(value) {
		if (arguments.length) {
			$scope.produto = value;
			if (angular.isObject(value)) {
				delete value.iconClass;
				var produtoGrupoProduto = {}
				produtoGrupoProduto.produto = $scope.produto;
				produtoGrupoProduto.grupoProduto = {};
				produtoGrupoProduto.grupoProduto.id = $scope.grupoProduto.id;
				var successCallback = function(response) {
					if (!response.anotaaiExceptionMessages) {
						$scope.grupoProduto.produtos.push(response.entity);
						$scope.produto = '';
						flash.setMessages(response.messages);
					} else {
						flash.setExceptionMessage(response.anotaaiExceptionMessages);
					}
				};
				var errorCallback = function(response) {
					flash.setExceptionMessage(response.data.exception);
				};
				ProdutoGrupoProdutoResource.save(produtoGrupoProduto, successCallback, errorCallback);
			} else {
				if (value == '') {
					//TODO - remover o icone e a frase de nenhum registro encontrado
					//$scope.noResults = false;
				}
			}
		} else {
			return $scope.produto;
		}
	};
	
	$scope.modelOptions = {
		debounce: {
			'default': 500,
			'blur': 250
		},
		getterSetter: true
	};

	$scope.getLocation = function(val) {
		var promise = $http.get('rest/produto/search', {
			params: {
				query: val,
				start: 0,
				max: constant.AUTO_COMPLETE_MAX_RESULT,
				idGrupoProduto: dataTransferObject.grupoProduto.id
			}
		});
		return promise.then(function(response){
			return response.data;
		});
	};
	//-------------------------------------------------------
});