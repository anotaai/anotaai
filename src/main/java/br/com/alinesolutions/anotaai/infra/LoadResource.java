package br.com.alinesolutions.anotaai.infra;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ejb.Singleton;
import javax.ejb.Startup;


@Singleton
@Startup
public class LoadResource {

	public String getFile(String fileName) {
		StringBuilder buffer = new StringBuilder();
		try (	
			InputStream is = getInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr)) {
			String s = br.readLine();
			
			while (s != null) {
				buffer.append(s);
				s = br.readLine();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return buffer.toString();
	}

	public InputStream getInputStream(String fileName) {
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream is = classLoader.getResourceAsStream(fileName);
		return is;
	}

}
