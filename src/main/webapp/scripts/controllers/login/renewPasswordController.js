'use strict';

angular.module('anotaai').controller('RenewPasswordController', function ($scope, $location, $timeout,$uibModalInstance,locationParser, flash, UsuarioResource, EnumResource,constant) {
	
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
	
	$timeout(function(){
		EnumResource.load('tiposAcesso', 
			function(response) {
				$scope.tiposAcesso = response.data;
			},
			function(response) {
				console.log(response);
			}
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
		
		if ($scope.confirmaSenha == $scope.usuario.senha) {
			
			var successCallback = function(response){
	        	flash.destroyAllMessages(); 
	        	var param = $scope.renewPassword.tipoAcesso == 'TELEFONE' ? 'sms' : 'e-mail';
	        	
				if (!response.anotaaiExceptionMessages) {
					
					$scope.usuario.senha = '';
					$scope.confirmaSenha = '';
					
					flash.setMessage({
						type: {type: constant.TYPE_MESSAGE.SUCCESS},
						key: 'renew.password.success',
						isKey: true,
						time: constant.MESSAGE_TIME,
						params: [param]
					});
					
				} else {
					flash.setExceptionMessage(response);
				}
	        };
	        var errorCallback = function(response) {
	        	flash.setExceptionMessage(response);
	        };
	        
	        UsuarioResource.renewPassword($scope.usuario, successCallback, errorCallback);
			
		} else {
			senhaInvalida();	
		}
        
		
    };
    
    function senhaInvalida() {
		$scope.usuario.senha = '';
		$scope.confirmaSenha = '';
		flash.setMessage({
			'type': constant.TYPE_MESSAGE.ERROR,
			'key': 'senha.nao.confere',
			'isKey': true
		});
		angular.element("#senha").focus();
	}
	


});
