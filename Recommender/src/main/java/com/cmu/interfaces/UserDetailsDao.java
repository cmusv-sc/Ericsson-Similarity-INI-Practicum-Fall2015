package com.cmu.interfaces;

import java.util.List;

import com.cmu.model.Movie;

public interface UserDetailsDao {

	public List<Movie> getUserRatedMovies(String username, Long movieId);
	
	public int getNumberOfRatedMovies(String username);
}
