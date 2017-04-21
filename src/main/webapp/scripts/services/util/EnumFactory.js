angular.module('anotaai').factory('EnumResource', function($resource, $http, $rootScope) {
	var resource = $resource;
	
	resource.load = load;
	
	function load(enumName, handleSuccess) {
		return $http.get('rest/enums/' + enumName).then(handleSuccess, $rootScope.defaultErrorCallback);
	}
	
	return resource;
});