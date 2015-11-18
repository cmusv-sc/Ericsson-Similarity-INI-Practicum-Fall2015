package com.cmu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cmu.interfaces.SearchDao;
import com.cmu.model.Movie;

public class SearchDaoImpl implements SearchDao{

	public List<Movie> findExactMatchRegex(String token) {
		
		List<Movie> movies = new ArrayList<Movie>();

		Connection conn = DBConnection.getOMDBConection();

		PreparedStatement statement;

		String sqlQuery = "Select movieid, Title,Year,movieID,Poster from smalldata where LOWER(Title) ~ LOWER('"+token+"') ORDER BY Year DESC";



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

	
}
