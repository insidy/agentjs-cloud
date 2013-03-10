package br.unisinos.swe.agentjs.web.vocab;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IOntology {

	String rootUri();

}
