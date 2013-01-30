package br.unisinos.swe.testing;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.TDBLoader;

/**
 * Servlet implementation class JenaLoad
 */
@WebServlet("/JenaLoad")
public class JenaLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JenaLoad() {
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
		
		String ontoPath = "";
		
		
		//File ontoFile = new File(ontoPath);
		
		try {
			//FileInputStream in = new FileInputStream(ontoFile);
			ontoPath = getServletContext().getRealPath("/WEB-INF/model/time.owl");
			TDBLoader.loadModel(base, "file:"+ontoPath);
			
			ontoPath = getServletContext().getRealPath("/WEB-INF/model/location.owl");
			TDBLoader.loadModel(base, "file:"+ontoPath);
			
			ontoPath = getServletContext().getRealPath("/WEB-INF/model/agents.owl");
			TDBLoader.loadModel(base, "file:"+ontoPath);
			
			ontoPath = getServletContext().getRealPath("/WEB-INF/model/context.owl");
			TDBLoader.loadModel(base, "file:"+ontoPath);
			
			base.close();
			
			//onto.read(in, "http://example.org/context");
			
			//in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded.");
		
		TDB.sync( onto );
		
		System.out.println("Synchronized.");
		
		
		response.getWriter().write("Loaded");
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

}
