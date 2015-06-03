

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String personId = request.getParameter("person_id");
      String firstName = request.getParameter("first_name");	
      String lastName = request.getParameter("last_name");		
      String email = request.getParameter("email");	
      String roles = request.getParameter("roles");	

      Network network = NetworkFactory.newInstance();
      Person person = network.getPersonById(Integer.parseInt(personId));
      person.setFirstName(firstName);
      person.setLastName(lastName);
      person.setEmail(email);
      person.setRole(roles);	
      network.update(person);  
      network.close();
    
%>