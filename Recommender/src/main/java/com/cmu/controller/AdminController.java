package com.cmu.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cmu.dao.EvaluationDaoImpl;
import com.cmu.model.EvaluationStatistics;


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

	  ModelAndView model = new ModelAndView();
	  
	  model.setViewName("algorithm");

	  return model;

	}
	
	
}
