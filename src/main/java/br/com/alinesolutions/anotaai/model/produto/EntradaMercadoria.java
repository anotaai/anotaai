package br.com.alinesolutions.anotaai.model.produto;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria.EntradaMercadoriaConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@NamedQueries({ @NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_NOME_KEY, query = EntradaMercadoriaConstant.FIND_BY_NOME_QUERY),
		@NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_DATE_KEY, query = EntradaMercadoriaConstant.FIND_BY_DATE_QUERY),
		@NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_DATE_COUNT, query = EntradaMercadoriaConstant.FIND_BY_DATE_QUERY_COUNT),
		@NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_NOME_AND_DATE_KEY, query = EntradaMercadoriaConstant.FIND_BY_NOME_AND_DATE_QUERY),
		@NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_NOME_AND_DATE_COUNT, query = EntradaMercadoriaConstant.FIND_BY_NOME_AND_DATE_QUERY_COUNT),
		@NamedQuery(name = EntradaMercadoriaConstant.LIST_ALL_KEY, query = EntradaMercadoriaConstant.LIST_ALL_QUERY),
		@NamedQuery(name = EntradaMercadoriaConstant.ITEM_ENTRADA_BY_ENTRADA_KEY, query = EntradaMercadoriaConstant.ITEM_ENTRADA_BY_ENTRADA_QUERY),
		@NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_NOME_COUNT, query = EntradaMercadoriaConstant.FIND_BY_NOME_QUERY_COUNT),
		@NamedQuery(name = EntradaMercadoriaConstant.LIST_ALL_COUNT, query = EntradaMercadoriaConstant.LIST_ALL_QUERY_COUNT),
		@NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_ENTRADA_MERCADORIA, query = EntradaMercadoriaConstant.FIND_BY_ENTRADA_MERCADORIA_QUERY),
		@NamedQuery(name = EntradaMercadoriaConstant.FIND_BY_ID_KEY, query = EntradaMercadoriaConstant.FIND_BY_ID_QUERY)
})
@DiscriminatorValue("ENTRADA")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = EntradaMercadoria.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update EntradaMercadoria set ativo = false where id = ?")
@XmlRootElement
public class EntradaMercadoria extends BaseEntity<Long, EntradaMercadoria> {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Long codigo;

	@OneToMany(mappedBy = "entradaMercadoria", cascade = { CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST })
	private List<ItemEntrada> itens;

	private ZonedDateTime dataEntrada;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	private Cliente cliente;

	public List<ItemEntrada> getItens() {
		return itens;
	}

	public void setItens(List<ItemEntrada> itens) {
		this.itens = itens;
	}

	public ZonedDateTime getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(ZonedDateTime dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public EntradaMercadoria(Long id) {
		setId(id);
	}

	public EntradaMercadoria() {

	}

	public EntradaMercadoria(Long id, ZonedDateTime dataEntrada, Long codigo) {
		setId(id);
		setDataEntrada(dataEntrada);
		setCodigo(codigo);
	}
	
	public EntradaMercadoria(Long id, ZonedDateTime dataEntrada) {
		setId(id);
		setDataEntrada(dataEntrada);
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public interface EntradaMercadoriaConstant {
		
		String ITEM_MERCADORIA = "Item(s) de mercadoria";
		String ENTRADA_MERCADORIA = "Entrada de mercadoria";
		String FIELD_NOME = "nome";
		String FIND_BY_NOME_KEY = "EntradaMercadoria.findByName";
		String FIND_BY_DATE_KEY = "EntradaMercadoria.findByDate";
		String FIND_BY_NOME_LIKE_KEY = "EntradaMercadoria.findByNameLike";
		String FIND_BY_NOME_QUERY = "select distinct new br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria(e.id,e.dataEntrada,e.codigo) from EntradaMercadoria e join e.itens itens join itens.movimentacaoProduto mov join mov.produto prod where prod.descricao =:descricao order by e.dataEntrada";
		String FIND_BY_DATE_QUERY = "select distinct new br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria(e.id,e.dataEntrada,e.codigo) from EntradaMercadoria e where trunc(e.dataEntrada) = trunc(:dataEntrada) order by e.dataEntrada";
		String FIND_BY_ID_KEY = "EntradaMercadoria.findById";
		String FIND_BY_ID_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria(e.id,e.dataEntrada,e.codigo) from EntradaMercadoria e where e.id = :id";

		String ITEM_ENTRADA_BY_ENTRADA_KEY = "EntradaMercadoria.itemEntradaByEntrada";
		String ITEM_ENTRADA_BY_ENTRADA_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.ItemEntrada(ie.id, ie.precoCusto, mov.id, mov.quantidade, p.id, p.descricao) from ItemEntrada ie left join ie.movimentacaoProduto mov left join mov.produto p where ie.entradaMercadoria = :entradaMercadoria";

		String FIND_BY_ENTRADA_MERCADORIA = "ItemEntrada.findByName";
		String FIND_BY_ENTRADA_MERCADORIA_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.ItemEntrada(i.precoCusto) from ItemEntrada i where i.entradaMercadoria.id =:idEntradaMercadoria";

		String LIST_ALL_KEY = "EntradaMercadoria.listAll";
		String LIST_ALL_QUERY = "select new br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria(e.id,e.dataEntrada,e.codigo) from EntradaMercadoria e order by e.dataEntrada";
		String LIST_ALL_COUNT = "EntradaMercadoria.listAllCount";
		String FIND_BY_NOME_COUNT = "EntradaMercadoria.findByNameCount";
		String FIND_BY_DATE_COUNT = "EntradaMercadoria.findByDateCount";
		String FIND_BY_NOME_AND_DATE_KEY = "EntradaMercadoria.findByDateAndName";
		String FIND_BY_NOME_AND_DATE_COUNT = "EntradaMercadoria.findByDateAndNameCount";
		String FIND_BY_DATE_QUERY_COUNT = "select distinct count(e) from EntradaMercadoria e where trunc(e.dataEntrada) = trunc(:dataEntrada)";
		String FIND_BY_NOME_QUERY_COUNT = "select distinct count(e) from EntradaMercadoria e join e.itens itens join itens.movimentacaoProduto mov join mov.produto prod where prod.descricao =:descricao";
		String LIST_ALL_QUERY_COUNT = "select count(e) from EntradaMercadoria e";
		String FIND_BY_NOME_AND_DATE_QUERY_COUNT = "select distinct count(e) from EntradaMercadoria e join e.itens itens join itens.movimentacaoProduto mov join mov.produto prod where prod.descricao =:descricao and trunc(e.dataEntrada) = trunc(:dataEntrada)";
		String FIND_BY_NOME_AND_DATE_QUERY = "select distinct new br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria(e.id,e.dataEntrada,e.codigo) from EntradaMercadoria e join e.itens itens join itens.movimentacaoProduto mov join mov.produto prod where prod.descricao =:descricao and trunc(e.dataEntrada) = trunc(:dataEntrada) order by e.dataEntrada";
	}

}
