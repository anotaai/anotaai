package br.com.alinesolutions.anotaai.test.produto;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.alinesolutions.anotaai.service.app.ProdutoService;

@RunWith(Arquillian.class)
public class DigitoVerificador {

	
	@Deployment
	public static Archive<?> createDeployment() {
		return new ProdutoDeploymentBuilder().build();
	}
	
	@Inject
	private ProdutoService service;
	
	@Test
	public void testDigitoVerificador() {
		assertNotNull(service);
	}
	
	
}
