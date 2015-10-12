package com.cmu.recommendationEngine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecommendationPoolUnit implements Comparable<RecommendationPoolUnit>{
	private long movieID;
	private List<String> algorithms;
	private double maxScore;

	public RecommendationPoolUnit(long movieId, List<String> algorithm, double maxScore) {
		this.movieID = movieId;
		this.algorithms = algorithm;
		this.maxScore = maxScore;
	}

	public RecommendationPoolUnit(long movieId, String algorithm, double score) {
		this.movieID = movieId;
		this.algorithms = new ArrayList<String>();
		this.algorithms.add(algorithm);
		this.maxScore = score;	
	}

	public long getMovieID() {
		return movieID;
	}
	public void setMovieID(long movieID) {
		this.movieID = movieID;
	}

	public List<String> getAlgorithms() {
		return algorithms;
	}
	public void setAlgorithms(List<String> algorithm) {
		this.algorithms = algorithm;
	}
	public void addAlgorithm(String algorithm){
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

			Long movieId1 = unit1.getMovieID();
			Long movieId2 = unit2.getMovieID();

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
			return score1.compareTo(score2);

			//descending order
			//return ranking1.compareTo(ranking2);

		}

	};

}
