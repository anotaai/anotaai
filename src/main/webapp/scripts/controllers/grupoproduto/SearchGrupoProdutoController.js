'use-strict'

angular.module('anotaai').controller('SearchGrupoProdutoController', function(dataTransferObject, $scope, $state, flash, $http, $filter, $log, GrupoProdutoResource, Base64Service) {

	var object = this;
	object.dataTransferObject = dataTransferObject;
	
	$scope.search={};
	$scope.currentPage = 0;
	$scope.pageSize = 10;
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
			$scope.searchResults = GrupoProdutoResource.queryAll(filter);
		} else {
			filter();
		}
	};
	
	$scope.edit = function(grupoProduto) {
		object.dataTransferObject.grupoProduto = grupoProduto;
		$state.go('app.grupo-produto-edit', {GrupoProdutoId: Base64Service.encode(grupoProduto.id.toString())});
	};
	
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
	
	$scope.remove = function(grupoProduto) {
		$scope.grupoProduto = new GrupoProdutoResource(grupoProduto);
		var successCallback = function(response) {
			if (response.isValid) {
				var index = $scope.searchResults.indexOf(grupoProduto);
				$scope.searchResults.splice(index, 1);
				$scope.filteredResults = $filter('searchFilter')($scope.searchResults, $scope);
				flash.setMessages(response.messages);
			} else {
				flash.setExceptionMessage(response.exception);
			}
		};
		var errorCallback = function(response) {
			flash.setExceptionMessage(response.data)
		};
		$scope.grupoProduto.$remove(successCallback, errorCallback);
	};
	
	$scope.performSearch();

});