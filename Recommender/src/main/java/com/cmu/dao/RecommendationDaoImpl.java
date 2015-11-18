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
import com.cmu.model.User;
import com.cmu.model.UserDetails;

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
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		String poster;
		String sqlString2 = "select Title,Genre,Plot, Poster, imdbId, year from smalldata where movieId = ?";
		try {
			PreparedStatement statement = conn2.prepareStatement(sqlString2);
			statement.setLong(1, id);
			rs = statement.executeQuery();
			if (rs.next()) {
				if (DBConnection.getPosterpath().length()>0)
					poster = DBConnection.getPosterpath() + "/" + id + ".jpg";
				else
					poster = rs.getString("Poster");
				mv = new com.cmu.model.Movie(rs.getString("Title"),id,rs.getString("Genre"), rs.getString("Plot"), poster , rs.getString("imdbId"), rs.getString("year"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				conn2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mv;
	}
	
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
		String poster;
		StringBuilder sqlBuilder = new StringBuilder();
		
		if(ids.size() == 0)
			return movies;

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
				if (DBConnection.getPosterpath().length()>0)
					poster = DBConnection.getPosterpath() + "/" + ids.get(index) + ".jpg";
				else
					poster = rs.getString("Poster");
				com.cmu.model.Movie mv = new com.cmu.model.Movie(rs.getString("Title"),ids.get(index++),rs.getString("Genre"), rs.getString("Plot"), poster, rs.getString("imdbId"), rs.getString("year"));
				mv.setSynopsis(rs.getString("Plot"));
				movies.add(mv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				conn2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return movies;
	}
	
	public List<Long> imdbIdtoMovielensId(List<String> ids){
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		List<Long> newid = new ArrayList<Long>();
		StringBuilder sqlBuilder = new StringBuilder();
		
		if(ids.size() == 0)
			return newid;

		sqlBuilder.append("select * from (select DISTINCT movieid, imdbid from links where imdbid in(");
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
		sqlBuilder.append(") as x (id, ordering) on c.imdbid = x.id order by x.ordering"); 
		
		try {
			PreparedStatement statement = conn.prepareStatement(sqlBuilder.toString());
			int index =1;
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setString(index, ids.get(i).substring(2));
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
				statement.setString(index, ids.get(i).substring(2));
				index++;
				statement.setLong(index, index2);
				index++;
				index2++;
			}
			ResultSet rs = statement.executeQuery();
			index = 0;
			while (rs.next()) {
				newid.add(rs.getLong("movieid"));
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
		
		return newid;
	}
	
	public List<Long> tmdbIdtoMovielensId(List<String> ids){
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		List<Long> newid = new ArrayList<Long>();
		StringBuilder sqlBuilder = new StringBuilder();
		
		if(ids.size() == 0)
			return newid;

		sqlBuilder.append("select * from (select DISTINCT movieid, tmdbid from links where tmdbid in(");
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
		sqlBuilder.append(") as x (id, ordering) on c.tmdbid = x.id order by x.ordering"); 
		
		try {
			PreparedStatement statement = conn.prepareStatement(sqlBuilder.toString());
			int index =1;
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setString(index, ids.get(i));
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
				statement.setString(index, ids.get(i));
				index++;
				statement.setLong(index, index2);
				index++;
				index2++;
			}
			ResultSet rs = statement.executeQuery();
			index = 0;
			while (rs.next()) {
				newid.add(rs.getLong("movieid"));
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
		
		return newid;
	}
	
	public List<com.cmu.model.Movie> getPopularMovies(int count){
		List<com.cmu.model.Movie> result = new ArrayList<com.cmu.model.Movie>();
		List<String> ids = new ArrayList<String>();
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getTMDBConection();
		ResultSet rs;
		String sqlString = "select distinct data->>'imdb_id' as imdb_id, (data->>'revenue')::bigint from tmdb20m order by (data->>'revenue')::bigint desc limit ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setInt(1, count);
			rs = statement.executeQuery();
			while (rs.next()) {
				ids.add(rs.getString("imdb_id"));
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
		
		List<Long> movieids = imdbIdtoMovielensId(ids);
		return getMovieDatas(movieids);
	}
	
	public void generateTMDBrating(){

		Long id;
		List<String> sim = new ArrayList<String>();
		List<Long> newsim = new ArrayList<Long>();
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getTMDBConection();
		Connection conn2 = DBConnection.getConection();
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		int count = 0;
		String sqlString = "select movieid, data->>'similar_movies top 0', data->>'similar_movies top 1', data->>'similar_movies top 2', " +
		"data->>'similar_movies top 3', data->>'similar_movies top 4', data->>'similar_movies top 5', data->>'similar_movies top 6', "+
		"data->>'similar_movies top 7', data->>'similar_movies top 8', data->>'similar_movies top 9', data->>'similar_movies top 10', "+
		"data->>'similar_movies top 11', data->>'similar_movies top 12', data->>'similar_movies top 13', data->>'similar_movies top 14', "+
		"data->>'similar_movies top 15', data->>'similar_movies top 16', data->>'similar_movies top 17', data->>'similar_movies top 18', "+
		"data->>'similar_movies top 19' from tmdb20m3";
		String sqlString2 = "insert into similarity values(?,?,?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			rs = statement.executeQuery();
			while (rs.next()) {
				id = rs.getLong(1);
				sim.clear();
				sb.setLength(0);
				for(int i = 2; i < 22; i++){
					if (rs.getString(i)!=null)
						sim.add(rs.getString(i));
				}
				if (sim.size() > 0)
				{
					newsim = tmdbIdtoMovielensId(sim);
					for (int j = 0; j < newsim.size(); j++){
						sb.append(newsim.get(j));
						sb.append(",1.0,");
					}
				}
				PreparedStatement statement2 = conn2.prepareStatement(sqlString2);
				statement2.setLong(1, id);
				statement2.setString(2, "TMDB_SIMILARITY");
				statement2.setString(3, sb.toString());
				statement2.executeUpdate();
				count++;
				if(count % 100 == 1)
					System.out.println(count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				conn2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("end!");
	}


}
