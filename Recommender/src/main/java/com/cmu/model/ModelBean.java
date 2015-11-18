package com.cmu.model;

import java.util.List;

import org.grouplens.lenskit.scored.ScoredId;

public class ModelBean {

	
	List<ScoredId> recommendations;
	
	Long id;
	
	public ModelBean(List<ScoredId> recommendations, Long id) {
		super();
		this.recommendations = recommendations;
		this.id = id;
	}

	public List<ScoredId> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<ScoredId> recommendations) {
		this.recommendations = recommendations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
}
