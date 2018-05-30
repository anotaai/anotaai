package br.com.alinesolutions.anotaai.model.usuario;

import java.time.ZonedDateTime;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSequencial;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPessoa;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.SituacaoPessoa;
import br.com.alinesolutions.anotaai.model.usuario.Vendedor.VendedorConstant;

@NamedQueries({ 
	@NamedQuery(name = VendedorConstant.FIND_BY_USUARIO_KEY, query = VendedorConstant.FIND_BY_USUARIO_QUERY) 
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Vendedor set ativo = false where id = ?")
public class Vendedor extends BaseEntity<Long, Vendedor> implements IPessoa {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Usuario usuario;

	private Long cpf;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Endereco endereco;

	@NotNull
	private ZonedDateTime dataCadastro;

	@Enumerated(EnumType.ORDINAL)
	private SituacaoPessoa situacaoVendedor;

	@OneToMany(mappedBy = "cliente", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	private List<AnotaaiSequencial> sequences;

	public Vendedor() {
		super();
	}

	@Override
	@Transient
	public TipoPessoa getTipoPessoa() {
		return TipoPessoa.VENDEDOR;
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

	public List<AnotaaiSequencial> getSequences() {
		return sequences;
	}

	public void setSequences(List<AnotaaiSequencial> sequences) {
		this.sequences = sequences;
	}

	public ZonedDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(ZonedDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public SituacaoPessoa getSituacaoVendedor() {
		return situacaoVendedor;
	}

	public void setSituacaoVendedor(SituacaoPessoa situacaoVendedor) {
		this.situacaoVendedor = situacaoVendedor;
	}

	public interface VendedorConstant {
		String FIND_BY_USUARIO_KEY = "Vendedor.findByPessoa";
		String FIND_BY_USUARIO_QUERY = "select v from Vendedor v where v.usuario =:usuario";
	}
}
