package com.minxia.soap;

import java.io.*;
import java.net.*;
import java.security.Security;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.net.ssl.SSLHandshakeException;


public class SOAPClient {
	public Properties prop = null;
	
	public static void main(String[] args) throws Exception {
		System.out.println("=============Welcome to use SOAP test client===================");
		System.out.println("# Fristly, you need to input config file name.");
		System.out.println("# Soap URL, Action and SSL information will load from this config file.");
		System.out.print("Please input config file name:");
		BufferedReader _input = new BufferedReader(new InputStreamReader(System.in));
		String configFile = "";
	    try {
	    	configFile = _input.readLine();
	    	if(configFile==null || configFile.equalsIgnoreCase(""))
	    	{
	    		configFile = "config.properties";
	    	}
	    }catch (IOException e) {
	    	e.printStackTrace();
	    }
		SOAPClient soapClient = new SOAPClient(configFile);
		soapClient.execute();
    }
	
	public SOAPClient(String configFile){
		this.prop = getProperties(System.getProperty("user.dir") + File.separator + configFile);
		boolean useSSL = Boolean.valueOf(prop.getProperty("useSSL")).booleanValue();
		System.out.println("# useSSL = "+prop.getProperty("useSSL"));
		if(useSSL){
			initSystemProperties();
		}
	}
	
	public void execute(){
		try {
			String soapURL = prop.getProperty("soapURL");
			String soapAction = prop.getProperty("soapAction");
			String inputFile = prop.getProperty("inputFile");
			String outputFile = prop.getProperty("outputFile");
			
			System.out.println("# soapURL = "+soapURL);
			System.out.println("# soapAction = "+soapAction);
			System.out.println("# inputFile = "+inputFile);
			System.out.println("# outputFile = "+outputFile);
			// Create the connection where we're going to send the file.
			URL url = new URL(soapURL);
			URLConnection connection = url.openConnection();
	        HttpURLConnection httpConn = (HttpURLConnection) connection;

	        // Open the input file. After we copy it to a byte array, we can see
	        // how big it is so that we can set the HTTP Cotent-Length
	        // property. (See complete e-mail below for more on this.)
	        FileInputStream fin = new FileInputStream(inputFile);

	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    
	        // Copy the SOAP file to the open connection.
	        copy(fin,bout);
	        fin.close();

	        byte[] b = bout.toByteArray();

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
	        OutputStream out = httpConn.getOutputStream();
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
	        
	        BufferedReader in = new BufferedReader(isr);

	        String inputLine;
	        String outputString = "";
	        while ((inputLine = in.readLine()) != null){
	        	outputString = outputString+  inputLine;
	        	//System.out.println(inputLine);
	        }
	        System.out.println("# outputString:");
	        System.out.println(outputString);
	        writeFile(outputFile, outputString);
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
        
	}


  // copy method from From E.R. Harold's book "Java I/O"
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
  
  public static Properties getProperties(String name)
  {
    System.out.println("# Loading properties file from " + name);
    Properties p = new Properties();
    InputStream in = null;
    try
    {
      in = new BufferedInputStream(new FileInputStream(name));

      p.load(in);
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (in != null)
        try {
          in.close();
        }
        catch (IOException e1) {
          e1.printStackTrace();
        }
    }
    return p;
  }
  
  public static void writeFile(String outputFile,String outputString){
	  try {
	   File f = new File(outputFile);
	   if(!f.exists()){
	    f.createNewFile();
	   }
	   BufferedWriter output = new BufferedWriter(new FileWriter(f));
	   output.write(outputString);
	   output.close();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	}
  
  public void initSystemProperties() {
	  for(Enumeration e=prop.propertyNames(); e.hasMoreElements();){  
		  String key=(String) e.nextElement();  
		  String value = prop.getProperty(key); 
		  if(!("soapURL".equals(key)) && !("soapAction".equals(key))
				  && !("inputFile".equals(key))
				  && !("outputFile".equals(key))
				  && !("useSSL".equals(key))){
			  System.setProperty(key, value);
		  }
	}
//	  Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();  
//	  while (it.hasNext()) {  
//		  Entry<Object, Object> entry = it.next();  
//		  String key = String.valueOf(entry.getKey());  
//		  String value = String.valueOf(entry.getValue());   
//		  if(!("soapURL".equals(key)) && !("soapAction".equals(key))
//				  && !("inputFile".equals(key))
//				  && !("outputFile".equals(key))
//				  && !("useSSL".equals(key))){
//			  System.setProperty(key, value);
//			  
//		  }
//	  }  
 

//	  if (this.prop.containsKey("system_prop_count")) {
//	      for (int i = 0; i < Integer.parseInt(this.prop.getProperty("system_prop_count")); i++)
//	        System.setProperty(this.prop.getProperty("system_prop_name_" + String.valueOf(i)), 
//	          this.prop.getProperty("system_prop_value_" + String.valueOf(i)));
//	    }
//	    else {
//	      System.setProperty("java.protocol.handler.pkgs", "com.ibm.net.ssl.www2.protocol");
//	      System.setProperty("javax.net.ssl.keyStore", System.getProperty("user.dir") + File.separator + "csi_keystore.jks");
//	      System.setProperty("javax.net.ssl.keyStorePassword", "C3Ktest");
//	    }
	  	System.out.println("# Set system properties:");
	    System.out.println("java.protocol.handler.pkgs = "+System.getProperty("java.protocol.handler.pkgs"));
	    System.out.println("javax.net.ssl.keyStore = "+System.getProperty("javax.net.ssl.keyStore"));
	    System.out.println("javax.net.ssl.keyStorePassword = "+System.getProperty("javax.net.ssl.keyStorePassword"));
  }

}
