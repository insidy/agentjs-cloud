package br.unisinos.swe.embbeded.agentjs.services;

import org.json.JSONException;
import org.teleal.cling.binding.annotations.*;

@UpnpService(
        serviceId = @UpnpServiceId("AgentJS"),
        serviceType = @UpnpServiceType(value = "AgentJS", version = 1)
)
public class AgentService {
	
	private static AgentManager _manager = new AgentManager();
	
	@UpnpStateVariable(datatype = "string")
	private String agents = "";
	
	@UpnpStateVariable(datatype = "string")
	private String agent = "";
	
	@UpnpStateVariable(datatype = "string")
	private String agentId = "";
	
	public String getAgents() {
		agents = "";
		try {
			agents = _manager.getAgentList().toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("agentes:" + agents);
		return agents;
	}
	
	@UpnpAction(
        name = "GetAgentList",
        out = @UpnpOutputArgument(
                name = "Agents"
        )
	)
	public String getAgentList() {
		return getAgents();
		
	}
	
	
	
	@UpnpAction(name = "GetAgentSource", out = @UpnpOutputArgument(name = "Agent"))
	public String getAgentSource(@UpnpInputArgument(name = "AgentId") String agentId) {
		
		agent = "";
		try {
			agent = _manager.getAgent(agentId).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Agente: " + agent);
		
		return agent;
	}
	
	// Actions: GetAgentList, output: JSONArray with agents (source not obligatory)
	// Actions: GetAgentSource, input: AgentId, output: JSONObject with agent
	
	
	/**
	 * 
	 * Agent structure:
	 *  { 'id' :  'uuid', 'name' : 'displayName', 'sourceCode' : 'code' }
	 * 
	 */

}
