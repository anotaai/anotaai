'use strict';

angular.module('anotaai').config(['$translateProvider', function ($translateProvider) {

	$translateProvider.translations('en-US', {
		TITULO: 'Your application is running.',
		USUARIO_SENHA_INVALIDO: 'User not found',
		NOME: 'Write it down',
	});
	
}]);