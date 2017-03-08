'use strict';

angular.module('anotaai').controller('ProfileController', function (flash, $scope, $state, $rootScope, AuthenticationResource, UsuarioResource, Upload) {
	
	$scope.dataLoading = $rootScope.globals.login ? true : false;
	$scope.login = $rootScope.globals.login || {};
	$scope.fotoUrl = 'img/blank_avatar.png';
	
	$rootScope.$on('$loginChange', function (event, login) {
		$scope.login = login;
		$scope.dataLoading = true;
		if (login.usuario.fotoPerfil) {
			var mediaType = login.usuario.fotoPerfil.tipoArquivo.mediaType;
			var data = 'data:' + mediaType + ';base64,' + login.usuario.fotoPerfil.file;
			$scope.fotoUrl = data;			
		}
		
	});
	
	$rootScope.$on('$logoutChange', function (event) {
		$scope.dataLoading = false;
	});
	
	$scope.upload = function(file) {
		if (file) {
			Upload.upload({
				url : 'rest/fileUpload/upload',
				data : {
					file : file,
					size : file.size,
					name : file.name,
					tipoArquivo : file.type
				}
			}).then(
				function(resp) {
					var mediaType = resp.data.entity.tipoArquivo.mediaType;
					var data = 'data:' + mediaType + ';base64,' + resp.data.entity.file;
					$scope.fotoUrl = data;
				},
				function(resp) {
					console.log('Error status: ' + resp.status);
				},
				function(evt) {
					var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
					console.log('progress: ' + progressPercentage + '% ' + evt.config.data.entity.file.name);
				}
			);
		}
	};
	
	$scope.logout = function() {
		flash.destroyAllMessages();
		var login = angular.copy($scope.login);
		delete login.cookieSessionName;
		UsuarioResource.logout(login,
			function (response) {
				AuthenticationResource.clearCredentials();
				$scope.dataLoading = false;
				$rootScope.selectedRoute = '';
				$state.go('access');
			},
			function (response) {
				flash.setExceptionMessage(response.data);
			}
		);
	}
});
