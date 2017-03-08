'use strict';

angular.module('anotaai').controller('NewConsumidorController', function ($rootScope, $scope, $location, $state, $timeout, locationParser, flash, ClienteConsumidorResource, constant) {
	
	var self = this;
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.clienteConsumidor = new ClienteConsumidorResource($scope.clienteConsumidor) || new ClienteConsumidorResource();
	$scope.clienteConsumidor.consumidor = $scope.clienteConsumidor.consumidor || {};
	$scope.clienteConsumidor.consumidor.usuario = $scope.clienteConsumidor.consumidor.usuario || {};
	$scope.clienteConsumidor.consumidor.usuario.telefone = $scope.clienteConsumidor.consumidor.usuario.telefone || {};
	$scope.telefone = '';
	$scope.action = 'new';
	$scope.consumidorJaCadastrado = false;
	$scope.removable = false;
	$scope.editable = false;
	flash.destroyAllMessages();
	
	(function() {
		$timeout(function(){
			angular.element('#nome').focus();
		}, 0);
	})()
	
	self.original = angular.copy($scope.clienteConsumidor);
	
	$scope.$watch('telefone', function() {
		flash.destroyAllMessages();
		$scope.consumidorJaCadastrado = false;
		var telefone = $rootScope.buildTelefone($scope.telefone);
		$scope.clienteConsumidor.consumidor.usuario.telefone = telefone;
		if (telefone.ddi && telefone.ddd && telefone.numero) {
			var successCallback = function(response) {
				if (response.data.isValid) {
					clean();
					$scope.editable = false;
					$scope.consumidorJaCadastrado = false;
					$scope.removable = false;
					$timeout(function(){
						angular.element('#nome').focus();						
					}, 0);
				} else {
					var clienteConsumidor = response.data.entity;
					$scope.clienteConsumidor = clienteConsumidor;
					angular.element('#nome').attr("disabled", "disabled");
					angular.element('#email').attr("disabled", "disabled");
					if (response.data.exception) {
						$scope.removable = true;
						$scope.editable = false;
						flash.setExceptionMessage(response.data.exception);
						$scope.consumidorJaCadastrado = true;
						$timeout(function(){
							angular.element('#saveConsumidor').attr("disabled", "disabled");
							angular.element('#limpar').focus();
						}, 0);
					} else {
						$scope.removable = false;
						$scope.editable = false;
						flash.setMessages(response.data.messages);
						$timeout(function(){
							angular.element('#saveConsumidor').focus();
						}, 0);
					}
				}
			};
			var errorCallback = function(response) {
				
			};
			ClienteConsumidorResource.findUsuarioByTelefone($scope.clienteConsumidor.consumidor.usuario.telefone, successCallback, errorCallback);
		} else {
			$scope.removable = false;
			clean();
		}
	});
	
	function clean() {
		$scope.clienteConsumidor.id = null;
		$scope.clienteConsumidor.cliente = null;
		$scope.clienteConsumidor.consumidor.id = null;
		$scope.clienteConsumidor.consumidor.usuario.id = null;
		$scope.clienteConsumidor.consumidor.usuario.nome = null;
		$scope.clienteConsumidor.consumidor.usuario.email = null;
		angular.element('#nome').removeAttr("disabled");
		angular.element('#email').removeAttr("disabled");
	}
	
	$scope.cleanForm = function() {
		clean();
		$scope.clienteConsumidor.consumidor.usuario.telefone =  {};
		$scope.telefone = '';
		$rootScope.cleanMessages();
		$timeout(function(){
			angular.element('#telefone').focus();
		}, 0);
	}
	
	$scope.save = function() {
		var commands = ['recomendaEdicao', 'saveConsumidor', 'editConsumidor', 'cancel', 'deleteConsumidor', 'limpar'];
		$rootScope.disableElements(commands);
		var successCallback = function(response) {
			flash.destroyAllMessages();
			if (!response.anotaaiExceptionMessages) {
				flash.setMessage({
					type: constant.TYPE_MESSAGE.SUCCESS,
					key: 'consumidor.gravado.sucesso',
					isKey: true,
					time: constant.MESSAGE_TIME,
					params: [$scope.clienteConsumidor.consumidor.usuario.nome]
				});
				$state.go('app.cliente-consumidor');
			} else {
				$rootScope.enableElements(commands);
				flash.setExceptionMessage(response);
			}
		};
		var errorCallback = function(response) {
			$rootScope.enableElements(commands);
			flash.setMessage(response);
		};
		$scope.clienteConsumidor.consumidor.type = 'consumidor';
		ClienteConsumidorResource.save($scope.clienteConsumidor, successCallback, errorCallback);
	};
	
	$scope.cancel = function() {
		flash.destroyAllMessages();
		$state.go('app.cliente-consumidor');
	};
	
	$scope.isClean = function() {
		$scope.ConsumidorForm;
		var hasTelefone = $scope.telefone == null || $scope.telefone == '' || $scope.telefone === undefined;
		var hasEmail = $scope.clienteConsumidor.consumidor.usuario.nome == null || $scope.clienteConsumidor.consumidor.usuario.nome == '';
		var hasNome = $scope.clienteConsumidor.consumidor.usuario.email == null || $scope.clienteConsumidor.consumidor.usuario.email == '';
		return hasTelefone && hasEmail && hasNome;
	}
	
});
