package br.unisinos.swe.agentjs.web.rest.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NetworkJson {
	public String state;
	public String subtypeName;
	public String typeName;
	public Boolean roaming;
	public Boolean failover;
	public Boolean connected;
}


/*

"network":
{
"state":"CONNECTED","subtypeName":"","typeName":"WIFI","roaming":false,"failover":false,"connected":true}
}

*/
