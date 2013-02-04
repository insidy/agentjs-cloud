package br.unisinos.swe.agentjs.web.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.unisinos.swe.agentjs.web.onto.ContextMapper;
import br.unisinos.swe.agentjs.web.rest.entities.ContextJson;

@Path("/context")
public class ContextResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postContext(ContextJson context) {
		System.out.println(context.toString());
		
		ContextMapper mapper = new ContextMapper();
		mapper.mapToOntology(context);

		return Response.ok().build();
	}
	
}
