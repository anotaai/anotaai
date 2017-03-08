angular.module('anotaai').factory('ItemReceitaResource', function($resource, $http) {
	
	var resource = $resource('rest/itemReceita/:ItemReceitaId', {ItemReceitaId:'@id'}, {
		'queryAll': {method: 'GET', isArray: true},
		'query': {method: 'GET', isArray: false},
		'update': {method: 'PUT'}}
	);
	
	return resource;
	
});