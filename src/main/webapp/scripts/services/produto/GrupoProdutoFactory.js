angular.module('anotaai').factory('GrupoProdutoResource', function($resource, $http) {
	var resource = $resource('rest/grupoproduto/:GrupoProdutoId',

	{GrupoProdutoId:'@id'}, {
		'queryAll':{method:'GET',isArray:true},
		'query':{method:'GET',isArray:false},
		'update':{method:'PUT'}
	});

	
	resource.produtoSelecionado = function(value) {
		if (arguments.length) {
			$scope.produto = value;
			if (angular.isObject(value)) {
				delete value.iconClass;
				var produtoGrupoProduto = {}
				produtoGrupoProduto.produto = $scope.produto;
				produtoGrupoProduto.grupoProduto = {};
				produtoGrupoProduto.grupoProduto.id = $scope.grupoProduto.id;
				var successCallback = function(response) {
					if (!response.anotaaiExceptionMessages) {
						$scope.grupoProduto.produtos.push(response.entity);
						$scope.produto = '';
						flash.setMessages(response.messages);
					} else {
						flash.setExceptionMessage(response.anotaaiExceptionMessages);
					}
				};
				var errorCallback = function(response) {
					flash.setExceptionMessage(response.data.exception);
				};
				ProdutoGrupoProdutoResource.save(produtoGrupoProduto, successCallback, errorCallback);
			} else {
				if (value == '') {
					//TODO - remover o icone e a frase de nenhum registro encontrado
					//$scope.noResults = false;
				}
			}
		} else {
			return $scope.produto;
		}
	};
	
	resource.modelOptions = {
		debounce: {
			'default': 500,
			'blur': 250
		},
		getterSetter: true
	};

	resource.getSetores = function(val) {
		var promise = $http.get('rest/produto/search', {
			params: {
				query: val,
				start: 0,
				max: constant.AUTO_COMPLETE_MAX_RESULT,
				idGrupoProduto: dataTransferObject.grupoProduto.id
			}
		});
		return promise.then(function(response){
			return response.data;
		});
	};
	//-------------------------------------------------------
	
	return resource;
});