package com.cmu.learner;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.grouplens.lenskit.GlobalItemRecommender;
import org.grouplens.lenskit.scored.ScoredId;

import com.cmu.enums.Algorithm;
import com.cmu.interfaces.ModelDao;

public class CosineTask implements Runnable {

	GlobalItemRecommender globalItemRecommender;
	Long id;

	ModelDao modelDao;

	public CosineTask(GlobalItemRecommender globalItemRecommender, Long id, ModelDao modelDao) {
		super();
		this.globalItemRecommender = globalItemRecommender;
		this.id = id;
		this.modelDao = modelDao;
	}

	public void run() {
		Set<Long> items = new HashSet<Long>();
		items.add(id);
		//System.out.println("Cosine: Generating recommendation for movie Id: " + id);
		List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items, 20);
		modelDao.addToModel(id, recommendations, Algorithm.COSINE_SIMILARITY);
	}

}
