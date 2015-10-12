package com.cmu.interfaces;

import java.util.List;

import com.cmu.enums.Algorithm;
import com.cmu.model.ItemScore;

public interface ModelDao {

	public void addToModel(Long id,  List<ItemScore> recommendations,Algorithm alg);
	
	public void deleteModel(Long id,Algorithm alg);
	
	
}
