package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cmu.interfaces.RecommendationDao;
import com.cmu.interfaces.UserDetailsDao;
import com.cmu.model.Movie;
import com.cmu.model.User;
import com.cmu.model.UserDetails;

public class UserDetailsDaoImpl implements UserDetailsDao{

	public UserDetailsDaoImpl(String username) {
		// TODO Auto-generated constructor stub
	}

	public List<Movie> getUserRatedMovies(String username, Long movieId) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		ResultSet rs;
		String sqlString = "select movieid1 from evaluation where username = ? and movieid2 = ?";
		String sqlString2 = "select movieid2 from evaluation where username = ? and movieid1 = ?";
		List<Long> mlist = new ArrayList<Long>();
		RecommendationDaoImpl rd = new RecommendationDaoImpl();
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, username);
			statement.setLong(2, movieId);
			rs = statement.executeQuery();
			while (rs.next()) {
				mlist.add(rs.getLong("movieid1"));
			}
			statement = conn.prepareStatement(sqlString2);
			statement.setString(1, username);
			statement.setLong(2, movieId);
			rs = statement.executeQuery();
			while (rs.next()) {
				mlist.add(rs.getLong("movieid2"));
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
		return rd.getMovieDatas(mlist);
	}

	public int getNumberOfRatedMovies(String username) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<UserDetails> getNumberOfRatedMoviesPerUser(){
		List<UserDetails> ratingsPerUser = new ArrayList<UserDetails>();
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getConection();
		ResultSet rs;
		String sqlString = "select d.username, count(d.*) as c from (select distinct movieid1, movieid2, username from evaluation where rating = 0 or rating = 1) as d group by username order by c desc";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			rs = statement.executeQuery();
			while (rs.next()) {
				UserDetails ud = new UserDetails();
				User us = new User();
				us.setLogin(rs.getString("username"));
				ud.setUser(us);
				ud.setNumberOfRatedMovies(rs.getInt("c"));
				ratingsPerUser.add(ud);
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
		
		return ratingsPerUser;
	}

}
