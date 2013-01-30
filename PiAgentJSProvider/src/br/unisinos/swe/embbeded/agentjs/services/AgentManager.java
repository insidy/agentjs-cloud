package br.unisinos.swe.embbeded.agentjs.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
					newScript.setName(agentScriptFile.getName());
					newScript.setSourceCode(agentSourceCode.toString());
					agents.add(newScript);
				}
			}
		}
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
