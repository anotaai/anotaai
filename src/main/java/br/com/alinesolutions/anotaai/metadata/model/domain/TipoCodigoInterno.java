package br.com.alinesolutions.anotaai.metadata.model.domain;

public enum TipoCodigoInterno {

	PRODUTO() {
		@Override
		public Long gerarCodigo(Long codigo, Long sequencial) {
			StringBuilder codigoStr = new StringBuilder(codigo.toString());
			codigoStr.append(leftPad(sequencial.toString(), 9));
			return Long.parseLong(codigoStr.toString());
		}
	},
	
	CUPOM() {
		@Override
		public Long gerarCodigo(Long codigo, Long sequencial) {
			StringBuilder codigoStr = new StringBuilder(codigo.toString());
			codigoStr.append(leftPad(sequencial.toString(), 11));
			return Long.parseLong(codigoStr.toString());
		}
	},
	
	ENTRADA_MERCADORIA() {

		@Override
		public Long gerarCodigo(Long codigo, Long sequencial) {
			StringBuilder codigoStr = new StringBuilder(codigo.toString());
			codigoStr.append(leftPad(sequencial.toString(), 11));
			return Long.parseLong(codigoStr.toString());
		}
		
	};

	public abstract Long gerarCodigo(Long codigo, Long sequencial);

	private static String leftPad(String sequence, Integer quantidade) {
		final StringBuilder codigo = new StringBuilder();
		for (int i = sequence.length(); i < quantidade; i++) {
			codigo.append("0");
		}
		return codigo.append(sequence).toString();
	}
}
