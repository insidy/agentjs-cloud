package br.unisinos.swe.agentjs.web.rest.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserJson {
	public String facebookId;
	public String facebookNameId;
	public String gender;
	public String name;
}

/*

"user":{"facebookId":"","id":"","gender":"","facebookNameId":"paulocesar.buttenbender","name":""},

*/