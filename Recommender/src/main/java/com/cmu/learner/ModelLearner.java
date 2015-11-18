package com.cmu.learner;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.grouplens.lenskit.GlobalItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.core.LenskitRecommenderEngine;
import org.grouplens.lenskit.core.ModelDisposition;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAOBuilder;
import org.grouplens.lenskit.knn.item.ItemItemGlobalScorer;
import org.grouplens.lenskit.knn.item.ItemVectorSimilarity;
import org.grouplens.lenskit.vectors.similarity.PearsonCorrelation;
import org.grouplens.lenskit.vectors.similarity.VectorSimilarity;

import com.cmu.dao.DBConnection;

public class ModelLearner {

	public static void main(String[] args) {
		LenskitConfiguration config = new LenskitConfiguration();

		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);
		config.within(ItemVectorSimilarity.class).bind(VectorSimilarity.class).to(PearsonCorrelation.class);
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
			LenskitRecommenderEngine engine =
				    LenskitRecommenderEngine.newBuilder()
				                            .addConfiguration(config)
				                            .addConfiguration(config,
				                                              ModelDisposition.EXCLUDED)
				                            .build();
			
			LenskitRecommender rec = engine.createRecommender(config);
			
			engine.write(new File("/Users/ivash/Documents/workspace/Ericsson-Similarity-INI-Practicum-Fall2015/Recommender/pearson_model.bin"));
			//LenskitRecommender rec = LenskitRecommender.build(config);

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
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
