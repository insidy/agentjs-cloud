package br.unisinos.swe.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openjena.atlas.lib.NotImplemented;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.impl.OntologyImpl;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelGetter;
import com.hp.hpl.jena.rdf.model.ModelReader;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.TDBLoader;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Servlet implementation class JenaTest
 */
@WebServlet("/JenaTest")
public class JenaTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static String ONT1 = "http://example.org/test#ont1";
    public static String ONT2 = "http://example.org/test#ont2";
 
    // the model we're going to load, which imports ont1 and ont2
    public static String SOURCE =
            "@prefix rdf:                <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs:               <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix owl:                <http://www.w3.org/2002/07/owl#> .\n" +
            "<http://example.org/test#upper> a owl:Ontology ;\n" +
            " owl:imports <" + ONT1 + "> ;\n" +
            " owl:imports <" + ONT2 + "> .\n";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JenaTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
Dataset dataset = initializeTDB();
		
		Model base = dataset.getDefaultModel();
		
		OntModel onto = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_RULE_INF, base );
		
		OntClass cls = onto.getOntClass("http://swe.unisinos.br/ont/agents#Person");
		OntProperty prop = onto.getOntProperty("http://swe.unisinos.br/ont/agents#facebookID");
		
		
		for(Iterator<Individual> i = onto.listIndividuals(cls); i.hasNext(); ) {
			Individual something = i.next();
			Statement st = something.getProperty(prop);
			if(st != null) { 
				System.out.println(something.getURI() + "//" + something.getLocalName() + "//" + st.getLong());
			}
		}
		
		response.getWriter().write("OK");
	}
	
	protected Dataset initializeTDB() {
			
		String dbPath = getServletContext().getRealPath("/WEB-INF/db/Dataset1");
		System.out.println(dbPath);
		return TDBFactory.createDataset(dbPath);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	

    /**
     * Now we create an ontology model that imports ont1 and ont2, but arrange
     * that these are obtained from the TDB image.
     *
     * @param base
     */
    protected OntModel loadImportingOntology( Model base, Dataset ds ) {
    	
        // this is a test, so empty the base first just to be sure
        base.removeAll();
 
        OntModelSpec spec = new OntModelSpec( OntModelSpec.OWL_MEM );
        //spec.setImportModelGetter( new LocalTDBModelGetter( ds ) );
 
        OntModel om = ModelFactory.createOntologyModel( spec, base );
 
        // now read the source model
        StringReader in = new StringReader( SOURCE );
        om.read( in, null, "Turtle" );
 
        return om;
    }

	
}
