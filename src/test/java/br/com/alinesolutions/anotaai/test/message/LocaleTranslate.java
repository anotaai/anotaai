package br.com.alinesolutions.anotaai.test.message;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alinesolutions.anotaai.i18n.Locale;

public class LocaleTranslate {

	private String actual;
	
	@Before
	public void bootContainer() throws Exception {
		actual = "[{\"key\":\"pt\",\"label\":\"Portugues\",\"code\":\"pt-BR\"},{\"key\":\"en\",\"label\":\"English\",\"code\":\"en-US\"}]";
	}

	
	@Test
	public void loadHtmlFileTest() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String locale = objectMapper.writeValueAsString(Locale.values());
		Assert.assertEquals(locale, actual);
	}

}
