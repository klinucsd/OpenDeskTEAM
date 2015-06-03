<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>

<%
     String path = request.getParameter("path");
     
     if (path != null && path.startsWith("//")) {
        path = path.substring(2);

        String root = pageContext.getServletContext().getRealPath("/");
        File datahome = new File(root+"/WEB-INF/datahome.txt");
	PrintWriter pw = new PrintWriter(new FileWriter(datahome));
	pw.println(path);
	pw.close();

	out.println(" { path: '"+path+"' }");
     }

%>