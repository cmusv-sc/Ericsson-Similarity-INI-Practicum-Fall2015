package com.cmu.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.cmu.enums.Algorithm;

public class RecommendationPoolUnit implements Comparable<RecommendationPoolUnit>{
	private Movie movie;
	private List<Algorithm> algorithms;
	private double maxScore;
	

	public RecommendationPoolUnit(Movie movie, List<Algorithm> algorithm, double maxScore) {
		this.movie = movie;
		this.algorithms = algorithm;
		this.maxScore = maxScore;
	}

	public RecommendationPoolUnit(Movie movie, Algorithm algorithm, double score) {
		this.movie = movie;
		this.algorithms = new ArrayList<Algorithm>();
		this.algorithms.add(algorithm);
		this.maxScore = score;	
	}

	public Movie getMovie() {
		return movie;
	}
	public void setMovieID(Movie movie) {
		this.movie = movie;
	}

	public List<Algorithm> getAlgorithms() {
		return algorithms;
	}
	public void setAlgorithms(List<Algorithm> algorithm) {
		this.algorithms = algorithm;
	}
	public void addAlgorithm(Algorithm algorithm){
		this.algorithms.add(algorithm);
	}

	public double getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public int compareTo(RecommendationPoolUnit unit) {
		return 0;		
	}
	
	public static Comparator<RecommendationPoolUnit> movieIdComparator 
	= new Comparator<RecommendationPoolUnit>() {

		public int compare(RecommendationPoolUnit unit1, RecommendationPoolUnit unit2) {

			Long movieId1 = unit1.getMovie().getId();
			Long movieId2 = unit2.getMovie().getId();

			//ascending order
			return movieId1.compareTo(movieId2);

			//descending order
			//return movieId2.compareTo(movieId1);

		}

	};

	public static Comparator<RecommendationPoolUnit> scoreComparator 
	= new Comparator<RecommendationPoolUnit>() {

		public int compare(RecommendationPoolUnit unit1, RecommendationPoolUnit unit2) {

			Double score1 = unit1.getMaxScore();
			Double score2 = unit2.getMaxScore();

			//ascending order
			//return score1.compareTo(score2);

			//descending order
			return score2.compareTo(score1);

		}

	};

}
