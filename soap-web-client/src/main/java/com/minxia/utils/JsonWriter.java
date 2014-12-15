package com.minxia.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.minxia.model.SoapForm;

public class JsonWriter {
	
	public final String createJson_str(boolean isSuc,String msg, long cost)
	{
		if(isValidJSON(msg)){
			System.out.println(msg);
			String json = "{";
			json += "\"isSuc\":" + (isSuc ? "true" : "false");
			json += ",\"msg\": " + msg;
			json += ",\"cost\": " + cost + "}";
			return json;
		}else{
			String output = XmlEscape.escapeXml(msg);
			String json = "{";
			json += "\"isSuc\":" + (isSuc ? "true" : "false");
			json += ",\"msg\":\"" + output + "\"";
			json += ",\"cost\": " + cost + "}";
			return json;
		}
	}
	
	public void writeJsonOutput(Boolean isSuc, HttpServletResponse response, String msg, long cost) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(createJson_str(isSuc, msg, cost));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}
	
	public boolean isValidJSON(final String json) {
		boolean valid = false;
		try {
			final JsonParser parser = new ObjectMapper().getJsonFactory().createJsonParser(json);
			while (parser.nextToken() != null) {
			}
			valid = true;
		} catch (JsonParseException jpe) {
			jpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return valid;
	}

}
