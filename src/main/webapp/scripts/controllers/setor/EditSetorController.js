'use-strict'

angular.module('anotaai').controller('EditSetorController', function(dataTransferObject, $scope, $timeout, $rootScope, $state, $stateParams, flash, SetorResource, $q, $http, constant, Base64Service) {

	var object = this;
	object.dataTransferObject = dataTransferObject;
	$scope.produto = undefined;
	$scope.disabled = false;
	$scope.action = 'edit';
	$scope.currentPage = 0;
	$scope.pageSize = 10;
	$scope.pageRange = [];

	$scope.cancel = function() {
		$state.go('app.setor');
	};
	
	$scope.get = function() {
		if ($stateParams.SetorId) {
			$scope.setor = new SetorResource(dataTransferObject.setor);
			var successCallback = function(response) {
				$scope.setor = response.entity;
			};
			var errorCallback = function(response) {
				console.log(response);
			};
			SetorResource.get({SetorId:Base64Service.decode($stateParams.SetorId)}, successCallback, errorCallback);
		} else {
			$scope.cancel();
		}
		$timeout(function() {
			angular.element('#nome').focus();
		}, 0);
	};
	
	$scope.get();

	$scope.isClean = function() {
		return angular.equals(object.original, $scope.setor);
	};
	
	$scope.save = function() {
		var elements = ['nome', 'descricao', 'saveSetor', 'cancel', 'deleteSetor', 'novoSetor']
		$rootScope.disableElements(elements);
		var setor = angular.copy($scope.setor);
		var successCallback = function(response){
			flash.setMessages(response.messages);
			object.dataTransferObject.setor = setor;
			$state.go('app.setor-edit', {SetorId: Base64Service.encode(object.dataTransferObject.setor.id.toString())});
		};
		var errorCallback = function(response) {
			$rootScope.enableElements(elements);
			flash.setExceptionMessage(response.data.exception);
		};
		var service = new SetorResource(setor);
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
	
	$scope.create = function() {
		flash.destroyAllMessages();
		$state.go('app.setor-new');
	};
	
	$scope.getGruposProduto = function(query) {
		var promise = $http.get('rest/grupoproduto/recuperarPorDescricao', {
			params: {
				query: query,
				start: 0,
				max: constant.AUTO_COMPLETE_MAX_RESULT,
				idSetor: $scope.setor.id
			}
		});
		return promise.then(function(response) {
			return response.data;
		});
	};

	$scope.addGrupoProduto = function() {
		object.dataTransferObject.setor = $scope.setor;
		$state.go('app.grupo-produto-edit', {SetorId: Base64Service.encode($scope.setor.id.toString())});
	};
});