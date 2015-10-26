package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

	private static Connection conn = null;
	private static Connection omdbconn = null;
	
	private DBConnection() {
		// TODO Auto-generated constructor stub
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Properties connectionProps = new Properties();
	    connectionProps.put("user", "ini");
	    connectionProps.put("password", "a12345");

	                   
	    
	   
			try {
				conn = DriverManager.getConnection(
				               "jdbc:" + "mysql" + "://" +
				               "52.10.143.249" +
				               ":" + "3306" + "/Ericssonsmall",
				               connectionProps);
				
				omdbconn = DriverManager.getConnection(
			               "jdbc:" + "mysql" + "://" +
			               "52.10.143.249" +
			               ":" + "3306" + "/OMDB",
			               connectionProps);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static synchronized Connection getConection()
	{
		if(conn==null)
			new DBConnection();
		
	    return conn;
	}
	
	public static synchronized Connection getOMDBConection()
	{
		if(omdbconn==null)
			new DBConnection();
		
	    return omdbconn;
	}
	
	
	
}
