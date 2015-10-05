package com.cmu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.grouplens.lenskit.GlobalItemRecommender;
import org.grouplens.lenskit.GlobalItemScorer;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.data.sql.JDBCRatingDAOBuilder;
import org.grouplens.lenskit.knn.item.ItemItemGlobalScorer;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.unimi.dsi.fastutil.longs.LongSet;

@Controller
public class RecommenderController {

	@RequestMapping("/itemSimilarity")
	public ModelAndView searchItemSimilarities(@RequestParam(value = "item", required = false, defaultValue = "1") String item) { 

		List<ScoredId> recommendations = getItems(item);
		ModelAndView mv = new ModelAndView("itemSimilarity");
		mv.addObject("recommendations", recommendations);
		mv.addObject("item", item);
		return mv;
	}

	private List<ScoredId> getItems(String item) {


		LenskitConfiguration config = new LenskitConfiguration();

		// Use item-item CF to score items
		config.bind(ItemScorer.class).to(ItemItemScorer.class);
		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);
		Connection conn = null;

		Properties connectionProps = new Properties();
		connectionProps.put("user", "ini");
		connectionProps.put("password", "a12345");

		Statement stmt = null;
		String query = "select * from `ratings` limit 10";


		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());

			conn = DriverManager.getConnection(
					"jdbc:" + "mysql" + "://" +
							"52.10.143.249" +
							":" + "3306" + "/Ericssonsmall",
							connectionProps);

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

			Set<Long> items = new HashSet<Long>();
			items.add(Long.parseLong(item));
			List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items, 10);

			return recommendations;

			

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RecommenderBuildException e) {
			e.printStackTrace();
		}
		return null;
	}

}
