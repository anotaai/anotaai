'use strict';

angular.module('anotaai').controller('EditComandaController', function($scope, $routeParams, $location, flash, ComandaResource , VendaResource, PagamentoResource, ClienteResource, ConsumidorResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.comanda = new ComandaResource(self.original);
            VendaResource.queryAll(function(items) {
                $scope.vendasSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.dataVenda
                    };
                    if($scope.comanda.vendas){
                        $.each($scope.comanda.vendas, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.vendasSelection.push(labelObject);
                                $scope.comanda.vendas.push(wrappedObject);
                            }
                        });
                        self.original.vendas = $scope.comanda.vendas;
                    }
                    return labelObject;
                });
            });
            PagamentoResource.queryAll(function(items) {
                $scope.pagamentosSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.dataPagamento
                    };
                    if($scope.comanda.pagamentos){
                        $.each($scope.comanda.pagamentos, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.pagamentosSelection.push(labelObject);
                                $scope.comanda.pagamentos.push(wrappedObject);
                            }
                        });
                        self.original.pagamentos = $scope.comanda.pagamentos;
                    }
                    return labelObject;
                });
            });
            ClienteResource.queryAll(function(items) {
                $scope.clienteSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.cpf
                    };
                    if($scope.comanda.cliente && item.id == $scope.comanda.cliente.id) {
                        $scope.clienteSelection = labelObject;
                        $scope.comanda.cliente = wrappedObject;
                        self.original.cliente = $scope.comanda.cliente;
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
                    if($scope.comanda.consumidor && item.id == $scope.comanda.consumidor.id) {
                        $scope.consumidorSelection = labelObject;
                        $scope.comanda.consumidor = wrappedObject;
                        self.original.consumidor = $scope.comanda.consumidor;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The comanda could not be found.'});
            $location.path("/Comandas");
        };
        ComandaResource.get({ComandaId:$routeParams.ComandaId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.comanda);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The comanda was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.comanda.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Comandas");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The comanda was deleted.'});
            $location.path("/Comandas");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.comanda.$remove(successCallback, errorCallback);
    };
    
    $scope.vendasSelection = $scope.vendasSelection || [];
    $scope.$watch("vendasSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.comanda) {
            $scope.comanda.vendas = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.comanda.vendas.push(collectionItem);
            });
        }
    });
    $scope.pagamentosSelection = $scope.pagamentosSelection || [];
    $scope.$watch("pagamentosSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.comanda) {
            $scope.comanda.pagamentos = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.comanda.pagamentos.push(collectionItem);
            });
        }
    });
    $scope.$watch("clienteSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.comanda.cliente = {};
            $scope.comanda.cliente.id = selection.value;
        }
    });
    $scope.$watch("consumidorSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.comanda.consumidor = {};
            $scope.comanda.consumidor.id = selection.value;
        }
    });
    
    $scope.get();
});