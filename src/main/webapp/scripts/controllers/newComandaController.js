
angular.module('anotaai').controller('NewComandaController', function ($scope, $location, locationParser, flash, ComandaResource , VendaResource, PagamentoResource, ClienteResource, ConsumidorResource) {
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.comanda = $scope.comanda || {};
	
	$scope.vendasList = VendaResource.queryAll(function(items){
		$scope.vendasSelectionList = $.map(items, function(item) {
			return ( {
				value : item.id,
				text : item.dataVenda
			});
		});
	});
	$scope.$watch("vendasSelection", function(selection) {
		if (typeof selection != 'undefined') {
			$scope.comanda.vendas = [];
			$.each(selection, function(idx,selectedItem) {
				var collectionItem = {};
				collectionItem.id = selectedItem.value;
				$scope.comanda.vendas.push(collectionItem);
			});
		}
	});

	$scope.pagamentosList = PagamentoResource.queryAll(function(items){
		$scope.pagamentosSelectionList = $.map(items, function(item) {
			return ( {
				value : item.id,
				text : item.dataPagamento
			});
		});
	});
	$scope.$watch("pagamentosSelection", function(selection) {
		if (typeof selection != 'undefined') {
			$scope.comanda.pagamentos = [];
			$.each(selection, function(idx,selectedItem) {
				var collectionItem = {};
				collectionItem.id = selectedItem.value;
				$scope.comanda.pagamentos.push(collectionItem);
			});
		}
	});

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
			$scope.comanda.cliente = {};
			$scope.comanda.cliente.id = selection.value;
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
			$scope.comanda.consumidor = {};
			$scope.comanda.consumidor.id = selection.value;
		}
	});
	

	$scope.save = function() {
		var successCallback = function(data,responseHeaders){
			var id = locationParser(responseHeaders);
			flash.setMessage({'type':'success','text':'The comanda was created successfully.'});
			$location.path('/Comandas');
		};
		var errorCallback = function(response) {
			if(response && response.data && response.data.message) {
				flash.setMessage({'type': 'error', 'text': response.data.message}, true);
			} else {
				flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
			}
		};
		ComandaResource.save($scope.comanda, successCallback, errorCallback);
	};
	
	$scope.cancel = function() {
		$location.path("/Comandas");
	};
});