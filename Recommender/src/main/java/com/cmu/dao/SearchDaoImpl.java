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
import com.cmu.interfaces.SearchDao;
import com.cmu.model.Movie;

public class SearchDaoImpl implements SearchDao{

	public List<Movie> findExactMatchRegex(String token) {
		
		List<Movie> movies = new ArrayList<Movie>();

		Connection conn = DBConnection.getOMDBConection();

		PreparedStatement statement;

		//String sqlQuery = "Select movieid, Title,Year,movieID,Poster from smalldata where LOWER(Title) ~ LOWER('"+token+"') ORDER BY Year DESC";
		String sqlQuery = "Select movieid, Title,Year,movieID,Poster from data20m where LOWER(Title) ~ LOWER('"+token+"') ORDER BY Year DESC";



		// execute select SQL stetement
		ResultSet rs;
		try {
			statement = conn.prepareStatement(sqlQuery);

			// statement.setArray(1, conn.createArrayOf("id", ids.toArray()));
			rs = statement.executeQuery();
			String title, poster, year;
			Long id ;

			while (rs.next()) {

				title = rs.getString("title");
				if (DBConnection.getPosterpath().length()>0)
					poster = DBConnection.getPosterpath() + rs.getLong("movieid") + ".jpg";
				else
					poster = rs.getString("Poster");
				year = rs.getString("Year");
				id = rs.getLong("movieID");

				Movie movie = new Movie(title, id, "", "", poster, "", "");
				movie.setYear(year);
				
				movies.add(movie);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return movies;
		
	}

	public void recordSearch(String username, String searchString) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		
		String sqlString = "insert into searchlog (username, term) values(?,?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, username);
			statement.setString(2, searchString);
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
