<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<%
     String path = request.getParameter("path");

     String osName = System.getProperty("os.name");	
     if (osName.toLowerCase().indexOf("windows") > -1) {
        if (path != null && path.startsWith("/")) {
	    path = path.substring(1);
	}
     }

     File file = new File(path);

     out.println("{ result: [");
     String[] list = file.list();
     boolean first = true;
     for (int i=0; i<list.length; i++) {
         if (!list[i].endsWith(".tpk")) continue;
         File tmp = new File(file, list[i]);
	 if (first) {
	     first = false;
	 } else {
	     out.println(",");
	 }
	 out.println("{ name:'"+list[i]+"' }");	
     }
     out.println("]}");
%>
