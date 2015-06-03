
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String trapId = request.getParameter("trap_id");
      String lat = request.getParameter("lat");
      String lon = request.getParameter("lon");
      String name = request.getParameter("trap_name");

      Network network = NetworkFactory.newInstance();
      SamplingUnit trap = network.getSamplingUnitById(Integer.parseInt(trapId));

      // check whether there is a trap using this new name
      SamplingUnit trap1 = network.getSamplingUnitByName(name);
      if (trap1 != null) {
          out.println("{");
     	  out.println("  message: 'Duplicate trap name:',");
     	  out.println("  array: '"+trap.getBlock().getName()+"',");
     	  out.println("  success: false");
     	  out.println("}");
	  return;
      }

      if (lat != null && !lat.equals("")) {
          trap.setLatitude(Double.parseDouble(lat));
      }	 
      if (lon != null && !lon.equals("")) {
          trap.setLongitude(Double.parseDouble(lon));
      }	 
      if (name != null && !name.equals("")) {
          String oldName = trap.getName();
	  if (!oldName.equals(name)) {
	     // rename the folder names for the camera trap
	     String root = pageContext.getServletContext().getRealPath("/");

             File imageDir = new File(root, "image-repository");
             File thumbnailDir = new File(root, "thumbnails");

             File projImageDir = new File(imageDir, trap.getSite().getName());
             File projThumbnailDir = new File(thumbnailDir, trap.getSite().getName());

	     String[] events = projImageDir.list();
	     if (events != null) {
                 for (int i=0; i<events.length; i++) {
                     File dir = new File(projImageDir, events[i]+"/Array"+trap.getBlock().getIndex()+"/"+trap.getName());
	             if (dir.exists()) {
                         File dir1 = new File(projImageDir, events[i]+"/Array"+trap.getBlock().getIndex()+"/"+name);
		         dir.renameTo(dir1);
                     }

                     dir = new File(projThumbnailDir, events[i]+"/Array"+trap.getBlock().getIndex()+"/"+trap.getName());
	             if (dir.exists()) {
		         File dir1 = new File(projThumbnailDir, events[i]+"/Array"+trap.getBlock().getIndex()+"/"+name);
		         dir.renameTo(dir1);
                     }
                  }
	      }
	  }
          trap.setName(name);
      } 
      network.update(trap);  
      network.close();
  
      out.println("{");
      out.println("  success: true");
      out.println("}");
      return;
  
%>