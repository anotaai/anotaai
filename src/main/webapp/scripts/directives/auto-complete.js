'use-strict'

angular.module("anotaai").directive('aiAutocomplete',
	function($parse) {
		return {
			scope : {
				aiAutocomplete: '=',
			},
			link : function(scope, element, attrs, model) {
				
				var newAutocomplete = function() {
					console.log(scope.aiAutocomplete);
				}
				
				scope.watch = function() {
					return 
				}
				
				$scope.$watch('selected', function() {
					alert('value: ' + scope.aiAutocomplete);
				});

			}
		};
	}
);