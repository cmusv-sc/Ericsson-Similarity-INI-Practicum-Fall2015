package com.cmu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cmu.enums.Algorithm;
import com.cmu.interfaces.UserDao;
import com.cmu.model.User;

public class UserDaoImpl implements UserDao {
    
 
    public User getUser(String username) {
          return null;
    }

	public void updatePassword(String username, String hashedPassword) {		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getUserconn();
		
		String sqlString = "update users set password = ? where username = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, hashedPassword);
			statement.setString(2, username);
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

	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}
 
    
}

