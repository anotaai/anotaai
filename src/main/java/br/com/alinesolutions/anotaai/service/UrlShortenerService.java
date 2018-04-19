package br.com.alinesolutions.anotaai.service;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.google.gson.JsonObject;

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
			JsonObject requestObj = new JsonObject();
			requestObj.addProperty("longDynamicLink", longUrl);
			StringEntity params = new StringEntity(requestObj.toString());
			params.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

			StringBuilder uri = new StringBuilder("https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=").append(key);
			HttpPost httpPost = new HttpPost(uri.toString());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
			httpPost.setEntity(params);
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