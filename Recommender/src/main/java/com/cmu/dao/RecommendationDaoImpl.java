package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.grouplens.lenskit.scored.ScoredId;

import com.cmu.enums.Algorithm;
import com.cmu.interfaces.RecommendationDao;
import com.cmu.model.Recommendation;

public class RecommendationDaoImpl implements RecommendationDao{

	public List<Recommendation> getRecommendation(Long id, Algorithm alg) {
		List<Recommendation> recs = new ArrayList<Recommendation>();
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		String sqlString = "Select sims from similarity where movieId = ? and algorithm = ?";
		ResultSet rs;
		String sim;
		String[] similarities;
		List<Long> ids = new ArrayList<Long>();
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setLong(1, id);
			statement.setString(2, alg.toString());
			rs = statement.executeQuery();
			if(rs.next()) {
				sim = rs.getString("sims");
				similarities = sim.split(",");
				for(int i = 0; i < similarities.length; i+=2){
					com.cmu.model.Movie mv = getMovieData(Long.parseLong(similarities[i]));
					Recommendation rec = new Recommendation(alg,Double.parseDouble(similarities[i+1]),mv);
					recs.add(rec);
				}
				/*for(int i = 0; i < similarities.length; i+=2){
					ids.add(Long.parseLong(similarities[i]));
				}
				List<com.cmu.model.Movie> allmovies = getMovieDatas(ids);*/
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recs;
	}
	
	public com.cmu.model.Movie getMovieData(Long id){
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		String sqlString = "select imdbId from links where movieId = ?";
		ResultSet rs;
		String imdbId = "";
		com.cmu.model.Movie mv = null;
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setLong(1, id);
			rs = statement.executeQuery();
			if (rs.next()) {
				imdbId = rs.getString("imdbId");
				imdbId = "tt" + imdbId;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sqlString2 = "select Title,Genre,Plot from omdbsmall where imdbId = ?";
		String olddb = "";
		try {
			olddb = conn.getCatalog();
			conn.setCatalog("OMDB");
			PreparedStatement statement = conn.prepareStatement(sqlString2);
			statement.setString(1, imdbId);
			rs = statement.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString("Title"));
				mv = new com.cmu.model.Movie(rs.getString("Title"),id,rs.getString("Genre"));
				mv.setSynopsis(rs.getString("Plot"));
			}
			conn.setCatalog(olddb);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mv;
	}
	
	//TODO: find out a way to implement this function
	/*public List<com.cmu.model.Movie> getMovieDatas(List<Long> ids){
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		List<com.cmu.model.Movie> movies = new ArrayList<com.cmu.model.Movie>();
		String sqlString = "select movieId, imdbId from links where movieId in(";
		for (int i = 0; i < ids.size(); i++) {
			sqlString+=" ?,"; 
		} 

		sqlString = sqlString.substring(0, sqlString.length()-1);
		sqlString+=" )";
		
		ResultSet rs;
		List<String> imdbId = new ArrayList<String>();
		List<Long> movieId = new ArrayList<Long>();
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			int index =1;
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setLong(index, ids.get(i));
				index++;
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				movieId.add(rs.getLong("movieId"));
				imdbId.add("tt" + rs.getString("imdbId"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sqlString2 = "select Title,Genre,Plot from omdbsmall where imdbId in(";
		for (int i = 0; i < ids.size(); i++) {
			sqlString2+=" ?,"; 
		} 

		sqlString2 = sqlString2.substring(0, sqlString2.length()-1);
		sqlString2+=" )";
		
		String olddb = "";
		try {
			olddb = conn.getCatalog();
			conn.setCatalog("OMDB");
			PreparedStatement statement = conn.prepareStatement(sqlString2);
			int index =1;
			for (int i = 0; i < imdbId.size(); i++)
			{
				statement.setString(index, imdbId.get(i));
				index++;
			}
			rs = statement.executeQuery();
			index = 0;
			while (rs.next()) {
				com.cmu.model.Movie mv = new com.cmu.model.Movie(rs.getString("Title"),movieId.get(index++),rs.getString("Genre"));
				mv.setSynopsis(rs.getString("Plot"));
				movies.add(mv);
			}
			conn.setCatalog(olddb);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movies;
	}*/

}
