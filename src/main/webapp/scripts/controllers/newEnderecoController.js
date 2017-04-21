angular.module('anotaai').controller('NewEnderecoController', function ($scope, $location, locationParser, flash, EnderecoResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.endereco = $scope.endereco || {};
    
	EnumResource.load('estados', function(response) {
		$scope.estadoList = response.data;
	});

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The endereco was created successfully.'});
            $location.path('/Enderecos');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        EnderecoResource.save($scope.endereco, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Enderecos");
    };
});