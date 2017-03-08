angular.module('anotaai').factory('ClienteResource', function($resource) {

	var resource = $resource('rest/clientes/:ClienteId',{ClienteId : '@id'}, {
		'queryAll':{method:'GET', isArray: true},
		'query':{method:'GET', isArray: false},
		'update':{method:'PUT'}
	});
	
	return resource;
});