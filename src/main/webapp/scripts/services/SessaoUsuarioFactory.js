angular.module('anotaai').factory('SessaoUsuarioResource', function($resource){
    var resource = $resource('rest/sessaousuarios/:SessaoUsuarioId',{SessaoUsuarioId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});