

<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      Network network = NetworkFactory.newInstance();
      List<Site> sites = network.getSites();

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeNumberField("totalCount", sites.size());
      jg.writeArrayFieldStart("result");
      for (Site site : sites) {
         jg.writeStartObject();
      	 jg.writeNumberField("id", site.getId());
         jg.writeStringField("name", site.getName());
      	 jg.writeStringField("abbrev", site.getAbbv());
      	 jg.writeStringField("objective", site.getStatus());

	 Float lat = site.getLatitude();
     	 if (lat != null) jg.writeNumberField("lat", lat);

	 Float lon = site.getLongitude();
     	 if (lon != null) jg.writeNumberField("lon", lon);

	 Integer useBait = site.getLastEventBy();
	 if (useBait != null) {
	     if (useBait.intValue() == 1) {
                 jg.writeStringField("useBait", "Yes");
             } else {
                 jg.writeStringField("useBait", "No");
	     }	
	 } else {
             jg.writeStringField("useBait", "No");
	 }

	 String bait = site.getInstName();
	 if (bait != null) {
      	     jg.writeStringField("bait", bait);
	 }
	 
	 Country country = site.getCountry();
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