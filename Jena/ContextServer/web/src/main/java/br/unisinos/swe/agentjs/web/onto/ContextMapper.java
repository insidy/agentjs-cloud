package br.unisinos.swe.agentjs.web.onto;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import com.ibm.icu.text.MessageFormat;

import br.unisinos.swe.agentjs.web.rest.entities.ContextJson;
import br.unisinos.swe.agentjs.web.vocab.Vocabulary;

public class ContextMapper {
	
	OntologyManager manager = null;
	public ContextMapper() {
		manager = OntologyManager.instance();
	}

	public void mapToOntology(ContextJson context) {
		Individual temporalThing = getTemporalThing(context);
		Individual spacialThing = getSpacialThing(context);
		Resource person = getPerson(context);
		Individual activity = getActivity(context, person);
		
		Individual newContext = generateContext(temporalThing, spacialThing, person, activity);
		OntologyManager.instance().getModel().commit();
		//newContext.getModel().write(System.out);
	}

	private Individual generateContext(Individual when, Individual where, Resource who, Individual what) {
		Individual newContext = Vocabulary.Context.Classes.Context.createIndividual();
		
		newContext.addProperty(Vocabulary.Context.ObjectProperties.isPerformedAtLocation, where);
		newContext.addProperty(Vocabulary.Context.ObjectProperties.isPerformedAtTime, when);
		newContext.addProperty(Vocabulary.Context.ObjectProperties.isPerformedBy, who);
		newContext.addProperty(Vocabulary.Context.ObjectProperties.isPerforming, what);
		
		return newContext;
	}

	private Individual getActivity(ContextJson context, Resource who) {
		Individual newActivity = Vocabulary.Context.Classes.Activity.createIndividual();
		Individual app = Vocabulary.Agents.Classes.Application.createIndividual();
		
		app.addLiteral(Vocabulary.Agents.DataProperties.Name, context.app.name);
		app.addLiteral(Vocabulary.Agents.DataProperties.Package, context.app.pack);
		
		newActivity.addProperty(Vocabulary.Context.ObjectProperties.usingSoftware, app);
		newActivity.addProperty(Vocabulary.Context.ObjectProperties.isPerformedBy, who);
		
		return newActivity;
	}

	private Resource getPerson(ContextJson context) {
		Resource existingPerson = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ?s ");
		sb.append("WHERE { ?s <http://swe.unisinos.br/ont/agents#facebookName> ?o FILTER( ?o = \"" + context.user.facebookNameId + "\" ) } ");
		
		Query query = QueryFactory.create(sb.toString());

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, OntologyManager.instance().getBaseModel());
		ResultSet results = qe.execSelect();
		
		if(results.hasNext()) {
			existingPerson = results.next().get("s").asResource();
		}
		qe.close();
		
		return existingPerson;
	}

	private Individual getSpacialThing(ContextJson context) {
		Individual newSpacePoint = Vocabulary.Location.Classes.GeoCoordiantes.createIndividual();
		newSpacePoint.addOntClass(Vocabulary.Location.Classes.SinglePoint);
		//newSpacePoint.addOntClass(Vocabulary.Location.Classes.AtomicPlace);
		
		Double latitude = 0.0;
		Double longitude = 0.0;
		
		if(context.location != null) {
			latitude = context.location.latitude;
			longitude = context.location.longitude;
		}
		
		newSpacePoint.addLiteral(Vocabulary.Location.DataProperties.Latitude, latitude);
		newSpacePoint.addLiteral(Vocabulary.Location.DataProperties.Longitude, longitude);
		
		newSpacePoint.addProperty(Vocabulary.Location.ObjectProperties.hasCoordinate, newSpacePoint);
		//newSpacePoint.addProperty(Vocabulary.Location.ObjectProperties.hasShape, newSpacePoint);
		
		return newSpacePoint;
	}

	private Individual getTemporalThing(ContextJson context) {
		Individual newInstant = Vocabulary.Time.Classes.Instant.createIndividual();
		newInstant.addLiteral(Vocabulary.Time.DataProperties.Timezone, -3);
		newInstant.addLiteral(Vocabulary.Time.DataProperties.AtTime, context.timestamp);
		
		return newInstant;
	}
}
