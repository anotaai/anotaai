package br.com.alinesolutions.anotaai.test.message;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageSerialize {

	private Gson gson;

	@Before
	public void bootContainer() throws Exception {
		gson = new GsonBuilder().create();
	}

	@Test
	public void loadHtmlFileTest() throws IOException {
		final Translate translate = new Translate();
		Assert.assertNotNull(translate.message.defultError);
		final String json = gson.toJson(translate);
		Assert.assertEquals("{\"app\":\"Anota Ai\",\"message\":{\"defultError\":\"Anota Ai\"}}", json);
	}
	
	final static class Translate {
		public String app = "Anota Ai";
		public Message message = new Message();
	}
	
	final static class Message {
		String defultError = "Anota Ai";
	}
	
}
