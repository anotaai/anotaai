package br.com.alinesolutions.anotaai.test.file;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.LoadResource;

public class LoadFileTest {

	private LoadResource loader;

	@Before
	public void bootContainer() throws Exception {
		loader = new LoadResource();
	}

	@Test
	public void loadHtmlFileTest() throws IOException {
		String html = loader.getFile(Constant.FileNane.CONFIRMACAO_CADASTRO_CONSUMIDOR_EMAIL);
		Assert.assertNotNull(html);
	}

}
