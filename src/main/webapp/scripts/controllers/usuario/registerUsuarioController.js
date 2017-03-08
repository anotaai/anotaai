angular.module('anotaai').controller('RegisterUsuarioController', function(dataTransferObject, $rootScope, $scope, $stateParams, $state, $location, flash) {
	
	var object = this;
	object.data = dataTransferObject;
	
	$scope.cadastrar = function(tipoCadastro) {
		object.data.tipoCadastro = tipoCadastro;
		$state.go('access.register-user');
	}
	
});