package com.cmu.model;

import java.util.List;

public class UserDetails {
	User user;
	
	int numberOfRatedMovies;
	
	public int getNumberOfRatedMovies() {
		return numberOfRatedMovies;
	}
	public void setNumberOfRatedMovies(int numberOfRatedMovies) {
		this.numberOfRatedMovies = numberOfRatedMovies;
	}
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
}
