package com.cmu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cmu.dao.DBConnection;
import com.cmu.dao.Movie;
import com.cmu.dao.MovieDao;

@Controller
public class RecommenderController {

	@RequestMapping("/itemSimilarity")
	public ModelAndView searchItemSimilarities(@RequestParam(value = "item", required = false, defaultValue = "1") String item,
			@RequestParam(value = "algorithm", required = false, defaultValue = "") String algorithm) { 
		MovieDao movieDao = new MovieDao();
		List<Movie> recommendations = getItems(item, algorithm);
		ModelAndView mv = new ModelAndView("itemSimilarity");
		List<Long> movieIds = new ArrayList<Long>();
		List<String> movieTitles = new ArrayList<String>();
		for(int i = 0; i < recommendations.size(); i++){
			movieIds.add(i, recommendations.get(i).getId());
			movieTitles.add(i, recommendations.get(i).getTitle());
		}
		
		mv.addObject("movieIds", movieIds);
		mv.addObject("movieTitles", movieTitles);
		mv.addObject("selectedMovieTitle", movieDao.getMovieById(Long.parseLong(item)).getTitle());
		mv.addObject("item", item);
		return mv;
	}

	private List<Movie> getItems(String item, String algorithm) {

		System.out.println(item);
		LenskitConfiguration config = new LenskitConfiguration();
		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);
		if(algorithm != null && algorithm.contentEquals("Pearson"))
			config.within(ItemVectorSimilarity.class).bind(VectorSimilarity.class).to(PearsonCorrelation.class);

		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());

			Connection conn = DBConnection.getConection();
			
			JDBCRatingDAOBuilder jdbcDaoBuilder = JDBCRatingDAO.newBuilder();
			jdbcDaoBuilder.setTableName("test_ratings");
			jdbcDaoBuilder.setItemColumn("movieId");
			jdbcDaoBuilder.setUserColumn("userId");
			jdbcDaoBuilder.setTimestampColumn("timestamp");
			jdbcDaoBuilder.setRatingColumn("rating");

			JDBCRatingDAO dao = jdbcDaoBuilder.build(conn);

			config.addComponent(dao);
			System.out.println(config);
			LenskitRecommender rec = LenskitRecommender.build(config);

			GlobalItemRecommender globalItemRecommender = rec.getGlobalItemRecommender();

			Set<Long> items = new HashSet<Long>();
			items.add(Long.parseLong(item));
			List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items, 20);

			MovieDao movieDao = new MovieDao();
			movieDao.getMoviesByIds(recommendations);
			for (Movie movie : movieDao.getMoviesByIds(recommendations)) {
				System.out.println("Movie Id: " + movie.getId() + " , Title : " + movie.getTitle() + " Genre : " + movie.getGenre());
			}

			
			System.out.println("###############################");

			return movieDao.getMoviesByIds(recommendations);
		} catch (RecommenderBuildException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
