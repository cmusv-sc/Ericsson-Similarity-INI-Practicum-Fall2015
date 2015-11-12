package com.cmu.learner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.grouplens.lenskit.GlobalItemRecommender;
import org.grouplens.lenskit.GlobalItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAOBuilder;
import org.grouplens.lenskit.knn.item.ItemItemGlobalScorer;

import com.cmu.dao.DBConnection;
import com.cmu.dao.ModelDaoImpl;
import com.cmu.dao.MovieDao;
import com.cmu.interfaces.ModelDao;
import com.cmu.interfaces.OfflineLearner;

public class CosineModelLearner implements OfflineLearner {

	ModelDao modelDao = new ModelDaoImpl();

	public static void main(String[] args) {
		CosineModelLearner learner = new CosineModelLearner();
		learner.learn();
	}

	public void learn() {
		LenskitConfiguration config = new LenskitConfiguration();

		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);

		try {
			Connection conn = DBConnection.getConection();

			JDBCRatingDAOBuilder jdbcDaoBuilder = JDBCRatingDAO.newBuilder();
			jdbcDaoBuilder.setTableName("ratings");
			jdbcDaoBuilder.setItemColumn("movieId");
			jdbcDaoBuilder.setUserColumn("userId");
			jdbcDaoBuilder.setTimestampColumn("timestamp");
			jdbcDaoBuilder.setRatingColumn("rating");

			JDBCRatingDAO dao = jdbcDaoBuilder.build(conn);

			System.out.println("Cosine Similarity learner started");
			long startTime = System.currentTimeMillis();
			config.addComponent(dao);
			LenskitRecommender rec = LenskitRecommender.build(config);

			GlobalItemRecommender globalItemRecommender = rec.getGlobalItemRecommender();

			LinkedBlockingQueue<Runnable> priorityBlockingQueue = new LinkedBlockingQueue<Runnable>();

			ExecutorService threadService = new ThreadPoolExecutor(20, 20, 10, TimeUnit.MINUTES, priorityBlockingQueue,
					new ThreadPoolExecutor.CallerRunsPolicy());

			MovieDao movieDao = new MovieDao();

			int i = 0;
			for (Long Id : movieDao.getAllMovieIds()) {

				if (Id != null) {
					CosineTask task = new CosineTask(globalItemRecommender, Id, modelDao);

					threadService.execute(task);
					// Set<Long> items = new HashSet<Long>();
					// items.add(Id);
					// System.out.println("Cosine: Generating recommendation for
					// movie Id: " + Id);
					// List<ScoredId> recommendations =
					// globalItemRecommender.globalRecommend(items, 20);
					//
					// modelDao.addToModel(Id, recommendations,
					// Algorithm.COSINE_SIMILARITY);
				}
				i++;

				if (i % 100 == 0) {
					System.out.println(System.currentTimeMillis() - startTime);
				}
			}

			System.out.println("###############################");
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (RecommenderBuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
