package br.com.alinesolutions.anotaai.model.produto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Setor.SetorConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@NamedQueries({
	@NamedQuery(name = SetorConstant.FIND_BY_NOME_KEY, query = SetorConstant.FIND_BY_NOME_QUERY),
	@NamedQuery(name = SetorConstant.FIND_BY_ID_KEY, query = SetorConstant.FIND_BY_ID_QUERY),
	@NamedQuery(name = SetorConstant.FIND_GRUPO_PRODUTO_KEY, query = SetorConstant.FIND_GRUPO_PRODUTO_QUERY),
	@NamedQuery(name = SetorConstant.LIST_ALL_KEY, query = SetorConstant.LIST_ALL_QUERY),
	@NamedQuery(name = SetorConstant.LIST_ALL_COUNT, query = SetorConstant.LIST_ALL_QUERY_COUNT)
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Setor set ativo = false where id = ?")
@XmlRootElement
public class Setor extends BaseEntity<Long, Setor> {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String nome;

	private String descricao;

	@OneToMany(mappedBy = "setor")
	private List<GrupoProduto> gruposProduto;

	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	public Setor() {
		super();
	}
	
	public Setor(Long id, String nome, String descricao) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		super.setId(id);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<GrupoProduto> getGruposProduto() {
		return gruposProduto;
	}

	public void setGruposProduto(List<GrupoProduto> gruposProduto) {
		this.gruposProduto = gruposProduto;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public interface SetorConstant {
		String FIELD_NOME = "nome";
		String FIND_BY_NOME_KEY = "Setor.findByName";
		String FIND_BY_NOME_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Setor(s.id, s.nome, s.descricao) from Setor s where s.cliente = :cliente and s.nome = :nome";
		String FIND_BY_ID_KEY = "Setor.findById";
		String FIND_BY_ID_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Setor(s.id, s.nome, s.descricao) from Setor s where s.cliente = :cliente and s.id = :id";
		String LIST_ALL_KEY = "Setor.listAll";
		String LIST_ALL_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.Setor(s.id, s.nome, s.descricao) from Setor s where s.cliente = :cliente order by s.nome";
		String LIST_ALL_COUNT = "Setor.listAllCount";
		String LIST_ALL_QUERY_COUNT = "select count(s) from Setor s where s.cliente = :cliente order by s.nome";
		String FIND_GRUPO_PRODUTO_KEY = "Setor.listGrupoProduto";
		String FIND_GRUPO_PRODUTO_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.GrupoProduto(g.id, g.nome, g.descricao) from GrupoProduto g join g.setor s where s.cliente = :cliente and g.setor = :setor order by g.nome";
	}

}
