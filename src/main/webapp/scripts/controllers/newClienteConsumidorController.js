'use strict'

angular.module('anotaai').controller('NewClienteConsumidorController', function ($scope, $location, locationParser, flash, ClienteConsumidorResource , ClienteResource, ConsumidorResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.clienteConsumidor = $scope.clienteConsumidor || {};
    
    $scope.clienteList = ClienteResource.queryAll(function(items){
        $scope.clienteSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.cpf
            });
        });
    });
    
    $scope.$watch("clienteSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.clienteConsumidor.cliente = {};
            $scope.clienteConsumidor.cliente.id = selection.value;
        }
    });
    
    $scope.consumidorList = ConsumidorResource.queryAll(function(items){
        $scope.consumidorSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.dataCadastro
            });
        });
    });
    $scope.$watch("consumidorSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.clienteConsumidor.consumidor = {};
            $scope.clienteConsumidor.consumidor.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The clienteConsumidor was created successfully.'});
            $location.path('/ClienteConsumidors');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        ClienteConsumidorResource.save($scope.clienteConsumidor, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/ClienteConsumidors");
    };
});