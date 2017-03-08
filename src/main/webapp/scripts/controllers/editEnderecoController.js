angular.module('anotaai').controller('EditEnderecoController', function($scope, $routeParams, $location, flash, EnderecoResource, EnumResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.endereco = new EnderecoResource(self.original);
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The endereco could not be found.'});
            $location.path("/Enderecos");
        };
        EnderecoResource.get({EnderecoId:$routeParams.EnderecoId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.endereco);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The endereco was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.endereco.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Enderecos");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The endereco was deleted.'});
            $location.path("/Enderecos");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.endereco.$remove(successCallback, errorCallback);
    };
    
    $scope.estadoList = EnumResource.enum({EnumName:'estados'});
    
    $scope.get();
});