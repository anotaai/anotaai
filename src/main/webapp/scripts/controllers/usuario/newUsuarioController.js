angular.module('anotaai').controller('NewUsuarioController', function (dataTransferObject, $rootScope, $timeout, $scope, $location, $log, $state, $stateParams, $translate, flash, UsuarioResource, EnumResource, constant, ClienteResource, EnderecoResource) {
	
	var self = this;
	
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.usuario = new UsuarioResource();
	$scope.usuario.telefone = {};
	$scope.telefone = '';
	$scope.confirmaSenha = '';
	$scope.tipoCadastro = dataTransferObject.tipoCadastro;
	$scope.emailFormat = '/^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/';
	$scope.ativacaoCadastro = false;
	$scope.cliente = {};
	$scope.cliente.endereco = {};
	$scope.cliente.usuario = $scope.usuario;
	
	self.original = angular.copy($scope.cliente);
	
	(function() {
		if ($stateParams.activationCode) {
			$scope.tipoCadastro = 'comprador';
		}
		if ($scope.tipoCadastro) {
			$timeout(function() {
				angular.element("#nome").focus();
			});
			atualizarTitulo();
			if ($stateParams.activationCode) {
				$scope.ativacaoCadastro = true;
				var successCallback = function(response, responseHeaders) {
					if (response.data.entity) {
						$scope.usuario = response.data.entity;
						$scope.telefone = $scope.usuario.telefone.ddd.toString() + $scope.usuario.telefone.numero.toString();
						angular.element('#nome').attr("disabled", "disabled");
						angular.element('#email').attr("disabled", "disabled");
						angular.element('#telefone').attr("disabled", "disabled");
						angular.element('#senha').focus();
					} else if(response.data.exception) {
						$state.go('access.login');
					}
				};			
				var errorCallback = function(response) {
					flash.setExceptionMessage(response.data);				
				};
				UsuarioResource.userByActivationCode($stateParams.activationCode, successCallback, errorCallback);
			}
		} else {
			$state.go('access.register');
		}
	}());

	$scope.$watch('cliente.endereco.cep', function() {
		if ($scope.cliente.endereco && $scope.cliente.endereco.cep) {
			var successCallback = function(response) {
				//TODO verificar obrigaroriedade de cep validado pelo webservice
				var cep = response.data;
				$scope.cliente.endereco.logradouro = cep.logradouro;
				$scope.cliente.endereco.bairro = cep.bairro;
				$scope.cliente.endereco.cidade = cep.localidade;
				$scope.cliente.endereco.estado = cep.uf;
				//cep's de cidades nao recuperam o nome do logradouro ex: 34.000.000 cep de nova lima
				if (cep.logradouro) {
					angular.element('#logradouro').attr("disabled", "disabled");
					$timeout(function(){
						angular.element('#numero').focus(); 
					}, 0);
				} else {
					$timeout(function(){
						angular.element('#logradouro').focus(); 
					}, 0);					
				}
				//cep's de cidades nao recuperam o nome do bairro ex: 34.000.000 cep de nova lima
				if (cep.bairro) {
					angular.element('#bairro').attr("disabled", "disabled");
				}
				if (cep.localidade) {
					angular.element('#cidade').attr("disabled", "disabled");
				}
				if (cep.uf) {
					angular.element('#estado').attr("disabled", "disabled");
				}
				
			};
			var errorCallback = function(response) {
				limparEndereco();
				flash.setExceptionMessage(response.data);
			};
			EnderecoResource.findCep($scope.cliente.endereco.cep, successCallback, errorCallback);
		} else {
			limparEndereco();
		}
	});
	
	$scope.$watch('telefone', function() {
		flash.destroyAllMessages();
		$scope.usuario.telefone = $rootScope.buildTelefone($scope.telefone);
		if ($scope.telefone) {
			var successCallback = function(response) {
				if (response.data.isValid) {
					if (response.data.entity) {
						angular.element('#telefone').attr("disabled", "disabled");
						angular.element('#nome').attr("disabled", "disabled");
						angular.element('#email').attr("disabled", "disabled");
						$scope.usuario = response.data.entity;
						$scope.ativacaoCadastro = true;
						var campoFoco = $scope.tipoCadastro == 'vendedor' ? 'nomeComercial' : 'senha';
						angular.element('#' + campoFoco).focus();
					} else {
						limparFormulario();
						angular.element('#email').focus();
					}					
				} else {
					angular.element('#telefone').attr("disabled", "disabled");
					angular.element('#nome').attr("disabled", "disabled");
					angular.element('#email').attr("disabled", "disabled");
					angular.element('#senha').attr("disabled", "disabled");
					angular.element('#confirmaSenha').attr("disabled", "disabled");
					angular.element('#limpar').focus();
					flash.setMessages(response.data.exception.anotaaiExceptionMessages);
				}
				
			};
			var errorCallback = function(response) {
				flash.setExceptionMessage(response.data);
			};
			UsuarioResource.findUsuarioByTelefone($scope.usuario.telefone, successCallback, errorCallback);			
		}
	});
	
	$scope.save = function() {
		if ($scope.confirmaSenha == $scope.usuario.senha) {
			var successCallback = function(data, responseHeaders) {
				if (data.isValid) {
					$state.go('access.register-success');
				} else {
					flash.setExceptionMessage(data.exception.anotaaiExceptionMessages);
				}
			};
			if ($scope.tipoCadastro == null || $scope.tipoCadastro == 'comprador') {
				UsuarioResource.save($scope.usuario, successCallback, $rootScope.defaultErrorCallback);
			} else {
				$scope.cliente.type = 'cliente';
				ClienteResource.save($scope.cliente, successCallback, $rootScope.defaultErrorCallback);
			}
		} else {
			senhaInvalida();
		}
	};
	
	$scope.activationUser = function () {
		if ($scope.confirmaSenha == $scope.usuario.senha) {
			var successCallback = function(data, responseHeaders) {
				$state.go('access.register-success');
				flash.setMessage({
					'type': constant.TYPE_MESSAGE.SUCCESS,
					'key': 'sucesso.ativacao.cadastro.consumidor',
					isKey: true
				});
			};
			var errorCallback = function(response) {
				flash.setExceptionMessage(response.data);				
			};
			UsuarioResource.activationUser($scope.usuario, successCallback, errorCallback);			
		} else {
			senhaInvalida();
		}
	}
	
	function senhaInvalida() {
		$scope.usuario.senha = '';
		$scope.confirmaSenha = '';
		flash.setMessage({
		'type': {'type' : constant.TYPE_MESSAGE.ERROR},
			'key': 'senha.nao.confere',
			'isKey': true
		});
		angular.element("#senha").focus();
	}
	
	$scope.isClean = function() {
		var clone = angular.copy($scope.cliente);
		return $rootScope.equals(self.original, clone);
	};
	
	$scope.limpar = function() {
		$scope.telefone = '';
		$scope.cliente = angular.copy(self.original);
		$scope.usuario = $scope.cliente.usuario;
		$scope.confirmaSenha = '';
		limparEndereco();
		limparFormulario();
		$scope.ativacaoCadastro = false;
		$timeout(function() {
			angular.element('#telefone').focus(); 
		}, 0);
	};
	
	$scope.cancel = function() {
		$state.go('access.login');
	};
	
	function limparFormulario() {
		angular.element('#telefone').removeAttr("disabled");
		angular.element('#nome').removeAttr("disabled");
		angular.element('#email').removeAttr("disabled");
		angular.element('#senha').removeAttr("disabled");
		angular.element('#confirmaSenha').removeAttr("disabled");
		flash.destroyAllMessages();
	}
	
	function limparEndereco() {
		angular.element('#logradouro').removeAttr("disabled");
		angular.element('#bairro').removeAttr("disabled");
		angular.element('#cidade').removeAttr("disabled");
		angular.element('#estado').removeAttr("disabled");
		$scope.cliente.endereco.logradouro = '';
		$scope.cliente.endereco.bairro = '';
		$scope.cliente.endereco.cidade = '';
		$scope.cliente.endereco.estado = '';
	}
	
	function atualizarTitulo() {
		var tipoCadastro = dataTransferObject.tipoCadastro != null ? dataTransferObject.tipoCadastro : 'comprador';
		$scope.titulo = $translate.instant('titulo.cadastro.' + tipoCadastro);
	}
	
	EnumResource.load('estados', function(response) {
		$scope.estadoList = response.data;
	});
	
});