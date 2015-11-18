package com.cmu.learner;

import java.sql.Connection;
import java.sql.SQLException;
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
import org.grouplens.lenskit.knn.item.ItemVectorSimilarity;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.vectors.similarity.PearsonCorrelation;
import org.grouplens.lenskit.vectors.similarity.VectorSimilarity;

import com.cmu.dao.DBConnection;
import com.cmu.dao.ModelDaoImpl;
import com.cmu.dao.MovieDao;
import com.cmu.enums.Algorithm;
import com.cmu.interfaces.ModelDao;
import com.cmu.interfaces.OfflineLearner;
import com.cmu.model.Movie;

public class PearsonCorelationModelLearner implements OfflineLearner {

	ModelDao modelDao = new ModelDaoImpl();

	public static void main(String[] args) {
		PearsonCorelationModelLearner learner = new PearsonCorelationModelLearner();
		learner.learn();
	}

	public void learn() {
		LenskitConfiguration config = new LenskitConfiguration();

		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);
		config.within(ItemVectorSimilarity.class).bind(VectorSimilarity.class).to(PearsonCorrelation.class);
		try {
			Connection conn = DBConnection.getConection();

			JDBCRatingDAOBuilder jdbcDaoBuilder = JDBCRatingDAO.newBuilder();
			jdbcDaoBuilder.setTableName("test_ratings");
			jdbcDaoBuilder.setItemColumn("movieId");
			jdbcDaoBuilder.setUserColumn("userId");
			jdbcDaoBuilder.setTimestampColumn("timestamp");
			jdbcDaoBuilder.setRatingColumn("rating");

			JDBCRatingDAO dao = jdbcDaoBuilder.build(conn);

			config.addComponent(dao);
			LenskitRecommender rec = LenskitRecommender.build(config);

			GlobalItemRecommender globalItemRecommender = rec.getGlobalItemRecommender();

			MovieDao movieDao = new MovieDao();

			for (Long Id : movieDao.getAllMovieIds()) {

				if (Id != null) {
					Set<Long> items = new HashSet<Long>();
					items.add(Id);
					List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items, 20);

					System.out.println("Movie Id: " + Id );
					modelDao.addToModel(Id, recommendations, Algorithm.PEARSON_COEFFICIENT);
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
