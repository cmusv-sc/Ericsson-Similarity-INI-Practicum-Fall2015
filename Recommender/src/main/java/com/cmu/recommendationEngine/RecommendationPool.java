package com.cmu.recommendationEngine;

import java.util.ArrayList;
import java.util.List;

public class RecommendationPool {
	private List<RecommendationPoolUnit> recommendationPool;
	
	public RecommendationPool() {
		recommendationPool = new ArrayList<RecommendationPoolUnit>();
	}
	
	public List<RecommendationPoolUnit> getRecommendationPool() {
		return recommendationPool;
	}
	
	public void setRecommendationPool(List<RecommendationPoolUnit> recommendationPool) {
		this.recommendationPool = recommendationPool;
	}
	
	public void addUnit(RecommendationPoolUnit unit){
		recommendationPool.add(unit);
	}
	public void addRecommendation(long movieId, String algorithm, double score){
		for (RecommendationPoolUnit unit : recommendationPool){
			if(unit.getMovieID() == movieId)
				if(!unit.getAlgorithms().contains(algorithm)){
					unit.addAlgorithm(algorithm);
					if(unit.getMaxScore() < score)
						unit.setMaxScore(score);
					return;
				}
		}
		
		recommendationPool.add(new RecommendationPoolUnit(movieId, algorithm, score));
	}
	
	public List<RecommendationPoolUnit> merge(List<RecommendationPoolUnit> otherPool){
		//iterator for recommendationPool
		int i = 0;
		//iterator for otherPool
		int j = 0;
		
		List<RecommendationPoolUnit> mergedPool = new ArrayList<RecommendationPoolUnit>();
		
		while (i < recommendationPool.size() && j < otherPool.size()){
			if(i >= recommendationPool.size()){
				mergedPool.add(otherPool.get(j);
				j++;
			}
			else if (j >= otherPool.size()){
				mergedPool.add(recommendationPool.get(i));
				i++;
			}
			else {
				if(recommendationPool.get(i).getMovieID() == otherPool.get(j).getMovieID()){
					
				}
			}
		}
		return recommendationPool;
	}
	
	public void sortPoolByMovieId(List<RecommendationPoolUnit> pool){
		pool.sort(RecommendationPoolUnit.movieIdComparator);	
	}
	
	public void sortPoolByRanking(List<RecommendationPoolUnit> pool){
		pool.sort(RecommendationPoolUnit.scoreComparator);	
	}
}
