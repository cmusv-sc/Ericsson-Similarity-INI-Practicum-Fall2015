package com.cmu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmu.dao.AlgorithmsDaoImpl;
import com.cmu.dao.EvaluationDaoImpl;
import com.cmu.dao.ModelDaoImpl;
import com.cmu.dao.MovieDao;
import com.cmu.dao.RecommendationDaoImpl;
import com.cmu.dao.SearchDaoImpl;
import com.cmu.dao.UserDetailsDaoImpl;
import com.cmu.enums.Algorithm;
import com.cmu.interfaces.EvaluationDao;
import com.cmu.interfaces.ModelDao;
import com.cmu.interfaces.RecommendationDao;
import com.cmu.interfaces.SearchDao;
import com.cmu.interfaces.UserDetailsDao;
import com.cmu.model.EvaluationStatistics;
import com.cmu.model.ItemScore;
import com.cmu.model.Movie;
import com.cmu.model.UserFeedback;
import com.cmu.recommendationEngine.RecommendationBuilder;

@Controller
public class RecommenderController {

	MovieDao movieDao = new MovieDao();

	@RequestMapping("/itemSimilarity")
	public ModelAndView searchItemSimilarities(@RequestParam(value = "item", required = false, defaultValue = "1") String item,
			@RequestParam(value = "algorithm", required = false, defaultValue = "") String algorithm) { 
		RecommendationDaoImpl rec = new RecommendationDaoImpl();
		Movie movie = rec.getMovieData(Long.valueOf(item));
		List<Movie> recommendations = new ArrayList<Movie>();
		List<String> posters = new ArrayList<String>();
		List<String> moviesPlots = new ArrayList<String>();
		List<Algorithm> algorithms = new ArrayList<Algorithm>();
		algorithms.add(Algorithm.COSINE_SIMILARITY);
		algorithms.add(Algorithm.PEARSON_COEFFICIENT);
		RecommendationBuilder recommendationBuilder= new RecommendationBuilder(Long.valueOf(item), algorithms);
		LinkedHashMap<Movie, List<Algorithm>> recommendationMap = (LinkedHashMap<Movie, List<Algorithm>>) recommendationBuilder.getRecommendations();

		for(Movie m : recommendationMap.keySet()) {
			recommendations.add(m);
		}

		ModelAndView mv = new ModelAndView("itemSimilarity");
		List<Long> movieIds = new ArrayList<Long>();
		List<String> movieTitles = new ArrayList<String>();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName(); //get logged in username
		UserDetailsDaoImpl u = new UserDetailsDaoImpl(username);
		List<Movie> alreadyRatedMovies = u.getUserRatedMovies(username, movie.getId());

		for(Movie m : recommendations){
			if(alreadyRatedMovies.contains(m))
				continue;
			movieIds.add(m.getId());
			movieTitles.add(m.getTitle() + " (" + m.getYear() + ")");
			posters.add(m.getPoster());
			moviesPlots.add(m.getSynopsis());
		}

		mv.addObject("selectedMovieId", movie.getId());
		mv.addObject("selectedPoster", movie.getPoster());
		mv.addObject("posters", ControllerHelper.createSemicolonSeparatedStringFromArray(posters));
		mv.addObject("synopsys", movie.getSynopsis());
		mv.addObject("movieIds", movieIds);
		mv.addObject("movieTitles", ControllerHelper.createSemicolonSeparatedStringFromArray(movieTitles));
		mv.addObject("moviesPlots", ControllerHelper.createSemicolonSeparatedStringFromArray(moviesPlots));
		mv.addObject("selectedMovieTitle", movie.getTitle() + " (" + movie.getYear()+ ")");
		mv.addObject("item", item);
		return mv;
	}

