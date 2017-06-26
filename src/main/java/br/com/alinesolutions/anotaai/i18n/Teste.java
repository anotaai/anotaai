package br.com.alinesolutions.anotaai.i18n;

import java.util.ArrayList;
import java.util.List;

import br.com.alinesolutions.anotaai.i18n.base.ITranslate.IMessage;
import br.com.alinesolutions.anotaai.i18n.pt.TranslatePT;

public class Teste {

	public static void main(String[] args) {
		final IMessage message = TranslatePT.getInstance().getMessage();
		System.out.println(getSuperClasses(message));
	}

	public static List<Class<?>> getSuperClasses(Object o) {
		List<Class<?>> classList = new ArrayList<>();
		Class<?> clazz = o.getClass();
		Class<?> superclass = clazz.getSuperclass();
		classList.add(superclass);
		while (superclass != null) {
			clazz = superclass;
			superclass = clazz.getSuperclass();
			classList.add(superclass);
		}
		return classList;
	}

}
