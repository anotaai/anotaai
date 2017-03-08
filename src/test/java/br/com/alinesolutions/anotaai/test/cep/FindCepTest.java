package br.com.alinesolutions.anotaai.test.cep;

import org.junit.Assert;
import org.junit.Test;

import br.com.alinesolutions.anotaai.metadata.model.Cep;
import br.com.alinesolutions.anotaai.service.AppService;

public class FindCepTest {

	@Test
	public void testCep() {
		AppService service = new AppService();
		Cep cep = service.findCep(30411580);
		Assert.assertNotNull(cep);
	}
	
}
