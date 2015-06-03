

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String personId = request.getParameter("person_id");
      String projectId = request.getParameter("project_id");

      Network network = NetworkFactory.newInstance();
      Person person = network.getPersonById(Integer.parseInt(personId));
   
      Set<Site> sites = new HashSet<Site>();
      for (Site site : person.getSites()) {
          if (!projectId.equals(site.getId()+"")) {
	      sites.add(site);
	  }
      }
      person.setSites(sites);
      network.save(person);
      network.close();
    
%>