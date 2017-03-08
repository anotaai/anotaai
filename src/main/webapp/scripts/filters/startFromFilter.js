'use strict';

angular.module('anotaai').filter('startFrom', function() {
	return function(input, start) {
		if (input) {
			start = +start; //parse to int
			return input.slice(start);
		}
	};
});