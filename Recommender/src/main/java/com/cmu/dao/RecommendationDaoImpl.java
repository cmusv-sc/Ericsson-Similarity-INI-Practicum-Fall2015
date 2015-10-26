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
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setLong(1, id);
			statement.setString(2, alg.toString());
			rs = statement.executeQuery();
			if(rs.next()) {
				sim = rs.getString("sims");
				similarities = sim.split(",");
				List<Long> silist = new ArrayList<Long>();
				for(int i = 0; i < similarities.length; i+=2){
					silist.add(Long.parseLong(similarities[i]));
				}
				List<com.cmu.model.Movie> allmovies = getMovieDatas(silist);
				for(int i = 1; i < similarities.length; i+=2){
					Recommendation rec = new Recommendation(alg,Double.parseDouble(similarities[i]),allmovies.get(i/2));
					recs.add(rec);
				}
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
		Connection conn2 = DBConnection.getOMDBConection();
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
		String sqlString2 = "select Title,Genre,Plot, Poster, imdbId from omdbsmall where imdbId = ?";
		try {
			PreparedStatement statement = conn2.prepareStatement(sqlString2);
			statement.setString(1, imdbId);
			rs = statement.executeQuery();
			if (rs.next()) {
				mv = new com.cmu.model.Movie(rs.getString("Title"),id,rs.getString("Genre"), rs.getString("Plot"), rs.getString("Poster"), rs.getString("imdbId"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mv;
	}
	
	//TODO: find out a way to implement this function
	public List<com.cmu.model.Movie> getMovieDatas(List<Long> ids){
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		Connection conn2 = DBConnection.getOMDBConection();
		List<com.cmu.model.Movie> movies = new ArrayList<com.cmu.model.Movie>();
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("select DISTINCT Title,Genre,Plot,Poster,imdbId from smalldata where movieId in(");
		for (int i = 0; i < ids.size(); i++) {
			sqlBuilder.append(" ?,"); 
		} 

		sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(" )");
		sqlBuilder.append(" ORDER BY FIELD(movieId,");
		for (int i = 0; i < ids.size(); i++) {
			sqlBuilder.append(" ?,"); 
		} 

		sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(" )");
		
		try {
			PreparedStatement statement = conn2.prepareStatement(sqlBuilder.toString());
			int index =1;
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setLong(index, ids.get(i));
				index++;
			}
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setLong(index, ids.get(i));
				index++;
			}
			ResultSet rs = statement.executeQuery();
			index = 0;
			while (rs.next()) {
				if(index == ids.size())
					System.out.println("???");
				com.cmu.model.Movie mv = new com.cmu.model.Movie(rs.getString("Title"),ids.get(index++),rs.getString("Genre"), rs.getString("Plot"), rs.getString("Poster"), rs.getString("imdbId"));
				mv.setSynopsis(rs.getString("Plot"));
				movies.add(mv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movies;
	}

}
