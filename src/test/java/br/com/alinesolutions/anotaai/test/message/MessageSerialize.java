package br.com.alinesolutions.anotaai.test.message;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alinesolutions.anotaai.i18n.pt.TranslatePT;

public class MessageSerialize {

	@Test
	public void loadHtmlFileTest() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String dtoAsString = objectMapper.writeValueAsString(TranslatePT.getInstance());
		Assert.assertNotNull(dtoAsString);
	}

}
