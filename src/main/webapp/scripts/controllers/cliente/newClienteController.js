'use strict';

angular.module('anotaai').controller('NewClienteController', function ($rootScope, $scope, $location, locationParser, flash, ClienteResource, EnumResource) {
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.cliente = $scope.cliente || {};
	$scope.cliente.usuario = $scope.cliente.usuario || {};
	$scope.cliente.usuario.telefone = $scope.cliente.usuario.telefone || {};
	$scope.cliente.usuario.telefone.ddi = $scope.cliente.usuario.telefone.ddi || '';
	$scope.cliente.usuario.telefone.ddd = $scope.cliente.usuario.telefone.ddd || '';
	$scope.cliente.usuario.telefone.numero = $scope.cliente.usuario.telefone.numero || '';
	$scope.cliente.usuario.telefone.numero = $scope.cliente.usuario.telefone.numero || '';
	$scope.telefone = '';
	$scope.confirmaSenha = '';
	
	$scope.$watch('telefone', function() {
		$scope.cliente.usuario.telefone = $rootScope.buildTelefone($scope.telefone);
	});
	
	$scope.save = function() {
		if ($scope.confirmaSenha == $scope.cliente.usuario.senha) {
			var successCallback = function(data, responseHeaders){
				$state.go('access.register-success');
			};
			var errorCallback = function(response) {
				console.log(response);
				if(response && response.data && response.data.message) {
					flash.setMessage({'type': 'error', 'text': response.data.message}, true);
				} else {
					flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
				}
			};
			ClienteResource.save($scope.cliente, successCallback, errorCallback);
		} else {
			$scope.confirmaSenha = '';
			$scope.cliente.usuario.senha = '';
			flash.setMessage({'type': 'error', 'text': 'Senha não confere.'});
		}
	};

	$scope.cancel = function() {
		$location.path("/Clientes");
	};

	$scope.estadoList = EnumResource.enum({EnumName:'estados'});

});