	@RequestMapping("/itemSimilarityType2")
	public ModelAndView searchItemSimilaritiesType2(@RequestParam(value = "item", required = false, defaultValue = "1") String item,
			@RequestParam(value = "algorithm", required = false, defaultValue = "") String algorithm) { 
		RecommendationDaoImpl rec = new RecommendationDaoImpl();
		Movie movie = rec.getMovieData(Long.valueOf(item));
		List<Movie> recommendations = new ArrayList<Movie>();
		List<String> posters = new ArrayList<String>();
		List<String> moviesPlots = new ArrayList<String>();
		List<Algorithm> algorithms = new ArrayList<Algorithm>();
		algorithms.add(Algorithm.COSINE_SIMILARITY);
		algorithms.add(Algorithm.PEARSON_COEFFICIENT);
		
		RecommendationBuilder recommendationBuilder= new RecommendationBuilder(Long.valueOf(item), algorithms);
		LinkedHashMap<Movie, List<Algorithm>> recommendationMap = (LinkedHashMap<Movie, List<Algorithm>>) recommendationBuilder.getRecommendations();
		for(Movie m : recommendationMap.keySet()) {
			recommendations.add(m);
		}
		ModelAndView mv = new ModelAndView("itemSimilarityType2");
		List<Long> movieIds = new ArrayList<Long>();
		List<String> movieTitles = new ArrayList<String>();
		for(int i = 0; i < recommendations.size(); i++){
			movieIds.add(i, recommendations.get(i).getId());
			movieTitles.add(i, recommendations.get(i).getTitle() + " (" + recommendations.get(i).getYear() + ")");
			posters.add(recommendations.get(i).getPoster());
			moviesPlots.add(recommendations.get(i).getSynopsis());
		}


		mv.addObject("selectedMovieId", movie.getId());
		mv.addObject("selectedPoster", movie.getPoster());
		mv.addObject("posters", posters);
		mv.addObject("synopsys", movie.getSynopsis());
		mv.addObject("movieIds", movieIds);
		mv.addObject("movieTitles", ControllerHelper.createSemicolonSeparatedStringFromArray(movieTitles));
		mv.addObject("moviesPlots", ControllerHelper.createSemicolonSeparatedStringFromArray(moviesPlots));
		mv.addObject("selectedMovieTitle", movie.getTitle());
		mv.addObject("item", item);
		return mv;
	}


	@RequestMapping("/home")
	public ModelAndView home() { 
		List<String> posters = new ArrayList<String>();
		List<Movie> randomItems = getRandomItems();
		ModelAndView mv = new ModelAndView("home");
		List<Long> movieIds = new ArrayList<Long>();
		List<String> movieTitles = new ArrayList<String>();
		for(Movie movie : randomItems){
			movieIds.add(movie.getId());
			movieTitles.add(movie.getTitle() + " (" + movie.getYear() + ")");
			posters.add(movie.getPoster());
		}
		
		List<String> popularPosters = new ArrayList<String>();
		List<Long> popularMovieIds = new ArrayList<Long>();
		List<String> popularMovieTitles = new ArrayList<String>();
		RecommendationDaoImpl r = new RecommendationDaoImpl();
		for(Movie movie : r.getPopularMovies(50)){
			popularPosters.add(movie.getPoster());
			popularMovieIds.add(movie.getId());
			popularMovieTitles.add(movie.getTitle()  + " (" + movie.getYear() + ")");
		}
		
		mv.addObject("posters", ControllerHelper.createSemicolonSeparatedStringFromArray(posters));
		mv.addObject("movieIds", movieIds);
		mv.addObject("movieTitles", ControllerHelper.createSemicolonSeparatedStringFromArray(movieTitles));
		mv.addObject("popularPosters", ControllerHelper.createSemicolonSeparatedStringFromArray(popularPosters));
		mv.addObject("popularMovieIds", popularMovieIds);
		mv.addObject("popularMovieTitles", ControllerHelper.createSemicolonSeparatedStringFromArray(popularMovieTitles));

		return mv;
	}

	private List<Movie> getRandomItems() {
		RecommendationDaoImpl rec = new RecommendationDaoImpl();
		List<Movie> result = new ArrayList<Movie>();
		List<Long> movieIds = new ArrayList<Long>();
		Random rand = new Random();
		for (int i = 0; i < 12; i++){
			Long id = new Long(rand.nextInt(80));

			while(containLong(movieIds, id) || (rec.getMovieData(id) == null))
				id = new Long(rand.nextInt(80));

			movieIds.add(id);
			result.add(rec.getMovieData(id));
		}

		return result;	
	}

	private boolean containLong(List<Long> movieIds, Long id){
		for(Long i : movieIds){
			if(i.compareTo(id) == 0)
				return true;
		}

		return false;
	}

