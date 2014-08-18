package com.minxia.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLHandshakeException;

import com.minxia.model.SoapForm;

public class SoapExecutor implements Executor {
	
	private SoapForm form;
	
	public SoapExecutor(SoapForm form)
	{
		this.form = form;
	}

	@Override
	public void Execute() {
		try {
			String soapURL = form.getUrl();
			String soapAction = form.getAction();
			String inputFile = form.getInput();
			String outputFile = form.getOutput();
			
			System.out.println("# soapURL = "+soapURL);
			System.out.println("# soapAction = "+soapAction);
			System.out.println("# inputFile = "+inputFile);
			System.out.println("# outputFile = "+outputFile);
			
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

}
