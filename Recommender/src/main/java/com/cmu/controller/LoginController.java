package com.cmu.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {

	@RequestMapping("/login")
	public ModelAndView home() { 
		ModelAndView mv = new ModelAndView("login");
		
		return mv;
	}
	
}
