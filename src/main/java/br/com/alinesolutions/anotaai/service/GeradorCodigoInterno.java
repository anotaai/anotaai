package br.com.alinesolutions.anotaai.service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.ShardingResourceFactory;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSequencial;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSequencial.AnotaaiSequenceConstant;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoCodigoInterno;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@Lock(LockType.READ)
@AccessTimeout(value = 60, unit = TimeUnit.SECONDS)
@Singleton
@Startup
public class GeradorCodigoInterno {
	
	@Inject
	private ShardingResourceFactory appManager;
	
	public Long gerarCodigoProduto(Cliente cliente) {
		return gerarCodigo(cliente, TipoCodigoInterno.PRODUTO);
	}
	
	public Long gerarNumeroCupom(Cliente cliente) {
		return gerarCodigo(cliente, TipoCodigoInterno.CUPOM);
	}
	
	public Long gerarCodigoEntradaMercadoria(Cliente cliente) {
		return gerarCodigo(cliente, TipoCodigoInterno.ENTRADA_MERCADORIA);
	}
	
	private Long gerarCodigo(Cliente cliente, TipoCodigoInterno tipoCodigoInterno) {
		TypedQuery<AnotaaiSequencial> query = appManager.getEntityManager().createNamedQuery(AnotaaiSequenceConstant.GET_SEQUENCE_KEY, AnotaaiSequencial.class);
		query.setParameter(Constant.Entity.CLIENTE, cliente);
		query.setParameter(AnotaaiSequencial.AnotaaiSequenceConstant.FIELD_TIPO_CODIGO_INTERNO, tipoCodigoInterno);
		AnotaaiSequencial sequencial = query.getSingleResult();
		Long codigo = tipoCodigoInterno.gerarCodigo(cliente.getId(), sequencial.getSequence());
		Long codigoDV = inserirDigitoVerificador(codigo);
		sequencial.setSequence(sequencial.getSequence() + 1);
		appManager.getEntityManager().merge(sequencial);
		return codigoDV;
	}
	
	private Long inserirDigitoVerificador(Long codigo) {
		if (codigo == null) {
			throw new IllegalArgumentException();
		}
		String codigoStr = codigo.toString();
		String codigoReverseStr = new StringBuilder(codigoStr).reverse().toString();
		char[] digitos = codigoReverseStr.toCharArray();
		Integer[] numeros = new Integer[digitos.length];
		Integer n = null;
		Integer index = 1;
		for (int i = 0; i < digitos.length; i++) {
			n = Integer.valueOf(digitos[i]) * index;
			numeros[i] = n >= 10 ? n + 1 : n;
			index = index == 1 ? 2 : 1;
		}
		Integer somaTotal = Arrays.asList(numeros).stream().mapToInt(Integer::intValue).sum();
		Integer modulo = somaTotal % 10;
		StringBuilder codigoComDigito = new StringBuilder(codigoStr).append(modulo);
		return Long.valueOf(codigoComDigito.toString());
	}

	public Boolean validarDigitoVerificador(Long codigoComDV) {
		Boolean valido = Boolean.FALSE;
		if (codigoComDV == null) {
			throw new IllegalArgumentException();
		}
		if (codigoComDV == 0) {
			valido = Boolean.TRUE;
		} else {
			if (codigoComDV < 10) {
				valido = Boolean.FALSE;
			} else {
				String codigoComDVStr = codigoComDV.toString();
				Long codigoSemDV = Long.valueOf(codigoComDVStr.substring(0, codigoComDVStr.length() - 1));
				Long codigoComDVConferido = inserirDigitoVerificador(codigoSemDV);
				valido = codigoComDV.equals(codigoComDVConferido);
			}
		}
		return valido;
	}

}
