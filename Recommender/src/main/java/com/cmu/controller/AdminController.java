package com.cmu.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


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
	  
	  model.setViewName("admin");

	  return model;

	}
	
	@RequestMapping(value = "/admin/algorithm", method = RequestMethod.GET)
	public ModelAndView algorithm() {

	  ModelAndView model = new ModelAndView();
	  
	  model.setViewName("admin");

	  return model;

	}
	
	
}
