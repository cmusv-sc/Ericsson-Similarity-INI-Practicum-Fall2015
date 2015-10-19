package com.cmu.recommendationEngine;

import java.util.HashMap;
import java.util.List;

import com.cmu.enums.Algorithm;

public class RecommendationsSelector {
	private HashMap <Algorithm, Integer> recommendationsPerAlgorithm;
	private int defaultNumberOfRecommendations = 20;

	public RecommendationsSelector() {
		recommendationsPerAlgorithm = new HashMap<Algorithm, Integer>();
		buildRecommendationsPerAlgorithm();
	}
	public RecommendationsSelector(List<Algorithm> algorithms) {
		recommendationsPerAlgorithm = new HashMap<Algorithm, Integer>();
		buildRecommendationsPerAlgorithm(algorithms);
	}

	private void buildRecommendationsPerAlgorithm() {
		for(Algorithm a : Algorithm.values()){
			addAlgorithm(a);
		}
	}

	private void buildRecommendationsPerAlgorithm(List<Algorithm> algorithms) {
		for(Algorithm a : algorithms){
			addAlgorithm(a);
		}
	}

	public HashMap<Algorithm, Integer> getRecommendationsPerAlgorithm() {
		return recommendationsPerAlgorithm;
	}
	public void setRecommendationsPerAlgorithm(HashMap<Algorithm, Integer> recommendationsPerAlgorithm) {
		this.recommendationsPerAlgorithm = recommendationsPerAlgorithm;
	}

	public int getDefaultNumberOfRecommendations() {
		return defaultNumberOfRecommendations;
	}
	public void setDefaultNumberOfRecommendations(int defaultNumberOfRecommendations) {
		this.defaultNumberOfRecommendations = defaultNumberOfRecommendations;
	}

	public void addAlgorithm(Algorithm algorithm){
		recommendationsPerAlgorithm.put(algorithm, defaultNumberOfRecommendations);
	}

	public void removeOneRecommendationFromAlgorithm(Algorithm algorithm){
		int newNumber = recommendationsPerAlgorithm.get(algorithm)-1;
		recommendationsPerAlgorithm.replace(algorithm, newNumber);
	}

	public Algorithm selectNextAlgorithm(){
		Algorithm next = null;
		int biggerValue = 0;
		for(Algorithm temp : Algorithm.values()){
			if(recommendationsPerAlgorithm.get(temp) > biggerValue){
				biggerValue = recommendationsPerAlgorithm.get(temp);
				next = temp;
			}
		}
		if (biggerValue == 0)
			return null;
		else{
			removeOneRecommendationFromAlgorithm(next);
			return next;
		}
	}

}
