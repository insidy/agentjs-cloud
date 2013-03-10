package br.unisinos.swe.agentjs.web.rest.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AgentJson {

	public String id;
	public String name;
	public String sourceCode;
	
	public AgentJson() {
		
	}
	
	public AgentJson(String uuid, String agentName, String code) {
		this.id = uuid;
		this.name = agentName;
		this.sourceCode = code;
	}
}
