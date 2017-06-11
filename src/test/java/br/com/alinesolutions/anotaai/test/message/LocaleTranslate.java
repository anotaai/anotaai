package br.com.alinesolutions.anotaai.test.message;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.alinesolutions.anotaai.i18n.pt.TranslatePT;

public class LocaleTranslate {

	private Gson gson;

	@Before
	public void bootContainer() throws Exception {
		gson = new GsonBuilder().create();
	}

	@Test
	public void loadHtmlFileTest() throws IOException {
		final TranslatePT translate = TranslatePT.getInstance();
		Assert.assertNotNull(translate);
		final String json = gson.toJson(Locale.EN);
		Assert.assertEquals("{\"app\":\"Anota Ai\",\"message\":{\"defultError\":\"Erro inesperado...\"}}", json);
	}

	enum Locale {

		PT("pt", "Português", "pt-BR"),
		EN("en", "English", "en-US");

		private Locale(String key, String label, String code) {
			this.key = key;
			this.label = label;
			this.code = code;
		}

		private String key;
		private String label;
		private String code;

		public String getKey() {
			return key;
		}

		public String getLabel() {
			return label;
		}

		public String getCode() {
			return code;
		}
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

	}

}
