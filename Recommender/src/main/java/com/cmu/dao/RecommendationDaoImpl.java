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
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
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
				if(similarities.length == 1)  //empty
					return recs;
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
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recs;
	}
	
	public com.cmu.model.Movie getMovieData(Long id){
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		Connection conn2 = DBConnection.getOMDBConection();
		ResultSet rs;
		com.cmu.model.Movie mv = null;
		String sqlString2 = "select Title,Genre,Plot, Poster, imdbId, year from smalldata where movieId = ?";
		try {
			PreparedStatement statement = conn2.prepareStatement(sqlString2);
			statement.setLong(1, id);
			rs = statement.executeQuery();
			if (rs.next()) {
				mv = new com.cmu.model.Movie(rs.getString("Title"),id,rs.getString("Genre"), rs.getString("Plot"), rs.getString("Poster"), rs.getString("imdbId"), rs.getString("year"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
			conn2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mv;
	}
	
	//TODO: find out a way to implement this function
	public List<com.cmu.model.Movie> getMovieDatas(List<Long> ids){
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		Connection conn2 = DBConnection.getOMDBConection();
		List<com.cmu.model.Movie> movies = new ArrayList<com.cmu.model.Movie>();
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("select * from (select DISTINCT movieId, Title,Genre,Plot,Poster,imdbId,year from smalldata where movieId in(");
		for (int i = 0; i < ids.size(); i++) {
			sqlBuilder.append(" ?,"); 
		} 

		sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(" )");
		//sqlBuilder.append(" ORDER BY FIELD(movieId,");

		//sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(" ) as c join ( values");
		for (int i = 0; i < ids.size(); i++) {
			sqlBuilder.append(" (?,?),"); 
		} 
		sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(") as x (id, ordering) on c.movieId = x.id order by x.ordering"); 
		
		try {
			PreparedStatement statement = conn2.prepareStatement(sqlBuilder.toString());
			int index =1;
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setLong(index, ids.get(i));
				index++;
			}
			/*for (int i = 0; i < ids.size(); i++)
			{
				statement.setLong(index, ids.get(i));
				index++;
			}*/
			int index2 =1;
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setLong(index, ids.get(i));
				index++;
				statement.setLong(index, index2);
				index++;
				index2++;
			}
			ResultSet rs = statement.executeQuery();
			index = 0;
			while (rs.next()) {
				com.cmu.model.Movie mv = new com.cmu.model.Movie(rs.getString("Title"),ids.get(index++),rs.getString("Genre"), rs.getString("Plot"), rs.getString("Poster"), rs.getString("imdbId"), rs.getString("year"));
				mv.setSynopsis(rs.getString("Plot"));
				movies.add(mv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
			conn2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movies;
	}

}
