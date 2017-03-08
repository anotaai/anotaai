
angular.module('anotaai').controller('NewSetorController', function (dataTransferObject, $scope, $rootScope, $state, $timeout, $location, locationParser, flash, SetorResource, Base64Service) {
	
	var object = this;
	object.dataTransferObject = dataTransferObject;
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.setor = $scope.setor || {};
	$scope.action = 'new';
	
	(function() {
		$timeout(function() {
			angular.element('#nome').focus();
		}, 0);
	})()

	$scope.save = function() {
		var elements = ['nome', 'descricao', 'saveSetor', 'cancel', 'deleteSetor', 'novoSetor'];
		$rootScope.disableElements(elements);
		var successCallback = function(response) {
			if (response.isValid) {
				flash.setMessages(response.messages);
				object.dataTransferObject.setor = response.entity;
				$rootScope.enableElements(elements);
				$state.go('app.setor-edit', {SetorId: Base64Service.encode(object.dataTransferObject.setor.id.toString())});
			} else {
				$rootScope.enableElements(elements);
				flash.setExceptionMessage(response.exception)
			}
		};
		var errorCallback = function(response) {
			flash.setMessage(response);
			$rootScope.enableElements(elements);
		};
		SetorResource.save($scope.setor, successCallback, errorCallback);
	};
	
	$scope.cancel = function() {
		flash.destroyAllMessages();
		$state.go('app.setor');
	};
	
});