package com.minxia.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.Map;

public class HashtableProxyTest {

    public static void main(String[] args) {
        Map<String, String> table = new Hashtable<String, String>();
        HashtableHandler handler = new HashtableHandler();
        table = handler.bind(table);
        
        table.put("abc", "abc");
        Map t1 = WriteFile.readFile();
        System.out.println(t1.size());
        
        m(table);
        t1 = WriteFile.readFile();
        System.out.println(t1.size());
        
        String abc = table.get("abc");        
        t1 = WriteFile.readFile();
        System.out.println(t1.size());
        System.out.println(abc);
        
        table.remove("abc");
        t1 = WriteFile.readFile();
        System.out.println(t1.size());
        
        table.clear();
        t1 = WriteFile.readFile();
        System.out.println(t1.size());
    }   
    
    public static void m(Map<String, String> map) {
        map.put("123", "123");
    }
}

class WriteFile {
    private static File file = new File("Hashtable.obj");
    public static void writeFile(Map<String, String> map) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(map);
            out.close();
        } catch (FileNotFoundException e) {            
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, String> readFile() {
        Map map = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            map = (Map<String, String>)in.readObject();
            in.close();            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }
}

class HashtableHandler implements InvocationHandler {
    private Map<String, String> table = null;
    
    @SuppressWarnings("unchecked")
    public Map<String, String> bind(Map<String, String> map) {
        table = map;
        Map<String, String> mapProxy = (Map<String, String>)Proxy.newProxyInstance(
                table.getClass().getClassLoader(),
                table.getClass().getInterfaces(),
                this                
            );
        return mapProxy;        
    }
    
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object obj = null;        
        // 调用原有的方法
         obj = method.invoke(table, args);        
        String name = method.getName();
        String changeMethod = "(clear)|(put)|(putAll)|(remove)";
        // 当调用的方法为更改 map 中值的方法时，写入文件中
         if(name.matches(changeMethod)) {
            WriteFile.writeFile(table);
        }
        return obj;
    }
}