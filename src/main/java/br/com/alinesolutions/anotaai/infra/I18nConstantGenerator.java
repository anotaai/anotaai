package br.com.alinesolutions.anotaai.infra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		String i18nFile = loader.getFile(Constant.FileNane.I18N_PT);
		JsonObject o = new JsonParser().parse(i18nFile).getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = o.entrySet();
		for (Map.Entry<String, JsonElement> entry : entries) {
			build("", entry);
		}
		printConstants();
	}

	private void printConstants() {
		constants.stream().forEach(System.out::println);
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
			constants.add(new StringBuilder("\tString ").append(key.replace(".", "_").toUpperCase()).append(" = \"").append(key).append("\";").toString());
		}

	}

}
