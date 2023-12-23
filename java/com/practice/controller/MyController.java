package com.practice.controller;

import java.awt.List;
import java.util.Date;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyController {
	
	
	
	@RequestMapping(value="/about",method=RequestMethod.GET)
	public String about(Model model)
	{
		//inputting data in 
		System.out.println("inside about handler");
		model.addAttribute("name", "mayur");
		model.addAttribute("currentDate", new Date().toLocaleString());
		return"about";
	}
	
	
	
	//handler for including fragment

	//inheriting template
	
	@GetMapping("/newabout")
	public String newabout()
	{
		return"newabout";
	}
}






