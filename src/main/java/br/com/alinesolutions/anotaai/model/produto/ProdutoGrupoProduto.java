package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@NamedQueries({ 
	@NamedQuery(name = ProdutoGrupoProduto.ProdutoGrupoProdutoConstant.ALL_BY_GRUPO_KEY, query = ProdutoGrupoProduto.ProdutoGrupoProdutoConstant.ALL_BY_GRUPO_QUERY) 
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ProdutoGrupoProduto set ativo = false where id = ?")
@XmlRootElement
public class ProdutoGrupoProduto extends BaseEntity<Long, ProdutoGrupoProduto> {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(optional = false)
	private Produto produto;

	@NotNull
	@ManyToOne(optional = false)
	private GrupoProduto grupoProduto;
	
	@Column(nullable = false)
	private Boolean ehPrincipal;
	
	public ProdutoGrupoProduto(Long id, Long idGrupoProduto , String nome , Boolean ehPrincipal ) {
		this.setId(id);
		this.grupoProduto = new GrupoProduto();
		this.grupoProduto.setId(idGrupoProduto);
		this.grupoProduto.setNome(nome);
		this.ehPrincipal = ehPrincipal;
	}

	public ProdutoGrupoProduto() {
		super();
	}

	public ProdutoGrupoProduto(Long id, Long idProduto, String descricao, Double precoVendaProduto) {
		this.setId(id);
		this.produto = new Produto();
		this.produto.setId(idProduto);
		this.produto.setDescricao(descricao);
		this.produto.setPrecoVenda(precoVendaProduto);
	}
	
	
	public Boolean getEhPrincipal() {
		return ehPrincipal;
	}
	
	public void setEhPrincipal(Boolean ehPrincipal) {
		this.ehPrincipal = ehPrincipal;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public GrupoProduto getGrupoProduto() {
		return grupoProduto;
	}

	public void setGrupoProduto(GrupoProduto grupoProduto) {
		this.grupoProduto = grupoProduto;
	}

	public interface ProdutoGrupoProdutoConstant {
		String FIELD_GRUPO_PRODUTO = "grupoProduto";
		String ALL_BY_GRUPO_KEY = "ProdutoGrupoProduto.findByGrupoProduto";
		String ALL_BY_GRUPO_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.ProdutoGrupoProduto(pgp.id, p.id, p.descricao, p.precoVenda) from ProdutoGrupoProduto pgp join pgp.produto p join pgp.grupoProduto g join g.setor s where pgp.grupoProduto = :grupoProduto and s.cliente = :cliente";
	}

}
