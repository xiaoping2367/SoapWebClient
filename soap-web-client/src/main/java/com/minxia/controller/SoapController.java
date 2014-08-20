package com.minxia.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.minxia.model.SoapForm;
import com.minxia.soap.Executor;
import com.minxia.soap.SoapExecutor;
import com.minxia.utils.SoapTemplateManager;
import com.minxia.utils.XmlEscape;

@Controller
public class SoapController {
	
	Executor executor;
	Collection<SoapForm> list = new CopyOnWriteArrayList<SoapForm>();
	ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "home.mx", method = RequestMethod.GET)
	public String loadHomePage(Model m) {
		if(SoapTemplateManager.isTempatesExsit()){
			try {
				list = SoapTemplateManager.readFile();
				m.addAttribute("tempList", list);
			} catch (Exception e) {
				String msg = "failed to read templates file";
				m.addAttribute("err", msg);
				e.printStackTrace();
			}
		}
		return "main";
	}
	
	@RequestMapping(value = "sendSoap.mx", method = RequestMethod.POST)
	public String sendSoap(HttpServletRequest request,HttpServletResponse response) {
		SoapForm form = new SoapForm();
		fillSoapForm(request, form);
		executor = new SoapExecutor(form);
		executor.Execute();
		writeJsonOutput(true, form, response, null);
		return null;
	}
	
	@RequestMapping(value = "saveTemp.mx", method = RequestMethod.POST)
	public String saveTemp(HttpServletRequest request,HttpServletResponse response) {
		String msg = null;
		Boolean isSuc = true;
		SoapForm form = new SoapForm();
		fillSoapForm(request, form);
		list.add(form);
		try {
			SoapTemplateManager.writeFile(list);
		} catch (Exception e) {
			msg = "failed to save the template file";
			isSuc = false;
			e.printStackTrace();
		}
		form.setOutput("save template file successfully");
		writeJsonOutput(isSuc, form, response, msg);
		return null;
	}

	@RequestMapping(value = "fill/{name}.mx", method = RequestMethod.GET)
	public String displaytemp(@PathVariable("name") String name, HttpServletResponse response) {
		Boolean found = false;
		SoapForm form = new SoapForm();
		String msg = null;
		name = name.replace("@", " ");
		for(SoapForm f : list){
			if(name.equalsIgnoreCase(f.getName())){
				form = f;
				found = true;
				break;
			}
		}
		if(found){
			try {
				String output = mapper.writeValueAsString(form);
				writeJsonOutput(output, response);
			} catch (IOException e) {
				msg = "error";
				found = false;
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private void writeJsonOutput(Boolean isSuc, SoapForm form, HttpServletResponse response, String msg) {
		// TODO Auto-generated method stub
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(createJson_str(isSuc,form, msg));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}
	
	private void writeJsonOutput(String output, HttpServletResponse response) {
		// TODO Auto-generated method stub
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(output);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}

	private void fillSoapForm(HttpServletRequest request, SoapForm form) {
		// TODO Auto-generated method stub
		form.setName(request.getParameter("name"));
		form.setUrl(request.getParameter("url"));
		form.setAction(request.getParameter("action"));
		if(request.getParameter("useSSL") == null){
			form.setUseSSL(false);
		}else
			form.setUseSSL(true);
		form.setProperties(request.getParameter("properties"));
		form.setInput(request.getParameter("input"));
		form.setOutput(request.getParameter("output"));
	}
	
	public final String createJson_str(boolean isSuc, SoapForm form, String msg)
	{
		String output;
		if(isSuc){
			output = form.getOutput();
		}else{
			output = msg;
		}
		String json = "{";
		json += "\"isSuc\":" + (isSuc ? "true" : "false");
		json += ",\"msg\":\"" + XmlEscape.escapeXml(output) + "\"}";
		return json;
	}
}
