package com.cmu.model;

import java.util.Map;

import com.cmu.enums.Algorithm;

public class UserFeedback {
	
	private Long movieId;
	
	/* can take values 1-5*/
	private int rating;
	
	/*
	 *  0 - have Seen
	 *  1 - have't Seen
	 *  -1 -- no response from user
	 */
	private int haveSeen;
	
	/* 
	 * All algorithms which predicted this movie and their scores
	 */
	private Map<Algorithm,Double> algorithmScores;

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getHaveSeen() {
		return haveSeen;
	}

	public void setHaveSeen(int haveSeen) {
		this.haveSeen = haveSeen;
	}

	public Map<Algorithm, Double> getAlgorithmScores() {
		return algorithmScores;
	}

	public void setAlgorithmScores(Map<Algorithm, Double> algorithmScores) {
		this.algorithmScores = algorithmScores;
	}
	

}
