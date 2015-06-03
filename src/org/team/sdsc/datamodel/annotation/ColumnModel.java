
package org.team.sdsc.datamodel.annotation;

import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;


public class ColumnModel {

    private List<Class> classes;
    private List<ColumnHeader> columns;

    public ColumnModel(List<Class> classes) { 
	this.classes = classes;
	
	columns = new ArrayList<ColumnHeader>();
	int classIndex = 0;
	for (Class clazz : classes) {
	    TreeSet<Method> displays = new TreeSet<Method>(new MethodComparator());
		
	    // parse all fields 
	    Method[] methods = clazz.getDeclaredMethods();
	    for (int i=0; i<methods.length; i++) {
		Annotation[] annotations = methods[i].getDeclaredAnnotations();
		for (Annotation annotation : annotations){
		    if (annotation instanceof Display){
			displays.add(methods[i]);
		    }
		}
	    }	
			
	    for (Method method : displays) {
		ColumnHeader header = new ColumnHeader(clazz, method, classIndex);
		columns.add(header);
	    }
			
	    classIndex++;
	}
    }


    public String toString() {
	StringBuilder builder = new StringBuilder();
	for (ColumnHeader header : columns) {
	    builder.append("column").append("=").append(header.getColumnClass().getName()+" "+header.getColumnMethod().getName()).append("\n");			
	}
	return builder.toString();	
    }


    public String[] getHeaders() {
	List<String> strings = new ArrayList<String>();
	for (ColumnHeader header : columns) {
	    strings.add(getDisplay(header.getColumnMethod()).title());
	}
	String[] result = new String[strings.size()];
	strings.toArray(result);
	return result;
    }
	

    public String[] format(List<Object> objects) throws Exception {
	String[] results = new String[columns.size()];
	
	int k = 0;
	for (ColumnHeader column :  columns) {
	    // invoke column.getColumnMethod at objects[column.getIndex]
	    Method method = column.getColumnMethod();
	    //System.out.println("\n-----> method = "+method);
			
	    Object object = objects.get(column.getIndex());
	    //System.out.println("-----> object = "+column.getIndex() +"   "+object+"    "+object.getClass().getName());
			
	    Object result = null;
	    if (object == null) {
		results[k++] = "";
	    } else {
		try {
		    result = method.invoke(object, new Object[0]);
		} catch (Exception ex) {
		    System.out.println("\n-----> method = "+method);
		    System.out.println("-----> object = "+column.getIndex() +"   "+object+"    "+object.getClass().getName());
		    System.out.println("----> result = "+result);			
		}
	
		if (result == null) {
		    results[k++] = "";
		} else {
		    results[k++] = result.toString();
		}
	    }
	}
	return results;
    }



    public String formatJSON(List<Object> objects) {

	StringBuffer sb = new StringBuffer();
	sb.append("{ ");
	
	boolean first = true;
	for (ColumnHeader column :  columns) {

	    // invoke column.getColumnMethod at objects[column.getIndex]
	    Method method = column.getColumnMethod();
	    Object object = objects.get(column.getIndex());
	    Display display = getDisplay(method);
	    Object result = null;
	    if (object != null) {
		try {
		    result = method.invoke(object, new Object[0]);
		} catch (Exception ex) {
		}
	
		if (result != null) {
		    if (first) {
			first = false;
		    } else {
			sb.append(",\n");
		    }

		    String name = display.var();
		    if (name.length() == 0) {
			name = display.title();
		    }

		    String resultString = result.toString();
		    sb.append(name+": '"+replace(resultString)+"'");
		}
	    }
	}
	sb.append(" }");
	return sb.toString();
    }


    private String replace(String string) {

        StringBuffer sb = new StringBuffer();
        if (string == null) {
            sb.append("");
        } else {
            for (int i=0; i<string.length(); i++) {
                char c = string.charAt(i);
                if ((c == '\\') || (c == '"') || (c == '>')) {
                    sb.append('\\');
                    sb.append(c);
                } else if (c == '\b') {
                    sb.append("\\b");
                } else if (c == '\t') {
                    sb.append("\\t");
                } else if (c == '\n') {
                    sb.append("\\n");
                } else if (c == '\f') {
                    sb.append("\\f");
                } else if (c == '\r') {
                    sb.append("\\r");
                } else if (c == '\'') {
                    sb.append("\\'");
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();

    }


    static protected Display getDisplay(Method method) {
	Annotation[] annotations = method.getDeclaredAnnotations();
	for (Annotation annotation : annotations){
	    if (annotation instanceof Display){
		return (Display)annotation;
	    }
	}
	return null;
    }


    class MethodComparator implements Comparator {
	
	public int compare(Object o1, Object o2)  {
	    if (o1 instanceof Method && o2 instanceof Method) {
		Method method1 = (Method)o1;
		Method method2 = (Method)o2;
				
		Display display1 = getDisplay(method1);
		Display display2 = getDisplay(method2);
				
		if (display1.index() < display2.index()) {
		    return -1;
		} else if (display1.index() == display2.index()) {
		    return 0;
		} else {
		    return 1;
		}
	    }
			
	    return 0;
			
	}
    }

}




			