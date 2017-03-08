angular.module('anotaai').factory('UsuarioResource', function($resource, $http) {
	
	var resource = $resource('rest/usuarios/:UsuarioId', {UsuarioId:'@id'}, {
		'queryAll':{method:'GET', isArray:true},
		'query':{method:'GET', isArray:false},
		'update':{method:'PUT'}
	});
	
	resource.activation = activation;
	resource.userByActivationCode = userByActivationCode;
	resource.login = login;
	resource.logout = logout;
	resource.activationUser = activationUser;
	resource.activationUserWithNotification = activationUserWithNotification;
	resource.findUsuarioByTelefone = findUsuarioByTelefone;
	resource.loadProfileImage = loadProfileImage;
	resource.renewPassword = renewPassword;
	
	function activation(activationCode, handleSuccess, handleError) {
		return $http.post('rest/usuarios/activation', activationCode).then(handleSuccess, handleError);
	}
	
	function userByActivationCode(activationCode, handleSuccess, handleError) {
		return $http.post('rest/usuarios/byactivationcode', activationCode).then(handleSuccess, handleError);
	}
	
	function login(login, handleSuccess, handleError) {
		return $http.post('rest/usuarios/login', login).then(handleSuccess, handleError);
	}
	
	function logout(usuario, handleSuccess, handleError) {
		return $http.post('rest/usuarios/logout', usuario).then(handleSuccess, handleError);
	}
	
	function activationUser(usuario, handleSuccess, handleError) {
		return $http.post('rest/usuarios/activationuser', usuario).then(handleSuccess, handleError);
	}
	
	function activationUserWithNotification(usuario, handleSuccess, handleError) {
		return $http.post('rest/usuarios/activationuserwithnotification', usuario).then(handleSuccess, handleError);
	}

	function findUsuarioByTelefone(telefone, handleSuccess, handleError) {
		return $http.post('rest/usuarios/findby/telefone', telefone).then(handleSuccess, handleError);
	}
	
	function loadProfileImage(idUsuario, handleSuccess, handleError) {
		return $http.get('rest/usuarios/profilePhoto/' + idUsuario).then(handleSuccess, handleError);
	}
	
	function renewPassword(usuario, handleSuccess, handleError) {
		return $http.post('rest/usuarios/renewpassword', usuario).then(handleSuccess, handleError);
	}
	
	return resource;
	
});