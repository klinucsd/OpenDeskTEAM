

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("project_id");

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(id));
      List<SamplingEvent> events = network.getSamplingEvents(site);

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeNumberField("totalCount", events.size());
      jg.writeArrayFieldStart("result");
      for (SamplingEvent event : events) {
         jg.writeStartObject();
      	 jg.writeNumberField("id", event.getId());
         jg.writeStringField("name", event.getEvent());
         jg.writeEndObject();
      }
      jg.writeEndArray();	
      jg.writeEndObject();
      network.close();
      jg.close();
      sw.flush();
      out.println(sw.toString());
    
%>