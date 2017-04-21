package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.ItemBalanco;
import br.com.alinesolutions.anotaai.model.produto.ItemEntrada;


@Entity
@NamedQueries({
	
})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update CupomFiscal set ativo = false where id = ?")
@XmlRootElement
public class CupomFiscal extends BaseEntity<Long, CupomFiscal> {

	private static final long serialVersionUID = 1L;

	@Any(metaColumn = @Column(name = "tipo_venda", length = 16), fetch = FetchType.LAZY)
	@AnyMetaDef(idType = "long", metaType = "string", metaValues = {
		@MetaValue(targetEntity = ItemBalanco.class, value = "VENDA_A_VISTA"),
		@MetaValue(targetEntity = ItemEntrada.class, value = "VENDA_ANOTADA"),
		@MetaValue(targetEntity = ItemEntrada.class, value = "VENDA_CLIENTE_AVULSO")
	})
	@JoinColumn(name = "movimentacao_id")
	private IVenda venda;

	/**
	 * Gerado automaticamente pelo sistema a partir da regra 1 - Concatena o id
	 * do cliente 2 - Concatena uma sequencia de 7 digitos auto incremento
	 * comecando de 1 3 - Concatena um digito verificador a partir da regra
	 * 
	 * http://www.vestibulandoweb.com.br/enem/prova-enem-amarela-2005.pdf
	 * questao 55
	 * 
	 * multiplica-se o último algarismo do número por 1, o penúltimo por 2, o
	 * antepenúltimo por 1, e assim por diante, sempre alternando multiplicações
	 * por 1 e por 2.  Soma-se 1 a cada um dos resultados dessas multiplicações
	 * que for maior do que ou igual a 10. Somam-se os resultados obtidos . 
	 * Calcula-se o resto da divisão dessa soma por 10
	 * 
	 */
	private Long numeroCupom;

	public Long getNumeroCupom() {
		return numeroCupom;
	}

	public void setNumeroCupom(Long numeroCupom) {
		this.numeroCupom = numeroCupom;
	}

}
