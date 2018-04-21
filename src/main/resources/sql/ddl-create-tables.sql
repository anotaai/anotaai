drop table if exists AnotaaiSequencial
drop table if exists Arquivo
drop table if exists ArquivoTemporario
drop table if exists Balanco
drop table if exists Caderneta
drop table if exists CadernetaVenda
drop table if exists Cliente
drop table if exists Cliente_Cupom
drop table if exists ClienteConsumidor
drop table if exists ConfiguracaoCaderneta
drop table if exists Consumidor
drop table if exists Cupom
drop table if exists Devolucao
drop table if exists Disponibilidade
drop table if exists Endereco
drop table if exists EntradaMercadoria
drop table if exists Estoque
drop table if exists EstoqueMovimentacao
drop table if exists Estorno
drop table if exists FolhaCaderneta
drop table if exists FolhaCadernetaVenda
drop table if exists GrupoProduto
drop table if exists ItemBalanco
drop table if exists ItemDevolucao
drop table if exists ItemEntrada
drop table if exists ItemEstorno
drop table if exists ItemQuebra
drop table if exists ItemReceita
drop table if exists ItemVenda
drop table if exists MovimentacaoProduto
drop table if exists Pagamento
drop table if exists PagamentoAnotado
drop table if exists PagamentoAVista
drop table if exists PagamentoVenda
drop table if exists Preferencia
drop table if exists Produto
drop table if exists ProdutoGrupoProduto
drop table if exists Quebra
drop table if exists SessaoUsuario
drop table if exists Setor
drop table if exists Telefone
drop table if exists Usuario
drop table if exists UsuarioPerfil
drop table if exists Venda
drop table if exists VendaAnotadaConsumidor
drop table if exists VendaAVistaAnonima
drop table if exists VendaAVistaConsumidor
create table AnotaaiSequencial (id bigint not null auto_increment, ativo bit, sequence bigint, tipoCodigoInterno varchar(64), cliente bigint not null, primary key (id))
create table Arquivo (id bigint not null auto_increment, ativo bit, dataCriacao datetime, name varchar(255), path varchar(255), size integer, tipoArquivo integer, primary key (id))
create table ArquivoTemporario (id bigint not null auto_increment, ativo bit, uuid varchar(255), arquivo_id bigint not null, sessaoUsuario_id bigint not null, primary key (id))
create table Balanco (id bigint not null auto_increment, ativo bit, data datetime, motivo varchar(255), tipo integer, cliente_id bigint, primary key (id))
create table Caderneta (id bigint not null auto_increment, ativo bit, dataAbertura datetime, dataFechamento datetime, descricao varchar(255), cliente_id bigint, configuracao_id bigint, primary key (id))
create table CadernetaVenda (id bigint not null auto_increment, ativo bit, caderneta_id bigint not null, venda_id bigint not null, primary key (id))
create table Cliente (id bigint not null auto_increment, ativo bit, cpf bigint, dataCadastro datetime, nomeComercial varchar(255), situacaoCliente integer, endereco_id bigint, usuario_id bigint, primary key (id))
create table Cliente_Cupom (Cliente_id bigint not null, cuponsFiscais_id bigint not null)
create table ClienteConsumidor (id bigint not null auto_increment, ativo bit, dataAssociacao datetime, nomeConsumidor varchar(255), situacao integer, cliente_id bigint not null, consumidor_id bigint not null, primary key (id))
create table ConfiguracaoCaderneta (id bigint not null auto_increment, ativo bit, diaBase integer, qtdDiasDuracaoFolha integer, timeoutSetupVenda integer, tipoVendaPadrao integer, primary key (id))
create table Consumidor (id bigint not null auto_increment, ativo bit, dataCadastro datetime, usuario_id bigint, primary key (id))
create table Cupom (id bigint not null auto_increment, ativo bit, numeroCupom bigint, tipo_venda varchar(32), movimentacao_id bigint, primary key (id))
create table Devolucao (id bigint not null auto_increment, ativo bit, data datetime, cliente_id bigint, primary key (id))
create table Disponibilidade (id bigint not null auto_increment, ativo bit, dia integer, produto_id bigint, primary key (id))
create table Endereco (id bigint not null auto_increment, ativo bit, bairro varchar(255) not null, cep integer not null, cidade varchar(255) not null, complemento varchar(255), estado integer not null, logradouro varchar(255) not null, numero bigint, primary key (id))
create table EntradaMercadoria (id bigint not null auto_increment, ativo bit, codigo bigint not null, dataEntrada datetime, cliente_id bigint not null, primary key (id))
create table Estoque (id bigint not null auto_increment, ativo bit, precoCusto double precision, quantidadeEstoque bigint, primary key (id))
create table EstoqueMovimentacao (id bigint not null auto_increment, ativo bit, tipo_movimentacao varchar(16), movimentacao_id bigint, estoque_id bigint not null, primary key (id))
create table Estorno (id bigint not null auto_increment, ativo bit, dataEstorno datetime, entradaMercadoria_id bigint not null, primary key (id))
create table FolhaCaderneta (id bigint not null auto_increment, ativo bit, dataCriacao datetime, dataInicio date, dataTermino date, caderneta_id bigint, clienteConsumidor_id bigint, primary key (id))
create table FolhaCadernetaVenda (id bigint not null auto_increment, ativo bit, folhaCaderneta_id bigint not null, venda_id bigint not null, primary key (id))
create table GrupoProduto (id bigint not null auto_increment, ativo bit, descricao varchar(255), nome varchar(255) not null, grupoPai_id bigint, setor_id bigint not null, primary key (id))
create table ItemBalanco (id bigint not null auto_increment, ativo bit, quantidadeContada bigint, quantidadeEstoque bigint, situacao integer, balanco_id bigint, movimentacaoProduto_id bigint, primary key (id))
create table ItemDevolucao (id bigint not null auto_increment, ativo bit, descricao varchar(255), motivo integer, precoCusto double precision, devolucao_id bigint, movimentacaoProduto_id bigint, primary key (id))
create table ItemEntrada (id bigint not null auto_increment, ativo bit, precoCusto double precision, entradaMercadoria_id bigint, movimentacaoProduto_id bigint, primary key (id))
create table ItemEstorno (id bigint not null auto_increment, ativo bit, estorno_id bigint, movimentacaoProduto_id bigint, primary key (id))
create table ItemQuebra (id bigint not null auto_increment, ativo bit, motivoQuebra integer, precoCusto double precision, movimentacaoProduto_id bigint, quebra_id bigint, primary key (id))
create table ItemReceita (id bigint not null auto_increment, ativo bit, quantidade integer, ingrediente_id bigint not null, produto_id bigint not null, primary key (id))
create table ItemVenda (id bigint not null auto_increment, ativo bit, precoCusto double precision, precoVenda double precision not null, statusItemVenda integer, movimentacaoProduto_id bigint, venda_id bigint, primary key (id))
create table MovimentacaoProduto (id bigint not null auto_increment, ativo bit, quantidade bigint, produto_id bigint, primary key (id))
create table Pagamento (id bigint not null auto_increment, ativo bit, dataPagamento datetime, meioPagamento integer, valorPagamento double precision, valorRecebido double precision, primary key (id))
create table PagamentoAnotado (id bigint not null auto_increment, ativo bit, folhaCaderneta_id bigint, pagamento_id bigint not null, primary key (id))
create table PagamentoAVista (id bigint not null auto_increment, ativo bit, pagamento_id bigint not null, venda_id bigint, primary key (id))
create table PagamentoVenda (id bigint not null auto_increment, ativo bit, tipo_pagamento varchar(16) not null, pagamento_id bigint not null, venda_id bigint not null, primary key (id))
create table Preferencia (id bigint not null auto_increment, ativo bit, itensPerPage integer, locale integer, primary key (id))
create table Produto (id bigint not null auto_increment, ativo bit, codigo bigint not null, codigoBarras bigint not null, descricao varchar(255) not null, descricaoResumida varchar(255), ehInsumo bit not null, iconClass varchar(255), precoVenda double precision not null, tipoArmazenamento integer, unidadeMedida integer, cliente_id bigint not null, estoque_id bigint, primary key (id))
create table ProdutoGrupoProduto (id bigint not null auto_increment, ativo bit, ehPrincipal bit not null, grupoProduto_id bigint not null, produto_id bigint not null, primary key (id))
create table Quebra (id bigint not null auto_increment, ativo bit, data datetime, cliente_id bigint, produto_id bigint, primary key (id))
create table SessaoUsuario (id bigint not null auto_increment, ativo bit, keepAlive bit, sessionID varchar(255), ultimoAcesso datetime, usuario_id bigint, primary key (id))
create table Setor (id bigint not null auto_increment, ativo bit, descricao varchar(255), nome varchar(255) not null, cliente_id bigint, primary key (id))
create table Telefone (id bigint not null auto_increment, ativo bit, ddd integer not null, ddi integer not null, numero integer not null, operadora integer, primary key (id))
create table Usuario (id bigint not null auto_increment, ativo bit, codigoAtivacao varchar(255), dataCadastro datetime, email varchar(255), nome varchar(255), senha varchar(255), situacao integer, fotoPerfil_id bigint, preferencia_id bigint, telefone_id bigint not null, primary key (id))
create table UsuarioPerfil (id bigint not null auto_increment, ativo bit, perfil integer, usuario_id bigint, primary key (id))
create table Venda (id bigint not null auto_increment, ativo bit, dataVenda datetime not null, statusVenda integer not null, primary key (id))
create table VendaAnotadaConsumidor (id bigint not null auto_increment, ativo bit, folhaCadernetaVenda_id bigint not null, primary key (id))
create table VendaAVistaAnonima (id bigint not null auto_increment, ativo bit, cadernetaVenda_id bigint not null, primary key (id))
create table VendaAVistaConsumidor (id bigint not null auto_increment, ativo bit, folhaCadernetaVenda_id bigint not null, primary key (id))
alter table AnotaaiSequencial add constraint UKd8j0tryx7nx1ml3sgp4bb9jwi unique (tipoCodigoInterno, cliente)
alter table Cliente_Cupom add constraint UK_sce5rdbc2x2bots7k7o1mxppn unique (cuponsFiscais_id)
alter table Usuario add constraint UK_4tdehxj7dh8ghfc68kbwbsbll unique (email)
alter table AnotaaiSequencial add constraint FK88x6u6o5in17soayryhntmo5m foreign key (cliente) references Cliente (id)
alter table ArquivoTemporario add constraint FKhnp00a8eqshuku40y3vuevwdj foreign key (arquivo_id) references Arquivo (id)
alter table ArquivoTemporario add constraint FK7sb1wnrhee7ymseh31rbev4ur foreign key (sessaoUsuario_id) references SessaoUsuario (id)
alter table Balanco add constraint FKq9ifhxuu010kyf7lkb5lrlv9t foreign key (cliente_id) references Cliente (id)
alter table Caderneta add constraint FK47hjs1t42sibvrtr8bfwt408t foreign key (cliente_id) references Cliente (id)
alter table Caderneta add constraint FK96o8bg7s21jagbww60rraeudj foreign key (configuracao_id) references ConfiguracaoCaderneta (id)
alter table CadernetaVenda add constraint FK1so7pqqk5vqd828deweu32dfs foreign key (caderneta_id) references Caderneta (id)
alter table CadernetaVenda add constraint FKg0fjee8a0bkf8no293l0dh263 foreign key (venda_id) references Venda (id)
alter table Cliente add constraint FKjj6qtrbuooa6vy5vcd9l5uevr foreign key (endereco_id) references Endereco (id)
alter table Cliente add constraint FK4tbh39k0ug33429sademagg6o foreign key (usuario_id) references Usuario (id)
alter table Cliente_Cupom add constraint FKhvko10xlsuyouxsphcscoy2nw foreign key (cuponsFiscais_id) references Cupom (id)
alter table Cliente_Cupom add constraint FKkrw17jwqrrjf0ss3pbnmkdrku foreign key (Cliente_id) references Cliente (id)
alter table ClienteConsumidor add constraint FKpdbkflekryd6kk92kh4830pwm foreign key (cliente_id) references Cliente (id)
alter table ClienteConsumidor add constraint FKio9ofe3ya7gw2n5vhyjjkkfie foreign key (consumidor_id) references Consumidor (id)
alter table Consumidor add constraint FKb40r5omiwx5p71so2o3t0g6ik foreign key (usuario_id) references Usuario (id)
alter table Devolucao add constraint FKn89fovujkgtp9f81lmcjko4pd foreign key (cliente_id) references Cliente (id)
alter table Disponibilidade add constraint FK4c81qnxb1dfx07d6pyrkcryvm foreign key (produto_id) references Produto (id)
alter table EntradaMercadoria add constraint FK5xdg687ilk53x62uwhhowpujk foreign key (cliente_id) references Cliente (id)
alter table EstoqueMovimentacao add constraint FK9xxc4wdnrh45n5ge5dldpy91l foreign key (estoque_id) references Estoque (id)
alter table Estorno add constraint FKfsq57hv7c5evwtacalyug6lra foreign key (entradaMercadoria_id) references EntradaMercadoria (id)
alter table FolhaCaderneta add constraint FKl1r0cnf4y463jy3ow0wt0yucs foreign key (caderneta_id) references Caderneta (id)
alter table FolhaCaderneta add constraint FK15ms3v3h1djlrswnr6qebd5qe foreign key (clienteConsumidor_id) references ClienteConsumidor (id)
alter table FolhaCadernetaVenda add constraint FK9kexoxipyfa2wnvo3trl24a7f foreign key (folhaCaderneta_id) references FolhaCaderneta (id)
alter table FolhaCadernetaVenda add constraint FK79qn3lakjj7r3x5nmmhkeb3ss foreign key (venda_id) references Venda (id)
alter table GrupoProduto add constraint FKk592i94h8snas5bagku44goyi foreign key (grupoPai_id) references GrupoProduto (id)
alter table GrupoProduto add constraint FKyslglhddt8of2w5x3k6hk1ic foreign key (setor_id) references Setor (id)
alter table ItemBalanco add constraint FKn3ovov0l1te4ld8xvs5pwg1k3 foreign key (balanco_id) references Balanco (id)
alter table ItemBalanco add constraint FK8rmdaq8ek75vgh4s7pc2s37t4 foreign key (movimentacaoProduto_id) references MovimentacaoProduto (id)
alter table ItemDevolucao add constraint FKaj8ranmoveav2qe7l8vk8wta3 foreign key (devolucao_id) references Devolucao (id)
alter table ItemDevolucao add constraint FKmgn6n2mlycllfnpexu2pr4ul1 foreign key (movimentacaoProduto_id) references MovimentacaoProduto (id)
alter table ItemEntrada add constraint FKkynql0xcbb4l8tkiy589ltrwu foreign key (entradaMercadoria_id) references EntradaMercadoria (id)
alter table ItemEntrada add constraint FKja13rtoefu2swo3vhvnqaebn9 foreign key (movimentacaoProduto_id) references MovimentacaoProduto (id)
alter table ItemEstorno add constraint FK3a5a21mpq217lj45gtv017ep9 foreign key (estorno_id) references Estorno (id)
alter table ItemEstorno add constraint FKgjeb7r7bp9675lgcwwoyi5wof foreign key (movimentacaoProduto_id) references MovimentacaoProduto (id)
alter table ItemQuebra add constraint FK25hwn8mibh91q11atj7xi3s1n foreign key (movimentacaoProduto_id) references MovimentacaoProduto (id)
alter table ItemQuebra add constraint FKc3xtossrw9bxip4ty8u5l1b9a foreign key (quebra_id) references Quebra (id)
alter table ItemReceita add constraint FKhel7udtqta3tdeywfwhjqhi7a foreign key (ingrediente_id) references Produto (id)
alter table ItemReceita add constraint FKcvxds0pc83eetvwdilthimjvk foreign key (produto_id) references Produto (id)
alter table ItemVenda add constraint FK7wg51531yj6n4nbh5sfgqyunb foreign key (movimentacaoProduto_id) references MovimentacaoProduto (id)
alter table ItemVenda add constraint FKn0kr2upuk6qhw34a49ibq6eji foreign key (venda_id) references Venda (id)
alter table MovimentacaoProduto add constraint FKr68x8k4ww7yo2wc99l0kbwxlk foreign key (produto_id) references Produto (id)
alter table PagamentoAnotado add constraint FKt442egly5cp8b4itrn3natdo5 foreign key (folhaCaderneta_id) references FolhaCaderneta (id)
alter table PagamentoAnotado add constraint FKcujkj25i3fh4p3igu6kanh4ml foreign key (pagamento_id) references Pagamento (id)
alter table PagamentoAVista add constraint FKhle43pw381tben5joooglwegv foreign key (pagamento_id) references Pagamento (id)
alter table PagamentoAVista add constraint FKrgmmhcb50uj7qeogun2o3rhxx foreign key (venda_id) references VendaAVistaAnonima (id)
alter table PagamentoVenda add constraint FK63jkgj5bi89nvecnpmpbl8cvk foreign key (venda_id) references Venda (id)
alter table Produto add constraint FK3dhhfkpqmnnhbypsm79j9dwtf foreign key (cliente_id) references Cliente (id)
alter table Produto add constraint FKqhb2r32a5tvcxiv5vc4976smy foreign key (estoque_id) references Estoque (id)
alter table ProdutoGrupoProduto add constraint FKis8qqes1w41nw41jjuuvh1edp foreign key (grupoProduto_id) references GrupoProduto (id)
alter table ProdutoGrupoProduto add constraint FKqs90u79aruvcsgk079a7f1fcm foreign key (produto_id) references Produto (id)
alter table Quebra add constraint FKtlscvaowyd78dfx1taf20x2ql foreign key (cliente_id) references Cliente (id)
alter table Quebra add constraint FKjkr6j8wcqjhiav6qmqn94ty6m foreign key (produto_id) references Produto (id)
alter table SessaoUsuario add constraint FK2yr1e0eubk6mprd8rqv4pngy7 foreign key (usuario_id) references Usuario (id)
alter table Setor add constraint FKa238i44fcy7kq3wdeunitwjkf foreign key (cliente_id) references Cliente (id)
alter table Usuario add constraint FK1cjgnwq7ny3s0x5ohefbtywk0 foreign key (fotoPerfil_id) references Arquivo (id)
alter table Usuario add constraint FK5y9udwckjgh05fde1kgsqohqx foreign key (preferencia_id) references Preferencia (id)
alter table Usuario add constraint FK5v3tnihrrxshryp0lqvjqcp37 foreign key (telefone_id) references Telefone (id)
alter table UsuarioPerfil add constraint FKncf2bpwj4qc1annptuhdp9m8t foreign key (usuario_id) references Usuario (id)
alter table VendaAnotadaConsumidor add constraint FKjjwu6o7fi75aexrkextofeuti foreign key (folhaCadernetaVenda_id) references FolhaCadernetaVenda (id)
alter table VendaAVistaAnonima add constraint FK187oo7q1ako3cr958o1xf0035 foreign key (cadernetaVenda_id) references CadernetaVenda (id)
alter table VendaAVistaConsumidor add constraint FKab0g2cdri48qknj2cilrko0hw foreign key (folhaCadernetaVenda_id) references FolhaCadernetaVenda (id)
