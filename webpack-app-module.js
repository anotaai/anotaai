// ------ Vendor -------

//Javascript
var $ = require('jquery');
window.jQuery = $;
window.$ = $;

require('angular');
require('angular-route');
require('angular-ui-router'); 
require('angular-resource');
require('angular-ui-mask');
require('angular-cookies');
require('angular-translate');
require('angular-growl-v2');
require('angular-animate');
require('angular-ui-bootstrap');
require('app-js/vendor/angular-locale_pt-br.js');
require('app-js/vendor/ng-currency.js');
require('app-js/vendor/angular/upload/ng-file-upload-all.min.js');
require('app-js/vendor/angular/upload/ng-file-upload-shim.min.js');
require('app-js/vendor/angular/upload/ng-file-upload.min.js');
require('select2');
require('app-js/vendor/select2/pt-BR.js');
firebase = require('firebase');


//Css
require("bootstrap-webpack!./bootstrap.config.js");
require('app-css/main.css'); 
require("npm/angular-growl-v2/build/angular-growl.min.css");
require("npm/select2/dist/css/select2.min.css");
//require('app-css/font-awesome.min.css'); 



// ---------- Application -----------

//Javascript
require("app-js/app/main.js");
require("app-js/app/access.js");
require("app-js/app/app.js");
require("app-js/app/httpInterceptor.js");
require("app-js/app/run.js");
require("app-js/app/offcanvas.js");
require("app-js/directives/datepicker.js");
require("app-js/directives/timepicker.js");
require("app-js/directives/datetimepicker.js");
require("app-js/directives/confirm-click.js");
require("app-js/filters/startFromFilter.js");
require("app-js/filters/telefoneFilter.js");
require("app-js/filters/genericSearchFilter.js");
require("app-js/services/app/flash.js");
require("app-js/services/app/LocationParser.js");
require("app-js/services/app/Authentication.js");
require("app-js/services/app/Base64Service.js");
require("app-js/services/util/EnumFactory.js");
require("app-js/services/produto/ProdutoFactory.js");
require("app-js/services/produto/DisponibilidadeFactory.js");
require("app-js/services/produto/ItemReceitaFactory.js");
require("app-js/services/produto/GrupoProdutoFactory.js");
require("app-js/services/produto/SetorFactory.js");
require("app-js/services/venda/VendaFactory.js");
require("app-js/services/cliente/ClienteConsumidorFactory.js");
require("app-js/services/SessaoUsuarioFactory.js");
require("app-js/services/EnderecoFactory.js");
require("app-js/services/ClienteFactory.js");
require("app-js/services/UsuarioFactory.js");
require("app-js/services/ComandaFactory.js");
require("app-js/services/ProdutoGrupoProdutoFactory.js");
require("app-js/services/menu/MenuFactory.js");
require("app-js/controllers/util/flashController.js");
require("app-js/controllers/main/MainController.js");
require("app-js/controllers/activate/activateController.js");
require("app-js/controllers/login/loginController.js");
require("app-js/controllers/login/renewPasswordController.js");
require("app-js/controllers/login/profileController.js");
require("app-js/controllers/cliente/newClienteController.js");
require("app-js/controllers/menu/MenuController.js");
require("app-js/app/resources/config.js");
require("app-js/app/resources/pt_BR.js");
require("app-js/app/resources/en_US.js");

//-------------------------------Cliente----------------------------------
require("app-js/controllers/cliente/searchClienteController.js");
require("app-js/controllers/cliente/editClienteController.js");

//-------------------------------Consumidor-------------------------------
require("app-js/controllers/consumidor/NewConsumidorController.js");
require("app-js/controllers/consumidor/SearchConsumidorController.js");
require("app-js/controllers/consumidor/EditConsumidorController.js");

//-------------------------------Produto----------------------------------
require("app-js/controllers/produto/NewProdutoController.js");
require("app-js/controllers/produto/SearchProdutoController.js");
require("app-js/controllers/produto/EditProdutoController.js");

//--------------------------------Venda-----------------------------------
require("app-js/controllers/venda/NewVendaController.js");
require("app-js/controllers/venda/SearchVendaController.js");
require("app-js/controllers/venda/EditVendaController.js");

//--------------------------------Setor-----------------------------------
require("app-js/controllers/setor/NewSetorController.js");
require("app-js/controllers/setor/SearchSetorController.js");
require("app-js/controllers/setor/EditSetorController.js");
	
require("app-js/controllers/newComandaController.js");
require("app-js/controllers/searchComandaController.js");
require("app-js/controllers/editComandaController.js");
require("app-js/controllers/newEnderecoController.js");
require("app-js/controllers/searchEnderecoController.js");
require("app-js/controllers/editEnderecoController.js");
require("app-js/controllers/grupoproduto/NewGrupoProdutoController.js");
require("app-js/controllers/grupoproduto/SearchGrupoProdutoController.js");
require("app-js/controllers/grupoproduto/EditGrupoProdutoController.js");
require("app-js/controllers/newProdutoGrupoProdutoController.js");
require("app-js/controllers/searchProdutoGrupoProdutoController.js");
require("app-js/controllers/editProdutoGrupoProdutoController.js");
require("app-js/controllers/newSessaoUsuarioController.js");
require("app-js/controllers/searchSessaoUsuarioController.js");
require("app-js/controllers/editSessaoUsuarioController.js");
require("app-js/controllers/usuario/registerUsuarioController.js");
require("app-js/controllers/usuario/newUsuarioController.js");
require("app-js/controllers/usuario/searchUsuarioController.js");
require("app-js/controllers/usuario/editUsuarioController.js");