package br.com.alinesolutions.anotaai.util;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSession;

public class RequestUtils {

	private static final ThreadLocal<AnotaaiSession> request = new ThreadLocal<AnotaaiSession>();
	
	public static void putRequest (AnotaaiSession session){
		request.set(session);
	}
	
	public static AnotaaiSession getRequest() {
		return request.get();
	}
	
}
