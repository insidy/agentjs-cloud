package br.unisinos.swe.agentjs.web.rest;

import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.unisinos.swe.agentjs.web.onto.ContextMapper;
import br.unisinos.swe.agentjs.web.rest.entities.AppJson;
import br.unisinos.swe.agentjs.web.rest.entities.ContextJson;
import br.unisinos.swe.agentjs.web.rest.entities.DeviceJson;
import br.unisinos.swe.agentjs.web.rest.entities.LocationJson;
import br.unisinos.swe.agentjs.web.rest.entities.NetworkJson;
import br.unisinos.swe.agentjs.web.rest.entities.UserJson;
import br.unisinos.swe.agentjs.web.rest.entities.WifiJson;

@Path("/load")
public class TestResource {

	private static long _lastPoint = 0L;
	private static Random rand = new Random();;
	
	@Path("/{ctx}")
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getAsText(@PathParam("ctx") String contexts) {
		
		if(_lastPoint <= 0) {
			_lastPoint = System.currentTimeMillis();
		}
		
		int createdContext = 0;
		int maxContext = Integer.parseInt(contexts);
		while(createdContext++ < maxContext) {
			_lastPoint += 1000L;
			
			ContextJson newContext = generateContext();
			
			ContextMapper mapper = new ContextMapper();
			mapper.mapToOntology(newContext);
			
			
		}
		
		return "ok";
	}

	private ContextJson generateContext() {
		ContextJson newCtx = new ContextJson();
		AppJson newApp = getRandomApp();
		DeviceJson newDevice = new DeviceJson();
		newDevice.imei = "00000000";
		newDevice.model = "Dext";
		newDevice.osVersion = "2.4.3";
		
		LocationJson newLoc = getRandomLoc();
		
		NetworkJson newNet = new NetworkJson();
		newNet.connected = true;
		newNet.failover = false;
		newNet.typeName = "WIFI";
		
		UserJson myUser = new UserJson();
		myUser.facebookNameId = "paulocesar.buttenbender";
		
		WifiJson newWifi = new WifiJson();
		newWifi.ssid = "Unisinos";
		
		
		newCtx.app = newApp;
		newCtx.device = newDevice;
		newCtx.location = newLoc;
		newCtx.network = newNet;
		newCtx.timestamp = _lastPoint;
		newCtx.user = myUser;
		newCtx.wifi = newWifi;
		
		return newCtx;
	}

	private LocationJson getRandomLoc() {
		LocationJson loc = new LocationJson();
		
		loc.latitude = -29.793245;
		loc.longitude = -51.152258;
		
		double val = rand.nextInt(10 - 0 + 1) + 0;
		val *= 0.01;
		loc.latitude += val;
		
		val = rand.nextInt(10 - 0 + 1) + 0;
		val *= 0.01;
		loc.longitude += val;
		
		return loc;
	}

	private AppJson getRandomApp() {
		AppJson app = new AppJson();
		
		int val = rand.nextInt(10 - 0 + 1) + 0;
		if(val < 3) {
			app.name = "AgentJS";
			app.pack = "br.unisinos.swe.agentjs";
		} else if(val < 6) {
			app.name = "Calendar";
			app.pack = "com.android.calendar";
		} else {
			app.name = "Launcher";
			app.pack = "com.android.launcher";
		}
		
		
		return app;
	}
	
}
