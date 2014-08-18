package com.minxia.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.minxia.model.SoapForm;

@Controller
public class SoapController {
	
	@RequestMapping(value = "home.mx", method = RequestMethod.GET)
	public String loadHomePage(Model m) {
		System.out.println("this is a soap test");
		m.addAttribute("soapform", new SoapForm());
		return "main";
	}
	
	@RequestMapping(value = "sendSoap.mx", method = RequestMethod.POST)
	public String sendSoap(HttpServletRequest request,HttpServletResponse response) {
		SoapForm form = new SoapForm();
		fillSoapForm(request, form);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(createJson_str(true,form));
		} catch (Exception e) {

		} finally {
			out.close();
		}
		
		return null;
	}

	private void fillSoapForm(HttpServletRequest request, SoapForm form) {
		// TODO Auto-generated method stub
		form.setUrl(request.getParameter("url"));
		form.setAction(request.getParameter("action"));
		form.setUrl(request.getParameter("useSSL"));
		form.setProperties(request.getParameter("properties"));
		form.setInput(request.getParameter("input"));
		form.setOutput(request.getParameter("output"));
	}
	
	public final String createJson_str(boolean isSuc, SoapForm form)
	{
		String json = "{";
		json += "\"isSuc\":" + (isSuc ? "true" : "false");
		json += ",\"msg\":\"" + form.toString() + "\"}";
		return json;
	}
}
