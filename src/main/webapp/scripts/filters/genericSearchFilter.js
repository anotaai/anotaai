'use strict';

angular.module('anotaai').filter('searchFilter', function($filter) {
	
	function isEquals(actualProperty, expectedProperty) {
		var actualPropertyStr = $filter('uppercase')(actualProperty.toString());
		var expectedPropertyStr = $filter('uppercase')(expectedProperty.toString());
		return actualPropertyStr.indexOf(expectedPropertyStr) != -1;
	}

	function matchObjectProperties(expectedObject, actualObject) {
		var flag = true;
		for(var key in expectedObject) {
			if(expectedObject.hasOwnProperty(key)) {
				var expectedProperty = expectedObject[key];
				if (expectedProperty == null || expectedProperty === "") {
					continue;
				}
				var actualProperty = actualObject[key];
				if (angular.isUndefined(actualProperty)) {
					continue;
				}
				if (actualProperty == null) {
					flag = false;
				} else if (angular.isObject(expectedProperty)) {
					flag = flag && matchObjectProperties(expectedProperty, actualProperty);
				} else {
					flag = flag && isEquals(actualProperty, expectedProperty);
				}
			}
		}
		return flag;
	}

	return function(results, scope) {

		scope.filteredResults = [];
		for (var ctr = 0; ctr < results.length; ctr++) {
			var flag = true;
			var searchCriteria = scope.search;
			var result = results[ctr];
			for (var key in searchCriteria) {
				if (searchCriteria.hasOwnProperty(key)) {
					var expectedProperty = searchCriteria[key];
					if (expectedProperty == null || expectedProperty === "") {
						continue;
					}
					var actualProperty = result[key];
					if (actualProperty == null) {
						flag = false;
					} else if (angular.isObject(expectedProperty)) {
						flag = flag && matchObjectProperties(expectedProperty, actualProperty);
					} else {
						flag = flag && isEquals(actualProperty, expectedProperty);
					}
				}
			}
			if (flag == true) {
				scope.filteredResults.push(result);
			}
		}
		scope.numberOfPages();
		return scope.filteredResults;
	};
});
