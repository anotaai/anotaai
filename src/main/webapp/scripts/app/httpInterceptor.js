'use strict';

angular.module('anotaai').config(function($httpProvider) {
	
	$httpProvider.interceptors.push(function($location, $cookies, $q, $log, flash, $rootScope) {
		return {
			'request': function(request) {
				//reativar a sessao do usuario
				if ($cookies.get('globals')) {
					var globals = $cookies.get('globals');
					var d = new Date();
					var expireDate = new Date();
					expireDate.setMinutes(d.getMinutes() + 30);
					$cookies.put('globals', globals, {'expires': expireDate});
				}
				return request;
			},
			'responseError': function(response) {
				switch (response.status) {
				case 401:
					//TODO abrir modal para refazer login
					flash.setExceptionMessage(response.data.exception);
					$rootScope.renewLogin();
					break;
				case 403:
					flash.setMessage(response.data);
					break;
				default:
					break;
				}
				return $q.reject(response);
			}
		};
	});
});