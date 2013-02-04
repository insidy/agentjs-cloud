package br.unisinos.swe.agentjs.web.rest.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContextJson {
	public DeviceJson device;
	public Long timestamp;
	public WifiJson wifi;
	public UserJson user;
	public NetworkJson network;
	public LocationJson location;
	public AppJson app;
}

/*

{
"device":
{
"product":"morrison","device":"morrison","model":"CLIQ","osVersion":"2.6.29.6JassyCliq","apiLevel":10,"imei":"356687030146462"
},
"timestamp":1359597690000,
"wifi":{"ssid":"GVT-5171","linkSpeed":54,"ip":"192.168.25.3","rssi":-52},
"user":{"facebookId":"","id":"","gender":"","facebookNameId":"paulocesar.buttenbender","name":""},
"network":
{
"state":"CONNECTED","subtypeName":"","typeName":"WIFI","roaming":false,"failover":false,"connected":true}
}


*/