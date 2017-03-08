angular.module('anotaai').controller('MenuController', function($scope, $log, $rootScope, $location, $state, flash, MenuResource) {
	
	$scope.login = $rootScope.globals && $rootScope.globals.login || {};
	$scope.nome =  $scope.login && $scope.login.primeiroNome || '';
	$scope.dataLoading = $scope.login.usuario ? true : false;
	$scope.listMenu = $scope.listMenu || [];
	$scope.root = $rootScope;
	
	$rootScope.$on('$loginChange', function (event, data) {
		$scope.login = data;
		$scope.get();
	});
	
	$rootScope.$on('$logoutChange', function (event) {
		$scope.dataLoading = false;
	});
	
	$scope.matchesRoute = function(route) {
		return $rootScope.selectedRoute == route;
	};
	
	$scope.get = function() {
		if ($scope.login.usuario) {
			$scope.dataLoading = true;
			MenuResource.getMenu('principal', 
				function (result) {
					$scope.listMenu = result.data;
				},
				function (result) {
					$rootScope.defaultErrorCallback();
				}
			);
		}
	};
	
	$scope.home = function() {
		$rootScope.selectedRoute = '';
		$state.go('app');
	}
	
	$scope.get();
	
});