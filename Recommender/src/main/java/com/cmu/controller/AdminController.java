package com.cmu.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.cmu.dao.UserDaoImpl;
import com.cmu.enums.Algorithm;
import com.cmu.model.EvaluationStatistics;
import com.cmu.model.Movie;
import com.cmu.model.User;
import com.cmu.model.UserFeedback;
import com.cmu.recommendationEngine.RecommendationBuilder;
import com.cmu.security.BCryptPasswordEncoder;

//Class that controls all the admin pages
@Controller
public class AdminController {

	//Controller for the main admin page
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView admin() {

		ModelAndView model = new ModelAndView();

		model.setViewName("admin");

		return model;

	}

	//Controller for the statistics page
	@RequestMapping(value = "/admin/statistics", method = RequestMethod.GET)
	public ModelAndView statistics() {
		ModelAndView model = new ModelAndView();
		EvaluationDaoImpl evaluationDao = new EvaluationDaoImpl();

		List<EvaluationStatistics> statistics = evaluationDao.retrieveStatistics();
		List<String> algorithms = new ArrayList<String>(); 
		List<String> falsePositives = new ArrayList<String>(); 	  
		List<String> totalEvaluations = new ArrayList<String>(); 

		//Populate the arrays with the statistics form DAO
		for (EvaluationStatistics e : statistics){
			algorithms.add(e.getAlgorithm().toString());
			falsePositives.add(String.valueOf(e.getFalsePositives()));
			totalEvaluations.add(String.valueOf(e.getTotalEvaluations()));
		}


		model.addObject("algorithms", ControllerHelper.createSemicolonSeparatedStringFromArray(algorithms));
		model.addObject("falsePositives", ControllerHelper.createSemicolonSeparatedStringFromArray(falsePositives));
		model.addObject("totalEvaluations", ControllerHelper.createSemicolonSeparatedStringFromArray(totalEvaluations));
		model.setViewName("statistics");
		return model;

	}

	//Controller for the algorithm management page
	@RequestMapping(value = "/admin/algorithm", method = RequestMethod.GET)
	public ModelAndView algorithm() {
		AlgorithmsDaoImpl algorithmsDao = new AlgorithmsDaoImpl();
		ModelAndView model = new ModelAndView();

		model.addObject("algorithms", ControllerHelper.createSemicolonSeparatedStringFromArray(algorithmsDao.getAlgorithms()));
		model.addObject("enabledAlgorithms", ControllerHelper.createSemicolonSeparatedStringFromArray(algorithmsDao.getEnabledAlgorithms()));
		
		model.setViewName("algorithm");

		return model;

	}

	//Controller for retrieving information of enabling/disabling algorithms
	@RequestMapping(value = "/admin/algorithmManagement", method = RequestMethod.GET)
	public @ResponseBody void algorithmManagement(
			@RequestParam("algorithm") String algorithm,
			@RequestParam("action") String action) {
		
		System.out.println("algorithm = " + algorithm + " action=" + action);
		AlgorithmsDaoImpl algorithmsDao = new AlgorithmsDaoImpl();
		if(action.contains("0")){
			algorithmsDao.disableAlgorithm(algorithm);
		}
		else{
			algorithmsDao.enableAlgorithm(algorithm);
		}

	}
	
	//Controller for the user management page
	@RequestMapping(value = "/admin/userManagement", method = RequestMethod.GET)
	public ModelAndView userManagement() {
		UserDaoImpl userDao = new UserDaoImpl();

		ModelAndView model = new ModelAndView();

		List<User> users = userDao.getUsers();
		
		List<String> usernames = new ArrayList<String>();
		List<String> userRoles = new ArrayList<String>();
		for(User u : users){
			usernames.add(u.getLogin());
			userRoles.add(u.getRole());
		}
		
		model.addObject("users", ControllerHelper.createSemicolonSeparatedStringFromArray(usernames));
		model.addObject("userRoles", ControllerHelper.createSemicolonSeparatedStringFromArray(userRoles));
		
		model.setViewName("userManagement");

		return model;

	}

	//Controller that deletes user. It is not integrated with the UI since deleting user
	// was found to be not a usual feature
	@RequestMapping(value = "/admin/deleteUser", method = RequestMethod.GET)
	public @ResponseBody void deleteUser(
			@RequestParam("username") String username) {
		
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.deleteUser(username);

	}
	
	//Controller for the add user page
	@RequestMapping(value = "/admin/addUser", method = RequestMethod.GET)
	public ModelAndView addUser() {
		ModelAndView model = new ModelAndView();		
		model.setViewName("addUser");
		return model;
	}
	
	//Controller that actually adds the user to the DB
	@RequestMapping(value = "/admin/addUserAction", method = RequestMethod.GET)
	public @ResponseBody void deleteUser(
			@RequestParam("username") String username, 
			@RequestParam("userRole") String userRole) {
		
		UserDaoImpl userDao = new UserDaoImpl();
		
		//The password for new users is "default"
		// If other password is wanted, one can easily initiate com.cmu.security.PasswordEncoderGenerator
		//in order to retrieve the hash for the default password
		String password = "$2a$10$9cH17x6PZ8Dx54yaHgpt9O2rfcuQYD84Tg0m/q3/yAQKOfpkNjd6a";
		User user = new User(username, password, userRole);
		System.out.println(username + " was added!");
		userDao.addUser(user);
		

	}
	
	//Not being used by the UI, but it would control the upload of models
	//in order to add algorithms to the DB
	@RequestMapping(value = "/admin/uploadAlgorithm", method = RequestMethod.GET)
	public ModelAndView uploadAlgorithm() {
		ModelAndView model = new ModelAndView();		
		model.setViewName("uploadAlgorithm");
		return model;
	}
}
