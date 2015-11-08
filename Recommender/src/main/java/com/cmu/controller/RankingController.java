package com.cmu.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cmu.dao.UserDetailsDaoImpl;
import com.cmu.model.UserDetails;


@Controller
public class RankingController {

	@RequestMapping(value = "/ranking", method = RequestMethod.GET)
	public ModelAndView ranking() {

	  ModelAndView model = new ModelAndView();
	  
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String username = auth.getName();
	  UserDetailsDaoImpl userDetailsDaoImpl = new UserDetailsDaoImpl(username);
	  List<String> users = new ArrayList<String>();
	  List<String> ratings = new ArrayList<String>();
	  List<UserDetails> ratingsPerUser = userDetailsDaoImpl.getNumberOfRatedMoviesPerUser();
	  
	  
	  for (UserDetails userdetail : ratingsPerUser){
		  users.add(userdetail.getUser().getLogin());
		  ratings.add(String.valueOf(userdetail.getNumberOfRatedMovies()));
	  }
      
	  
	  model.addObject("users", ControllerHelper.createSemicolonSeparatedStringFromArray(users));
	  model.addObject("ratings", ControllerHelper.createSemicolonSeparatedStringFromArray(ratings));
	  model.addObject("username", username);
	  model.setViewName("ranking");
	  return model;

	}
	
}
