package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmu.enums.Algorithm;
import com.cmu.interfaces.EvaluationDao;
import com.cmu.model.EvaluationStatistics;
import com.cmu.model.User;
import com.cmu.model.UserDetails;
import com.cmu.model.UserFeedback;

public class EvaluationDaoImpl implements EvaluationDao{

	public List<EvaluationStatistics> retrieveStatistics(){
		
		List<EvaluationStatistics> result = new ArrayList<EvaluationStatistics>();
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		ResultSet rs;
		String sqlString = "select algorithm, SUM(CASE WHEN rating = 0 OR rating = 1 THEN 1 ELSE 0 END) as all, SUM(CASE WHEN rating = 0 THEN 1 ELSE 0 END) bad from evaluation group by algorithm;";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			rs = statement.executeQuery();
			while (rs.next()) {
				EvaluationStatistics es = new EvaluationStatistics();
				es.setAlgorithm(Algorithm.valueOf(rs.getString("algorithm")));
				es.setFalsePositives(rs.getInt("bad"));
				es.setTotalEvaluations(rs.getInt("all"));
				result.add(es);
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
	
	
	public void submitFeedback(UserFeedback feedback) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		
		String sqlString = "insert into evaluation (movieId1,movieId2,rating,haveseen,algorithm,score,username) values(?,?,?,?,?,?,?)";
		try {
			for (Map.Entry<Algorithm, Double> entry : feedback.getAlgorithmScores().entrySet())
			{
				PreparedStatement statement = conn.prepareStatement(sqlString);
				List<Long> movieids = feedback.getMovieIds(); 
				statement.setLong(1, movieids.get(0));
				statement.setLong(2, movieids.get(1));
				statement.setInt(3, feedback.getRating());
				statement.setInt(4, feedback.getHaveSeen());
				statement.setString(5, entry.getKey().toString());
				statement.setDouble(6, entry.getValue());
				statement.setString(7, feedback.getUsername());
				statement.executeUpdate();
			}
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
