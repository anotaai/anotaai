package br.com.alinesolutions.anotaai.test.path;

import org.junit.Assert;
import org.junit.Test;

import br.com.alinesolutions.anotaai.metadata.model.domain.Path;

public class TestEnumPath {

	@Test
	public void test() {
		String path = Path.PROFILE.getPath();
		System.out.println(path);
		Assert.assertEquals(path, "\\files\\usuario\\profile\\");
	}
	
}
