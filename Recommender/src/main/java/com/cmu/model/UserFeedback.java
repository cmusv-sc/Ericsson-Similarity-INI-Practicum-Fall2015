package com.cmu.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cmu.enums.Algorithm;

public class UserFeedback {
	
	
	//movieId[0] == Fixed, movieId[1] == selected
	private Long[] movieId = new Long[2];
	
	/* can take values 0-1*/
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

	public List<Long> getMovieIds() {
		return Arrays.asList(movieId);
	}

	public void setMovieIds(Long movieId1,Long movieId2) {
		this.movieId[0] = movieId1;
		this.movieId[1] = movieId2;
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
