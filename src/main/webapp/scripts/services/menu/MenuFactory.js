angular.module('anotaai').factory('MenuResource', function($resource, $http) {
	var resource = $resource();
	
	resource.getMenu = getMenu;
	
	function getMenu(menu, handleSuccess, handleError) {
		return $http.get('rest/menu/' + menu).then(handleSuccess, handleError);
	}
	return resource;
});