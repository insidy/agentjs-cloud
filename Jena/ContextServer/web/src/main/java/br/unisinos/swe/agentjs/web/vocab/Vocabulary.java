package br.unisinos.swe.agentjs.web.vocab;

import java.lang.reflect.Field;

import br.unisinos.swe.agentjs.web.onto.OntologyManager;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Property;

public class Vocabulary {
	
	@SuppressWarnings("rawtypes")
	public static void initialize(OntologyManager manager) {
		//Time.Classes.Instant = manager.getModel().getOntClass("http://swe.unisinos.br/ont/time#Instant");
		//Time.DataProperties.Timezone = manager.getModel().getProperty("http://swe.unisinos.br/ont/time#timezone");
		//Time.DataProperties.AtTime  = manager.getModel().getProperty("http://swe.unisinos.br/ont/time#atTime");
		
		for(Class ontoClass : Vocabulary.class.getDeclaredClasses()) {
			IOntology onto = (IOntology) ontoClass.getAnnotation(IOntology.class);
			if(onto != null) {
			
			for(Class groupClass : ontoClass.getDeclaredClasses()) {
				for(Field field : groupClass.getDeclaredFields()) {
					IVocabulary vocab = field.getAnnotation(IVocabulary.class);
					try {
					if(vocab != null) {
						switch(vocab.type()) {
						case Class:
							field.set(null, manager.getModel().getOntClass(onto.rootUri() + vocab.uri()));
							break;
						case DataProperty:
							field.set(null, manager.getModel().getProperty(onto.rootUri() + vocab.uri()));
							break;
						case ObjectProperty:
							field.set(null, manager.getModel().getProperty(onto.rootUri() + vocab.uri()));
							break;
						}
					}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			}
			
		}
	}
	
	/**
	 * 
	 * @author paulo
	 *
	 */
	@IOntology(rootUri = "http://swe.unisinos.br/ont/time#")
	public static class Time {
		public static class Classes {
			@IVocabulary(type = IVocabulary.Types.Class, uri ="Instant")
			public static OntClass Instant = null;
			
		}
		
		public static class DataProperties {
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="timezone")
			public static Property Timezone = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="atTime")
			public static Property AtTime = null;
		}
	}
	
	/**
	 * 
	 * @author paulo
	 *
	 */
	@IOntology(rootUri = "http://swe.unisinos.br/ont/location#")
	public static class Location {
		public static class Classes {
			@IVocabulary(type = IVocabulary.Types.Class, uri ="GeoCoordinates")
			public static OntClass GeoCoordiantes = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="SinglePoint")
			public static OntClass SinglePoint = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="AtomicPlace")
			public static OntClass AtomicPlace = null;
		}
		
		public static class ObjectProperties {
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="hasShape")
			public static Property hasShape = null;
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="hasCoordinate")
			public static Property hasCoordinate;
		}
		
		public static class DataProperties {
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="latitude")
			public static Property Latitude = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="longitude")
			public static Property Longitude = null;
		}
	}
	
	/**
	 * 
	 * @author paulo
	 *
	 */
	@IOntology(rootUri = "http://swe.unisinos.br/ont/context#")
	public static class Context {
		public static class Classes {
			@IVocabulary(type = IVocabulary.Types.Class, uri ="Context")
			public static OntClass Context = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="Activity")
			public static OntClass Activity = null;
		}
		
		public static class ObjectProperties {
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="isPerformedBy")
			public static Property isPerformedBy = null;
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="isPerforming")
			public static Property isPerforming;
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="isPerformedAtTime")
			public static Property isPerformedAtTime;
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="isPerformedAtLocation")
			public static Property isPerformedAtLocation;
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="usingSoftware")
			public static Property usingSoftware;
		}
	}
	
	/**
	 * 
	 * @author paulo
	 *
	 */
	@IOntology(rootUri = "http://swe.unisinos.br/ont/agents#")
	public static class Agents {
		public static class Classes {
			@IVocabulary(type = IVocabulary.Types.Class, uri ="FacebookProfile")
			public static OntClass FacebookProfile = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="Person")
			public static OntClass Person = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="Hardware")
			public static OntClass Hardware = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="MobileDevice")
			public static OntClass MobileDevice = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="Software")
			public static OntClass Software = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="AgentJS")
			public static OntClass AgentJS = null;
			@IVocabulary(type = IVocabulary.Types.Class, uri ="Application")
			public static OntClass Application = null;
		}
		
		public static class ObjectProperties {
			@IVocabulary(type = IVocabulary.Types.ObjectProperty, uri ="ownAgent")
			public static Property ownAgent = null;
		}
		
		public static class DataProperties {
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="package")
			public static Property Package = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="facebookName")
			public static Property FacebookName = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="facebookId")
			public static Property FacebookId = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="maker")
			public static Property Maker = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="model")
			public static Property Model = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="name")
			public static Property Name = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="uuid")
			public static Property Uuid = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="sourceCode")
			public static Property SourceCode = null;
			@IVocabulary(type = IVocabulary.Types.DataProperty, uri ="version")
			public static Property Version = null;
		}
	}
	
}
