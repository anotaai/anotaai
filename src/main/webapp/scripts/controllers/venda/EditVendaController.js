'use strict';

angular.module('anotaai').controller('EditVendaController', function(dataTransferObject, $rootScope, $scope, $stateParams, $state, $location, $log, $timeout, flash, ClienteConsumidorResource, UsuarioResource, constant) {
	
	var self = this;
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.action = 'edit';
	$scope.editable = true;
	$scope.removable = true;
	$scope.isLoad = false;

	self.original = angular.copy(dataTransferObject);

	

});