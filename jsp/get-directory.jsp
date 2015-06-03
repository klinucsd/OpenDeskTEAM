<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%
     String text = request.getParameter("text");
     String node = request.getParameter("node");
     String path = request.getParameter("path");

     System.out.println("text = "+text);
     System.out.println("node = "+node);
     System.out.println("path = "+path);

     //String desktopPath = System.getProperty("user.home") + "/Desktop";
     //System.out.print("desktop = "+desktopPath.replace("\\", "/"));

     //String homePath = System.getProperty("user.home");
     //File home = new File(homePath);

     if (path.equals("/")) {
         File[] roots = File.listRoots();
         out.println("[");
         boolean first = true;
	 for (int i=0; i<roots.length; i++) {
	     if (first) {
	        first = false;
	     } else {
	        out.println(",");
	     }
	     String rpath = roots[i].getAbsolutePath();
	     if (rpath.endsWith("\\")) rpath = rpath.substring(0, rpath.length()-1);
	     out.println("{ text:'"+rpath+"', path:'//"+rpath+"', leaf: false}");	
         }
         out.println("]");
	 return;
     }

     if (path.startsWith("//")) {
        path = path.substring(2)+"\\";
     }

     File file = new File(path);
     out.println("[");
     String[] list = file.list();
     boolean first = true;
     for (int i=0; i<list.length; i++) {
         if (list[i].startsWith(".")) continue;
         File tmp = new File(file, list[i]);
	 if (tmp.isDirectory()) {
	     if (first) {
	        first = false;
	     } else {
	        out.println(",");
	     }
	     out.println("{ text:'"+list[i]+"', path:'"+tmp.getAbsolutePath()+"', leaf: false}");	
         }
     }
     out.println("]");
%>
