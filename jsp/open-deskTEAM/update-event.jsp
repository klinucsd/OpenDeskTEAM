
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
      String eventId = request.getParameter("event_id");
      String name = request.getParameter("name");	

      Network network = NetworkFactory.newInstance();
      SamplingEvent event = network.getSamplingEventById(Integer.parseInt(eventId));
      String oldEvent = event.getEvent();
      event.setEvent(name);
      network.update(event);  

      // update the events in the collections
      Session hsession = network.getSession();	
      Query query = hsession.createQuery("FROM CameraTrapPhotoCollection as col WHERE col.cameraTrap.site.id="+event.getSite().getId()+" AND event='"+oldEvent+"'");
      List<CameraTrapPhotoCollection> collections = query.list();
      for (CameraTrapPhotoCollection collection : collections) {
         collection.setEvent(name);
	 network.update(collection);
      }

      // change the folder name
      String root = pageContext.getServletContext().getRealPath("/");

      File imageDir = new File(root, "image-repository");
      File thumbnailDir = new File(root, "thumbnails");

      File projImageDir = new File(imageDir, event.getSite().getName());
      File projThumbnailDir = new File(thumbnailDir, event.getSite().getName());

      File projImageEventDir = new File(projImageDir, oldEvent);
      File projThumbnailEventDir = new File(projThumbnailDir, oldEvent);

      File projImageEventDirNew = new File(projImageDir, name);
      File projThumbnailEventDirNew = new File(projThumbnailDir, name);

      projImageEventDir.renameTo(projImageEventDirNew);
      projThumbnailEventDir.renameTo(projThumbnailEventDirNew);

      network.close();
    
%>