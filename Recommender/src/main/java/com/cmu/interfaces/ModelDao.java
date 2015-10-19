package com.cmu.interfaces;

import java.util.List;

import org.grouplens.lenskit.scored.ScoredId;

import com.cmu.enums.Algorithm;

public interface ModelDao {

	public void addToModel(Long id,  List<ScoredId> recommendations,Algorithm alg);
	
	public void deleteModel(Long id,Algorithm alg);
	
	
}
