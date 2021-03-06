package br.unisinos.swe.agentjs.web.vocab;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IVocabulary {
	
	public enum Types {
		Class, DataProperty, ObjectProperty
	}

	Types type();

	String uri();

}
