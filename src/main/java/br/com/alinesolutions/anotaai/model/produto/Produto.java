package br.com.alinesolutions.anotaai.model.produto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.alinesolutions.anotaai.metadata.model.domain.Icon;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoArmazenamento;
import br.com.alinesolutions.anotaai.model.domain.UnidadeMedida;
import br.com.alinesolutions.anotaai.model.produto.Produto.ProdutoConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@NamedQueries({
	@NamedQuery(name = ProdutoConstant.LIST_ALL_KEY, query = ProdutoConstant.LIST_ALL_QUERY),
	@NamedQuery(name = ProdutoConstant.LIST_SEARCH_BY_GRUPO_PRODUTO_KEY, query = ProdutoConstant.LIST_SEARCH_BY_GRUPO_PRODUTO_QUERY),
	@NamedQuery(name = ProdutoConstant.LIST_SEARCH_BY_DESCRICAO_KEY, query = ProdutoConstant.LIST_SEARCH_BY_DESCRICAO_QUERY),
	@NamedQuery(name = ProdutoConstant.PRODUTO_BY_ID_KEY, query = ProdutoConstant.PRODUTO_BY_ID_QUERY),
	@NamedQuery(name = ProdutoConstant.PRODUTO_BY_CODIGO_KEY, query = ProdutoConstant.PRODUTO_BY_CODIGO_QUERY),
	@NamedQuery(name = ProdutoConstant.DISPONIBILIDADE_BY_PRODUTO_KEY, query = ProdutoConstant.DISPONIBILIDADE_BY_PRODUTO_QUERY),
	@NamedQuery(name = ProdutoConstant.EDIT_KEY, query = ProdutoConstant.EDIT_QUERY),
	@NamedQuery(name = ProdutoConstant.ITEM_RECEITA_BY_PRODUTO_KEY, query = ProdutoConstant.ITEM_RECEITA_BY_PRODUTO_QUERY)
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Produto set ativo = false where id = ?")
@XmlRootElement
public class Produto extends BaseEntity<Long, Produto> {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Long codigo;
	
	@Column(nullable = false)
	private String descricao;
	
	private String descricaoResumida;
	
	@Column(nullable = false)
	private Double precoVenda;
	
	@Column(nullable = false)
	private Boolean ehInsumo;
	
	@Column(nullable = false)
	private Boolean codigoGerado;

	@Enumerated(EnumType.STRING)
	private Icon iconClass;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	private Cliente cliente;

	@OneToOne(optional = true, cascade = { CascadeType.ALL })
	private Estoque estoque;

	@Enumerated(EnumType.ORDINAL)
	private UnidadeMedida unidadeMedida;

	@OneToMany(mappedBy = "produto", cascade = { CascadeType.DETACH })
	private List<MovimentacaoProduto> movimentacoes;

	@JsonManagedReference(value="diasDisponibilidade")
	@OneToMany(mappedBy = "produto", cascade = { CascadeType.ALL })
	private List<Disponibilidade> diasDisponibilidade;

	@JsonManagedReference(value="itensReceita")
	@OneToMany(mappedBy = "produto", cascade = { CascadeType.ALL })
	private List<ItemReceita> itensReceita;

	@OneToMany(mappedBy = "produto", cascade = { CascadeType.ALL })
	private List<ProdutoGrupoProduto> grupos;

	@Enumerated(EnumType.ORDINAL)
	private TipoArmazenamento tipoArmazenamento;
	
	public Produto() {
		super();
	}

	public Produto(Long id, String descricao, String descricaoResumida, Double precoVenda, Icon iconClass) {
		this();
		setId(id);
		this.descricao = descricao;
		this.descricaoResumida = descricaoResumida;
		this.precoVenda = precoVenda;
		this.iconClass = iconClass;
	}

	public Produto(Long id, String descricao, String descricaoResumida, Double precoVenda, Icon iconClass,
			Long idEstoque, Long quantidadeEstoque, Double precoCusto, Long codigo, UnidadeMedida unidadeMedida) {
		this(id, descricao, descricaoResumida, precoVenda, iconClass);
		this.estoque = new Estoque();
		this.estoque.setId(idEstoque);
		this.estoque.setQuantidadeEstoque(quantidadeEstoque);
		this.estoque.setPrecoCusto(precoCusto);
		this.codigo = codigo;
		this.unidadeMedida = unidadeMedida;
	}

	public Produto(Long id, String descricao, String descricaoResumida, Double precoVenda, Icon iconClass, Long codigo,
			UnidadeMedida unidadeMedida, Boolean ehInsumo, Long idCliente) {
		this(id, descricao, descricaoResumida, precoVenda, iconClass);
		this.ehInsumo = ehInsumo;
		this.codigo = codigo;
		this.unidadeMedida = unidadeMedida;
		this.cliente = new Cliente();
		this.cliente.setId(idCliente);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoResumida() {
		return descricaoResumida;
	}

	public void setDescricaoResumida(String descricaoResumida) {
		this.descricaoResumida = descricaoResumida;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public Boolean getEhInsumo() {
		return ehInsumo;
	}

	public void setEhInsumo(Boolean ehInsumo) {
		this.ehInsumo = ehInsumo;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public List<ItemReceita> getItensReceita() {
		return itensReceita;
	}

	public void setItensReceita(List<ItemReceita> itensReceita) {
		this.itensReceita = itensReceita;
	}

	public List<ProdutoGrupoProduto> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<ProdutoGrupoProduto> grupos) {
		this.grupos = grupos;
	}

	public List<Disponibilidade> getDiasDisponibilidade() {
		return diasDisponibilidade;
	}

	public void setDiasDisponibilidade(List<Disponibilidade> diasDisponibilidade) {
		this.diasDisponibilidade = diasDisponibilidade;
	}

	public Icon getIconClass() {
		return iconClass;
	}

	public void setIconClass(Icon iconClass) {
		this.iconClass = iconClass;
	}

	public Estoque getEstoque() {
		return estoque;
	}

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}

	public List<MovimentacaoProduto> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<MovimentacaoProduto> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Boolean getCodigoGerado() {
		return codigoGerado;
	}

	public void setCodigoGerado(Boolean codigoGerado) {
		this.codigoGerado = codigoGerado;
	}
	
	public TipoArmazenamento getTipoArmazenamento() {
		return tipoArmazenamento;
	}

	public void setTipoArmazenamento(TipoArmazenamento tipoArmazenamento) {
		this.tipoArmazenamento = tipoArmazenamento;
	}



	public interface ProdutoConstant {

		String FIELD_CODIGO = "codigo";

		String LIST_ALL_KEY = "Produto.listAll";
		String LIST_ALL_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Produto(p.id, p.descricao, p.descricaoResumida, p.precoVenda, p.iconClass, e.id, e.quantidadeEstoque, e.precoCusto, p.codigo, p.unidadeMedida) from Produto p left join p.estoque e where p.cliente = :cliente order by p.descricao";

		String EDIT_KEY = "Produto.editProduto";
		String EDIT_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Produto(p.id, p.descricao, p.descricaoResumida, p.precoVenda, p.iconClass, p.codigo, p.unidadeMedida, p.ehInsumo, c.id) from Produto p join p.cliente c where p.id = :id";

		String LIST_SEARCH_BY_GRUPO_PRODUTO_KEY = "Produto.listByGrupoProduto";
		String LIST_SEARCH_BY_GRUPO_PRODUTO_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Produto(p.id, p.descricao, p.descricaoResumida, p.precoVenda, p.iconClass) from Produto p where p.cliente = :cliente and p.id not in (select pgp.produto.id from ProdutoGrupoProduto pgp where pgp.grupoProduto = :grupoProduto) and upper(p.descricao) like upper(concat('%', :query, '%')) order by p.descricao";

		String LIST_SEARCH_BY_DESCRICAO_KEY = "Produto.listByDescricao";
		String LIST_SEARCH_BY_DESCRICAO_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Produto(p.id, p.descricao, p.descricaoResumida, p.precoVenda, p.iconClass) from Produto p where p.cliente = :cliente and p.id not in(:list) and upper(p.descricao) like upper(concat('%', :query, '%')) order by p.descricao";

		String PRODUTO_BY_ID_KEY = "Produto.loadProdutoByCliente";
		String PRODUTO_BY_ID_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Produto(p.id, p.descricao, p.descricaoResumida, p.precoVenda, p.iconClass) from Produto p where p.cliente = :cliente and p.id = :id";

		String PRODUTO_BY_CODIGO_KEY = "Produto.findByCodigo";
		String PRODUTO_BY_CODIGO_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Produto(p.id, p.descricao, p.descricaoResumida, p.precoVenda, p.iconClass) from Produto p where p.cliente = :cliente and p.codigo = :codigo";

		String ITEM_RECEITA_BY_PRODUTO_KEY = "Produto.itemReceitaByProduto";
		String ITEM_RECEITA_BY_PRODUTO_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.ItemReceita(ir.id, ir.quantidade, ing.id, ing.descricao, p.id, p.descricao) from ItemReceita ir left join ir.produto p left join ir.ingrediente ing where ir.produto = :produto and p.cliente = :cliente";

		String DISPONIBILIDADE_BY_PRODUTO_KEY = "Produto.disponibilidadeByProduto";
		String DISPONIBILIDADE_BY_PRODUTO_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Disponibilidade(d.id, d.dia, p.id, p.descricao) from Disponibilidade d join d.produto p where d.produto = :produto and p.cliente = :cliente";

	}

}
