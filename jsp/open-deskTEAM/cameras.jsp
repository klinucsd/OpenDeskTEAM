

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("project_id");

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(id));
      List<Camera> cameras = network.getCameras(site);

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeNumberField("totalCount", cameras.size());
      jg.writeArrayFieldStart("result");
      for (Camera camera : cameras) {
         jg.writeStartObject();
      	 jg.writeNumberField("id", camera.getId());
         jg.writeStringField("type", camera.getType());
         jg.writeStringField("manufacturer", camera.getManufacturer());
         jg.writeStringField("model", camera.getModel());
         jg.writeStringField("serial", camera.getSerialNumber());
         jg.writeEndObject();
      }
      jg.writeEndArray();	
      jg.writeEndObject();
      network.close();
      jg.close();
      sw.flush();
      out.println(sw.toString());
    
%>