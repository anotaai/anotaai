insert into sessaousuario (ativo, keepAlive, sessionID, ultimoAcesso, usuario_id) values (true, true, '10481', now(), 1);
insert into Endereco (bairro, cep, cidade, complemento, estado, logradouro, ativo) values ('Calafate', '30411580', 'Belo Horizonte', 'apartamento 102', 10, 'rua da paz', true);
insert into Telefone (ddd, ddi, numero, operadora, ativo) values (31, 55, 997749131, 1, true);
insert into Preferencia (itensPerPage, ativo) values (5, true); 
insert into Usuario (dataCadastro, email, fotoPerfil_id, nome, senha, telefone_id, situacao, codigoativacao, preferencia_id, ativo) values (Now(), 'anotaai@gmail.com', null, 'Anota ai', '5A8D23E9E52765D58E75534EDDEFCA6DE8F88E99284FFA09E445D5BE52267922', 1, 0, '0cf81e55-8a22-48ed-bd8f-b3e5166eca90', 1, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (1, 1, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (2, 1, true);
insert into Cliente (cpf, dataCadastro, endereco_id, nomeComercial, situacaoCliente, usuario_id, ativo) values (55546265302, Now(), 1, 'A Line Solutions', 0, 1, true);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'PRODUTO', 1);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'CUPOM', 1);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'ENTRADA_MERCADORIA', 1);

insert into Endereco (bairro, cep, cidade, complemento, estado, logradouro, ativo) values ('Conjunto Água Branca', '32370500', 'Contagem', 'casa', 68, 'rua m', true);
insert into Telefone (ddd, ddi, numero, operadora, ativo) values (31, 55, 988300083, 1, true);
insert into Preferencia (itensPerPage, ativo) values (5, true);
insert into Usuario (dataCadastro, email, fotoPerfil_id, nome, senha, telefone_id, situacao, codigoativacao, preferencia_id, ativo) values (Now(), 'gleidsongmoura@gmail.com', null, 'Gleidson Guimarães Moura', '5A8D23E9E52765D58E75534EDDEFCA6DE8F88E99284FFA09E445D5BE52267922', 2, 0, '0cf81e55-8a22-48ed-bd8f-b3e5166eca85', 2, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (1, 2, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (2, 2, true);
insert into Cliente (cpf, dataCadastro, endereco_id, nomeComercial, situacaoCliente, usuario_id, ativo) values (55546265303, Now(), 2, 'Lanchonete da hora', 0, 2, true);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'PRODUTO', 2);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'CUPOM', 2);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'ENTRADA_MERCADORIA', 2);

insert into Setor (nome, descricao, cliente_id, ativo) values ('Liquido', 'Setor teste', 1, true);
insert into GrupoProduto (nome, descricao, setor_id, ativo) values ('Bebidas', 'Produtos Líquidos', 1, true);
insert into Estoque (precocusto, quantidadeestoque, ativo) values (55.50, 150, true);
insert into Produto (descricao, descricaoresumida, precovenda, unidademedida, iconclass, cliente_id, estoque_id, codigo, ehInsumo, codigoBarras, ativo, tipoArmazenamento) values ('Cerveja Brahma 350ml', 'Brahma 350ml', 3, 3, 'GLYPHICON_MUSIC', 1, 1, 7891149010301, false, false, true, 1);
insert into ProdutoGrupoProduto (grupoproduto_id, produto_id, ativo, ehPrincipal) values (1, 1, true, true);

