package com.cmu.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cmu.dao.UserDetailsDaoImpl;
import com.cmu.model.User;


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
	  HashMap<User, Integer> ratingsPerUser = userDetailsDaoImpl.getNumberOfRatedMoviesPerUser();
	  
	  /*
	  for (User user : ratingsPerUser.keySet()){
		  users.add(user.getLogin());
		  ratings.add(ratingsPerUser.get(user).toString());
	  }
      */
	  
	  for(int i=0; i<10; i++){
		  users.add("User" + i);
		  ratings.add(""+i*71);
	  }
	  users.add(username);
	  ratings.add("12312");
	  model.addObject("users", ControllerHelper.createSemicolonSeparatedStringFromArray(users));
	  model.addObject("ratings", ControllerHelper.createSemicolonSeparatedStringFromArray(ratings));
	  model.addObject("username", username);
	  model.setViewName("ranking");
	  return model;

	}
	
}
