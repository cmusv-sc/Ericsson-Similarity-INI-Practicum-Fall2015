package com.cmu.services;


import com.cmu.interfaces.UserDao;
import com.cmu.interfaces.UserService;
import com.cmu.model.User;

public class UserServiceImpl implements UserService {
    
    private UserDao userDao;
 
    public User getUser(String login) {
        return userDao.getUser(login);
    }
 
}