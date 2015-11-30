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

//Class to control the settings page and functions
@Controller
public class SettingsController {

	//Controller for the settings page
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public ModelAndView settings() {
	  ModelAndView model = new ModelAndView();  
	  model.setViewName("settings");
	  return model;
	}
	
	//Controller to update the user's password
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public ModelAndView updatePassword(@RequestParam("password") String password) {

		  ModelAndView model = new ModelAndView();
		  UserDaoImpl u = new UserDaoImpl();
		  
		  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		  String hashedPassword = passwordEncoder.encode(password);
	      //get logged in username
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      String username = auth.getName(); 
	      	
		  u.updatePassword(username, hashedPassword);
		  model.setViewName("home");
		  return model;
	}
	
}
