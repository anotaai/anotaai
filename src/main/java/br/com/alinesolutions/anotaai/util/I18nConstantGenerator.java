package br.com.alinesolutions.anotaai.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class I18nConstantGenerator {

	private LoadResource loader;
	private List<String> constants;

	public static void main(String[] args) throws Exception {
		new I18nConstantGenerator().build();
	}
	
	public I18nConstantGenerator() throws Exception {
		loader = new LoadResource();
		constants = new ArrayList<>();
	}

	private void build() throws IOException {
		String i18nFile = loader.getFile(Constant.FileNane.I18N_EN);
		JsonObject o = new JsonParser().parse(i18nFile).getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = o.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			build("", entry);
		}
		printConstants();
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
