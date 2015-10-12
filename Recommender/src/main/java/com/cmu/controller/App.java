package com.cmu.controller;

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

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		LenskitConfiguration config = new LenskitConfiguration();

		// Use item-item CF to score items
		// config.bind(ItemScorer.class).to(ItemItemScorer.class);
		// let's use personalized mean rating as the baseline/fallback
		// predictor.
		// 2-step process:
		// First, use the user mean rating as the baseline scorer
		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);
		// config.bind(ItemSimilarity.class).to(ItemVectorSimilarity.class);
		//config.within(ItemVectorSimilarity.class).bind(VectorSimilarity.class).to(PearsonCorrelation.class);

		// Second, use the item mean rating as the base for user means
		// config.bind(UserMeanBaseline.class, ItemScorer.class)
		// .to(ItemMeanRatingItemScorer.class);
		// and normalize ratings by baseline prior to computing similarities
		// config.bind(UserVectorNormalizer.class)
		// .to(BaselineSubtractingUserVectorNormalizer.class);

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
				System.out.println("Movie Id: " + movie.getId() + " , Title : " + movie.getTitle() + " Genre : " + movie.getGenre());
			}

			System.out.println("###############################");

		} catch (RecommenderBuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
