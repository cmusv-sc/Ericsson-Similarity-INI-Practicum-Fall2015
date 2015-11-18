package com.cmu.recommendationEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cmu.enums.Algorithm;
import com.cmu.model.Movie;
import com.cmu.model.RecommendationPoolUnit;

public class RecommendationBuilder {
	private Map<Movie, List<Algorithm>> recommendations;
	private RecommendationsSelector selector;
	private RecommendationPool recommendationPool;
	
	public RecommendationBuilder(Long movieId, List<Algorithm> algorithms) {
		recommendations = new LinkedHashMap<Movie, List<Algorithm>>();
		selector = new RecommendationsSelector(algorithms);
		recommendationPool = new RecommendationPool(movieId, algorithms);
		buildRecommendations(); 
	}
	
	public void setRecommendations(LinkedHashMap<Movie, List<Algorithm>> recommendations) {
		this.recommendations = recommendations;
	}
	public Map<Movie, List<Algorithm>> getRecommendations() {
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
		List<RecommendationPoolUnit> recommendationPoolList = recommendationPool.getRecommendationPool();
		List<RecommendationPoolUnit> toBeRemoved = new ArrayList<RecommendationPoolUnit>();
		for(int cont = 0; cont < 20; cont++){
			Algorithm nextAlgorithm = selector.selectNextAlgorithm();
			if(nextAlgorithm == null)
				return;
			for(int i = 0; i < recommendationPoolList.size(); i++){
				if(recommendationPoolList.get(i).getAlgorithms().contains(nextAlgorithm)){
					Movie movie = recommendationPoolList.get(i).getMovie();
					List<Algorithm> algorithms = new ArrayList<Algorithm>();
					for(RecommendationPoolUnit r : recommendationPoolList){
						if(r.getMovie().getTitle().contains( movie.getTitle())){
							algorithms.addAll(r.getAlgorithms());
							toBeRemoved.add(r);
						}
					}
					recommendations.put(movie, algorithms);
					for(Algorithm a : recommendations.get(movie)){
						if (a.equals(nextAlgorithm))
							continue;
						else
							selector.removeOneRecommendationFromAlgorithm(a);
					}
					recommendationPoolList.removeAll(toBeRemoved);
					if(recommendationPoolList.isEmpty())
						return;
					break;
				}
			}
		}
		
	}

}
