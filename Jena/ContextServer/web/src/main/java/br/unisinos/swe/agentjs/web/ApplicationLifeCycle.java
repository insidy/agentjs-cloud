package br.unisinos.swe.agentjs.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.unisinos.swe.agentjs.web.onto.OntologyContext;
import br.unisinos.swe.agentjs.web.onto.OntologyManager;
import br.unisinos.swe.agentjs.web.vocab.Vocabulary;

/**
 * Application Lifecycle Listener implementation class ApplicationLifeCycle
 *
 */
@WebListener
public class ApplicationLifeCycle implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ApplicationLifeCycle() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        if(!OntologyManager.exists()) {
        	OntologyManager.create(new OntologyContext(event.getServletContext()));
        	Vocabulary.initialize(OntologyManager.instance());
        }
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        
    }
	
}
