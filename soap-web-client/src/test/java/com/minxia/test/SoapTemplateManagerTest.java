package com.minxia.test;

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

public class SoapTemplateManagerTest {

	private static File file = new File("src/main/resources/soap_templates.obj");
	/** 
	 * @author Min
	 * @date Aug 20, 2014
	 * @return void 
	 * @throws 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SoapForm form = new SoapForm();
		Collection<SoapForm> formList = new CopyOnWriteArrayList<SoapForm>();
		form.setUrl("www.test.com");
		form.setAction("test");		
		formList.add(form);
		
//		writeFile(formList);
		
		Collection<SoapForm> rmap = readFile();
		System.out.println(((CopyOnWriteArrayList<SoapForm>)rmap).get(0).toString());

	}
	
	 public static void writeFile(Collection<SoapForm> list) {
	        try {
	            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
	            out.writeObject(list);
	            out.close();
	        } catch (FileNotFoundException e) {            
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	     
	    @SuppressWarnings("unchecked")
	    public static Collection<SoapForm> readFile() {
	    	Collection<SoapForm> list = null;
	        try {
	            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
	            list = (Collection<SoapForm>)in.readObject();
	            in.close();            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return list;
	    }

}
