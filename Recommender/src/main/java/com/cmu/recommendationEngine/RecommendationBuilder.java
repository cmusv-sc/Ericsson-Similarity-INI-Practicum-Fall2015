package com.cmu.recommendationEngine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cmu.enums.Algorithm;
import com.cmu.model.Movie;
import com.cmu.model.RecommendationPoolUnit;

//Class that controls which recommendations will be shown to the user.
//Implements a sort of Round Robin to be fair to all the algorithms.
public class RecommendationBuilder {
	private Map<Movie, List<Algorithm>> recommendations;
	private RecommendationsSelector selector;
	private RecommendationPool recommendationPool;
	
	public RecommendationBuilder(Long movieId, List<Algorithm> algorithms) {
		recommendations = new LinkedHashMap<Movie, List<Algorithm>>();
		//Build recommendation pool with all the recommendations from all the algorithms
		recommendationPool = new RecommendationPool(movieId, algorithms);
		List<Algorithm> algorithmsUsed = new ArrayList<Algorithm>();
		
		//If an algorithm from "algorithms" does not have any recommendations, then it is removed form the
		//recommendation engine
		for(int index = 0; index < recommendationPool.getRecommendationPool().size(); index++){
			Algorithm a = recommendationPool.getRecommendationPool().get(index).getAlgorithms().get(0);
			if(!algorithmsUsed.contains(a))
				algorithmsUsed.add(a);
		}
		
		//initialize the selector with only the algorithms that are bein used
		selector = new RecommendationsSelector(algorithmsUsed);
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
		
		//Gets the 20 best recommendations
		for(int cont = 0; cont < 20; cont++){
			//Selector controls which algorithm it is fair to get the next movie from
			Algorithm nextAlgorithm = selector.selectNextAlgorithm();
			if(nextAlgorithm == null)
				return;
			
			//iterate over the pool of all recommendations searching for the next movie for the nextAlgorithm
			for(int i = 0; i < recommendationPoolList.size(); i++){
				if(recommendationPoolList.get(i).getAlgorithms().contains(nextAlgorithm)){
					Movie movie = recommendationPoolList.get(i).getMovie();
					List<Algorithm> algorithms = new ArrayList<Algorithm>();
					for(RecommendationPoolUnit r : recommendationPoolList){
						//Add the movie to the recommendations
						if(r.getMovie().getTitle().contentEquals( movie.getTitle())){
							algorithms.addAll(r.getAlgorithms());
							//Add all the algorithms that recommended the movie to be removed
							//so it will be fair with all the algorithms
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
