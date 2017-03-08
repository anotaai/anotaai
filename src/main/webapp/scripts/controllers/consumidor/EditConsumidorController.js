'use strict';

angular.module('anotaai').controller('EditConsumidorController', function(dataTransferObject, $rootScope, $scope, $stateParams, $state, $location, $log, $timeout, flash, ClienteConsumidorResource, UsuarioResource, constant) {
	
	var self = this;
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.action = 'edit';
	$scope.editable = true;
	$scope.removable = true;
	$scope.isLoad = false;

	self.original = angular.copy(dataTransferObject);

	$scope.consumidorJaCadastrado = false;
	
	$scope.cancel = function() {
		flash.destroyAllMessages();
		$state.go('app.cliente-consumidor');
	};
	
	(function() {
		if (dataTransferObject.consumidor && dataTransferObject.consumidor.usuario) {
			$scope.telefone = dataTransferObject.consumidor.usuario.telefone.ddd.toString() + dataTransferObject.consumidor.usuario.telefone.numero.toString();
			$scope.clienteConsumidor = new ClienteConsumidorResource(dataTransferObject);
			var successCallback = function(response) {
				$scope.editable = response.data.isValid;
				if (!$scope.editable) {
					angular.element('#telefone').attr("disabled", "disabled");
					angular.element('#nome').attr("disabled", "disabled");
					angular.element('#email').attr("disabled", "disabled");
					flash.setExceptionMessage(response.data.exception);
				}
			};
			var errorCallback = function(response) {
				console.log(response);
			};
			ClienteConsumidorResource.isEditable(dataTransferObject.consumidor.usuario, successCallback, errorCallback);			
		} else {
			$scope.cancel();
		}
	}());
	
	$scope.$watch('telefone', function() {
		flash.destroyAllMessages();
		$scope.consumidorJaCadastrado = false;
		var telefone = $rootScope.buildTelefone($scope.telefone);
		$scope.clienteConsumidor.consumidor.usuario.telefone = telefone;
		if (telefone.ddi && telefone.ddd && telefone.numero && $scope.isLoad) {
			$scope.isLoad = true;
			var successCallback = function(response) {

				angular.element('#nome').removeAttr("disabled");
				angular.element('#email').removeAttr("disabled");
				angular.element('#nome').focus();
			};
			var errorCallback = function(response) {
				var clienteConsumidor = response.data.entity;
				angular.element('#nome').attr("disabled", "disabled");
				angular.element('#email').attr("disabled", "disabled");
				flash.setExceptionMessage(response.data.exception);
				$scope.isEditable = false;
				$timeout(function(){
					angular.element('#saveConsumidor').focus();
				}, 0);
				
			};
			ClienteConsumidorResource.findUsuarioByTelefone($scope.clienteConsumidor.consumidor.usuario.telefone, successCallback, errorCallback);
		} else {''
			angular.element('#nome').removeAttr("disabled");
			angular.element('#email').removeAttr("disabled");
		}
	});

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.clienteConsumidor);
	};

	$scope.recomendarEdicao = function() {
		flash.destroyAllMessages();
		var successCallback = function(response) {
			flash.setMessages(response.data.messages);
			$state.go('app.cliente-consumidor');
		};
		var errorCallback = function(response) {
			flash.setExceptionMessage(response.data.exception)
		};
		$scope.clienteConsumidor.consumidor.type = 'consumidor'
		ClienteConsumidorResource.recomendarEdicao($scope.clienteConsumidor, successCallback, errorCallback);
	}
	
	$scope.edit = function() {
		var resource = new UsuarioResource($scope.clienteConsumidor.consumidor.usuario);
		var successCallback = function(response) {
			flash.setMessages(response.messages);
			$state.go('app.cliente-consumidor');
		};
		var errorCallback = function(response) {
			   flash.setExceptionMessage(response.data.exception)
		};
		resource.$update(successCallback, errorCallback);
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

});