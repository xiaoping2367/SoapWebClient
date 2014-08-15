package com.minxia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.minxia.model.SoapForm;

@Controller
@RequestMapping(value = "/")
public class SoapController {
	
	@RequestMapping(value = "home.mx")
	public String loadHomePage(Model m) {
		System.out.println("this is a soap test");
		m.addAttribute("soapform", new SoapForm());
		return "main";
	}
	
	@RequestMapping(value = "sendSoap.mx", method = RequestMethod.POST)
	public String sendSoap(@ModelAttribute("soapform") SoapForm soapForm, BindingResult result, Model m) {
		System.out.println(soapForm.getUrl());
		return "main";
	}
	
}
