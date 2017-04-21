'use strict';

angular.module('anotaai').controller('ResetPasswordController', function ($scope, $location, $state, locationParser, flash, UsuarioResource, EnumResource, $stateParams, constant, $rootScope) {
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.loading = false;
	$scope.senha = '';
	$scope.usuario = {};
	
	(function() {
		if ($stateParams.activationCode) {
			var successCallback = function(response) {
				if (response.data.entity) {
					$scope.usuario = response.data.entity;
					$scope.usuario.codigoAtivacao = $stateParams.activationCode;
					var telefoneStr = $scope.usuario.telefone.ddd.toString() + $scope.usuario.telefone.numero.toString();
					$scope.telefone = telefoneStr.replace(/^(\d{2})(\d)/g,"($1) $2");
					$scope.telefone = $scope.telefone.replace(/(\d)(\d{4})$/,"$1-$2"); 
					angular.element('#senha').focus();
				} else if(response.data.exception) {
					flash.setMessages(response.data.exception.anotaaiExceptionMessages);
					$state.go('access.login');
				}
			};
			UsuarioResource.recuperarUsuarioAlteracaoSenha($stateParams.activationCode, successCallback, $rootScope.defaultErrorCallback);
		} else {
			$state.go('access.login');
		}
	}());
	
	
	$scope.reset= function() {
		
		if ($scope.usuario.senha == $scope.senha) {
			var successCallback = function(response) {
				flash.destroyAllMessages();
				if (response.data.isValid) {
					flash.setMessages(response.data.messages);
				} else {
					flash.setMessages(response.data.exception.anotaaiExceptionMessages);
				}
				$state.go('access.login');
			};
			UsuarioResource.alterarSenha($scope.usuario, successCallback, $rootScope.defaultErrorCallback);
		} else {
			$scope.usuario.senha = '';
			$scope.senha = '';
			flash.setMessage(constant.MESSAGE_SENHA_NAO_CONFERE);
		}
		
	};

});
