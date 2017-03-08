'use strict';

angular.module('anotaai').controller('ActivateController', function ($scope, flash, $stateParams, $state, $location, UsuarioResource) {
	$scope.activate = function() {
		var path = $location.path();
		if (path.startsWith('/access/Activate/')) {
			var ac = $stateParams.activationCode;
			UsuarioResource.activation(ac,
				function(response) {
					if (response.data.isValid) {
						$state.go('access.success-activate');
					} else {
						$state.go('access.login');
					}
				},
				function(response) {
					if (response.data.exception) {
						flash.setExceptionMessage(response.data.exception);
						$state.go('access.invalidate');
					} else {
						$state.go('main');
					}
				}
			);		
		}
	};
	$scope.activate();
});
