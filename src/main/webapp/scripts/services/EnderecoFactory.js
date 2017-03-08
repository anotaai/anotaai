angular.module('anotaai').factory('EnderecoResource', function($resource, $http){
	var resource = $resource('rest/enderecos/:EnderecoId',{EnderecoId:'@id'}, {
		'queryAll':{method:'GET',isArray:true},
		'query':{method:'GET',isArray:false},
		'update':{method:'PUT'}
	});
	
	resource.findCep = findCep;
	
	function findCep(cep, handleSuccess, handleError) {
		return $http.get('rest/enderecos/findcep/' + cep).then(handleSuccess, handleError);
	}
	
	return resource;
});