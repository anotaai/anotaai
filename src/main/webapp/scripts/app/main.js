'use strict';

angular.module('anotaai', ['ngRoute', 'ngResource', 'ui.mask', 'ngCookies', 'pascalprecht.translate', 
						   'ui.router', 'angular-growl', 'ngAnimate', 'ui.bootstrap', 'ng-currency',
						   'ngFileUpload'])
	.constant('constant', {
		//constantes da aplicacao, deve injetar nos controller's para recuperar a instancia deste objeto
		"MESSAGE_TIME": 5000,
		"MESSAGE_CONSTANT": -1,
		TYPE_MESSAGE: {'SUCCESS': 'SUCCESS', 'ERROR': 'ERROR', 'INFO': 'INFO', 'WARNING': 'WARNING'},
		"AUTO_COMPLETE_MAX_RESULT" : 10,
		MESSAGE_SENHA_NAO_CONFERE : {'type': {'type' : 'ERROR'}, 'key': 'senha.nao.confere', 'isKey': true },
	})
	.config(function($routeProvider, $stateProvider, $httpProvider, $urlRouterProvider, growlProvider, constant) {
	
		// url padrao
		$urlRouterProvider.otherwise("/access");
		
		//cinco segundos para as mensagens sumirem
		growlProvider.globalTimeToLive(constant.MESSAGE_TIME);
		growlProvider.messagesKey("anotaai-messages");
		growlProvider.messageTextKey("anotaai-messagetext");
	
		$stateProvider
			.state('main', {
				url : "/",
				templateUrl : "main.html",
				controller: "MainController"
			}).state('access', {
				url : "/access",
				templateUrl : "access.html"
			}).state('app', {
				url : "/app",
				templateUrl : "app.html"
			}
		);
	
	}).service("dataTransferObject", function DataTransferObject() {
		
		//troca de objetos entre controller's, este objeto é limpo sempre que ocorre uma navegação entre menus
		//este objeto deve ser injetado nos controller para a transferencia do objeto entre os controller's
		var object = this;
		
	}).run(function(dataTransferObject, $rootScope, $location, $http, $state, $cookies, $window, growlMessages, $translate, $timeout, flash) {
		
		$rootScope.selectedRoute = '';
	
		// keep user logged in after page refresh
		$rootScope.globals = ($cookies.get('globals') && JSON.parse($cookies.get('globals'))) || {};
		if ($rootScope.globals.login) {
			$http.post('rest/sessaousuario/isactive', $rootScope.globals.login.sessionID).then(
				function(response) {
					if (!response.data.isValid) {
						$rootScope.globals = {};
						$cookies.remove('globals');
						$http.defaults.headers.common.Authorization = 'Basic';
						$state.go('access.login');
					}
				}, 
				function(reason) {
					console.log('ok reason');
				}
			)
			$http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.login.authdata;
		}

		$rootScope.$on('$locationChangeStart', function (event, next, current) {
			var path = $location.path();
			//paginas de acesso livre
			var freePage = $.inArray(path, ['/', '/access', '/access/Login', '/access/Register', '/access/RegisterUsuario', 
											'/access/RegisterVendedor', '/access/InvalidActivationCode', '/access/SuccessActivate', 
											'/access/RegisterSuccess']) >= 0;
											
			//paginas de acesso livre com parametro
			var freePageWithParam = path.startsWith('/access/Activate/') || path.startsWith('/access/RegisterUsuario/') || path.startsWith('/access/ResetPassword/');
			//usuario ja logado, para nao acessar mais as paginas de login
			var loggedIn = $rootScope.globals.login;
			if (!freePage && !freePageWithParam && !loggedIn) {
				$location.path('/access');
			} else if (loggedIn && path.indexOf('/access') > -1) {
				$location.path('/app');
			}
		});

		$rootScope.$on('$locationChangeSuccess', function (event, next, current) {
			$window.ga('set', 'page', $location.url());
			$window.ga('send', 'pageview');
			init();
		});
		
		$rootScope.$on('$stateChangeSuccess', function () {
			$window.ga('set', 'page', $location.url());
			$window.ga('send', 'pageview');
			init();
		});
		
		$rootScope.$on('$routeChangeSuccess', function() {
			$window.ga('set', 'page', $location.url());
			$window.ga('send', 'pageview');
		});
		
		$rootScope.buildTelefone = function(telefoneStr) {
			var ddi = '';
			var ddd = '';
			var numero = '';
			var telefone = {};
			if (telefoneStr) {
				ddi = 55;
				ddd = telefoneStr.substring(0, 2);
				numero = telefoneStr.substring(2, telefoneStr.length);
			}
			telefone.ddi = ddi;
			telefone.ddd = ddd;
			telefone.numero = numero;
			
			return telefone;
		}
		
		$rootScope.changeRoute = function (route) {
			//limpar o objeto de troca de informações sempre que navega no menu, mudança de escopo
			var fields = Object.keys(dataTransferObject);
			angular.forEach(fields, function(field) {
				delete dataTransferObject[field];
			});
			$rootScope.selectedRoute = route;
			
			//limpa as mensagens
			if(growlMessages.getAllMessages().length) {
				growlMessages.destroyAllMessages();
			}
	
			$state.go(route);
		};
		
		$rootScope.equals = function (oldObject, newObject) {
			removeNullAndUndefined(oldObject);
			removeNullAndUndefined(newObject);
			return angular.equals(oldObject, newObject);
		};
		
		function removeNullAndUndefined(model) {
			if (model) {
				var fields = Object.keys(model);
				angular.forEach(fields, function(field) {
					var prop = model[field];
					if (prop != null && prop != undefined && angular.isObject(prop)) {
						removeNullAndUndefined(prop);
					} else {
						if (prop == null || prop == '' || prop === undefined) {
							delete model[field];
						}
					}
				});
			}
		}
		
		$rootScope.cleanMessages = function (route) {
			if(growlMessages.getAllMessages().length) {
				growlMessages.destroyAllMessages();
			}
		};
		var backElements = [];
		$rootScope.enableElements = function (elements) {
			if (elements) {
				$.each(elements, function(index, element) {
					angular.element('#' + element).removeAttr('disabled');
				});
			}
			backElements = [];
		};
		
		$rootScope.disableElements = function (elements) {
			if (elements) {
				$.each(elements, function(index, element) {
					angular.element('#' + element).attr('disabled', 'disabled');
				});
			}
			backElements = elements;
		};
		
		$rootScope.renewLogin = function() {
			$rootScope.globals = {};
			$cookies.remove('globals');
			$http.defaults.headers.common.Authorization = 'Basic';
			//dispara o evento para o escutador
			$rootScope.$broadcast('$logoutChange');
			$state.go('access.login');
		};
		
		$rootScope.translateMessage = function (key, params) {
			var message = $translate.instant(key);
			if (params && params.length > 0) {
				$.each(params, function(index, param) {
					message = message.replace('{' + index + '}', param);
				});
			}
			return message;
		}
		
		/*
		 * quando devolve um response diferente de 200 esta funcao deve ser executada
		 * ponto unico de tratamento de excecoes não previstas no codigo
		 * 
		 * esta funcao deve ser passada como parametro para as chamadas axax como errorCallback
		 * 
		 */
		$rootScope.defaultErrorCallback = function(response) {
			
			$rootScope.enableElements(backElements);
			var message = {
				isKey: true,
				key: 'msg.erro.nao.identificado',
				type: {
					type: 'ERROR',
				},
				time: 10000//dez segundos
			};
			flash.setMessage(message);
		}
		
		function init() {
			$window.scrollTo(0, 0);
		}
	}
);
