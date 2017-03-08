package br.com.alinesolutions.anotaai.test.criptografia;

import org.junit.Assert;
import org.junit.Test;

import br.com.alinesolutions.anotaai.util.Criptografia;


public class CriptografiaTest {

	@Test
	public void testCriptografia() {
//		teste_criptografia
		String actual = "27352B8FFCA83E660FB974DD5CA3F61A7E4E1A0CAC1BB57CF244D371CAEDBCC2";
		String senhaCriptografada = Criptografia.criptografar("teste_criptografia");
		Assert.assertEquals(senhaCriptografada, actual);	
	}
	
}
