package com.cmu.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnection {

	/*private static Connection conn = null;
	private static Connection usrconn = null;
	private static Connection omdbconn = null;*/
	private static ComboPooledDataSource conn = null;
	private static ComboPooledDataSource usrconn = null;
	private static ComboPooledDataSource omdbconn = null;
	private static ComboPooledDataSource tmdbconn = null;
	private static String posterpath = null;
	
	private DBConnection() {
		// TODO Auto-generated constructor stub
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Properties connectionProps = new Properties();
	    connectionProps.put("user", "ini");
	    connectionProps.put("password", "a12345");

	                   
	    
	   
			try {
				/*conn = DriverManager.getConnection(
				               "jdbc:" + "mysql" + "://" +
				               "52.10.143.249" +
				               ":" + "3306" + "/Ericssonsmall",
				               connectionProps);
				
				omdbconn = DriverManager.getConnection(
			               "jdbc:" + "mysql" + "://" +
			               "52.10.143.249" +
			               ":" + "3306" + "/OMDB",
			               connectionProps);
				usrconn = DriverManager.getConnection(
			               "jdbc:" + "mysql" + "://" +
			               "52.10.143.249" +
			               ":" + "3306" + "/Users",
			               connectionProps);*/
				/*conn = DriverManager.getConnection(
			               "jdbc:" + "postgresql" + "://" +
			               "54.218.101.198" +
			               ":" + "5432" + "/Ericssonsmall",
			               connectionProps);
			
				omdbconn = DriverManager.getConnection(
			               "jdbc:" + "postgresql" + "://" +
			               "54.218.101.198" +
			               ":" + "5432" + "/OMDB",
			               connectionProps);
				usrconn = DriverManager.getConnection(
			               "jdbc:" + "postgresql" + "://" +
			               "54.218.101.198" +
			               ":" + "5432" + "/Users",
			               connectionProps);*/
				conn = new ComboPooledDataSource();
				omdbconn = new ComboPooledDataSource();
				tmdbconn = new ComboPooledDataSource();
				usrconn = new ComboPooledDataSource();
				try {
					conn.setDriverClass("org.postgresql.Driver");
					omdbconn.setDriverClass("org.postgresql.Driver");
					tmdbconn.setDriverClass("org.postgresql.Driver");
					usrconn.setDriverClass("org.postgresql.Driver");
				} catch (PropertyVetoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //loads the jdbc driver
				conn.setJdbcUrl("jdbc:postgresql://54.218.101.198:5432/Ericssonsmall");
				conn.setUser("ini");
				conn.setPassword("a12345");
				
				omdbconn.setJdbcUrl("jdbc:postgresql://54.218.101.198:5432/OMDB");
				omdbconn.setUser("ini");
				omdbconn.setPassword("a12345");
				
				tmdbconn.setJdbcUrl("jdbc:postgresql://54.218.101.198:5432/TMDB");
				tmdbconn.setUser("ini");
				tmdbconn.setPassword("a12345");
				
				usrconn.setJdbcUrl("jdbc:postgresql://54.218.101.198:5432/Users");
				usrconn.setUser("ini");
				usrconn.setPassword("a12345");
				
				conn.setMinPoolSize(3);
				conn.setAcquireIncrement(5);
				conn.setMaxPoolSize(20);
				conn.setMaxStatements(180);
		        
				omdbconn.setMinPoolSize(3);
				omdbconn.setAcquireIncrement(5);
				omdbconn.setMaxPoolSize(20);
				omdbconn.setMaxStatements(180);
				
				tmdbconn.setMinPoolSize(3);
				tmdbconn.setAcquireIncrement(5);
				tmdbconn.setMaxPoolSize(20);
				tmdbconn.setMaxStatements(180);
		        
				usrconn.setMinPoolSize(3);
				usrconn.setAcquireIncrement(5);
				usrconn.setMaxPoolSize(20);
				usrconn.setMaxStatements(180);
				
				posterpath = "http://54.218.101.198:8080/recimg/JPG_REPO/";
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	public static synchronized Connection getConection()
	{
		if(conn==null)
			new DBConnection();
		
	    //return conn;
		try {
			return conn.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static synchronized Connection getUserconn()
	{
		if(usrconn==null)
			new DBConnection();
		
	    //return usrconn;
		try {
			return usrconn.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static synchronized Connection getOMDBConection()
	{
		if(omdbconn==null)
			new DBConnection();
		
	    //return omdbconn;
		try {
			return omdbconn.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static synchronized Connection getTMDBConection()
	{
		if(tmdbconn==null)
			new DBConnection();
		
	    //return tmdbconn;
		try {
			return tmdbconn.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static synchronized String getPosterpath()
	{
		return posterpath;
	}
	
	
}
