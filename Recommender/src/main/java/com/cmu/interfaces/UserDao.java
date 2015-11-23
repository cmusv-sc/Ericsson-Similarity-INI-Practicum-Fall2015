package com.cmu.interfaces;

import java.util.List;

import com.cmu.model.User;

public interface UserDao {

    public User getUser(String username);
	public void updatePassword(String username, String hashedPassword);	
	public List<User> getUsers();
	public void deleteUser(String username);
	public boolean addUser(User user);
}
