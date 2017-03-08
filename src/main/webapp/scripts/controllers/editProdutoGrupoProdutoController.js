

angular.module('anotaai').controller('EditProdutoGrupoProdutoController', function($scope, $routeParams, $location, flash, ProdutoGrupoProdutoResource , ProdutoResource, GrupoProdutoResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.produtoGrupoProduto = new ProdutoGrupoProdutoResource(self.original);
            ProdutoResource.queryAll(function(items) {
                $scope.produtoSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.descricao
                    };
                    if($scope.produtoGrupoProduto.produto && item.id == $scope.produtoGrupoProduto.produto.id) {
                        $scope.produtoSelection = labelObject;
                        $scope.produtoGrupoProduto.produto = wrappedObject;
                        self.original.produto = $scope.produtoGrupoProduto.produto;
                    }
                    return labelObject;
                });
            });
            GrupoProdutoResource.queryAll(function(items) {
                $scope.grupoProdutoSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.descricao
                    };
                    if($scope.produtoGrupoProduto.grupoProduto && item.id == $scope.produtoGrupoProduto.grupoProduto.id) {
                        $scope.grupoProdutoSelection = labelObject;
                        $scope.produtoGrupoProduto.grupoProduto = wrappedObject;
                        self.original.grupoProduto = $scope.produtoGrupoProduto.grupoProduto;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The produtoGrupoProduto could not be found.'});
            $location.path("/ProdutoGrupoProdutos");
        };
        ProdutoGrupoProdutoResource.get({ProdutoGrupoProdutoId:$routeParams.ProdutoGrupoProdutoId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.produtoGrupoProduto);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The produtoGrupoProduto was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.produtoGrupoProduto.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/ProdutoGrupoProdutos");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The produtoGrupoProduto was deleted.'});
            $location.path("/ProdutoGrupoProdutos");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.produtoGrupoProduto.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("produtoSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.produtoGrupoProduto.produto = {};
            $scope.produtoGrupoProduto.produto.id = selection.value;
        }
    });
    $scope.$watch("grupoProdutoSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.produtoGrupoProduto.grupoProduto = {};
            $scope.produtoGrupoProduto.grupoProduto.id = selection.value;
        }
    });
    
    $scope.get();
});