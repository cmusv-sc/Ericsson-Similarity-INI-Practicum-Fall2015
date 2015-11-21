package com.cmu.model;

public class IDScoreTuple implements Comparable<IDScoreTuple> {

	public String id;
	public Double score;

	
	
	public IDScoreTuple(String id, Double score) {
		super();
		this.id = id;
		this.score = score;
	}



	//@Override
	public int compareTo(IDScoreTuple arg0) {
		// TODO Auto-generated method stub
		return this.score.compareTo(arg0.score);
	}
}
