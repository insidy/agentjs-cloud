package br.unisinos.swe.agentjs.web.onto;

import br.unisinos.swe.hbase.rdf.layout.simple.StoreSimple;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.talis.hbase.rdf.HBaseRdfFactory;
import com.talis.hbase.rdf.Store;
import com.talis.hbase.rdf.StoreDesc;
import com.talis.hbase.rdf.connection.HBaseRdfConnection;
import com.talis.hbase.rdf.store.LayoutType;

public class OntologyManager {

	private static OntologyManager _instance = null;
	private static String userHome = null;
	private static final String hbaseConf = "/Desktop/hbase/hbase/conf/hbase-site.xml";
	
	private Store ss = null;
	private Model baseModel = null;
	private OntModel onto = null;
	
	private boolean refresh = false;
	
	private OntologyManager(OntologyContext initializationContext) {
		userHome = System.getProperty("user.home");
		
		openConnection();
		loadOntologies(initializationContext);
	}
	
	public static boolean exists() {
		return (_instance != null);
	}

	public static OntologyManager create(OntologyContext initializationContext) {
		if(_instance == null) {
			_instance = new OntologyManager(initializationContext);
		}
		
		return _instance;
	}

	public static OntologyManager instance() {
		return _instance;
	}
	
	public Model getBaseModel() {
		return baseModel;
	}
	
	public OntModel getModel() {
		return onto;
	}
	
	private void loadOntologies(OntologyContext initializationContext) {
		String[] arquivos = new String[]{
				initializationContext.getRealPath("/WEB-INF/model/time.owl"),
				initializationContext.getRealPath("/WEB-INF/model/location.owl"),
				initializationContext.getRealPath("/WEB-INF/model/agents.owl"),
				initializationContext.getRealPath("/WEB-INF/model/context.owl")
		};
		
		String[] namespace = new String[] {
			"http://swe.unisinos.br/ont/time",
			"http://swe.unisinos.br/ont/location",
			"http://swe.unisinos.br/ont/agents",
			"http://swe.unisinos.br/ont/context"
		};
		
		
		
		OntModelSpec spec = new OntModelSpec( OntModelSpec.OWL_MEM_RULE_INF );
		
		OntDocumentManager dm = spec.getDocumentManager();
		for(int idx = 0; idx < arquivos.length; idx++) {
			dm.addAltEntry(namespace[idx], "file:" + arquivos[idx]);
		}
		
		onto = ModelFactory.createOntologyModel( spec, baseModel );
		
		
		OntClass testCls = onto.getOntClass("http://swe.unisinos.br/ont/agents#Person");
		
		if(testCls == null) {
			
			for(int idx = 0; idx < arquivos.length; idx++) {
				onto.read("file:" + arquivos[idx]);
			}
			onto.commit();
		}
		
	}

	private void openConnection() {
		if( ss == null )
		{
			HBaseRdfConnection conn = HBaseRdfFactory.createConnection( userHome + hbaseConf , false ) ;
			StoreDesc desc = new StoreDesc( LayoutType.LayoutSimple, "agentjs" ) ;
			ss = new StoreSimple( conn, desc ) ;
			if(refresh) {
				ss.getTableFormatter().format() ;
			}
		}
		else {
			//ss.getTableFormatter().truncate();
		}
		
		baseModel = HBaseRdfFactory.connectDefaultModel( ss );
	}

	public Store getStore() {
		return this.ss;
	}
	
}
