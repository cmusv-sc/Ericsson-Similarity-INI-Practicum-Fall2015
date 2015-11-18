package com.cmu.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.grouplens.lenskit.GlobalItemRecommender;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.scored.ScoredId;

public class CosineProducer implements Runnable {

	LenskitRecommender rec;
	
	BlockingQueue<ModelBean> queue;
	
	CountDownLatch latch;
	
	List<Long> ids;

	public CosineProducer(LenskitRecommender rec, BlockingQueue<ModelBean> queue, CountDownLatch latch,
			List<Long> ids) {
		super();
		this.rec = rec;
		this.queue = queue;
		this.latch = latch;
		this.ids = ids;
	}

	public void run() {
		// TODO Auto-generated method stub
		
		GlobalItemRecommender globalItemRecommender = rec.getGlobalItemRecommender();
		
		int index =1;
		for (Long id : ids) {
			if (id != null) {
				Set<Long> items = new HashSet<Long>();
				items.add(id);
				List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items, 20);

				if(index %100 ==0)
				{
					System.out.println("producing" +index +" " + Thread.currentThread().getName());
				}
				queue.offer(new ModelBean(recommendations, id));
				index++;
			}
		}
		
		latch.countDown();
		
	}
	
	
	
	
}
