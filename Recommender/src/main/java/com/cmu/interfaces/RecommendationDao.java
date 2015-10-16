package com.cmu.interfaces;

import java.util.List;

import com.cmu.enums.Algorithm;
import com.cmu.model.Recommendation;

public interface RecommendationDao {

	public List<Recommendation> getRecommendation(Long id, Algorithm alg); 
	
}
