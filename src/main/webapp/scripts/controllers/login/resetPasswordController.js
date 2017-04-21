'use strict';

angular.module('anotaai').controller('ResetPasswordController', function ($scope, $location, $timeout, locationParser, flash, UsuarioResource, EnumResource, $stateParams, $rootScope) {
	
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
					var telefoneStr = $scope.usuario.telefone.ddd.toString() + $scope.usuario.telefone.numero.toString();
					$scope.telefone = telefoneStr.replace(/^(\d{2})(\d)/g,"($1) $2");
					$scope.telefone = telefoneStr.replace(/(\d)(\d{4})$/,"$1-$2"); 
					angular.element('#senha').focus();
				} else if(response.data.exception) {
					$state.go('access.login');
				}
			};
			UsuarioResource.recuperarSenha($stateParams.activationCode, successCallback, $rootScope.defaultErrorCallback);
		} else {
			$state.go('access.login');
		}
	}());
	
	
	$scope.reset= function() {
			
		var successCallback = function(response) {
			flash.destroyAllMessages();
			if (response.data.isValid) {
				flash.setMessage(response.data.messages);
			} else {
				flash.setMessages(response.data.exception.anotaaiExceptionMessages);
			}
		};
		UsuarioResource.renewPassword($scope.usuario, successCallback, $rootScope.defaultErrorCallback);
	};

});
