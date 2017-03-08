
angular.module('anotaai').controller('NewSessaoUsuarioController', function ($scope, $location, locationParser, flash, SessaoUsuarioResource , UsuarioResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.sessaoUsuario = $scope.sessaoUsuario || {};
    
    $scope.keepAliveList = [
        "true",
        "false"
    ];

    $scope.usuarioList = UsuarioResource.queryAll(function(items){
        $scope.usuarioSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.nome
            });
        });
    });
    $scope.$watch("usuarioSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.sessaoUsuario.usuario = {};
            $scope.sessaoUsuario.usuario.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The sessaoUsuario was created successfully.'});
            $location.path('/SessaoUsuarios');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        SessaoUsuarioResource.save($scope.sessaoUsuario, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/SessaoUsuarios");
    };
});