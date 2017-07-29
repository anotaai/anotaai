package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ConfiguracaoCaderneta set ativo = false where id = ?")
public class ConfiguracaoCaderneta extends BaseEntity<Long, ConfiguracaoCaderneta> {
	
	private static final long serialVersionUID = 1L;
	
	
	private Integer qtdDiasDuracaoFolha;
	
	

}
