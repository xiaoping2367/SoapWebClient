package com.minxia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SoapController {
	
	@RequestMapping(value = "/sendForm", method = RequestMethod.GET)
	public String getResponse(){
		return null;
	}
	

}