	@RequestMapping(value = "/evaluation", method = RequestMethod.GET)
	public @ResponseBody String processAJAXRequest(
			@RequestParam("similarity") String similarity,
			@RequestParam("movieId1") String movieId1,
			@RequestParam("movieId2") String movieId2) {

		UserFeedback  feedback = new UserFeedback();
		feedback.setMovieIds(Long.valueOf(movieId1), Long.valueOf(movieId2));
		feedback.setHaveSeen(-1);
		feedback.setRating(Integer.valueOf(similarity));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName(); //get logged in username
		
		feedback.setUsername(username);
		
		List<Algorithm> usedAlgorithms = new ArrayList<Algorithm>();
		usedAlgorithms.add(Algorithm.COSINE_SIMILARITY);
		usedAlgorithms.add(Algorithm.PEARSON_COEFFICIENT);
		RecommendationBuilder recommendationBuilder= new RecommendationBuilder(Long.valueOf(movieId1), usedAlgorithms);
		LinkedHashMap<Movie, List<Algorithm>> recommendationMap = (LinkedHashMap<Movie, List<Algorithm>>) recommendationBuilder.getRecommendations();

		Movie selected = new Movie("", 0l,"","","","", "");
		for(Movie m : recommendationMap.keySet())
			if(m.getId() == Long.valueOf(movieId2))
				selected = m;
		List<Algorithm> algorithms = recommendationMap.get(selected);

		Map<Algorithm, Double> algorithmScores = new HashMap<Algorithm, Double>();
		//TODO get the score of the algorithm.
		for(Algorithm a : algorithms)
			algorithmScores.put(a, 0.0);

		feedback.setAlgorithmScores(algorithmScores);

		EvaluationDaoImpl evaluationDao = new EvaluationDaoImpl();
		evaluationDao.submitFeedback(feedback);

		return similarity;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView searchMovies(
			@RequestParam("searchString") String searchString) {
		SearchDao searchDao = new SearchDaoImpl();
		List<Movie> searchedMovies = searchDao.findExactMatchRegex(searchString);
		ModelAndView mv = new ModelAndView("search");
		List<String> posters = new ArrayList<String>();
		List<Long> movieIds = new ArrayList<Long>();
		List<String> titles = new ArrayList<String>();
		for(Movie m : searchedMovies){
			posters.add(m.getPoster());
			movieIds.add(m.getId());
			titles.add(m.getTitle() + " (" + m.getYear() + ")");
			System.out.println(m.getTitle());
		}

		mv.addObject("posters", ControllerHelper.createSemicolonSeparatedStringFromArray(posters));
		mv.addObject("movieIds", movieIds);
		if(titles.isEmpty())
			mv.addObject("movieTitles", titles);
		else
			mv.addObject("movieTitles", ControllerHelper.createSemicolonSeparatedStringFromArray(titles));

		return mv;
	}

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
		//Movie vv = q.getMovieData(1L);
		EvaluationDao v = new EvaluationDaoImpl();
		UserFeedback u = new UserFeedback();
		Map<Algorithm,Double> hmap = new HashMap<Algorithm,Double>();
		hmap.put(Algorithm.COSINE_SIMILARITY, 0.331);
		hmap.put(Algorithm.PEARSON_COEFFICIENT, 0.343);
		u.setMovieIds(1L,2L);
		u.setRating(1);
		u.setHaveSeen(0);
		u.setAlgorithmScores(hmap);
		//v.submitFeedback(u);
		
		UserDetailsDao ud = new UserDetailsDaoImpl("");
		//List<Movie> mu = ud.getUserRatedMovies("user1@user.com", 52L);
		
		UserDetailsDaoImpl userDetailsDaoImpl = new UserDetailsDaoImpl("");
		//List<UserDetails> ratingsPerUser = userDetailsDaoImpl.getNumberOfRatedMoviesPerUser();
		//List<Movie> pm = q.getPopularMovies(20);
		//List<EvaluationStatistics> es = v.retrieveStatistics();
		
		AlgorithmsDaoImpl al = new AlgorithmsDaoImpl();
		//al.disableAlgorithm("COSINE_SIMILARITY");
		//List<String> aa = al.getEnabledAlgorithms();
		//al.enableAlgorithm("COSINE_SIMILARITY");
		//aa = al.getEnabledAlgorithms();
		
		int pp = 1;
		return null;
	}

}
