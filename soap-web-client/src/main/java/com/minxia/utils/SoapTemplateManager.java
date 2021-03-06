package com.minxia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import com.minxia.model.SoapForm;

public class SoapTemplateManager {
	
	private static String path = "C:/Work/soap_templates.obj";
	
//	private static String path = "/appl/ntelagent/6080/bis/soap_templates.obj";

	private static File file = new File(path);
	/**
	 * @throws Exception  
	 * @author Min
	 * @date Aug 20, 2014
	 * @return void 
	 * @throws 
	 */
	
	 public static synchronized void writeFile(Collection<SoapForm> list) throws Exception {
		 ObjectOutputStream out = null;
	        try {
	            out = new ObjectOutputStream(new FileOutputStream(file));
	            out.writeObject(list);
	        } catch (Exception e) {  
	        	throw e;
	        } finally{
	        	out.close();
	        }
	    }
	     
	    @SuppressWarnings("unchecked")
	    public static Collection<SoapForm> readFile() throws Exception {
	    	Collection<SoapForm> list = null;
	    	ObjectInputStream in = null;
	        try {
	            in = new ObjectInputStream(new FileInputStream(file));
	            list = (Collection<SoapForm>)in.readObject();
	        } catch (Exception e) {
	        	throw e;
	        } finally{
	        	in.close();
	        }
	        return list;
	    }
	    
	    public static boolean isTempatesExsit(){
	    	if(file.exists()){
	    		return true;
	    	}
	    	return false;
	    }
}
