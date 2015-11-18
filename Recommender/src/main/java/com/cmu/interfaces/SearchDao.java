package com.cmu.interfaces;

import java.util.List;

import com.cmu.model.Movie;

public interface SearchDao {

	public List<Movie> findExactMatchRegex(String token);
}
