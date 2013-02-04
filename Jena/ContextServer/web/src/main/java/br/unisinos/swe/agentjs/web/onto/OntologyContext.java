package br.unisinos.swe.agentjs.web.onto;

import javax.servlet.ServletContext;

public class OntologyContext {
	
	private ServletContext context = null;

	public OntologyContext(ServletContext servletContext) {
		context = servletContext;
	}

	public String getRealPath(String string) {
		return context.getRealPath(string);
	}

}
