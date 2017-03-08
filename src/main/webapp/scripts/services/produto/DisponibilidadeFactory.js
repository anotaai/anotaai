angular.module('anotaai').factory('DisponibilidadeResource', function($resource, $http) {
	
	var resource = $resource('rest/disponibilidade/:DisponibilidadeId', {DisponibilidadeId:'@id'}, {
		'queryAll': {method: 'GET', isArray: true},
		'query': {method: 'GET', isArray: false},
		'update': {method: 'PUT'}}
	);
	
	return resource;
	
});