<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%
     String siteId = request.getParameter("siteId");
     Network network = NetworkFactory.newInstance();
     Site site = network.getSiteById(new Integer(siteId));	
     List<Person> persons = network.getPersons(site);
     network.close();

     out.println("{ results: [");
     int k = 0;
     for (Person person : persons ) {
	if (k > 0) out.println("      ,");
	out.println("  {  id: "+person.getId()+", name: '"+person.getFirstName().replace("'", "\\'")+" "+person.getLastName().replace("'", "\\'")+"' }" ); 
	k++;
     }
     out.println("]}");
%>



