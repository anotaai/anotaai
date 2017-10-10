package br.com.alinesolutions.anotaai.model.venda;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.venda.Caderneta.CadernetaConstant;

@NamedQueries({
	@NamedQuery(name = CadernetaConstant.FIND_BY_DESCRICAO_LIKE_KEY, query = CadernetaConstant.FIND_BY_DESCRICAO_LIKE_QUERY),
	@NamedQuery(name = CadernetaConstant.FIND_BY_DESCRICAO_COUNT, query = CadernetaConstant.FIND_BY_DESCRICAO_QUERY_COUNT),	
	@NamedQuery(name = CadernetaConstant.LIST_ALL_KEY, query = CadernetaConstant.LIST_ALL_QUERY),
	@NamedQuery(name = CadernetaConstant.EDIT_KEY, query = CadernetaConstant.EDIT_QUERY),
	@NamedQuery(name = CadernetaConstant.CADERNETA_BY_CONFIGURACAO_KEY, query = CadernetaConstant.CADERNETA_BY_CONFIGURACAO_QUERY),
	@NamedQuery(name = CadernetaConstant.CADERNETA_BY_KEYS, query = CadernetaConstant.CADERNETA_BY_KEYS_QUERY),
	@NamedQuery(name = CadernetaConstant.LIST_ALL_COUNT, query = CadernetaConstant.LIST_ALL_QUERY_COUNT)
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Caderneta set ativo = false where id = ?")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Caderneta.class)
public class Caderneta extends BaseEntity<Long, Caderneta> {

	private static final long serialVersionUID = 1L;

	/**
	 * O Cliente pode anotar em locais diferentes, e para cada local ele pode ter uma caderneta diferente
	 * 
	 * Ex, caderneta da faculdade
	 *     caderneta do trabalho
	 */
	private String descricao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAbertura;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFechamento;
	
	@OneToMany(mappedBy="caderneta")
	private List<FolhaCaderneta> folhas;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	private Cliente cliente;
	
	@ManyToOne
	private ConfiguracaoCaderneta configuracao;
	
	public Caderneta() {
		super();
	}
	
	public Caderneta(Long id, Long idConfiguracao, String descricao, Integer qtdDiasDuracaoFolha, Integer diaBase, Integer timeoutSetupVenda) {
		setId(id);
		ConfiguracaoCaderneta c = new ConfiguracaoCaderneta();
		c.setId(idConfiguracao);
		c.setQtdDiasDuracaoFolha(qtdDiasDuracaoFolha);
		c.setDiaBase(diaBase);
		c.setTimeoutSetupVenda(timeoutSetupVenda);
		this.configuracao = c;
		this.descricao = descricao;
	 
	}
	
	public Caderneta(Long id, String descricao) {
		setId(id);
		this.descricao = descricao;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public List<FolhaCaderneta> getFolhas() {
		return folhas;
	}

	public void setFolhas(List<FolhaCaderneta> folhas) {
		this.folhas = folhas;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ConfiguracaoCaderneta getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(ConfiguracaoCaderneta configuracao) {
		this.configuracao = configuracao;
	}
	
	
	public interface CadernetaConstant {
		
		String CADERNETA = "Caderneta";
		String FIELD_DESCRICAO = "descricao";
		String FIELD_DIA_BASE = "diaBase";
		String FIELD_QTD_DIAS_DURACAO = "qtdDiasDuracaoFolha";
		String FIND_BY_DESCRICAO_LIKE_KEY = "Caderneta.findByNameLike"; 
		String FIND_BY_DESCRICAO_LIKE_QUERY = "select new br.com.alinesolutions.anotaai.model.venda.Caderneta(c.id, c.configuracao.id, c.descricao, cc.qtdDiasDuracaoFolha, cc.diaBase, cc.timeoutSetupVenda) from Caderneta c join c.configuracao cc where c.cliente = :cliente and upper(c.descricao) like :descricao order by c.descricao";
		String LIST_ALL_KEY = "Caderneta.listAll";
		String LIST_ALL_QUERY = "select new br.com.alinesolutions.anotaai.model.venda.Caderneta(c.id, cc.id, c.descricao, cc.qtdDiasDuracaoFolha, cc.diaBase, cc.timeoutSetupVenda) from Caderneta c join c.configuracao cc where c.cliente = :cliente order by c.descricao";
		String LIST_ALL_COUNT = "Caderneta.listAllCount";
		String FIND_BY_DESCRICAO_COUNT = "Caderneta.findByNameCount";
		String FIND_BY_DESCRICAO_QUERY_COUNT = "select count(c) from Caderneta c where c.cliente = :cliente and upper(c.descricao) like :descricao";
		String LIST_ALL_QUERY_COUNT = "select count(c) from Caderneta c where c.cliente = :cliente";
		String EDIT_KEY = "Caderneta.editCaderneta";
		String EDIT_QUERY = "select new br.com.alinesolutions.anotaai.model.venda.ConfiguracaoCaderneta(cc.id, cc.qtdDiasDuracaoFolha, cc.diaBase) from ConfiguracaoCaderneta cc  where cc.id = :id";
		String CADERNETA_BY_CONFIGURACAO_KEY = "Caderneta.cadernetaByConfiguracao";
		String CADERNETA_BY_CONFIGURACAO_QUERY = "select new br.com.alinesolutions.anotaai.model.venda.Caderneta(c.id, c.descricao) from Caderneta c  where c.configuracao = :configuracao";
		String CADERNETA_BY_KEYS = "Caderneta.cadernetaByKeys";
		String CADERNETA_BY_KEYS_QUERY = "select distinct new br.com.alinesolutions.anotaai.model.venda.ConfiguracaoCaderneta(cc.id) from ConfiguracaoCaderneta cc join cc.cadernetas c where c.cliente =:cliente and cc.diaBase =:diaBase and cc.qtdDiasDuracaoFolha =:qtdDiasDuracaoFolha and cc.id <> :id";
	}
	
}
