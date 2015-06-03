

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String projectId = request.getParameter("project_id");
      String firstName = request.getParameter("first_name");	
      String lastName = request.getParameter("last_name");		
      String email = request.getParameter("email");	
      String roles = request.getParameter("roles");	

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(projectId));
 
      Person person = network.getPersonByEmail(email);
      if (person == null) {
          // create a person
          person = new Person();
          Integer minPersonId = network.getMinPersonId();	
          person.setId(minPersonId);
          person.setUserName("default");
          person.setFirstName(firstName);
          person.setLastName(lastName);
          person.setEmail(email);
	  person.setRole(roles);
          Set<Site> sites = new HashSet<Site>();
          sites.add(site);
          person.setSites(sites);
          network.save(person);
      } else {
          person.setFirstName(firstName);
          person.setLastName(lastName);
          person.setEmail(email);
	  person.setRole(roles);
          Set<Site> sites = person.getSites();
          sites.add(site);
          person.setSites(sites);
          network.update(person);
      }
      network.close();    

      out.println("{ person_id: "+person.getId()+" }"); 

%>