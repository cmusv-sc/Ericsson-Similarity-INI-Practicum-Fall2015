package com.cmu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.grouplens.lenskit.scored.ScoredId;

import com.cmu.model.Movie;

public class MovieDao {

	public List<Movie> getMoviesByIds(List<Long> ids) {

		List<Movie> movies = new ArrayList<Movie>();

		Connection conn = DBConnection.getConection();

		PreparedStatement statement;

		String sqlQuery = "Select title,genres,movieID from movies where movieID IN (";

		for (int i = 0; i < ids.size(); i++) {
			sqlQuery += " ?,";
		}

		sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
		sqlQuery += " )";

		// execute select SQL stetement
		ResultSet rs;
		try {
			statement = conn.prepareStatement(sqlQuery);

			int index = 1;
			for (Long id : ids) {
				statement.setLong(index, id);
				index++;
			}

			// statement.setArray(1, conn.createArrayOf("id", ids.toArray()));
			rs = statement.executeQuery();

			while (rs.next()) {

				String title = rs.getString("title");
				String genre = rs.getString("genres");
				Long id = rs.getLong("movieID");

				movies.add(new Movie(title, id, genre, "", "", "", ""));

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

		return movies;

	}

	public Movie getMovieById(Long id) {

		List<Movie> movies = new ArrayList<Movie>();

		Connection conn = DBConnection.getConection();
		PreparedStatement statement;
		String sqlQuery = "Select title,genres,movieID from movies where movieID = ?";

		// execute select SQL stetement

		ResultSet rs;

		try {
			statement = conn.prepareStatement(sqlQuery);

			statement.setLong(1, id);

			// statement.setArray(1, conn.createArrayOf("id", ids.toArray()));

			rs = statement.executeQuery();

			while (rs.next()) {

				String title = rs.getString("title");

				String genre = rs.getString("genres");

				Long movieid = rs.getLong("movieID");

				movies.add(new Movie(title, movieid, genre, "", "", "", ""));

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

		return movies.get(0);

	}

	public List<Long> getAllMovieIds() {

		List<Long> movies = new ArrayList<Long>();

		Connection conn = DBConnection.getConection();

		PreparedStatement statement;

		String sqlQuery = "Select movieID from movies";

		// execute select SQL stetement
		ResultSet rs;
		try {
			statement = conn.prepareStatement(sqlQuery);

			// statement.setArray(1, conn.createArrayOf("id", ids.toArray()));
			rs = statement.executeQuery();

			while (rs.next()) {

				movies.add(rs.getLong("movieID"));

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

		return movies;

	}
}
