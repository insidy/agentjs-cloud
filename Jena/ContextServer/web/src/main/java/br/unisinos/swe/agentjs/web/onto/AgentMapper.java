package br.unisinos.swe.agentjs.web.onto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.unisinos.swe.agentjs.web.rest.entities.AgentJson;
import br.unisinos.swe.agentjs.web.rest.entities.ContextJson;
import br.unisinos.swe.agentjs.web.vocab.Vocabulary;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import com.ibm.icu.text.MessageFormat;

public class AgentMapper {

	OntologyManager manager = null;

	public AgentMapper() {
		manager = OntologyManager.instance();
	}

	public List<AgentJson> getAgentListForUser(String user) {
		List<AgentJson> agents = new ArrayList<AgentJson>();

		StringBuilder sb = new StringBuilder();
		sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		sb.append("PREFIX agents: <http://swe.unisinos.br/ont/agents#> \n");

		sb.append("SELECT ?uuid ?name ?code \n");
		sb.append("WHERE { \n");
		sb.append("?owner rdf:type agents:FacebookProfile . \n");
		sb.append("?owner agents:facebookName ?fbName . \n");
		sb.append("?owner agents:ownAgent ?anAgent . \n");

		sb.append("  ?anAgent rdf:type agents:AgentJS . \n");
		sb.append("?anAgent agents:uuid ?uuid . \n");
		sb.append("?anAgent agents:name ?name . \n");
		sb.append("?anAgent agents:sourceCode ?code \n");
		sb.append("FILTER ( ?fbName = \"" + user + "\" )  \n");
		sb.append("}");

		Query query = QueryFactory.create(sb.toString());

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, OntologyManager.instance().getBaseModel());
		ResultSet results = qe.execSelect();

		while (results.hasNext()) {
			QuerySolution result = results.next();
			String uuid = result.get("uuid").asLiteral().getString();
			String name = result.get("name").asLiteral().getString();
			String code = result.get("code").asLiteral().getString();
			agents.add(new AgentJson(uuid, name, code));
		}

		return agents;
	}

	public boolean createAgent(String user, AgentJson agentData) {

		boolean created = true;

		Resource userIndividual = this.getPerson(user);
		

		if (userIndividual != null) {

			Individual newAgent = Vocabulary.Agents.Classes.AgentJS.createIndividual();

			newAgent.addLiteral(Vocabulary.Agents.DataProperties.Uuid, UUID.randomUUID().toString());
			newAgent.addLiteral(Vocabulary.Agents.DataProperties.Name, agentData.name);
			newAgent.addLiteral(Vocabulary.Agents.DataProperties.SourceCode, agentData.sourceCode);

			userIndividual.addProperty(Vocabulary.Agents.ObjectProperties.ownAgent, newAgent);
			
			OntologyManager.instance().getModel().commit();
		} else {
			created = false;
		}

		return created;
	}

	public boolean updateAgent(String user, String agentId, AgentJson agentData) {
		
		boolean changed = true;
		
		Resource existingAgent = null;
		StringBuilder sb = new StringBuilder();
		
		sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
		sb.append("PREFIX agents: <http://swe.unisinos.br/ont/agents#> \n");

		sb.append("SELECT ?anAgent \n");
		sb.append("WHERE { \n");
		sb.append("?anAgent rdf:type agents:AgentJS . \n");
		sb.append("?anAgent agents:uuid ?uuid . \n");
		sb.append("FILTER( ?uuid = \"" + agentId + "\" )  \n");
		sb.append(" }");
		
		Query query = QueryFactory.create(sb.toString());

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, OntologyManager
				.instance().getBaseModel());
		ResultSet results = qe.execSelect();

		if (results.hasNext()) {
			existingAgent = results.next().get("anAgent").asResource();
		}
		qe.close();
		
		if(existingAgent != null) {
		
			existingAgent.removeAll(Vocabulary.Agents.DataProperties.Name);
			existingAgent.removeAll(Vocabulary.Agents.DataProperties.SourceCode);
			
			existingAgent.addLiteral(Vocabulary.Agents.DataProperties.Name, agentData.name);
			existingAgent.addLiteral(Vocabulary.Agents.DataProperties.SourceCode, agentData.sourceCode);
			
			OntologyManager.instance().getBaseModel().commit();
		} else {
			changed = false;
		}
		
		return changed;
	}

	private Resource getPerson(String facebookName) {
		Resource existingPerson = null;

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ?s ");
		sb.append("WHERE { ?s <http://swe.unisinos.br/ont/agents#facebookName> ?o FILTER( ?o = \"" + facebookName + "\" ) } " );
		

		Query query = QueryFactory.create(sb.toString());

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, OntologyManager
				.instance().getBaseModel());
		ResultSet results = qe.execSelect();

		if (results.hasNext()) {
			existingPerson = results.next().get("s").asResource();
		}
		qe.close();

		return existingPerson;
	}

}
