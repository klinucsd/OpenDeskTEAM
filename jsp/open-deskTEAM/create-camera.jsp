

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String projectId = request.getParameter("project_id");
      String type = request.getParameter("camera_type");	
      String manufacturer = request.getParameter("manufacturer");		
      String model = request.getParameter("model");		
      String serial = request.getParameter("serial");		

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(projectId));
 
      // create a camera
      Camera camera = new Camera();
      camera.setType(type);
      camera.setManufacturer(manufacturer);
      camera.setModel(model);
      camera.setSerialNumber(serial);
      camera.setSite(site);
      network.save(camera);

      out.println("{ camera_id: "+camera.getId()+" }"); 
      network.close();    
%>