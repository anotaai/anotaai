package br.com.alinesolutions.anotaai.test.google.urlshortener;

import org.junit.Assert;
import org.junit.Test;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.urlshortener.Urlshortener;
import com.google.api.services.urlshortener.UrlshortenerRequestInitializer;
import com.google.api.services.urlshortener.model.Url;

public class UrlShortenerTest {

	private static final String APPLICATION_NAME = "Anota ai";

	private static final String API_KEY = "AIzaSyA6Szqu_fjU6PtSoTcqctvhhDmceTEuVbo";

	@Test
	public void shortener() {
		UrlShortenerTest sample = new UrlShortenerTest();
		String longUrl = "http://anotaai-alinesolutions.rhcloud.com/main.html#/access";

		try {
			UrlshortenerRequestInitializer initializer = sample.authorize();
			Urlshortener service = sample.getService(initializer);
			Url shortUrl = sample.getShortUrl(service, longUrl);
			Assert.assertNotNull(shortUrl.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Url getShortUrl(Urlshortener service, String longUrl) throws Exception {
		System.out.println("getShortUrl in");

		Url url = new Url().setLongUrl(longUrl);
		Url result = service.url().insert(url).execute();

		return result;
	}

	private UrlshortenerRequestInitializer authorize() throws Exception {
		System.out.println("authorize in");

		return new UrlshortenerRequestInitializer(API_KEY);
	}

	private Urlshortener getService(UrlshortenerRequestInitializer initializer) throws Exception {
		System.out.println("service in");

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		Urlshortener service = new Urlshortener.Builder(httpTransport, jsonFactory, null)
				.setUrlshortenerRequestInitializer(initializer).setApplicationName(APPLICATION_NAME).build();

		return service;
	}

}