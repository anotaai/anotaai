package br.com.alinesolutions.anotaai.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

@Singleton
@Startup
public class UrlShortenerService {

	// private Shortener shortener;

	@EJB
	private AppService service;

	@PostConstruct
	private void init() {
		String url = shortener("https://hc.apache.org/httpcomponents-client-ga/quickstart.html");
		System.out.println(url);
	}

	public String shortener(String longUrl) {
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String key = service.getGooleServiceAccountKey();
			StringBuilder uri = new StringBuilder("https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=").append(key);
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("longDynamicLink", "https://firebase.google.com/docs/dynamic-links/rest?authuser=0"));
			HttpPost httpPost = new HttpPost(uri.toString());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(httpPost);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				return entity.toString();
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeHttpResponse(response);
		}
	}

	private void closeHttpResponse(CloseableHttpResponse response) {
		if (response != null) {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}