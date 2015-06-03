

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String arrayId = request.getParameter("array_id");
      String lat = request.getParameter("lat");
      String lon = request.getParameter("lon");

      Network network = NetworkFactory.newInstance();
      Block block = network.getBlockById(Integer.parseInt(arrayId));
      if (lat != null && !lat.equals("")) {
          block.setPoint1Latitude(Double.parseDouble(lat));
      }	 
      if (lon != null && !lon.equals("")) {
          block.setPoint1Longitude(Double.parseDouble(lon));
      }	 
      network.update(block);  
      network.close();
    
%>