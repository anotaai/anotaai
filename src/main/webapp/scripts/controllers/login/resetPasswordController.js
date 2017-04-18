'use strict';

angular.module('anotaai').controller('RenewPasswordController', function ($scope, $location, $timeout, $uibModalInstance, locationParser, flash, UsuarioResource, EnumResource, constant, $rootScope) {
	
	$scope.disabled = false;
	$scope.$location = $location;
	
	$scope.telefone = '';
	$scope.login = {};
	$scope.login.usuario = {};
	$scope.login.usuario.email = '';
	$scope.login.usuario.telefone;
	$scope.login.usuario.telefone = $scope.usuario.telefone || {};
	$scope.login.usuario.telefone.ddi = $scope.usuario.telefone.ddi || '';
	$scope.login.usuario.telefone.ddd = $scope.usuario.telefone.ddd || '';
	$scope.login.usuario.telefone.numero = $scope.usuario.telefone.numero || '';
	$scope.login.usuario.telefone.numero = $scope.usuario.telefone.numero || '';
	$scope.loading = false;
	
	(function() {
		if ($stateParams.activationCode) {
			var successCallback = function(response) {
				if (response.data.entity) {
					$scope.login.usuario = response.data.entity;
					$scope.telefone = $scope.usuario.telefone.ddd.toString() + $scope.usuario.telefone.numero.toString();
					angular.element('#email').attr("disabled", "disabled");
					angular.element('#telefone').attr("disabled", "disabled");
					angular.element('#senha').focus();
				} else if(response.data.exception) {
					$state.go('access.login');
				}
			};			
			var errorCallback = function(response) {
				flash.setExceptionMessage(response.data);				
			};
			UsuarioResource.userByActivationCode($stateParams.activationCode, successCallback, errorCallback);
		} else {
			$state.go('access.login');
		}
	}());
	
	
	$scope.reset= function() {
			
		var successCallback = function(response) {
			flash.destroyAllMessages();
			if (response.data.isValid) {
				flash.setMessages(response.anotaaiExceptionMessages);
			} else {
				flash.setExceptionMessage(response);
			}
		};
		UsuarioResource.renewPassword($scope.usuario, successCallback, $rootScope.defaultErrorCallback);
	};

});
