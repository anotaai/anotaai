
angular.module('anotaai').controller('NewProdutoGrupoProdutoController', function ($scope, $location, locationParser, flash, ProdutoGrupoProdutoResource , ProdutoResource, GrupoProdutoResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.produtoGrupoProduto = $scope.produtoGrupoProduto || {};
    
    $scope.produtoList = ProdutoResource.queryAll(function(items){
        $scope.produtoSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.descricao
            });
        });
    });
    $scope.$watch("produtoSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.produtoGrupoProduto.produto = {};
            $scope.produtoGrupoProduto.produto.id = selection.value;
        }
    });
    
    $scope.grupoProdutoList = GrupoProdutoResource.queryAll(function(items){
        $scope.grupoProdutoSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.descricao
            });
        });
    });
    $scope.$watch("grupoProdutoSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.produtoGrupoProduto.grupoProduto = {};
            $scope.produtoGrupoProduto.grupoProduto.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The produtoGrupoProduto was created successfully.'});
            $location.path('/ProdutoGrupoProdutos');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        ProdutoGrupoProdutoResource.save($scope.produtoGrupoProduto, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/ProdutoGrupoProdutos");
    };
});