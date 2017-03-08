'use-strict'

angular.module('anotaai').controller('SearchConsumidorController', function(dataTransferObject, $scope, $http, $filter, $state, flash, ClienteConsumidorResource) {

	var object = this;
	object.clienteConsumidor = dataTransferObject;
	
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
	
	$scope.edit = function(index) {
		object.clienteConsumidor.consumidor = {};
		object.clienteConsumidor.consumidor.usuario = $scope.searchResults[index].usuario;
		$state.go('app.cliente-consumidor-edit');
	}

	$scope.performSearch = function() {
		var filter = function() {
			$scope.filteredResults = $filter('searchFilter')($scope.searchResults, $scope);
			$scope.currentPage = 0;
		};
		if ($scope.searchResults.length == 0) {
			$scope.searchResults = ClienteConsumidorResource.queryAll(filter);
		} else {
			filter();
		}
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

	$scope.performSearch();
});