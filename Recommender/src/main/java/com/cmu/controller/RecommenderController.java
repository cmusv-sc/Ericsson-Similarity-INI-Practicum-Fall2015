package com.cmu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import com.cmu.dao.EvaluationDaoImpl;
import com.cmu.dao.ModelDaoImpl;
import com.cmu.dao.MovieDao;
import com.cmu.dao.RecommendationDaoImpl;
import com.cmu.enums.Algorithm;
import com.cmu.interfaces.EvaluationDao;
import com.cmu.interfaces.ModelDao;
import com.cmu.interfaces.RecommendationDao;
import com.cmu.model.ItemScore;
import com.cmu.model.Movie;
import com.cmu.model.Recommendation;
import com.cmu.model.UserFeedback;
import com.cmu.recommendationEngine.RecommendationBuilder;

@Controller
public class RecommenderController {
	
	MovieDao movieDao = new MovieDao();

	@RequestMapping("/itemSimilarity")
	public ModelAndView searchItemSimilarities(@RequestParam(value = "item", required = false, defaultValue = "1") String item,
			@RequestParam(value = "algorithm", required = false, defaultValue = "") String algorithm) { 
		MovieDao movieDao = new MovieDao();
		RecommendationDaoImpl rec = new RecommendationDaoImpl();
		Movie movie = rec.getMovieData(Long.valueOf(item));
		List<Movie> recommendations = new ArrayList<Movie>();
		List<String> posters = new ArrayList<String>();
		List<String> moviesPlots = new ArrayList<String>();

		//recommendations = getItems(item, algorithm);
		RecommendationBuilder recommendationBuilder= new RecommendationBuilder(Long.valueOf(item));
		LinkedHashMap<Movie, List<Algorithm>> recommendationMap = (LinkedHashMap<Movie, List<Algorithm>>) recommendationBuilder.getRecommendations();
		for(Movie m : recommendationMap.keySet()) {
			recommendations.add(m);
		}
		ModelAndView mv = new ModelAndView("itemSimilarity");
		List<Long> movieIds = new ArrayList<Long>();
		List<String> movieTitles = new ArrayList<String>();
		for(int i = 0; i < recommendations.size(); i++){
			movieIds.add(i, recommendations.get(i).getId());
			movieTitles.add(i, recommendations.get(i).getTitle());
			posters.add(rec.getMovieData(recommendations.get(i).getId()).getPoster());
			moviesPlots.add(rec.getMovieData(recommendations.get(i).getId()).getSynopsis());
		}
		
		System.out.println();
		mv.addObject("selectedPoster", movie.getPoster());
		mv.addObject("posters", posters);
		mv.addObject("synopsys", movie.getSynopsis());
		mv.addObject("movieIds", movieIds);
		mv.addObject("movieTitles", movieTitles);
		mv.addObject("moviesPlots", createSemicolonSeparatedStringFromArray(moviesPlots));
		mv.addObject("selectedMovieTitle", movie.getTitle());
		mv.addObject("item", item);
		return mv;
	}
	
	private String createSemicolonSeparatedStringFromArray(List<String> array){
		StringBuilder nameBuilder = new StringBuilder();

	    for (String n : array) {
	        nameBuilder.append(n.replace("\"", "\'")).append("||");
	    }

	    nameBuilder.deleteCharAt(nameBuilder.length() - 1);

	    return nameBuilder.toString();
	}

	@RequestMapping("/home")
	public ModelAndView home() { 
		List<String> posters = new ArrayList<String>();
		List<Movie> recommendations = getRandomItems();
		ModelAndView mv = new ModelAndView("home");
		List<Long> movieIds = new ArrayList<Long>();
		List<String> movieTitles = new ArrayList<String>();
		for(int i = 0; i < recommendations.size(); i++){
			movieIds.add(recommendations.get(i).getId());
			movieTitles.add(recommendations.get(i).getTitle());
			posters.add(recommendations.get(i).getPoster());
		}

		mv.addObject("posters", posters);
		mv.addObject("movieIds", movieIds);
		mv.addObject("movieTitles", movieTitles);
		return mv;
	}
	
