package org.openerpclient.lib;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {
	private String name;
	private Service objectService;
	private Connection connection;
	
	public final int MIN_ARG_SIZE = 5;
	
	public Model(String name, Connection connection) {
		this.name = name;
		this.connection = connection;
		this.objectService = new Service(Service.OBJECT, this.connection.getConnector());
	}
	
	public Object call(String method, Object...methodArgs) {
		Object[] params = new Object[MIN_ARG_SIZE + methodArgs.length];
		params[0] = this.connection.getDatabase();
		params[1] = this.connection.getUid();
		params[2] = this.connection.getPassword();
		params[3] = this.name;
		params[4] = method;
		for(int i = 0; i < methodArgs.length; i++) {
			System.out.println("Model args:" + methodArgs[i]);
			params[MIN_ARG_SIZE + i] = methodArgs[i];
		}
		try {
			System.out.println("---------------->"+ params);
			return this.objectService.call("execute", params);
		}
		catch(ConnectorException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object[] search(String domain) {
		Object objectDomain = this.connection.getConnector().parseDomain(domain);
		Object result = this.call("search", objectDomain);
		if(result.getClass().isArray()) {
			Object[] object_ids = (Object[]) result;
			int[] ids = new int[object_ids.length];
			for(int i = 0; i < ids.length; i++) {
				ids[i] = (Integer) object_ids[i];
			}
			return object_ids;
		}
		return null;
	}

	public ArrayList<HashMap> read(Object[] ids, String[] fields){
		Object result = this.call("read", ids, fields);

		ArrayList datas = new ArrayList();

		if(result.getClass().isArray()) {
			for (int i = 0; i <= (((Object[]) result).length-1); i++) {
				datas.add(((HashMap) ((Object[]) result)[i]));
			}
			return datas;
		}
		return null;
	}
}
