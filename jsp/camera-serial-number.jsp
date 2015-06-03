<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%
     String siteId = request.getParameter("siteId");
     Network network = NetworkFactory.newInstance();
     Site site = network.getSiteById(new Integer(siteId));	
     List<Camera> cameras = network.getCameras(site);
     network.close();

     out.println("{ results: [");
     int k = 0;
     for (Camera camera : cameras ) {
	if (k > 0) out.println("      ,");
	out.println("  {  name: '"+camera.getSerialNumber()+"' }" ); 
	k++;
     }
     out.println("]}");
%>



