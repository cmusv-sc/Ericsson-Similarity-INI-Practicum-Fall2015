package com.cmu.learner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.grouplens.lenskit.GlobalItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAOBuilder;
import org.grouplens.lenskit.knn.item.ItemItemGlobalScorer;
import org.grouplens.lenskit.knn.item.ItemVectorSimilarity;
import org.grouplens.lenskit.vectors.similarity.PearsonCorrelation;
import org.grouplens.lenskit.vectors.similarity.VectorSimilarity;

import com.cmu.dao.DBConnection;
import com.cmu.dao.ModelDaoImpl;
import com.cmu.dao.MovieDao;
import com.cmu.interfaces.ModelDao;
import com.cmu.interfaces.OfflineLearner;
import com.cmu.model.CosineProducer;
import com.cmu.model.ModelBean;

public class CosineModelLearner implements OfflineLearner {

	ModelDao modelDao = new ModelDaoImpl();

	public static void main(String[] args) {
		CosineModelLearner learner = new CosineModelLearner();
		learner.learn();
	}

	public void learn() {
		LenskitConfiguration config = new LenskitConfiguration();

		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);
		//config.within(ItemVectorSimilarity.class).bind(VectorSimilarity.class).to(PearsonCorrelation.class);
		try {
			Connection conn = DBConnection.getConection();

			JDBCRatingDAOBuilder jdbcDaoBuilder = JDBCRatingDAO.newBuilder();
			jdbcDaoBuilder.setTableName("ratings");
			jdbcDaoBuilder.setItemColumn("movieId");
			jdbcDaoBuilder.setUserColumn("userId");
			jdbcDaoBuilder.setTimestampColumn("timestamp");
			jdbcDaoBuilder.setRatingColumn("rating");

			JDBCRatingDAO dao = jdbcDaoBuilder.build(conn);

			System.out.println("The New OnlyCosine/Pearson Similarity learner started");
			long startTime = System.currentTimeMillis();
			config.addComponent(dao);
			LenskitRecommender rec = LenskitRecommender.build(config);

			// GlobalItemRecommender globalItemRecommender =
			// rec.getGlobalItemRecommender();

			int producerCount = 4;
			int consumerCount = 3;

			List<List<Long>> idLists = new ArrayList<List<Long>>();

			for (int i = 0; i < producerCount; i++) {
				idLists.add(new ArrayList<Long>());
			}

			MovieDao movieDao = new MovieDao();

			List<Long> allMovieIds = movieDao.getAllMovieIds();
			int index = 0;
			for (Long mid : allMovieIds) {

				idLists.get(index).add(mid);
				index = (index + 1) % producerCount;

			}

			CountDownLatch latch = new CountDownLatch(producerCount);
			BlockingQueue<ModelBean> priorityBlockingQueue = new LinkedBlockingQueue<ModelBean>();

			for (int i = 0; i < producerCount; i++) {

				Thread thread = new Thread(new CosineProducer(rec, priorityBlockingQueue, latch, idLists.get(i)));
				thread.start();
			}

			for (int i = 0; i < consumerCount; i++) {

				Thread thread = new Thread(new CosineTask(i, latch, priorityBlockingQueue));
				thread.start();
			}

			
			latch.await();
			
			System.out.println("###############################" + (System.currentTimeMillis() - startTime));
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (RecommenderBuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
