package br.unisinos.swe.embbeded.agentjs.services;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class AgentScript {
	protected String _id = "";
	protected String _name = "";
	protected String _sourceCode = "";
	
	public AgentScript() {
		_id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return this._id;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setSourceCode(String sourceCode) {
		_sourceCode = sourceCode;
	}
	
	public JSONObject asJSONCompact() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", _id);
		json.put("name", _name);
		
		return json;
	}
	
	public JSONObject asJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", _id);
		json.put("name", _name);
		json.put("sourceCode", _sourceCode);
		
		return json;
	}

}
