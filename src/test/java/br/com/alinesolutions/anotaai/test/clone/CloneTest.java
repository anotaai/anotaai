package br.com.alinesolutions.anotaai.test.clone;

import org.junit.Assert;
import org.junit.Test;

import br.com.alinesolutions.anotaai.model.usuario.Telefone;

public class CloneTest {

	@Test
	public void testClone() {
		Telefone t = new Telefone();
		t.setId(1L);
		t.setDdi(55);
		t.setDdd(31);
		t.setNumero(987749131);
		
		Telefone clone = t.clone();
		Assert.assertEquals(clone, t);
	}
	
}
