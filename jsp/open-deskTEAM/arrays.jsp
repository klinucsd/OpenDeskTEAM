

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("id");

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(id));
      List<Block> blocks = network.getBlocks(site);

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeNumberField("totalCount", blocks.size());
      jg.writeArrayFieldStart("result");
      for (Block block : blocks) {
         jg.writeStartObject();
      	 jg.writeNumberField("id", block.getId());
         jg.writeStringField("name", block.getName());

	 Double lat = block.getPoint1Latitude();
     	 if (lat != null) jg.writeNumberField("lat", lat);

	 Double lon = block.getPoint1Longitude();
     	 if (lon != null) jg.writeNumberField("lon", lon);

         jg.writeEndObject();
      }
      jg.writeEndArray();	
      jg.writeEndObject();
      network.close();
      jg.close();
      sw.flush();
      out.println(sw.toString());
    
%>