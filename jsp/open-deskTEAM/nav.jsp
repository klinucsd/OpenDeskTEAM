
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%

      String text = request.getParameter("text");
      String path = request.getParameter("path");

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);
      jg.writeStartArray();
      
      if (path != null && path.equals("/")) {

         // node for projects 
         jg.writeStartObject();
      	 jg.writeStringField("text", "Projects");
         jg.writeStringField("path", "/Projects");
         jg.writeStringField("icon", "images/icons/medal.png");
         jg.writeBooleanField("leaf", false);
         jg.writeEndObject();

	 /*
	 // node for persons
         jg.writeStartObject();
      	 jg.writeStringField("text", "People");
         jg.writeStringField("path", "/People");
         jg.writeStringField("icon", "images/icons/group.png");
         jg.writeBooleanField("leaf", false);
         jg.writeEndObject();

	 // node for cameras
         jg.writeStartObject();
      	 jg.writeStringField("text", "Cameras");
         jg.writeStringField("path", "/Cameras");
         jg.writeStringField("icon", "images/icons/camera.gif");
         jg.writeBooleanField("leaf", false);
         jg.writeEndObject();
	 */

      } else if (path != null && path.equals("/Projects")) {

          Network network = NetworkFactory.newInstance();
          List<Site> sites = network.getSites();
          for (Site site : sites) {
              jg.writeStartObject();
      	      jg.writeStringField("text", "<b>"+site.getName()+"</b>");
              jg.writeStringField("path", "/Projects/"+site.getId());
              jg.writeStringField("icon", "images/icons/cone.png");	
              jg.writeBooleanField("leaf", false);
              jg.writeEndObject();
          }
          network.close();

      } else if (path != null && path.startsWith("/Projects/")) {
          path = path.substring(10);
	  String[] elements = path.split("/");
	  if (elements.length == 1) {
              jg.writeStartObject();
      	      jg.writeStringField("text", "Camera Trap Arrays");
              jg.writeStringField("path", "/Projects/"+elements[0]+"/Arrays");
              jg.writeStringField("icon", "images/icons/arrow-right.gif");
              jg.writeBooleanField("leaf", false);
              jg.writeEndObject();

              jg.writeStartObject();
      	      jg.writeStringField("text", "Events");
              jg.writeStringField("path", "/Projects/"+elements[0]+"/Events");
              jg.writeStringField("icon", "images/icons/arrow-right.gif");
              jg.writeBooleanField("leaf", true);
              jg.writeEndObject();

              jg.writeStartObject();
      	      jg.writeStringField("text", "Cameras");
              jg.writeStringField("path", "/Projects/"+elements[0]+"/Cameras");
              jg.writeStringField("icon", "images/icons/arrow-right.gif");
              jg.writeBooleanField("leaf", true);
              jg.writeEndObject();

              jg.writeStartObject();
      	      jg.writeStringField("text", "Institutions");
              jg.writeStringField("path", "/Projects/"+elements[0]+"/Institutions");
              jg.writeStringField("icon", "images/icons/arrow-right.gif");
              jg.writeBooleanField("leaf", true);
              jg.writeEndObject();

              jg.writeStartObject();
      	      jg.writeStringField("text", "People");
              jg.writeStringField("path", "/Projects/"+elements[0]+"/People");
              jg.writeStringField("icon", "images/icons/arrow-right.gif");
              jg.writeBooleanField("leaf", true);
              jg.writeEndObject();

	  } else if (elements.length == 2) {

	      if (elements[1].equals("Arrays")) {
	          Network network = NetworkFactory.newInstance();
                  Site site = network.getSiteById(Integer.parseInt(elements[0]));
	          List<Block> blocks = network.getBlocks(site);
	          for (Block block : blocks) {
                      jg.writeStartObject();
      	              jg.writeStringField("text", "Array: "+block.getName());
                      jg.writeStringField("path", "/Projects/"+site.getId()+"/Arrays/"+block.getId());
                      jg.writeStringField("icon", "images/icons/network.png");
                      jg.writeBooleanField("leaf", false);
                      jg.writeEndObject();
	          }
	          network.close();
	      } else if (elements[1].equals("Events")) {

	      }
	  } else if (elements.length == 3) {
	      Network network = NetworkFactory.newInstance();
              Site site = network.getSiteById(Integer.parseInt(elements[0]));
	      Block block = network.getBlockById(Integer.parseInt(elements[2]));
              Protocol tv = network.getProtocol(4);		
	      List<SamplingUnit> trapList = network.getSamplingUnits(block);
              TreeSet<SamplingUnit> traps = new TreeSet<SamplingUnit>(new SamplingUnitNameComparator2());
              traps.addAll(trapList);
	      for (SamplingUnit trap : traps) {
                  jg.writeStartObject();
      	          jg.writeStringField("text", "Trap: "+trap.getName());
                  jg.writeStringField("path", "/Projects/"+site.getId()+"/"+block.getId()+"/"+trap.getId());
                  jg.writeStringField("icon", "images/icons/camera.gif");
                  jg.writeBooleanField("leaf", true);
                  jg.writeEndObject();
	      }
	  }
      }

      jg.writeEndArray();	
      jg.close();
      sw.flush();
      out.println(sw.toString());
   
%>