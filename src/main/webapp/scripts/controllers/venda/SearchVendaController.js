'use-strict'

angular.module('anotaai').controller('SearchVendaController', function(dataTransferObject, $scope, $http, $filter, $state, flash, VendaResource) {

	var object = this;
	object.venda = dataTransferObject;
	
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
	
	$scope.edit = function(venda) {
		object.dataTransferObject.venda = venda;
		$state.go('app.venda-edit', {VendaId: Base64Service.encode(venda.id.toString())});
	};

	$scope.performSearch = function() {
		var filter = function() {
			$scope.filteredResults = $filter('searchFilter')($scope.searchResults, $scope);
			$scope.currentPage = 0;
		};
		if ($scope.searchResults.length == 0) {
			$scope.searchResults = VendaResource.queryAll({dataInicial:'09/12/1980 00:00:00', dataFinal:'09/12/1980 23:59:59'}, filter);
			
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