insert into GrupoProduto (nome, descricao, setor_id, ativo) values ('Salgados', 'Produtos Fritos', 1, true);
insert into Estoque (precocusto, quantidadeestoque, ativo) values (10.50, 1200, true);
insert into Produto (descricao, descricaoresumida, precovenda, unidademedida, iconclass, cliente_id, estoque_id, codigo, ehInsumo, codigoBarras, ativo, tipoArmazenamento) values ('Coxinha de Frango', 'Coxinha', 3.5, 7, 'GLYPHICON_CLOUD', 1, 2, 7894568551258, false, false, true, 2);
insert into Disponibilidade (dia, produto_id, ativo) values (0, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (1, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (2, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (3, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (4, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (5, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (6, 1, true);
insert into ProdutoGrupoProduto (grupoproduto_id, produto_id, ativo, ehPrincipal) values (2, 2, true, true);

insert into Setor (nome, descricao, cliente_id, ativo) values ('Padaria', 'Setor dois', 2, true);
insert into GrupoProduto (nome, descricao, setor_id, ativo) values ('Bolos', 'Grupo Produto Bolos', 2, true);
insert into Estoque (precocusto, quantidadeestoque, ativo) values (0, 0, true);
insert into Produto (descricao, descricaoresumida, precovenda, unidademedida, iconclass, cliente_id, estoque_id, codigo, ehInsumo, codigoBarras, ativo, tipoArmazenamento) values ('Sacola Plástica', 'Sacola', 3.5, 7, 'GLYPHICON_STAR', 2, 3, 7893214568825, false, false, true, 0);
insert into ItemReceita (produto_id, ingrediente_id, quantidade, ativo) values (2, 1, 12, true);
insert into Disponibilidade (dia, produto_id, ativo) values (0, 3, true);
insert into Disponibilidade (dia, produto_id, ativo) values (1, 3, true);
insert into Disponibilidade (dia, produto_id, ativo) values (2, 3, true);
insert into Disponibilidade (dia, produto_id, ativo) values (3, 3, true);
insert into Disponibilidade (dia, produto_id, ativo) values (4, 3, true);
insert into Disponibilidade (dia, produto_id, ativo) values (5, 3, true);
insert into Disponibilidade (dia, produto_id, ativo) values (6, 3, true);
insert into ProdutoGrupoProduto (grupoproduto_id, produto_id, ativo, ehPrincipal) values (2, 3, true, true);

insert into EntradaMercadoria (ativo, dataEntrada,codigo,cliente_id) values (true, now(),7894568551258,1);
insert into MovimentacaoProduto (ativo, quantidade, produto_id) values (true, 1200, 2);
insert into ItemEntrada (ativo, precoCusto, entradaMercadoria_id, movimentacaoProduto_id) values (true, 10.50, 1, 1);
insert into EstoqueMovimentacao (ativo, tipo_movimentacao, movimentacao_id, estoque_id) values (true, 'ENTRADA', 1, 1);

insert into MovimentacaoProduto (ativo, quantidade, produto_id) values (true, 50, 1);
insert into ItemEntrada (ativo, precoCusto, entradaMercadoria_id, movimentacaoProduto_id) values (true, 5.25  , 1, 2);
insert into EstoqueMovimentacao (ativo, tipo_movimentacao, movimentacao_id, estoque_id) values (true, 'ENTRADA', 1, 1);

insert into EntradaMercadoria (ativo, dataEntrada,codigo,cliente_id) values (true, now(),7893214568825,1);
insert into MovimentacaoProduto (ativo, quantidade, produto_id) values (true, 100, 1);
insert into ItemEntrada (ativo, precoCusto, entradaMercadoria_id, movimentacaoProduto_id) values (true, 50.25, 2, 3);
insert into EstoqueMovimentacao (ativo, tipo_movimentacao, movimentacao_id, estoque_id) values (true, 'ENTRADA', 1, 1);

insert into Venda (ativo, dataVenda) values (true, now());
insert into MovimentacaoProduto (ativo, quantidade, produto_id) values (true, 12, 1);
insert into ItemVenda (ativo, precoCusto, precoVenda, movimentacaoProduto_id, venda_id) values (true, 2, 3, 1, 1);
insert into EstoqueMovimentacao (ativo, tipo_movimentacao, movimentacao_id, estoque_id) values (true, 'VENDA', 2, 1);

insert into Consumidor(ativo,dataCadastro, usuario_id) values (true,Now(),1);
insert into ClienteConsumidor(ativo,cliente_id,consumidor_id,dataAssociacao,situacao) values (true,1,1,Now(),1);

insert into ConfiguracaoCaderneta(ativo,qtdDiasDuracaoFolha,diaBase) values (true,30,1);
insert into Caderneta(ativo, descricao , dataAbertura , dataFechamento , configuracao_id, cliente_id) values (true,'Caderneta Escola', now(), now(),1,1);