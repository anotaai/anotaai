package br.com.alinesolutions.anotaai.test.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.LoadResource;

public class CastJsonToConstant {

	private LoadResource loader;
	private List<String> constants;

	@Before
	public void bootContainer() throws Exception {
		loader = new LoadResource();
		constants = new ArrayList<>();
	}

	@Test
	public void loadHtmlFileTest() throws IOException {
		String i18nFile = loader.getFile(Constant.FileNane.I18N_PT);
		JsonObject o = new JsonParser().parse(i18nFile).getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = o.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			build("", entry);
		}
		printConstants();
		Assert.assertNotNull(i18nFile);
	}

	private void printConstants() {
		constants.stream().forEach(constant -> {
			System.out.println(constant);
		});
	}

	private void build(String root, Map.Entry<String, JsonElement> entry) {
		root += "." + entry.getKey();
		if (!(entry.getValue() instanceof JsonPrimitive)) {
			for (Map.Entry<String, JsonElement> entryField : entry.getValue().getAsJsonObject().entrySet()) {
				build(root, entryField);
			}
		}
		if (entry.getValue() instanceof JsonPrimitive) {
			String key = root.substring(1);
			constants.add("\tString " + key.replace(".", "_").toUpperCase() + " = \"" + key + "\";");
		}

	}

}
