package com.minxia.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

import javax.net.ssl.SSLHandshakeException;


import com.minxia.model.SoapForm;

public class SoapExecutor implements Executor {
	
	private SoapForm form;
	private Properties prop = null;
	
	public SoapExecutor(SoapForm form)
	{
		this.form = form;
	}

	@Override
	public void Execute() {
		OutputStream out = null;
		BufferedReader in = null;
		HttpURLConnection httpConn= null;
		
		try {
			String soapURL = form.getUrl();
			String soapAction = form.getAction();
			String inputFile = form.getInput();
			String outputFile = form.getOutput();
			
			System.out.println("# soapURL = "+soapURL);
			System.out.println("# soapAction = "+soapAction);
			System.out.println("# inputFile = "+inputFile);
			System.out.println("# outputFile = "+outputFile);
			
			if(form.isUseSSL()){
				String properties  = form.getProperties();
				prop = parsePropertiesString(properties);
				initSystemProperties();
			}
			
			URL url = new URL(soapURL);
			URLConnection connection = url.openConnection();
	        httpConn = (HttpURLConnection) connection;

	        byte[] b = inputFile.getBytes();
	    
	        // Set the appropriate HTTP parameters.
	        httpConn.setRequestProperty( "Content-Length",
	                                     String.valueOf( b.length ) );
	        httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
			  httpConn.setRequestProperty("SOAPAction",soapAction);
	        httpConn.setRequestMethod( "POST" );
	        httpConn.setDoOutput(true);
	        httpConn.setDoInput(true);
	        httpConn.setAllowUserInteraction(true);
	        
	        
	        System.out.println("# inputString:");
	        System.out.println(new String(b));
	        System.out.println("");
	        // Everything's set up; send the XML that was read in to b.
	        out = httpConn.getOutputStream();
	        out.write( b );    
	        out.close();

	        // Read the response and write it to standard out.
	        InputStreamReader isr;
	        try{
	        isr =
	            new InputStreamReader(httpConn.getInputStream());
	        }catch(Exception e){
	        	isr = new InputStreamReader(httpConn.getErrorStream());
	        }
	        
	        in = new BufferedReader(isr);

	        String inputLine;
	        String outputString = "";
	        while ((inputLine = in.readLine()) != null){
	        	outputString = outputString+  inputLine;
	        }
	        System.out.println("# outputString:");
	        System.out.println(outputString);
	        form.setOutput(outputString);
	        in.close();
		} catch (MalformedURLException e) {
			System.out.println("test mal");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SSLHandshakeException e){
			System.out.println("test ssl");
			e.printStackTrace();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			 System.out.println("test IO");
			e.printStackTrace();
		}
		finally{
			if(out!=null){
				try {
					out.close();
				} catch (Exception e1) {
				}
			}
			if(in!=null){
				try {
					in.close();
				} catch (Exception e1) {
				}
			}
			if(httpConn!=null){
				try {
					httpConn.disconnect();
				} catch (Exception e) {
				}
			}			
		}
	}
	
	  public static void copy(InputStream in, OutputStream out) throws IOException {

		    // do not allow other threads to read from the
		    // input or write to the output while copying is
		    // taking place
		    synchronized (in) {
		      synchronized (out) {

		        byte[] buffer = new byte[256];
		        while (true) {
		          int bytesRead = in.read(buffer);
		          if (bytesRead == -1) break;
		          out.write(buffer, 0, bytesRead);
		        }
		      }
		    }
		  }

	public SoapForm getForm() {
		return form;
	}

	public void setForm(SoapForm form) {
		this.form = form;
	}
	
	public Properties parsePropertiesString(String s) {
	    // grr at load() returning void rather than the Properties object
	    // so this takes 3 lines instead of "return new Properties().load(...);"
	    final Properties p = new Properties();
	    try {
			p.load(new StringReader(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return p;
	}
	
	public void initSystemProperties() {
		for(Enumeration e=prop.propertyNames(); e.hasMoreElements();){  
			String key=(String) e.nextElement();  
			String value = prop.getProperty(key); 
			System.setProperty(key, value);
		}

		System.out.println("# Set system properties:");
		for(Enumeration e=System.getProperties().propertyNames(); e.hasMoreElements();){  
			String key=(String) e.nextElement();  
			String value = prop.getProperty(key); 
			System.out.println("key: "+ key + "      value: " + value);
		}
	}

}
