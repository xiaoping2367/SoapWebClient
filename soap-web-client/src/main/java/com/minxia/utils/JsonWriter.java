package com.minxia.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.minxia.model.SoapForm;

public class JsonWriter {
	
	public final String createJson_str(boolean isSuc,String msg)
	{
		String output = XmlEscape.escapeXml(msg);
		String json = "{";
		json += "\"isSuc\":" + (isSuc ? "true" : "false");
		json += ",\"msg\":\"" + output + "\"}";
		return json;
	}
	
	public void writeJsonOutput(Boolean isSuc, HttpServletResponse response, String msg) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(createJson_str(isSuc, msg));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}

}
