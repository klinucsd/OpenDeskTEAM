

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String arrayId = request.getParameter("array_id");

      Network network = NetworkFactory.newInstance();
      Block block = network.getBlockById(Integer.parseInt(arrayId));	
      List<SamplingUnit> trapList = network.getSamplingUnits(block);
      TreeSet<SamplingUnit> traps = new TreeSet<SamplingUnit>(new SamplingUnitNameComparator2());
      traps.addAll(trapList);

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeNumberField("totalCount", traps.size());
      jg.writeArrayFieldStart("result");
      for (SamplingUnit trap : traps) {
         jg.writeStartObject();
      	 jg.writeNumberField("id", trap.getId());
         jg.writeStringField("name", trap.getName());

	 Double lat = trap.getLatitude();
     	 if (lat != null) jg.writeNumberField("lat", lat);

	 Double lon = trap.getLongitude();
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