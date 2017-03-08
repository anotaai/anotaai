'use-strict'

angular.module('anotaai').directive('aiConfirmClick', ['$q', 'dialogModal', function($q, dialogModal) {
	return {
		link: function (scope, element, attrs) {
			var ngClick = attrs.ngClick.replace('confirmClick()', 'true').replace('confirmClick(', 'confirmClick(true,');
			
			var message = attrs.aiMessage;
			var title = attrs.aiTitle;
			var okButton = attrs.aiOkButtonTitle;
			var cancelButton = attrs.aiCancelButtonTitle;
			
			scope.confirmClick = function(confirmad) {
				if (confirmad === true) {
					return true;
				}
				dialogModal(message, title, okButton, cancelButton).result.then(function() {
					scope.$eval(ngClick);
				});
				return false;
			};
		}
	}
}]).service('dialogModal', ['$uibModal', function($uibModal) {
	return function (message, title, okButton, cancelButton) {
		okButton = okButton === false ? false : (okButton || 'Confirmar');
		cancelButton = cancelButton === false ? false : (cancelButton || 'Cancelar');
		var ModalInstanceCtrl = function ($scope, settings) {
			angular.extend($scope, settings);
			$scope.ok = function () {
				modalInstance.close(true);
			};
			$scope.cancel = function () {
				modalInstance.dismiss('cancel');
			};
		};

		var modalInstance = $uibModal.open({
			templateUrl: '../templates/directives/modal-confirm-click.html',
			controller: ModalInstanceCtrl,
			resolve: {
				settings: function() {
					return {
						modalTitle: title,
						modalBody: message,
						okButton: okButton,
						cancelButton: cancelButton
					};
				}
			}
		});
		return modalInstance;
	}
}]);
 