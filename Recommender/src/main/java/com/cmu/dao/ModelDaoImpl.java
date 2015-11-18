package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.grouplens.lenskit.scored.ScoredId;

import com.cmu.enums.Algorithm;
import com.cmu.interfaces.ModelDao;

public class ModelDaoImpl implements ModelDao{

	public void addToModel(Long id, List<ScoredId> recommendations, Algorithm alg) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		String sqltest = "Select sims from similarity where movieId = ? and algorithm = ?";
		boolean bnew = true;;
		try {
			PreparedStatement statement = conn.prepareStatement(sqltest);
			statement.setLong(1, id);
			statement.setString(2, alg.toString());
			ResultSet rs = statement.executeQuery();
			if(rs.next()){  //duplicate entry;
				bnew = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bnew){
			String sqlString = "insert into similarity values(?,?,?)";
			StringBuilder s = new StringBuilder();
			for(int i = 0; i < recommendations.size(); i++){
				s.append(recommendations.get(i).getId());
				s.append(",");
				s.append(recommendations.get(i).getScore());
				s.append(",");
			}
			try {
				PreparedStatement statement = conn.prepareStatement(sqlString);
				statement.setLong(1, id);
				statement.setString(2, alg.toString());
				statement.setString(3, s.toString());
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			String sqlString = "update similarity set sims = ? where movieId = ? and algorithm = ?";
			StringBuilder s = new StringBuilder();
			for(int i = 0; i < recommendations.size(); i++){
				s.append(recommendations.get(i).getId());
				s.append(",");
				s.append(recommendations.get(i).getScore());
				s.append(",");
			}
			try {
				PreparedStatement statement = conn.prepareStatement(sqlString);
				statement.setString(1, s.toString());
				statement.setLong(2, id);
				statement.setString(3, alg.toString());
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		
	}

	public void deleteModel(Long id, Algorithm alg) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		String sqlString = "delete from similarity where movieId = ? and algorithm = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setLong(1, id);
			statement.setString(2, alg.toString());
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
