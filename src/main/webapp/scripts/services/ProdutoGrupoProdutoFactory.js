angular.module('anotaai').factory('ProdutoGrupoProdutoResource', function($resource){
	var resource = $resource('rest/produtogrupoproduto/:ProdutoGrupoProdutoId', 
	
	{ProdutoGrupoProdutoId:'@id'}, {
		'queryAll':{method:'GET',isArray:true},
		'query':{method:'GET',isArray:false},
		'update':{method:'PUT'}
	});
	
	return resource;
});