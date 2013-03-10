package br.unisinos.swe.agentjs.web.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.unisinos.swe.agentjs.web.onto.AgentMapper;
import br.unisinos.swe.agentjs.web.rest.entities.AgentJson;

@Path("/agent/{user}")
public class AgentResource {
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<AgentJson> get(@PathParam("user") String user) {
		return (new AgentMapper()).getAgentListForUser(user);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response post(@PathParam("user") String user, AgentJson agentData) {
		if((new AgentMapper()).createAgent(user, agentData)) {
			return Response.ok().build();
		} else {
			return Response.notModified().build();
		}
		
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response post(@PathParam("user") String user, @PathParam("id") String agentId, AgentJson agentData) {
		if((new AgentMapper()).updateAgent(user, agentId, agentData)) {
			return Response.ok().build();
		} else {
			return Response.notModified().build();
		}
		
	}
}
