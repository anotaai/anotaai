package br.com.alinesolutions.anotaai.model.produto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto.GrupoProdutoConstant;

@NamedQueries({
		@NamedQuery(name = GrupoProdutoConstant.FIND_BY_NOME_COUNT, query = GrupoProdutoConstant.FIND_BY_NOME_QUERY_COUNT),
		@NamedQuery(name = GrupoProdutoConstant.FIND_BY_NOME_KEY, query = GrupoProdutoConstant.FIND_BY_NOME_QUERY),
		@NamedQuery(name = GrupoProdutoConstant.LIST_ALL_KEY, query = GrupoProdutoConstant.LIST_ALL_QUERY),
		@NamedQuery(name = GrupoProdutoConstant.LIST_ALL_COUNT, query = GrupoProdutoConstant.LIST_ALL_QUERY_COUNT),
		@NamedQuery(name = GrupoProdutoConstant.ALL_BY_SETOR_KEY, query = GrupoProdutoConstant.ALL_BY_SETOR_QUERY),
		@NamedQuery(name = GrupoProdutoConstant.FIND_BY_ID_KEY, query = GrupoProdutoConstant.FIND_BY_ID_QUERY) })
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update GrupoProduto set ativo = false where id = ?")
public class GrupoProduto extends BaseEntity<Long, GrupoProduto> {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String nome;

	private String descricao;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private Setor setor;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private GrupoProduto grupoPai;

	@OneToMany(mappedBy = "grupoPai")
	private List<GrupoProduto> subGrupos;

	@OneToMany(mappedBy = "grupoProduto", cascade = { CascadeType.REMOVE, CascadeType.MERGE })
	private List<ProdutoGrupoProduto> produtos;

	public GrupoProduto() {
		super();
	}

	public GrupoProduto(Long id, String nome, String descricao) {
		super();
		setId(id);
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public GrupoProduto(Long id, String nome, String descricao, Long idSetor , String nomeSetor , String descricaoSetor) {
		super();
		setId(id);
		this.nome = nome;
		this.descricao = descricao;
		this.setor = new Setor(idSetor,nomeSetor,descricaoSetor);
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<ProdutoGrupoProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoGrupoProduto> produtos) {
		this.produtos = produtos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public GrupoProduto getGrupoPai() {
		return grupoPai;
	}

	public void setGrupoPai(GrupoProduto grupoPai) {
		this.grupoPai = grupoPai;
	}

	public List<GrupoProduto> getSubGrupos() {
		return subGrupos;
	}

	public void setSubGrupos(List<GrupoProduto> subGrupos) {
		this.subGrupos = subGrupos;
	}

	public interface GrupoProdutoConstant {
		String FIELD_NOME = "nome";
		String FIELD_NOME_SETOR = "nomeSetor";
		String FIELD_SESSION_ID = "sessionID";
		String FIELD_ULTIMO_ACESSO = "ultimoAcesso";
		String FIELD_PRODUTOS = "produtos";
		String LIST_ALL_COUNT = "GrupoProduto.listAllCount";
		String LIST_ALL_KEY = "GrupoProduto.listAll";
		String LIST_ALL_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.GrupoProduto(gp.id, gp.nome, gp.descricao) from GrupoProduto gp join gp.setor s where s.cliente = :cliente order by gp.nome";
		String LIST_ALL_QUERY_COUNT = "select count(gp) from GrupoProduto gp join gp.setor s where s.cliente = :cliente";
		String FIND_BY_NOME_COUNT = "GrupoProduto.findByNameCount";
		String FIND_BY_NOME_QUERY_COUNT = "select count(s) from GrupoProduto gp join gp.setor s where s.cliente = :cliente and gp.nome = :nome";
		String FIND_BY_NOME_KEY = "GrupoProduto.findByName";
		String FIND_BY_NOME_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.GrupoProduto(gp.id, gp.nome, gp.descricao) from GrupoProduto gp join gp.setor s where s.cliente = :cliente and gp.nome = :nome and s.nome = :nomeSetor";
		String FIND_BY_ID_KEY = "GrupoProduto.findByID";
		String FIND_BY_ID_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.GrupoProduto(gp.id, gp.nome, gp.descricao, gp.setor.id,gp.setor.nome,gp.setor.descricao) from GrupoProduto gp join gp.setor s where gp.id = :id and s.cliente = :cliente";
		String ALL_BY_SETOR_KEY = "GrupoProduto.findBySetor";
		String ALL_BY_SETOR_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.GrupoProduto(gp.id, gp.nome, gp.descricao) from GrupoProduto gp join gp.setor s where s.id = :id and s.nome like :nome and s.cliente = :cliente";
	}

}
