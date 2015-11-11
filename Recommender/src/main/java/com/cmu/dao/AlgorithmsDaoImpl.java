package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cmu.enums.Algorithm;
import com.cmu.interfaces.AlgorithmsDao;
import com.cmu.model.EvaluationStatistics;

public class AlgorithmsDaoImpl implements AlgorithmsDao {

	public List<String> getAlgorithms() {
		return null;
	}
	
	public List<String> getEnabledAlgorithms() {
		List<String> result = new ArrayList<String>();
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		ResultSet rs;
		String sqlString = "select algorithm_name from algorithm where enabled = 1;";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			rs = statement.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("algorithm_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public void enableAlgorithm(String algorithm) {
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		ResultSet rs;
		String sqlString = "update algorithm set enabled = 1 where algorithm_name = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, algorithm);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void disableAlgorithm(String algorithm) {
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		ResultSet rs;
		String sqlString = "update algorithm set enabled = 0 where algorithm_name = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, algorithm);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	

}
