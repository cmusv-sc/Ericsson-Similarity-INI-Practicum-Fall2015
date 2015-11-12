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
import com.cmu.enums.Algorithm;
import com.cmu.model.EvaluationStatistics;
import com.cmu.model.Movie;
import com.cmu.model.UserFeedback;
import com.cmu.recommendationEngine.RecommendationBuilder;


@Controller
public class AdminController {

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView admin() {

		ModelAndView model = new ModelAndView();

		model.setViewName("admin");

		return model;

	}

	@RequestMapping(value = "/admin/statistics", method = RequestMethod.GET)
	public ModelAndView statistics() {
		ModelAndView model = new ModelAndView();
		EvaluationDaoImpl evaluationDao = new EvaluationDaoImpl();

		List<EvaluationStatistics> statistics = evaluationDao.retrieveStatistics();
		List<String> algorithms = new ArrayList<String>(); 
		List<String> falsePositives = new ArrayList<String>(); 	  
		List<String> totalEvaluations = new ArrayList<String>(); 

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

	@RequestMapping(value = "/admin/algorithm", method = RequestMethod.GET)
	public ModelAndView algorithm() {
		AlgorithmsDaoImpl algorithmsDao = new AlgorithmsDaoImpl();

		ModelAndView model = new ModelAndView();

		model.addObject("algorithms", ControllerHelper.createSemicolonSeparatedStringFromArray(algorithmsDao.getAlgorithms()));
		model.addObject("enabledAlgorithms", ControllerHelper.createSemicolonSeparatedStringFromArray(algorithmsDao.getEnabledAlgorithms()));
		
		model.setViewName("algorithm");

		return model;

	}

	@RequestMapping(value = "/admin/algorithmManagement", method = RequestMethod.GET)
	public @ResponseBody void processAJAXRequest(
			@RequestParam("algorithm") String algorithm,
			@RequestParam("action") String action) {
		
		System.out.println("algorithm = " + algorithm + " action=" + action);
		AlgorithmsDaoImpl algorithmsDao = new AlgorithmsDaoImpl();
		if(action.contains("0")){
			algorithmsDao.disableAlgorithm(algorithm);
			System.out.println("Disabled");
		}
		else{
			algorithmsDao.enableAlgorithm(algorithm);
			System.out.println("Enabled");
		}

	}

}
