package com.cmu.model;

import com.cmu.enums.Algorithm;

public class Recommendation {

	private Algorithm algorithm;
	
	private double score;
	
	private Movie movie;

	public Recommendation(Algorithm algorithm, double score, Movie movie) {
		super();
		this.algorithm = algorithm;
		this.score = score;
		this.movie = movie;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	
	
	
	
}
