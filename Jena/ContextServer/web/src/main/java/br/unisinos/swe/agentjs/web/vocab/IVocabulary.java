package br.unisinos.swe.agentjs.web.vocab;

public @interface IVocabulary {
	
	public enum Types {
		Class, DataProperty, ObjectProperty
	}

	Types type();

	String uri();

}
