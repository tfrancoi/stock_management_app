package org.openerpclient.lib.xmlrpc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openerpclient.lib.Connector;
import org.openerpclient.lib.ConnectorException;
import org.openerpclient.lib.Service;
import org.openerpclient.lib.jsonrpc.JSONRPCConnector;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class XmlRpcConnector implements Connector {
	
	private String baseURL;
	
	public XmlRpcConnector(String host, int port) {
		this.baseURL = "http://" + host + ":" + port + "/xmlrpc/";
	}
	
	@Override
	public Object call(String service, String method, Object... args) throws ConnectorException {
		System.out.println("Call: " + service + " - " + method);
		for (Object o : args) {
			System.out.println(o);
		}
		try {
			XMLRPCClient rpcClient = new XMLRPCClient(this.baseURL + service);
			return rpcClient.callEx(method, args);
		}
		catch(XMLRPCException e) {
			throw new ConnectorException(e);
		}
	}
}
