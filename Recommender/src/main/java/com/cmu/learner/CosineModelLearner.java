package com.cmu.learner;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.grouplens.lenskit.GlobalItemRecommender;
import org.grouplens.lenskit.GlobalItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAOBuilder;
import org.grouplens.lenskit.knn.item.ItemItemGlobalScorer;
import org.grouplens.lenskit.scored.ScoredId;

import com.cmu.dao.DBConnection;
import com.cmu.dao.Movie;
import com.cmu.dao.MovieDao;
import com.cmu.interfaces.OfflineLearner;

public class CosineModelLearner implements OfflineLearner {

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

			config.addComponent(dao);
			LenskitRecommender rec = LenskitRecommender.build(config);

			GlobalItemRecommender globalItemRecommender = rec.getGlobalItemRecommender();

			Set<Long> items = new HashSet<Long>();
			items.add(1l);
			List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items, 20);

			MovieDao movieDao = new MovieDao();
			movieDao.getMoviesByIds(recommendations);
			for (Movie movie : movieDao.getMoviesByIds(recommendations)) {
				System.out.println("Movie Id: " + movie.getId() + " , Title : " + movie.getTitle() + " Genre : "
						+ movie.getGenre());
			}

			System.out.println("###############################");

		} catch (RecommenderBuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
