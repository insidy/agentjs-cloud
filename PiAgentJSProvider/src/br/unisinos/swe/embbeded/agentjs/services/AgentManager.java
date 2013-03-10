package br.unisinos.swe.embbeded.agentjs.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.unisinos.swe.embbeded.agentjs.PiServer;

public class AgentManager {

	private List<AgentScript> agents;
	private final File folder;
	
	public AgentManager() {
		// load agents from disk
		agents = new ArrayList<AgentScript>();
		
		String userHome = System.getProperty("user.home");
		folder = new File(userHome + "/agents/src/");
		
		if(!folder.exists()) {
			folder.mkdirs();
		}
		readAgents();
		
		Thread readerThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(60 * 1000); // wait for 1 minute
					readAgents(); //reload agents
				} catch (InterruptedException e) {
				} 
				
			}
		});
		
		readerThread.start();
		
	}

	private void readAgents() {
		FilenameFilter jsFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".js")) {
					return true;
				} else {
					return false;
				}
			}
		};

		File[] availableAgents = folder.listFiles(jsFilter);

		if (availableAgents != null) {

			agents = new ArrayList<AgentScript>();
			for (File agentScriptFile : availableAgents) {
				AgentScript newScript = new AgentScript();
				StringBuilder agentSourceCode = new StringBuilder();
				if (agentScriptFile.isFile()) {
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(agentScriptFile));

						String line;

						while ((line = br.readLine()) != null) {
							agentSourceCode.append(line);
							agentSourceCode.append('\n');
						}

						br.close();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					newScript.setId(agentScriptFile.getName());
					newScript.setName(agentScriptFile.getName());
					newScript.setSourceCode(agentSourceCode.toString());
					agents.add(newScript);
				}
			}
			
			AgentScript newScript = new AgentScript();
			newScript.setId("aula.js");
			newScript.setName("Docs da aula de hoje");
			try {
				String ipv4 = getCurrentIp();
				if(ipv4 != null) {
					newScript.setSourceCode("agent.apps.launchAppForUrl('http://"+ipv4+":8080/pi');");
					agents.add(newScript);
				}
			} catch(Exception e) {
				
			}
		}
	}

	private String getCurrentIp() throws SocketException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()){
		    NetworkInterface current = interfaces.nextElement();

		    if (!current.isUp() || current.isLoopback() || current.isVirtual()) 
		    	continue;
		    
		    Enumeration<InetAddress> addresses = current.getInetAddresses();
		    while (addresses.hasMoreElements()){
		        InetAddress current_addr = addresses.nextElement();
		        if (current_addr.isLoopbackAddress()) continue;
		        
		        if(current_addr instanceof Inet4Address)
		        	return current_addr.getHostAddress();
		    }
		}
		return null;
	}

	public JSONArray getAgentList() throws JSONException {
		JSONArray arrAgents = new JSONArray();
		
		for(AgentScript agent : agents) { 
			arrAgents.put(agent.asJSONCompact());
		}
		
		return arrAgents;
	}
	
	public JSONObject getAgent(String agentId) throws JSONException {
		for(AgentScript agent : agents) {
			if(agent.getId().equals(agentId)) {
				return agent.asJSON();
			}
		}
		
		return null;
	}
	
}
