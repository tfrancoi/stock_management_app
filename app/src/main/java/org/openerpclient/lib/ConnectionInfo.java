package org.openerpclient.lib;

/**
 * Created by mythrys on 01-05-16.
 */
public class ConnectionInfo {
    protected String host;
    protected int port;
    protected String username;
    protected String password;
    protected String database;
    protected ConnectorType protocol;
    protected int uid = 0;

    public ConnectionInfo() {}
}
