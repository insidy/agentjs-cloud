package br.unisinos.swe.agentjs.web.rest;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import br.unisinos.swe.agentjs.web.onto.OntologyManager;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

@Path("/query")
public class QueryResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_XML)
	public String get(String sparql) {
		
		System.out.println(sparql);
		Query query = QueryFactory.create(sparql);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, OntologyManager.instance().getModel());
		ResultSet results = qe.execSelect();
		
		OutputStream output = new OutputStream()
	    {
	        private StringBuilder string = new StringBuilder();
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        
	        @Override
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    Model modl = ResultSetFormatter.toModel(results);
		modl.write(output, "RDF/XML");
		
		
		qe.close();
		
		return output.toString();
	}
	
	@Path("/text")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getAsText(String sparql) {
		Query query = QueryFactory.create(sparql);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, OntologyManager.instance().getModel());
		ResultSet results = qe.execSelect();
		
		OutputStream output = new OutputStream()
	    {
	        private StringBuilder string = new StringBuilder();
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        
	        @Override
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    ResultSetFormatter.out(output, results, query);
		
		
		qe.close();
		
		return output.toString();

	}
	
	@Path("/json")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAsJson(@PathParam("output") String type, String sparql) {
		
		Query query = QueryFactory.create(sparql);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, OntologyManager.instance().getModel());
		ResultSet results = qe.execSelect();
		
		OutputStream output = new OutputStream()
	    {
	        private StringBuilder string = new StringBuilder();
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        
	        @Override
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    ResultSetFormatter.outputAsJSON(output, results);
		
		
		qe.close();
		
		return output.toString();
	}
	
}
