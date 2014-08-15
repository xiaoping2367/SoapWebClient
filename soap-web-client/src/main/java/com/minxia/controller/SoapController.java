package com.minxia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class SoapController {
	
	@RequestMapping(value = "home.mx")
	public String loadHomePage(Model m) {
		System.out.println("this is a soap test");
		return "main";
	}
	
	@RequestMapping(value = "test.mx")
	public String test(Model m) {
		m.addAttribute("name", "CodeTutr");
		System.out.println("this is a test");
		return "home";
	}
}
