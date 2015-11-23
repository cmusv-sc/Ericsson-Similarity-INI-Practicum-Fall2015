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
		List<User> result = new ArrayList<User>();
		
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getUserconn();
		ResultSet rs;
		String sqlString = "select a.username, a.password, b.role from users a inner join user_roles b on (a.username = b.username);";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			rs = statement.executeQuery();
			while (rs.next()) {
				User u = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
				result.add(u);
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
		
		return result;
	}

	public void deleteUser(String username) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getUserconn();
		
		String sqlString = "update users set enabled = 0 where username = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString);
			statement.setString(1, username);
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

	public boolean addUser(User user) {
		try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			DriverManager.registerDriver(new org.postgresql.Driver ());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = DBConnection.getUserconn();
		ResultSet rs;	
		boolean res = true;
		
		String sqlString = "insert into users (username,password,enabled) values (?,?,?)";
		String sqlString2 = "insert into user_roles (username,role) values (?,?)";
		String sqlString3 = "select enabled from users where username = ?";
		String sqlString4 = "update users set password = ?, enabled = 1 where username = ?";
		String sqlString5 = "update user_roles set role = ? where username = ?";
		try {
			PreparedStatement statement = conn.prepareStatement(sqlString3);
			statement.setString(1, user.getLogin());
			rs = statement.executeQuery();
			if (rs.next()) {
				if(rs.getLong("enabled") == 1) //already registered
					res = false;  
				else { //already disabled, enable it
					statement = conn.prepareStatement(sqlString4);
					statement.setString(1, user.getPassword());
					statement.setString(2, user.getLogin());
					statement.executeUpdate();
					
					statement = conn.prepareStatement(sqlString5);
					statement.setString(1, user.getRole());
					statement.setString(2, user.getLogin());
					statement.executeUpdate();
				}
			}
			else { //create new user
				statement = conn.prepareStatement(sqlString);
				statement.setString(1, user.getLogin());
				statement.setString(2, user.getPassword());
				statement.setLong(3, 1);
				statement.executeUpdate();
				
				statement = conn.prepareStatement(sqlString2);
				statement.setString(1, user.getLogin());
				statement.setString(2, user.getRole());
				statement.executeUpdate();
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
		
		return res;
		
	}
 
    
}

