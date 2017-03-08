'use strict';

angular.module('anotaai').filter('telefone', function() {
	return function(telefone) {
		var telefoneStr = telefone.numero.toString();
		var numeroDireita = telefoneStr.substring(0, 4);
		var numeroEsquerda = telefoneStr.substring(4, telefoneStr.length);
		
		return '(' + telefone.ddd + ') ' + numeroDireita + '-' + numeroEsquerda;
	};
});