'use strict';

angular.module('anotaai').controller('RenewPasswordController', function ($scope, $location, $timeout, $uibModalInstance, flash, UsuarioResource, EnumResource, $state, $rootScope) {
	
	$scope.disabled = false;
	$scope.$location = $location;
	
	$scope.telefone = '';
	$scope.renewPassword = {};
	$scope.renewPassword.tipoAcesso = 'EMAIL';
	$scope.usuario = {};
	$scope.usuario.email = '';
	$scope.usuario.telefone;
	$scope.usuario.telefone = $scope.usuario.telefone || {};
	$scope.usuario.telefone.ddi = $scope.usuario.telefone.ddi || '';
	$scope.usuario.telefone.ddd = $scope.usuario.telefone.ddd || '';
	$scope.usuario.telefone.numero = $scope.usuario.telefone.numero || '';
	$scope.usuario.telefone.numero = $scope.usuario.telefone.numero || '';
	$scope.loading = false;
	
	$timeout(function() {
		EnumResource.load('tiposAcesso', 
			function(response) {
				$scope.tiposAcesso = response.data;
			}, $rootScope.defaultErrorCallback
		);
		angular.element('#email').focus();
	}, 0);
	 
	
	$scope.changeTipoAcesso = function(value) {
		switch (value) {
		case 'TELEFONE':
			$scope.usuario.email = '';
			$timeout(function(){
				angular.element('#telefone').focus();
			}, 0);
			break;
		case 'EMAIL':
			$scope.telefone = '';
			$scope.usuario.telefone = {};
			$timeout(function(){
				angular.element('#email').focus();
			}, 0);
			break;
		default:
			break;
		}
	};
	
	$scope.$watch('telefone', function() {
		if ($scope.telefone) {
			$scope.usuario.telefone = $rootScope.buildTelefone($scope.telefone);
			$timeout(function(){
				angular.element('#tipoAcesso').focus();
			}, 0);
		}
	});
	
	$scope.cancel = function() {
		 $uibModalInstance.dismiss('cancel');
	}
	
	$scope.renew= function() {
		var successCallback = function(response) {
			flash.destroyAllMessages();
			if (response.data.isValid) {
				flash.setMessages(response.data.messages);
				$scope.cancel();
			} else {
				flash.setMessages(response.data.exception.anotaaiExceptionMessages);
			}
		};
		UsuarioResource.solicitarMensagemAlteracaoSenha($scope.usuario, successCallback, $rootScope.defaultErrorCallback);
	};

});
