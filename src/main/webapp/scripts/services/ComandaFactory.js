angular.module('anotaai').factory('ComandaResource', function($resource){
    var resource = $resource('rest/comandas/:ComandaId',{ComandaId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});