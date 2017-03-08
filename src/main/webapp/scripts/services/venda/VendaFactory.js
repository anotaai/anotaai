angular.module('anotaai').factory('VendaResource', function($resource, $http) {
	var resource = $resource('rest/venda/:VendaId', 
		{VendaId:'@id'}, {
			'queryAll': {
				method: 'GET', 
				params: {dataInicial: '@dataInicial', dataFinal: '@dataFinal'},
				isArray: true
			},
			'query': {method:'GET', isArray:false}
		}
	);
	
	return resource;
});