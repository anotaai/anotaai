'use strict';

angular.module('anotaai')
	.config(function ($routeProvider, $stateProvider, $urlRouterProvider) {
		$stateProvider.state('access.register', {
			url: "/Register",
			templateUrl: "views/Acesso/Register/register.html",
			controller: 'RegisterUsuarioController as object'
		}).state('access.register-user', {
			url: "/RegisterUsuario/:activationCode",
			templateUrl: "views/Acesso/Register/register-usuario.html",
			controller: 'NewUsuarioController'
		}).state('access.register-success', {
			url: "/RegisterSuccess",
			templateUrl: "views/Acesso/Register/register-success.html",
			controller: 'NewClienteController'
		}).state('access.active', {
			url: "/Activate/:activationCode",
			templateUrl: "views/Acesso/Activation/activate.html",
			controller: 'ActivateController'
		}).state('access.success-activate', {
			url: "/SuccessActivate",
			templateUrl: "views/Acesso/Activation/activate-success.html",
			controller: 'ActivateController'
		}).state('access.invalidate', {
			url: "/InvalidActivationCode",
			templateUrl: "views/Acesso/Activation/invalid-activation-code.html",
			controller: 'LoginController'
		}).state('access.login', {
			url: "/Login",
			templateUrl: "views/Acesso/Login/login.html",
			controller: 'LoginController'
		}).state('access.renew', {
			url: "/ResetPassword/:activationCode",
			templateUrl: "views/Acesso/Reset/reset-password.html",
			controller: 'ResetPasswordController'
		})
	}
);
