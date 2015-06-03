

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("project_id");

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(id));
      List<Person> persons = network.getPersons(site);

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeNumberField("totalCount", persons.size());
      jg.writeArrayFieldStart("result");
      for (Person person : persons) {
         jg.writeStartObject();
      	 jg.writeNumberField("id", person.getId());
         jg.writeStringField("first_name", person.getFirstName());
         jg.writeStringField("last_name", person.getLastName());
         jg.writeStringField("email", person.getEmail());

	 String[] roles = person.getRoles();
	 String roleStr = "";
	 for (String role : roles) {
	     if (roleStr.equals("")) {
	         roleStr += role;
	     } else {
	         roleStr += ","+role;
             }
	 }

         jg.writeStringField("roles", roleStr);
         //jg.writeStringField("institution", person.getInstitution());
         jg.writeEndObject();
      }
      jg.writeEndArray();	
      jg.writeEndObject();
      network.close();
      jg.close();
      sw.flush();
      out.println(sw.toString());
    
%>