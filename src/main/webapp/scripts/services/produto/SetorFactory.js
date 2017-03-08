angular.module('anotaai').factory('SetorResource', function($resource, $http) {
	var resource = $resource('rest/setor/:SetorId',
	
	{SetorId:'@id'}, {
		'queryAll':{method:'GET', isArray:true},
		'query':{method:'GET', isArray:false},
		'update':{method:'PUT'}
	});
	
	return resource;
});