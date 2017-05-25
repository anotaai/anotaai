package br.com.alinesolutions.anotaai.test.clone;

import org.junit.Assert;
import org.junit.Test;

import br.com.alinesolutions.anotaai.model.usuario.Telefone;

public class CloneEntity {

	@Test
	public void testClone() {
		Telefone t = new Telefone(55, 31, 987749131);
		t.setId(1L);
		Telefone clone = t.clone();
		Assert.assertEquals(clone, t);
		
	}
	
}
