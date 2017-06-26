insert into Endereco (bairro, cep, cidade, complemento, estado, logradouro, ativo) values ('Calafate', '30411580', 'Belo Horizonte', 'apartamento 102', 10, 'rua da paz', true);
insert into Telefone (ddd, ddi, numero, operadora, ativo) values (31, 55, 997749131, 1, true);
insert into Preferencia (itensPerPage, ativo) values (5, true); 
insert into Usuario (dataCadastro, email, fotoPerfil_id, nome, senha, telefone_id, situacao, codigoativacao, preferencia_id, ativo) values (Now(), 'anotaai@gmail.com', null, 'Anota ai', '5A8D23E9E52765D58E75534EDDEFCA6DE8F88E99284FFA09E445D5BE52267922', 1, 0, '0cf81e55-8a22-48ed-bd8f-b3e5166eca90', 1, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (1, 1, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (2, 1, true);
insert into Cliente (cpf, dataCadastro, endereco_id, nomeComercial, situacaoCliente, usuario_id, ativo) values (55546265302, Now(), 1, 'A Line Solutions', 0, 1, true);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'PRODUTO', 1);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'CUPOM', 1);

insert into Endereco (bairro, cep, cidade, complemento, estado, logradouro, ativo) values ('Conjunto Água Branca', '32370500', 'Contagem', 'casa', 68, 'rua m', true);
insert into Telefone (ddd, ddi, numero, operadora, ativo) values (31, 55, 988300083, 1, true);
insert into Preferencia (itensPerPage, ativo) values (5, true);
insert into Usuario (dataCadastro, email, fotoPerfil_id, nome, senha, telefone_id, situacao, codigoativacao, preferencia_id, ativo) values (Now(), 'gleidsongmoura@gmail.com', null, 'Gleidson Guimarães Moura', '5A8D23E9E52765D58E75534EDDEFCA6DE8F88E99284FFA09E445D5BE52267922', 2, 0, '0cf81e55-8a22-48ed-bd8f-b3e5166eca85', 2, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (1, 2, true);
insert into UsuarioPerfil (perfil, usuario_id, ativo) values (2, 2, true);
insert into Cliente (cpf, dataCadastro, endereco_id, nomeComercial, situacaoCliente, usuario_id, ativo) values (55546265303, Now(), 2, 'Lanchonete da hora', 0, 2, true);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'PRODUTO', 2);
insert into Anotaaisequencial(ativo, sequence, tipocodigointerno, cliente) VALUES (true, 1, 'CUPOM', 2);

insert into Setor (nome, descricao, cliente_id, ativo) values ('Liquido', 'Setor teste', 1, true)
insert into GrupoProduto (nome, descricao, setor_id, ativo) values ('Bebidas', 'Produtos Líquidos', 1, true);
insert into Estoque (precocusto, quantidadeestoque, ativo) values (1.8, 1000, true);
insert into Produto (descricao, descricaoresumida, precovenda, unidademedida, iconclass, cliente_id, estoque_id, codigo, ehInsumo, codigoGerado, ativo) values ('Cerveja Brahma 350ml', 'Brahma 350ml', 3, 3, 'GLYPHICON_MUSIC', 1, 1, 7891149010301, false, false, true);
insert into ProdutoGrupoProduto (grupoproduto_id, produto_id, ativo) values (1, 1, true);

insert into GrupoProduto (nome, descricao, setor_id, ativo) values ('Salgados', 'Produtos Fritos', 1, true);
insert into Estoque (precocusto, quantidadeestoque, ativo) values (8.2, 300, true);
insert into Produto (descricao, descricaoresumida, precovenda, unidademedida, iconclass, cliente_id, estoque_id, codigo, ehInsumo, codigoGerado, ativo) values ('Coxinha de Frango', 'Coxinha', 3.5, 7, 'GLYPHICON_CLOUD', 1, 2, 7894568551258, false, false, true);
insert into Disponibilidade (dia, produto_id, ativo) values (0, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (1, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (2, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (3, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (4, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (5, 1, true);
insert into Disponibilidade (dia, produto_id, ativo) values (6, 1, true);
insert into ProdutoGrupoProduto (grupoproduto_id, produto_id, ativo) values (2, 2, true);

insert into Setor (nome, descricao, cliente_id, ativo) values ('Padaria', 'Setor dois', 2, true)
insert into GrupoProduto (nome, descricao, setor_id, ativo) values ('Bolos', 'Grupo Produto Bolos', 2, true);
insert into Estoque (precocusto, quantidadeestoque, ativo) values (0.09, 875, true);
insert into Produto (descricao, descricaoresumida, precovenda, unidademedida, iconclass, cliente_id, estoque_id, codigo, ehInsumo, codigoGerado, ativo) values ('Sacola Plástica', 'Sacola', 3.5, 7, 'GLYPHICON_STAR', 2, 3, 7893214568825, false, false, true);
insert into ItemReceita (produto_id, ingrediente_id, quantidade, ativo) values (2, 1, 12, true);
insert into Disponibilidade (dia, produto_id, ativo) values (0, 2, true);
insert into Disponibilidade (dia, produto_id, ativo) values (1, 2, true);
insert into Disponibilidade (dia, produto_id, ativo) values (2, 2, true);
insert into Disponibilidade (dia, produto_id, ativo) values (3, 2, true);
insert into Disponibilidade (dia, produto_id, ativo) values (4, 2, true);
insert into Disponibilidade (dia, produto_id, ativo) values (5, 2, true);
insert into Disponibilidade (dia, produto_id, ativo) values (6, 2, true);
insert into ProdutoGrupoProduto (grupoproduto_id, produto_id, ativo) values (2, 3, true);

insert into Venda (ativo, dataVenda) values (true, now());
insert into MovimentacaoProduto (ativo, quantidade, tipoAtualizacao, tipoMovimentacao, produto_id) values (true, 12, 0, 1, 1);
insert into ItemVenda (ativo, precoCusto, precoVenda, movimentacao_id, venda_id) values (true, 2, 3, 1, 1);
insert into EstoqueMovimentacao (ativo, tipo_movimentacao, movimentacao_id, estoque_id) values (true, 'VENDA', 1, 1)

insert into Consumidor(ativo,dataCadastro, usuario_id) values (true,Now(),1);
insert into ClienteConsumidor(ativo,cliente_id,consumidor_id,dataAssociacao,situacao) values (true,1,1,Now(),1);