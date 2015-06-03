
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String cameraId = request.getParameter("camera_id");
      String type = request.getParameter("camera_type");	
      String manufacturer = request.getParameter("manufacturer");		
      String model = request.getParameter("model");		
      String serial = request.getParameter("serial");		

      Network network = NetworkFactory.newInstance();
      Camera camera = network.getCameraById(Integer.parseInt(cameraId));
      camera.setType(type);
      camera.setManufacturer(manufacturer);
      camera.setModel(model);
      String oldSerial =  camera.getSerialNumber();
      camera.setSerialNumber(serial);
      network.update(camera);  

      if (!serial.equals(oldSerial)) {
          // update the collections
          Session hsession = network.getSession();	
          Query query = hsession.createQuery("FROM CameraTrapPhotoCollection as col WHERE col.cameraTrap.site.id="+camera.getSite().getId()+" AND cameraSerialNumber='"+oldSerial+"'");
          List<CameraTrapPhotoCollection> collections = query.list();
          for (CameraTrapPhotoCollection collection : collections) {
              collection.setCameraSerialNumber(serial);
	      network.update(collection);
          }
      }

      network.close();
    
%>