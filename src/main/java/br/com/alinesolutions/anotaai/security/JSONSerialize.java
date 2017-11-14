package br.com.alinesolutions.anotaai.security;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class JSONSerialize extends ResteasyJackson2Provider {

	@Override
	public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType json, MultivaluedMap<String, Object> headers,
			OutputStream body) throws IOException {

		ObjectMapper mapper = locateMapper(type, json);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		super.writeTo(value, type, genericType, annotations, json, headers, body);
	}

}
