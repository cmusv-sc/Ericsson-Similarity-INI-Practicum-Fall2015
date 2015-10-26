package com.cmu.interfaces;

import java.util.List;

import com.cmu.model.Movie;

public interface UserDetailsDao {

	public List<Movie> getUserRatedMovies(int userId);
	
	public int getNumberOfRatedMovies(int userId);
}
