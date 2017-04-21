
angular.module('anotaai').controller('NewGrupoProdutoController', function (dataTransferObject, $scope, $rootScope, $state, $timeout, $location, locationParser, flash, GrupoProdutoResource) {
	
	var object = this;
	object.dataTransferObject = dataTransferObject;
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.grupoProduto = $scope.grupoProduto || {};
	$scope.action = 'new';
	
	(function() {
		$timeout(function() {
			angular.element('#nome').focus();
		}, 0);
	})()

	$scope.save = function() {
		var elements = ['nome', 'descricao', 'saveGrupoProduto', 'cancel', 'deleteGrupoProduto'];
		$rootScope.disableElements(elements);
		var successCallback = function(response) {
			if (response.isValid) {
				flash.setMessages(response.messages);
				object.dataTransferObject.grupoProduto = response.entity;
				$rootScope.enableElements(elements);
				$state.go('app.grupo-produto-edit');
			} else {
				$rootScope.enableElements(elements);
				flash.setMessages(response.exception.anotaaiExceptionMessages)
			}
		};
		
		GrupoProdutoResource.save($scope.grupoProduto, successCallback, $rootScope.defaultErrorCallback);
	};
	
	$scope.cancel = function() {
		flash.destroyAllMessages();
		$state.go('app.grupo-produto');
	};
	
});