

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String projectId = request.getParameter("project_id");
      String name = request.getParameter("name");	

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(projectId));
 
      // create an event
      SamplingEvent event = new SamplingEvent();
      event.setEvent(name);
      event.setSite(site);
      network.save(event);

      out.println("{ event_id: "+event.getId()+" }"); 
      network.close();    
%>