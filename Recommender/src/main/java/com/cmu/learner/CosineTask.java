package com.cmu.learner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.grouplens.lenskit.scored.ScoredId;

import com.cmu.enums.Algorithm;
import com.cmu.model.ModelBean;

public class CosineTask implements Runnable {

	int fileId;

	List<Long> ids;

	BlockingQueue<ModelBean> queue;
	CountDownLatch latch;

	public CosineTask(int fileId, CountDownLatch latch, BlockingQueue<ModelBean> queue) {
		super();
		this.queue = queue;
		this.fileId = fileId;
		this.latch = latch;
	}

	public void run() {

		// System.out.println("Cosine: Generating recommendation for movie Id: "
		// + id);

		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(new File("/Users/ivash/rough/pearson/cosine_t1" + fileId + ".csv")), 8192 * 50);

			int index = 1;
			while (latch.getCount() > 0) {

				ModelBean model = queue.poll();

				if (model != null) {
					Long id = model.getId();
					List<ScoredId> recommendations = model.getRecommendations();
					StringBuilder s = new StringBuilder();
					for (int i = 0; i < recommendations.size(); i++) {
						s.append(recommendations.get(i).getId());
						s.append(",");
						s.append(recommendations.get(i).getScore());
						s.append(",");
					}
					if (index % 100 == 0) {
						System.out.println(index + "Consumer" + fileId + " " + Thread.currentThread().getName());
					}
					writer.write(id + "\t" + Algorithm.COSINE_SIMILARITY.toString() + "\t" + s.toString() + "\n");
					index++;
				}

			}

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
