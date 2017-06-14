package br.com.alinesolutions.anotaai.metadata.model.domain;

public class SearchCriteria<T> {

	private String key;

	private String operation;

	private T value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}