angular.module('anotaai').controller('EditClienteConsumidorController', function($scope, $routeParams, $location, flash, ClienteConsumidorResource , ClienteResource, ConsumidorResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.clienteConsumidor = new ClienteConsumidorResource(self.original);
            ClienteResource.queryAll(function(items) {
                $scope.clienteSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.cpf
                    };
                    if($scope.clienteConsumidor.cliente && item.id == $scope.clienteConsumidor.cliente.id) {
                        $scope.clienteSelection = labelObject;
                        $scope.clienteConsumidor.cliente = wrappedObject;
                        self.original.cliente = $scope.clienteConsumidor.cliente;
                    }
                    return labelObject;
                });
            });
            ConsumidorResource.queryAll(function(items) {
                $scope.consumidorSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.dataCadastro
                    };
                    if($scope.clienteConsumidor.consumidor && item.id == $scope.clienteConsumidor.consumidor.id) {
                        $scope.consumidorSelection = labelObject;
                        $scope.clienteConsumidor.consumidor = wrappedObject;
                        self.original.consumidor = $scope.clienteConsumidor.consumidor;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The clienteConsumidor could not be found.'});
            $location.path("/ClienteConsumidors");
        };
        ClienteConsumidorResource.get({ClienteConsumidorId:$routeParams.ClienteConsumidorId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.clienteConsumidor);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The clienteConsumidor was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.clienteConsumidor.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/ClienteConsumidors");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The clienteConsumidor was deleted.'});
            $location.path("/ClienteConsumidors");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.clienteConsumidor.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("clienteSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.clienteConsumidor.cliente = {};
            $scope.clienteConsumidor.cliente.id = selection.value;
        }
    });
    $scope.$watch("consumidorSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.clienteConsumidor.consumidor = {};
            $scope.clienteConsumidor.consumidor.id = selection.value;
        }
    });
    
    $scope.get();
});