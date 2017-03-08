angular.module('anotaai').factory('EnumResource', function($resource, $http) {
	var resource = $resource;
	
	resource.load = load;
	
	function load(enumName, handleSuccess, handleError) {
		return $http.get('rest/enums/' + enumName).then(handleSuccess, handleError);
	}
	
	return resource;
});