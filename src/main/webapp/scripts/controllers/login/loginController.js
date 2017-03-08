'use strict';

angular.module('anotaai').controller('LoginController', function (dataTransferObject, $scope, $state, $location, $timeout, $rootScope, $cookies, $http, $routeParams, $translate,$uibModal,locationParser, flash, UsuarioResource, AuthenticationResource, Base64Service, EnumResource) {
	
	$scope.disabled = false;
	$scope.$location = $location;
	
	$scope.telefone = '';
	$scope.userLogin = {};
	$scope.userLogin.keepAlive = false;
	$scope.userLogin.tipoAcesso = 'EMAIL';
	$scope.userLogin.usuario = {};
	$scope.userLogin.usuario.email;
	$scope.userLogin.usuario.telefone;
	$scope.userLogin.usuario.senha;
	$scope.userLogin.usuario.telefone = $scope.userLogin.usuario.telefone || {};
	$scope.userLogin.usuario.telefone.ddi = $scope.userLogin.usuario.telefone.ddi || '';
	$scope.userLogin.usuario.telefone.ddd = $scope.userLogin.usuario.telefone.ddd || '';
	$scope.userLogin.usuario.telefone.numero = $scope.userLogin.usuario.telefone.numero || '';
	$scope.userLogin.usuario.telefone.numero = $scope.userLogin.usuario.telefone.numero || '';
	$scope.loading = false;
	
	$timeout(function(){
		EnumResource.load('tiposAcesso', 
			function(response) {
				$scope.tiposAcesso = response.data;
			},
			function(response) {
				console.log(response);
			}
		);
		$scope.userLogin.usuario.email = 'anotaai@gmail.com';
		$scope.userLogin.usuario.senha = '10481';
		angular.element('#email').focus();
	}, 0);
	
	$scope.$watch('telefone', function() {
		if ($scope.telefone) {
			$scope.userLogin.usuario.telefone = $rootScope.buildTelefone($scope.telefone);
			$timeout(function(){
				angular.element('#senha').focus();
			}, 0);
		}
	});
	
	$scope.changeTipoAcesso = function(value) {
		switch (value) {
		case 'TELEFONE':
			$scope.userLogin.usuario.email = '';
			$timeout(function(){
				angular.element('#telefone').focus();
			}, 0);
			break;
		case 'EMAIL':
			$scope.telefone = '';
			$scope.userLogin.usuario.telefone = {};
			$timeout(function(){
				angular.element('#email').focus();
			}, 0);
			break;
		default:
			break;
		}
	};
	
	$scope.login = function() {
		
		$scope.loading = true;
		angular.element('#btn-login').attr('disabled', 'disabled');

		(function initController() {
			$rootScope.globals = {};
			$cookies.remove('globals');
			$http.defaults.headers.common.Authorization = 'Basic';
		})();
		
		flash.destroyAllMessages();
		UsuarioResource.login($scope.userLogin,
			function (response) {
				$scope.loading = false;
				flash.destroyAllMessages();
				AuthenticationResource.setCredentials(response.data);
				$state.go('app');
				angular.element('btn-login').removeAttr('disabled');
			},
			function (response) {
				$scope.loading = false;
				$scope.userLogin.usuario.senha = '';
				flash.setExceptionMessage(response.data);
				angular.element('btn-login').removeAttr('disabled');
			}
		);
	};
	
	function loadProfileImage(idUsuario) {
		$timeout(function() {
			UsuarioResource.loadProfileImage(idUsuario, 
				function(response) {
					$scope.tiposAcesso = response.data;
				},
				$rootScope.defaultErrorCallback
			);
			$scope.userLogin.usuario.email = 'anotaai@gmail.com';
			$scope.userLogin.usuario.senha = '10481';
			angular.element('#email').focus();
		}, 0);
	}
	
	$scope.renewPassword = function() {
		
		var modalInstance = $uibModal.open({
			templateUrl: 'views/Acesso/Login/renew-password.html',
			controller: 'RenewPasswordController'
		});
	};	

});
