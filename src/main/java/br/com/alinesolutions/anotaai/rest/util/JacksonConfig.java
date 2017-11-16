package br.com.alinesolutions.anotaai.rest.util;

import java.util.logging.Logger;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {
	
	private static final Logger log = Logger.getLogger(JacksonConfig.class.getName());

	@Override
	public ObjectMapper getContext(final Class<?> type) {
		
		final ObjectMapper mapper = new ObjectMapper();
		FilterProvider filters = new SimpleFilterProvider().setFailOnUnknownId(Boolean.FALSE);
		log.info("Configured Jackson Json module");
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		mapper.setFilterProvider(filters);
		return mapper;
	}
}