package com.cmu.model;

public class ItemScore {

	private Long id;
	
	private double score;
	
	

	public ItemScore(Long id, double score) {
		super();
		this.id = id;
		this.score = score;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	
	
}
