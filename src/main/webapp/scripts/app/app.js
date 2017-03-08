'use strict';

angular.module('anotaai')
	.config(function ($routeProvider, $stateProvider, $urlRouterProvider) {
		$stateProvider
		.state('app.cliente-consumidor', {
			url: '/Cliente/Consumidor',
			templateUrl:'views/Cliente/Consumidor/search.html',
			//para injetar o objeto no dataTransferObjet este valor 
			//deve ser passado no controller
			controller:'SearchConsumidorController as object'}
		).state('app.cliente-consumidor-new', {
			url: '/Cliente/Consumidor/new',
			templateUrl:'views/Cliente/Consumidor/detail.html',
			controller:'NewConsumidorController'}
		).state('app.cliente-consumidor-edit', {
			url: '/Cliente/Consumidor/edit/:ConsumidorId',
			templateUrl:'views/Cliente/Consumidor/detail.html',
			controller:'EditConsumidorController as object'}
		).state('app.grupo-produto', {
			url: '/GrupoProduto',
			templateUrl:'views/GrupoProduto/search.html',
			controller:'SearchGrupoProdutoController as object'}
		).state('app.grupo-produto-new', {
			url: '/GrupoProduto/new',
			templateUrl:'views/GrupoProduto/detail.html',
			controller:'NewGrupoProdutoController'}
		).state('app.grupo-produto-edit', {
			url: '/GrupoProduto/edit/:GrupoProdutoId',
			templateUrl:'views/GrupoProduto/detail.html',
			controller:'EditGrupoProdutoController as object'}
		).state('app.setor', {
			url: '/Setor',
			templateUrl:'views/Setor/search.html',
			controller:'SearchSetorController as object'}
		).state('app.setor-new', {
			url: '/Setor/new',
			templateUrl:'views/Setor/detail.html',
			controller:'NewSetorController'}
		).state('app.setor-edit', {
			url: '/Setor/edit/:SetorId',
			templateUrl:'views/Setor/detail.html',
			controller:'EditSetorController as object'}
		).state('app.produto', {
			url: '/Produto',
			templateUrl:'views/Produto/search.html',
			controller:'SearchProdutoController'}
		).state('app.produto-new', {
			url: '/Produto/new',
			templateUrl:'views/Produto/detail.html',
			controller:'NewProdutoController'}
		).state('app.produto-edit', {
			url: '/Produto/edit/:ProdutoId',
			templateUrl:'views/Produto/detail.html',
			controller:'EditProdutoController'}
		).state('app.venda', {
			url: '/Venda',
			templateUrl:'views/Venda/search.html',
			controller:'SearchVendaController'}
		).state('app.venda-new', {
			url: '/Venda/new',
			templateUrl:'views/Venda/detail.html',
			controller:'NewVendaController'}
		).state('app.venda-edit', {
			url: '/Venda/edit/:VendaId',
			templateUrl:'views/Venda/detail.html',
			controller:'EditVendaController'}
		);
	}
);
