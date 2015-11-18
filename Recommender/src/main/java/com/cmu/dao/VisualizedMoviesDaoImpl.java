package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cmu.enums.Algorithm;
import com.cmu.interfaces.VisualizedMoviesDao;

public class VisualizedMoviesDaoImpl implements VisualizedMoviesDao{

	public void saveVisualizedMovie(String username, String selectedMovie, String seenMovie) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		
		String sqlString = "insert into seenmovie (username,basemovie,tomovie) values(?,?,?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, username);
			statement.setLong(2, Long.parseLong(selectedMovie));
			statement.setLong(3, Long.parseLong(seenMovie));
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
