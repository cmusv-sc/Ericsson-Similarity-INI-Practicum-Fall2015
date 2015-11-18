package com.cmu.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cmu.dao.UserDaoImpl;

@Controller
public class SettingsController {

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView settings() {

	  ModelAndView model = new ModelAndView();
	  
	  model.setViewName("settings");

	  return model;

	}
	
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public ModelAndView updatePassword(@RequestParam("password") String password) {

		  ModelAndView model = new ModelAndView();
		  UserDaoImpl u = new UserDaoImpl();
		  
		  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		  String hashedPassword = passwordEncoder.encode(password);
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      String username = auth.getName(); //get logged in username
	      System.out.println(password);
	      	
		  u.updatePassword(username, hashedPassword);
		  model.setViewName("home");
		  return model;
	}
	
}
