package br.com.alinesolutions.anotaai.security;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class JSONSerialize extends ResteasyJackson2Provider {

	@Override
	public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType json, MultivaluedMap<String, Object> headers,
			OutputStream body) throws IOException {

		super.writeTo(value, type, genericType, annotations, json, headers, body);
	}

	@Override
	public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {

		FilterProvider filters = new SimpleFilterProvider().setFailOnUnknownId(false);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.writer(filters);
		return Optional.ofNullable(_mapperConfig.getConfiguredMapper()).orElse(mapper);
	}

}
