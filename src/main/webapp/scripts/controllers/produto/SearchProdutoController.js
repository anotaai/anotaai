'use-strict'

angular.module('anotaai').controller('SearchProdutoController', function(dataTransferObject, $scope, $state, $rootScope, $http, $filter, ProdutoResource, flash, Base64Service) {

	var object = this;
	object.dataTransferObject = dataTransferObject;
	
	$scope.search={};
	$scope.currentPage = 0;
	$scope.pageSize= 10;
	$scope.searchResults = [];
	$scope.filteredResults = [];
	$scope.pageRange = [];
	
	$scope.numberOfPages = function() {
		var result = Math.ceil($scope.filteredResults.length/$scope.pageSize);
		var max = (result == 0) ? 1 : result;
		$scope.pageRange = [];
		for(var ctr=0;ctr<max;ctr++) {
			$scope.pageRange.push(ctr);
		}
		return max;
	};

	$scope.performSearch = function() {
		var filter = function() {
			$scope.filteredResults = $filter('searchFilter')($scope.searchResults, $scope);
			$scope.currentPage = 0;
		};
		if ($scope.searchResults.length == 0) {
			$scope.searchResults = ProdutoResource.queryAll(filter);
		} else {
			filter();
		}
	}
	
	$scope.previous = function() {
		if($scope.currentPage > 0) {
			$scope.currentPage--;
		}
	};
	
	$scope.next = function() {
		if($scope.currentPage < ($scope.numberOfPages() - 1) ) {
			$scope.currentPage++;
		}
	};
	
	$scope.setPage = function(n) {
		$scope.currentPage = n;
	};
	
	$scope.edit = function(produto) {
		object.dataTransferObject.produto = produto;
		$state.go('app.produto-edit', {ProdutoId: Base64Service.encode(produto.id.toString())});
	};
	
	$scope.remove = function(produto, index) {
		$scope.produto = new ProdutoResource(produto);
		var successCallback = function(response) {
			if (response.isValid) {
				var temp = $scope.searchResults.splice(index, 1);
				$scope.filteredResults = $filter('searchFilter')($scope.searchResults, $scope);
				flash.setMessages(response.messages);
			} else {
				flash.setExceptionMessage(response.exception);
			}
		};
		var errorCallback = function(response) {
			flash.setExceptionMessage(response.data)
		};
		$scope.produto.$remove(successCallback, errorCallback);
	};

	$scope.performSearch();
});