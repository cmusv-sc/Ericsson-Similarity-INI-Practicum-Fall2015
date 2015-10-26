package com.cmu.model;

import java.util.List;

public class UserDetails {
	User user;
	
	List<Movie> ratedMovies;
	
	public List<Movie> getRatedMovies() {
		return ratedMovies;
	}
	public User getUser() {
		return user;
	}
	public void setRatedMovies(List<Movie> ratedMovies) {
		this.ratedMovies = ratedMovies;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
