package com.cmu.recommendationEngine;

import java.util.HashMap;
import java.util.List;

import com.cmu.enums.Algorithm;
import com.cmu.model.Movie;
import com.cmu.model.RecommendationPoolUnit;

public class RecommendationBuilder {
	private HashMap<Movie, List<Algorithm>> recommendations;
	private RecommendationsSelector selector;
	private RecommendationPool recommendationPool;
	
	public RecommendationBuilder(Long movieId) {
		recommendations = new HashMap<Movie, List<Algorithm>>();
		selector = new RecommendationsSelector();
		recommendationPool = new RecommendationPool(movieId);
		buildRecommendations();
	}
	
	public void setRecommendations(HashMap<Movie, List<Algorithm>> recommendations) {
		this.recommendations = recommendations;
	}
	public HashMap<Movie, List<Algorithm>> getRecommendations() {
		return recommendations;
	}
	public RecommendationPool getRecommendationPool() {
		return recommendationPool;
	}
	public void setRecommendationPool(RecommendationPool recommendationPool) {
		this.recommendationPool = recommendationPool;
	}
	public RecommendationsSelector getSelector() {
		return selector;
	}
	public void setSelector(RecommendationsSelector selector) {
		this.selector = selector;
	}
	
	private void buildRecommendations(){
		Algorithm nextAlgorithm = selector.selectNextAlgorithm();
		
		
		
	}

}
