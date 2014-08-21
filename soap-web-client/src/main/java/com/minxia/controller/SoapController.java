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
import com.minxia.utils.JsonWriter;
import com.minxia.utils.SoapTemplateManager;
import com.minxia.utils.XmlEscape;

@Controller
public class SoapController {
	
	Executor executor;
	Collection<SoapForm> list = new CopyOnWriteArrayList<SoapForm>();
	ObjectMapper mapper = new ObjectMapper();
	JsonWriter jw = new JsonWriter();
	
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
		String msg;
		SoapForm form = new SoapForm();
		fillSoapForm(request, form);
		executor = new SoapExecutor(form);
		executor.Execute();
		msg = form.getOutput();
		jw.writeJsonOutput(true, response, msg);
		return null;
	}
	
	@RequestMapping(value = "saveTemp.mx", method = RequestMethod.POST)
	public String saveTemp(HttpServletRequest request,HttpServletResponse response) {
		String msg = null;
		Boolean isSuc = true;
		Boolean isExist = false;
		SoapForm form = new SoapForm();
		
		fillSoapForm(request, form);
		for(int ii=0;ii<list.size();ii++){
			if(form.getName().equalsIgnoreCase(((CopyOnWriteArrayList<SoapForm>)list).get(ii).getName())){
				isExist = true;
				((CopyOnWriteArrayList<SoapForm>)list).set(ii, form);
			}
		}
		if(!isExist){
			list.add(form);
		}
		try {
			SoapTemplateManager.writeFile(list);
		} catch (Exception e) {
			msg = "failed to save the template file";
			isSuc = false;
			e.printStackTrace();
		}
		if(isSuc){
			msg = "save template file successfully";
		}
		jw.writeJsonOutput(isSuc, response, msg);
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
				jw.writeJsonOutput(true, response, output);
			} catch (IOException e) {
				msg = "error";
				found = false;
				e.printStackTrace();
			}
		}
		return null;
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
	
	
}
