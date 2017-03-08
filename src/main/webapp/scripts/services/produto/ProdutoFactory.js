angular.module('anotaai').factory('ProdutoResource', function($resource, $http) {
	var resource = $resource('rest/produto/:ProdutoId', 
		{ProdutoId:'@id'}, {
			'queryAll': {
				method: 'GET', 
				params: {query: '@query', start: '@start', max: '@max'},
				isArray: true
			},
			'query': {method:'GET', isArray:false},
			'update': {method:'PUT'}
		}
	);
	resource.loadItensReceita = function(produto, handleSuccess, handleError) {
		return $http.post('rest/produto/loadItensReceita', produto).then(handleSuccess, handleError);
	};
	
	resource.loadDisponibilidades = function(produto, handleSuccess, handleError) {
		return $http.post('rest/produto/loadDisponibilidades', produto).then(handleSuccess, handleError);
	};

	resource.addItemReceita = function(itemReceita, handleSuccess, handleError) {
		return $http.post('rest/produto/addItemReceita', itemReceita).then(handleSuccess, handleError);
	};
	
	resource.editItemReceita = function(itemReceita, handleSuccess, handleError) {
		return $http.post('rest/produto/editItemReceita', itemReceita).then(handleSuccess, handleError);
	};
	
	resource.deleteItemReceita = function(itemReceita, handleSuccess, handleError) {
		return $http.post('rest/produto/deleteItemReceita', itemReceita).then(handleSuccess, handleError);
	};

	return resource;
});