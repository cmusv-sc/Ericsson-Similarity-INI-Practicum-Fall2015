package com.cmu.interfaces;

import java.util.List;

import com.cmu.model.Movie;

public interface TmdbSimilarityDao {

	public List<Movie> getSimilarMovies(Movie movie);
}
