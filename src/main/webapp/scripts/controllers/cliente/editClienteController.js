'use strict';

angular.module('anotaai').controller('EditClienteController', function($rootScope, $scope, $stateParams, $state, $location, $log, flash, ClienteResource, EnumResource) {
	
	var self = this;
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.cliente = $scope.cliente || {};
	$scope.cliente.usuario = $scope.cliente.usuario || {};
	$scope.cliente.usuario.telefone = $scope.cliente.usuario.telefone || {};
	$scope.cliente.usuario.telefone.ddi = $scope.cliente.usuario.telefone.ddi || '';
	$scope.cliente.usuario.telefone.ddd = $scope.cliente.usuario.telefone.ddd || '';
	$scope.cliente.usuario.telefone.numero = $scope.cliente.usuario.telefone.numero || '';
	$scope.telefone = '';
	
	$scope.$watch('telefone', function() {
		$scope.cliente.usuario.telefone = $rootScope.buildTelefone($scope.telefone);
	});
	
	$scope.get = function() {
		var successCallback = function(data){
			self.original = data;
			$scope.cliente = new ClienteResource(self.original);
			$scope.telefone  = $scope.cliente.usuario.telefone.ddd.toString() + $scope.cliente.usuario.telefone.numero.toString();
			$log.info($scope.cliente.usuario.senha);
		};
		var errorCallback = function() {
			flash.setMessage({'type': 'error', 'text': 'The cliente could not be found.'});
			$location.path("/Clientes");
		};
		ClienteResource.get({ClienteId:$stateParams.ClienteId}, successCallback, errorCallback);
	};

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.cliente);
	};

	$scope.save = function() {
		$log.info('editando cliente: ', $scope.cliente.id);
		var successCallback = function() {
			flash.setMessage({'type':'success','text':'The cliente was updated successfully.'}, true);
			$scope.get();
		};
		var errorCallback = function(response) {
			if(response && response.data && response.data.message) {
				flash.setMessage({'type': 'error', 'text': response.data.message}, true);
			} else {
				flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
			}
		};
		$scope.cliente.$update(successCallback, errorCallback);
	};

	$scope.cancel = function() {
		$location.path("/Clientes");
	};

	$scope.remove = function() {
		var successCallback = function() {
			flash.setMessage({'type': 'error', 'text': 'The cliente was deleted.'});
			$location.path("/Clientes");
		};
		var errorCallback = function(response) {
			if(response && response.data && response.data.message) {
				flash.setMessage({'type': 'error', 'text': response.data.message}, true);
			} else {
				flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
			}
		}; 
		$scope.cliente.$remove(successCallback, errorCallback);
	};
	
	EnumResource.load('estados', function(response) {
		$scope.estadoList = response.data;
	});
	
	$scope.get();
});