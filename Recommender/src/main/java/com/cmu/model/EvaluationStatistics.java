package com.cmu.model;

import org.codehaus.groovy.classgen.genArrayAccess;

import com.cmu.enums.Algorithm;

public class EvaluationStatistics {
	Algorithm algorithm;
	int falsePositives;
	int totalEvaluations;

	public EvaluationStatistics() {
		// TODO Auto-generated constructor stub
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}
	public int getFalsePositives() {
		return falsePositives;
	}
	public int getTotalEvaluations() {
		return totalEvaluations;
	}
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}
	public void setFalsePositives(int falsePositives) {
		this.falsePositives = falsePositives;
	}
	public void setTotalEvaluations(int totalEvaluations) {
		this.totalEvaluations = totalEvaluations;
	}
}
