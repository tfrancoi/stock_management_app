package org.openerpclient.lib;

import org.openerpclient.lib.jsonrpc.JSONRPCConnector;
import org.openerpclient.lib.xmlrpc.XmlRpcConnector;

public class Connection {
	private ConnectionInfo info = new ConnectionInfo();
	
	private Connector connector;
	

	
	public Connection(String host, int port, String database, String username, String password, ConnectorType connectorType) {
		this.info.host = host;
		this.info.port = port;
		this.info.database = database;
		this.info.username = username;
		this.info.database = database;
		this.info.password = password;
        this.info.protocol = connectorType;
		this.initConnector();
	}

    public Connection(ConnectionInfo info) {
        this.info = info;
        this.initConnector();
    }

    private void initConnector() {
        switch (this.info.protocol) {
            case JSONRPC:
                this.connector = new JSONRPCConnector(this.info.host, this.info.port);
                break;
            case XMLRPC:
                this.connector = new XmlRpcConnector(this.info.host, this.info.port);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }


	
	public boolean login()  {
		try {
			Service common = this.getService(Service.COMMON);
			Object res = common.call("login", this.info.database, this.info.username, this.info.password);
			if (res instanceof Integer) {
				this.info.uid = (Integer) res;
				System.out.println("Connection successfull " + this.info.uid);
				return true;
			}
			else if (res instanceof Boolean){
				System.out.println("Connection Failed");
				return false;
			}
			return false;
		}
		catch(ConnectorException e) {
			e.printStackTrace();
			System.out.println("Connection Failed");
			return false;
		}

	}

	public ConnectionInfo getConnectionInfo() { return this.info; }
	
	public String getDatabase() {
		return info.database;
	}
	
	public String getPassword() {
		return info.password;
	}
	
	public int getUid() {
		return info.uid;
	}
	
	public Connector getConnector() {
		return this.connector;
	}
	
	public Model getModel(String model) {
		return new Model(model, this);
	}
	
	public Service getService(String serviceName) {
		return new Service(serviceName, this.connector);
	}
}
	
	
	

