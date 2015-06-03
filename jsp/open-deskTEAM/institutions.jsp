

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("project_id");
      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(id));
      List<Institution> institutions = network.getInstitutions(site); 
 
      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeNumberField("totalCount", institutions.size());
      jg.writeArrayFieldStart("result");
      for (Institution institution : institutions) {
         jg.writeStartObject();
      	 jg.writeNumberField("id", institution.getId());
         jg.writeStringField("name", institution.getName());
         jg.writeStringField("address", institution.getAddress());
         jg.writeStringField("city", institution.getCity());
         jg.writeStringField("state", institution.getState());
         jg.writeStringField("zipcode", institution.getZipcode());
         jg.writeStringField("phone", institution.getPhoneNumber());
         jg.writeStringField("email", institution.getEmail());

	 Country country = institution.getCountry();
	 if (country != null) {
     	     jg.writeStringField("country_id", ""+country.getId());
     	     jg.writeStringField("country_name", country.getName());
	 }

         jg.writeEndObject();
      }
      jg.writeEndArray();	
      jg.writeEndObject();
      network.close();
      jg.close();
      sw.flush();
      out.println(sw.toString());
    
%>