	private List<Movie> getRandomItems() {
		//return getItems("1", "");
		RecommendationDaoImpl rec = new RecommendationDaoImpl();
		List<Movie> result = new ArrayList<Movie>();
		Random rand = new Random();
		for (int i = 0; i < 12; i++)
		{
			Movie movie = rec.getMovieData(new Long(rand.nextInt(80)));
			while(movie == null)
				movie = rec.getMovieData(new Long(rand.nextInt(80)));
			result.add(movie);
		}
		System.out.println(result);
		return result;
		
	}
	
//	private List<Movie> getItems(String item, String algorithm) {
//
//		System.out.println(item);
//		LenskitConfiguration config = new LenskitConfiguration();
//		config.bind(GlobalItemScorer.class).to(ItemItemGlobalScorer.class);
//		if(algorithm != null && algorithm.contentEquals("Pearson"))
//			config.within(ItemVectorSimilarity.class).bind(VectorSimilarity.class).to(PearsonCorrelation.class);
//
//		try {
//			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
//
//			Connection conn = DBConnection.getConection();
//			
//			JDBCRatingDAOBuilder jdbcDaoBuilder = JDBCRatingDAO.newBuilder();
//			jdbcDaoBuilder.setTableName("test_ratings");
//			jdbcDaoBuilder.setItemColumn("movieId");
//			jdbcDaoBuilder.setUserColumn("userId");
//			jdbcDaoBuilder.setTimestampColumn("timestamp");
//			jdbcDaoBuilder.setRatingColumn("rating");
//
//			JDBCRatingDAO dao = jdbcDaoBuilder.build(conn);
//
//			config.addComponent(dao);
//			System.out.println(config);
//			LenskitRecommender rec = LenskitRecommender.build(config);
//
//			GlobalItemRecommender globalItemRecommender = rec.getGlobalItemRecommender();
//
//			Set<Long> items = new HashSet<Long>();
//			items.add(Long.parseLong(item));
//			List<ScoredId> recommendations = globalItemRecommender.globalRecommend(items, 20);
//
//			MovieDao movieDao = new MovieDao();
//			movieDao.getMoviesByIds(recommendations);
//			for (Movie movie : movieDao.getMoviesByIds(recommendations)) {
//				System.out.println("Movie Id: " + movie.getId() + " , Title : " + movie.getTitle() + " Genre : " + movie.getGenre());
//			}
//
//			
//			System.out.println("###############################");
//
//			return movieDao.getMoviesByIds(recommendations);
//		} catch (RecommenderBuildException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	@RequestMapping("/test")
	public ModelAndView test(){
		ModelDao m = new ModelDaoImpl();
		RecommendationDao r = new RecommendationDaoImpl();
		String name = Algorithm.COSINE_SIMILARITY.toString();
		List<ItemScore> li = new ArrayList<ItemScore>();
		ItemScore a = new ItemScore(1L,0.331);
		ItemScore b = new ItemScore(2L,0.443);
		ItemScore c = new ItemScore(3L,0.567);
		li.add(a);
		li.add(b);
		li.add(c);
		//m.addToModel(100000L, li, Algorithm.COSINE_SIMILARITY);
		//m.deleteModel(10L, Algorithm.COSINE_SIMILARITY);
		//List<Recommendation> rr = r.getRecommendation(100000L, Algorithm.COSINE_SIMILARITY);
		List<Long> mi = new ArrayList<Long>();
		mi.add(3L);
		mi.add(2L);
		mi.add(1L);
		RecommendationDaoImpl q = new RecommendationDaoImpl();
		//List<Movie> ms = q.getMovieDatas(mi);
		//Movie v = q.getMovieData(1L);
		EvaluationDao v = new EvaluationDaoImpl();
		UserFeedback u = new UserFeedback();
		Map<Algorithm,Double> hmap = new HashMap<Algorithm,Double>();
		hmap.put(Algorithm.COSINE_SIMILARITY, 0.331);
		hmap.put(Algorithm.PEARSON_COEFFICIENT, 0.343);
		u.setMovieIds(1L,2L);
		u.setRating(1);
		u.setHaveSeen(0);
		u.setAlgorithmScores(hmap);
		v.submitFeedback(u);
		
		int pp = 1;
		return null;
	}

}
