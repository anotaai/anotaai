angular.module('anotaai').factory('ClienteConsumidorResource', function($resource, $http) {
	
	var resource = $resource('rest/clienteconsumidor/:ClienteConsumidorId', {ClienteConsumidorId:'@id'}, {
		'queryAll': {method: 'GET', isArray: true},
		'query': {method: 'GET', isArray: false},
		'update': {method: 'PUT'}}
	);
	
	resource.findUsuarioByTelefone = function findUsuarioByTelefone(telefone, handleSuccess, handleError) {
		return $http.post('rest/clienteconsumidor/findby/telefone', telefone).then(handleSuccess, handleError);
	};
	
	resource.isEditable = function editable(usuario, handleSuccess, handleError) {
		return $http.post('rest/clienteconsumidor/usuario/editable', usuario).then(handleSuccess, handleError);
	};
	
	resource.recomendarEdicao = function editable(usuario, handleSuccess, handleError) {
		return $http.post('rest/clienteconsumidor/usuario/recomendaredicao', usuario).then(handleSuccess, handleError);
	};
	
	return resource;
	
});