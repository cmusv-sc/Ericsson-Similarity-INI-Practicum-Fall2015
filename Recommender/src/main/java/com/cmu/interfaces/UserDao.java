package com.cmu.interfaces;

import com.cmu.model.User;

public interface UserDao {

    public User getUser(String username);
	public void updatePassword(String username, String hashedPassword);	
}
