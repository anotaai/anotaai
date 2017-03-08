package br.com.alinesolutions.anotaai.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.urlshortener.Urlshortener;
import com.google.api.services.urlshortener.UrlshortenerRequestInitializer;
import com.google.api.services.urlshortener.model.Url;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.alinesolutions.anotaai.metadata.model.Shortener;
import br.com.alinesolutions.anotaai.util.Constant;
import br.com.alinesolutions.anotaai.util.LoadResource;

@Singleton
@Startup
public class UrlShortenerService {

	private Shortener shortener;

	@Inject
	private LoadResource fileUtil;

	@PostConstruct
	private void init() {
		
		InputStream content = fileUtil.getInputStream(Constant.FileNane.SHORTENER);

		try {
			Reader reader = new InputStreamReader(content);
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			shortener = gson.fromJson(reader, Shortener.class);
			content.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String shortener(String longUrl) {
		try {
			UrlshortenerRequestInitializer initializer = authorize();
			Urlshortener service = getService(initializer);
			Url shortUrl = getShortUrl(service, longUrl);
			return shortUrl.getId();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Url getShortUrl(Urlshortener service, String longUrl) throws Exception {
		Url url = new Url().setLongUrl(longUrl);
		Url result = service.url().insert(url).execute();
		return result;
	}

	private UrlshortenerRequestInitializer authorize() throws Exception {
		return new UrlshortenerRequestInitializer(shortener.getKey());
	}

	private Urlshortener getService(UrlshortenerRequestInitializer initializer) throws Exception {
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		Urlshortener service = new Urlshortener.Builder(httpTransport, jsonFactory, null)
				.setUrlshortenerRequestInitializer(initializer).setApplicationName(shortener.getApplication()).build();

		return service;
	}

}