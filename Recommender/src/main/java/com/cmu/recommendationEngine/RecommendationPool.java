package com.cmu.recommendationEngine;

import java.util.ArrayList;
import java.util.List;

import com.cmu.dao.RecommendationDaoImpl;
import com.cmu.enums.Algorithm;
import com.cmu.model.Movie;
import com.cmu.model.Recommendation;
import com.cmu.model.RecommendationPoolUnit;

public class RecommendationPool {
	private List<RecommendationPoolUnit> recommendationPool;
	RecommendationDaoImpl recommendationDaoImpl;
	
	public RecommendationPool(Long movieId) {
		recommendationPool = new ArrayList<RecommendationPoolUnit>();
		recommendationDaoImpl = new RecommendationDaoImpl();
		buildRecommendationPool(movieId);
	}


	public RecommendationPool(Long movieId, List<Algorithm> algorithms) {
		recommendationPool = new ArrayList<RecommendationPoolUnit>();
		buildRecommendationPool(movieId, algorithms);
		sortPoolByRanking(recommendationPool);
	}

	private void buildRecommendationPool(Long movieId) {
		List<Recommendation> recommendations = null;
		for (Algorithm a : Algorithm.values()){
			recommendations = recommendationDaoImpl.getRecommendation(movieId, a);
			
			System.out.println("#####################" + a.getClass() + "#########");
			int index =0; 
			for (Recommendation rec : recommendations)
			{
				System.out.println(index + ". " + rec.getMovie().getTitle());
				index++;
			}
			System.out.println("#####################" + "END" + "#########");
			
			merge(generatePool(recommendations));
		}
	}

	private void buildRecommendationPool(Long movieId, List<Algorithm> algorithms) {
		List<Recommendation> recommendations = null;
		for (Algorithm a : algorithms){
			recommendations = recommendationDaoImpl.getRecommendation(movieId, a);
			merge(generatePool(recommendations));
		}		
	}

	private List<RecommendationPoolUnit> generatePool(List<Recommendation> recommendations) {
		List<RecommendationPoolUnit> pool = new ArrayList<RecommendationPoolUnit>();
		for(Recommendation r : recommendations)
			pool.add(exportRecommendationToUnit(r));
		return pool;
	}


	private RecommendationPoolUnit exportRecommendationToUnit(Recommendation recommendation) {
		RecommendationPoolUnit unit = new RecommendationPoolUnit(recommendation.getMovie(), recommendation.getAlgorithm(), recommendation.getScore());
		return unit;
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

	public void addRecommendation(Movie movie, Algorithm algorithm, double score){
		for (RecommendationPoolUnit unit : recommendationPool){
			if(unit.getMovie().getId() == movie.getId())
				if(!unit.getAlgorithms().contains(algorithm)){
					unit.addAlgorithm(algorithm);
					if(unit.getMaxScore() < score)
						unit.setMaxScore(score);
					return;
				}
		}

		recommendationPool.add(new RecommendationPoolUnit(movie, algorithm, score));
	}

	public List<RecommendationPoolUnit> merge(List<RecommendationPoolUnit> otherPool){
		//iterator for recommendationPool
		int i = 0;
		//iterator for otherPool
		int j = 0;

		if(otherPool.isEmpty())
			return recommendationPool;
		if(recommendationPool.isEmpty()){
			recommendationPool = otherPool;
			return recommendationPool;
		}
			
		
		List<RecommendationPoolUnit> mergedPool = new ArrayList<RecommendationPoolUnit>();

		while (i < recommendationPool.size() && j < otherPool.size()){
			if(i >= recommendationPool.size()){
				mergedPool.add(otherPool.get(j));
				j++;
			}
			else if (j >= otherPool.size()){
				mergedPool.add(recommendationPool.get(i));
				i++;
			}
			else {
				if(recommendationPool.get(i).getMovie().getId() == otherPool.get(j).getMovie().getId()){
					if (recommendationPool.get(i).getMaxScore() > otherPool.get(j).getMaxScore())
						mergedPool.add(recommendationPool.get(i));
					else
						mergedPool.add(otherPool.get(j));
					i++;
					j++;
				}
				else if (recommendationPool.get(i).getMovie().getId() < otherPool.get(j).getMovie().getId()){
					mergedPool.add(recommendationPool.get(i));
					i++;
				}
				else{
					mergedPool.add(otherPool.get(j));
					j++;
				}
			}
		}
		recommendationPool = new ArrayList<RecommendationPoolUnit>(mergedPool);
		return recommendationPool;
	}

	public void sortPoolByMovieId(List<RecommendationPoolUnit> pool){
		pool.sort(RecommendationPoolUnit.movieIdComparator);	
	}

	public void sortPoolByRanking(List<RecommendationPoolUnit> pool){
		pool.sort(RecommendationPoolUnit.scoreComparator);	
	}
}
