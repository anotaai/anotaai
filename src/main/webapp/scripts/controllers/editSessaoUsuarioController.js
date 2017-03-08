

angular.module('anotaai').controller('EditSessaoUsuarioController', function($scope, $routeParams, $location, flash, SessaoUsuarioResource , UsuarioResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.sessaoUsuario = new SessaoUsuarioResource(self.original);
            UsuarioResource.queryAll(function(items) {
                $scope.usuarioSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.nome
                    };
                    if($scope.sessaoUsuario.usuario && item.id == $scope.sessaoUsuario.usuario.id) {
                        $scope.usuarioSelection = labelObject;
                        $scope.sessaoUsuario.usuario = wrappedObject;
                        self.original.usuario = $scope.sessaoUsuario.usuario;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The sessaoUsuario could not be found.'});
            $location.path("/SessaoUsuarios");
        };
        SessaoUsuarioResource.get({SessaoUsuarioId:$routeParams.SessaoUsuarioId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.sessaoUsuario);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The sessaoUsuario was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.sessaoUsuario.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/SessaoUsuarios");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The sessaoUsuario was deleted.'});
            $location.path("/SessaoUsuarios");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.sessaoUsuario.$remove(successCallback, errorCallback);
    };
    
    $scope.keepAliveList = [
        "true",
        "false"
    ];
    $scope.$watch("usuarioSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.sessaoUsuario.usuario = {};
            $scope.sessaoUsuario.usuario.id = selection.value;
        }
    });
    
    $scope.get();
});