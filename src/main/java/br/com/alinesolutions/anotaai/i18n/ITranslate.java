package br.com.alinesolutions.anotaai.i18n;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public interface ITranslate extends Serializable {

	String app = "Anota Ai";
}
