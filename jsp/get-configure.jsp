<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<%
     String path = request.getParameter("path");
     File file = new File(path);

     out.println("{ result: [");
     String[] list = file.list();
     boolean first = true;
     for (int i=0; i<list.length; i++) {
         if (!list[i].endsWith(".tcf")) continue;
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
