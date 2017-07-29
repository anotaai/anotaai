package br.com.alinesolutions.anotaai.model.usuario;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSequencial;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.SituacaoCliente;
import br.com.alinesolutions.anotaai.model.produto.ProdutoGrupoProduto;
import br.com.alinesolutions.anotaai.model.usuario.Cliente.ClienteConstant;
import br.com.alinesolutions.anotaai.model.venda.Cupom;

@NamedQueries({ 
	@NamedQuery(name = ClienteConstant.FIND_BY_USUARIO_KEY, query = ClienteConstant.FIND_BY_USUARIO_QUERY),
	@NamedQuery(name = ClienteConstant.FIND_BY_PRODUTO_KEY, query = ClienteConstant.FIND_BY_PRODUTO_QUERY),
	@NamedQuery(name = ClienteConstant.FIND_BY_DISPONIBILIDADE_KEY, query = ClienteConstant.FIND_BY_DISPONIBILIDADE_QUERY),
	@NamedQuery(name = ClienteConstant.FIND_BY_GRUPO_PRODUTO_KEY, query = ClienteConstant.FIND_BY_GRUPO_PRODUTO_QUERY),
	@NamedQuery(name = ClienteConstant.FIND_BY_SETOR_KEY, query = ClienteConstant.FIND_BY_SETOR_QUERY),
	@NamedQuery(name = ClienteConstant.FIND_BY_PRODUTO_GRUPO_PRODUTO_KEY, query = ClienteConstant.FIND_BY_PRODUTO_GRUPO_PRODUTO_QUERY)
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Cliente set ativo = false where id = ?")
public class Cliente extends BaseEntity<Long, Cliente> implements IPessoa {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private Usuario usuario;

	private String nomeComercial;

	private Long cpf;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Endereco endereco;

	@OneToMany(mappedBy = "produto")
	private List<ProdutoGrupoProduto> produtos;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
	private List<ClienteConsumidor> consumidores;

	@OneToMany
	private List<Cupom> cuponsFiscais;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Enumerated(EnumType.ORDINAL)
	private SituacaoCliente situacaoCliente;

	@OneToMany(mappedBy = "cliente", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	private List<AnotaaiSequencial> sequences;

	public Cliente() {
		super();
	}

	public Cliente(Long id, String nomeComercial, Long cpf, Date dataCadastro, Long idUsuario, String nomeUsuario) {
		super();
		this.nomeComercial = nomeComercial;
		this.cpf = cpf;
		this.dataCadastro = dataCadastro;
		this.setId(id);
		this.setUsuario(new Usuario());
		this.getUsuario().setId(idUsuario);
		this.getUsuario().setNome(nomeUsuario);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<ProdutoGrupoProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoGrupoProduto> produtos) {
		this.produtos = produtos;
	}

	public List<ClienteConsumidor> getConsumidores() {
		return consumidores;
	}

	public void setConsumidores(List<ClienteConsumidor> consumidores) {
		this.consumidores = consumidores;
	}

	public List<AnotaaiSequencial> getSequences() {
		return sequences;
	}

	public void setSequences(List<AnotaaiSequencial> sequences) {
		this.sequences = sequences;
	}

	public String getNomeComercial() {
		return nomeComercial;
	}

	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public SituacaoCliente getSituacaoCliente() {
		return situacaoCliente;
	}

	public void setSituacaoCliente(SituacaoCliente situacaoCliente) {
		this.situacaoCliente = situacaoCliente;
	}

	public List<Cupom> getCuponsFiscais() {
		return cuponsFiscais;
	}

	public void setCuponsFiscais(List<Cupom> cuponsFiscais) {
		this.cuponsFiscais = cuponsFiscais;
	}

	public interface ClienteConstant {

		String FIELD_USUARIO = "usuario";
		String FIND_BY_USUARIO_KEY = "Cliente.findByUsuario";
		String FIND_BY_USUARIO_QUERY = "select c from Cliente c where c.usuario = :usuario";

		String FIND_BY_PRODUTO_KEY = "Cliente.findClienteByProduto";
		String FIND_BY_PRODUTO_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Cliente(c.id, c.nomeComercial, c.cpf, c.dataCadastro, u.id, u.nome) from Produto p join p.cliente c join c.usuario u where p.id = :id";

		String FIND_BY_DISPONIBILIDADE_KEY = "Cliente.findClienteByDisponibilidade";
		String FIND_BY_DISPONIBILIDADE_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Cliente(c.id, c.nomeComercial, c.cpf, c.dataCadastro, u.id, u.nome) from Disponibilidade d join d.produto p join p.cliente c join c.usuario u where d.id = :id";

		String FIND_BY_GRUPO_PRODUTO_KEY = "Cliente.findClienteByGrupoProduto";
		String FIND_BY_GRUPO_PRODUTO_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Cliente(c.id, c.nomeComercial, c.cpf, c.dataCadastro, u.id, u.nome) from GrupoProduto gp join gp.setor s join s.cliente c join c.usuario u where gp.id = :id";

		String FIND_BY_SETOR_KEY = "Cliente.findClienteBySetor";
		String FIND_BY_SETOR_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Cliente(c.id, c.nomeComercial, c.cpf, c.dataCadastro, u.id, u.nome) from Setor s join s.cliente c join c.usuario u where s.id = :id";
		
		String FIND_BY_PRODUTO_GRUPO_PRODUTO_KEY = "Cliente.findClienteByProdutoGrupoProduto";
		String FIND_BY_PRODUTO_GRUPO_PRODUTO_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Cliente(c.id, c.nomeComercial, c.cpf, c.dataCadastro, u.id, u.nome) from ProdutoGrupoProduto pgp join pgp.produto p join p.cliente c join c.usuario u where pgp.id = :id";
	}
}
