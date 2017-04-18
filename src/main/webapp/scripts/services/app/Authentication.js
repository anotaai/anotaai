'use strict';
angular.module('anotaai').factory('AuthenticationResource', function($resource, $rootScope, $cookies, $http, Base64Service) {
	var service = $resource();

	service.setCredentials = setCredentials;
	service.clearCredentials = clearCredentials;

	return service;

	function setCredentials(login) {
		var telefoneStr = login.usuario.telefone.ddd.toString() + login.usuario.telefone.numero.toString();

		var authdata = Base64Service.encode(login.usuario.email + ':' + login.sessionID + ':' + login.usuario.telefone.ddi.toString() + telefoneStr);
		var index = login.usuario.nome.indexOf(' ');
		var primeiroNome = index != -1 ? login.usuario.nome.substring(0, index) : login.usuario.nome;
		
		telefoneStr = telefoneStr.replace(/^(\d{2})(\d)/g,"($1) $2");
		telefoneStr = telefoneStr.replace(/(\d)(\d{4})$/,"$1-$2"); 
		$rootScope.globals = {};
		$rootScope.globals.login = login;
		$rootScope.globals.login.telefoneStr = telefoneStr;
		$rootScope.globals.login.authdata = authdata;
		$rootScope.globals.login.primeiroNome = primeiroNome;
				
		var d = new Date();
		var expireDate = new Date();
		expireDate.setMinutes(d.getMinutes() + login.sessionTime);
		if (login.keepAlive) {
			expireDate.setFullYear(d.getFullYear() + 1);
		}
		
		$http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
		$cookies.put(login.cookieSessionName, JSON.stringify($rootScope.globals), {'expires': expireDate});
		// dispara o evento para o escutador
		$rootScope.$broadcast('$loginChange', $rootScope.globals.login);
	}

	function clearCredentials() {
		$rootScope.globals = {};
		$cookies.remove('globals');
		$http.defaults.headers.common.Authorization = 'Basic';
		//dispara o evento para o escutador
		$rootScope.$broadcast('$logoutChange');
	}
});