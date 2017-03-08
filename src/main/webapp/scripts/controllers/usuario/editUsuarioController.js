'use-strict'

angular.module('anotaai').controller('EditUsuarioController', function($rootScope, $scope, $stateParams, $state, $location, flash, UsuarioResource, EnumResource) {
	var self = this;
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.usuario = $scope.usuario || {};
	$scope.usuario.telefone = $scope.usuario.telefone || {};
	$scope.usuario.telefone.ddd = $scope.usuario.telefone.ddi || '';
	$scope.usuario.telefone.ddd = $scope.usuario.telefone.ddd || '';
	$scope.usuario.telefone.numero = $scope.usuario.telefone.numero || '';
	$scope.telefone = '';
	
	$scope.$watch('telefone', function() {
		$scope.usuario.telefone = $rootScope.buildTelefone($scope.telefone);
	});
	
	$scope.get = function() {
		var successCallback = function(data){
			self.original = data;
			$scope.usuario = new UsuarioResource(self.original);
		};
		var errorCallback = function() {
			flash.setMessage({'type': 'error', 'text': 'O Usuário não pôde ser encontrado.'});
			$location.path("/Usuarios");
		};
		UsuarioResource.get({UsuarioId:$stateParams.UsuarioId}, successCallback, errorCallback);
	};

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.usuario);
	};

	$scope.save = function() {
		var successCallback = function() {
			flash.setMessage({'type':'success','text':'The usuario was updated successfully.'}, true);
			$scope.get();
		};
		var errorCallback = function(response) {
			if(response && response.data && response.data.message) {
				flash.setMessage({'type': 'error', 'text': response.data.message});
			} else {
				flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'});
			}
		};
		$scope.usuario.$update(successCallback, errorCallback);
	};

	$scope.cancel = function() {
		$location.path("/Usuarios");
	};

	$scope.remove = function() {
		var successCallback = function() {
			flash.setMessage({'type': 'error', 'text': 'The usuario was deleted.'});
			$location.path("/Usuarios");
		};
		var errorCallback = function(response) {
			if(response && response.data && response.data.message) {
				flash.setMessage({'type': 'error', 'text': response.data.message});
			} else {
				flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'});
			}
		}; 
		$scope.usuario.$remove(successCallback, errorCallback);
	};

	$scope.perfilList = EnumResource.enum({EnumName:'perfis'});

	$scope.get();
});