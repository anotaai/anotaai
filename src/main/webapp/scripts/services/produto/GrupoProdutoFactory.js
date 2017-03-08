angular.module('anotaai').factory('GrupoProdutoResource', function($resource, $http) {
	var resource = $resource('rest/grupoproduto/:GrupoProdutoId',

	{GrupoProdutoId:'@id'}, {
		'queryAll':{method:'GET',isArray:true},
		'query':{method:'GET',isArray:false},
		'update':{method:'PUT'}
	});

	return resource;
});