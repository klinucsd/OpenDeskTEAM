
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
      Network network = NetworkFactory.newInstance();
      SamplingEvent event = network.getSamplingEventById(Integer.parseInt(eventId));	
      network.deleteObject(event);

      // find the collections for this event
      Session hsession = network.getSession();	
      Query query = hsession.createQuery("FROM CameraTrapPhotoCollection as col WHERE col.cameraTrap.site.id="+event.getSite().getId()+" AND event='"+event.getEvent()+"'");
      List<CameraTrapPhotoCollection> collections = query.list();
      for (CameraTrapPhotoCollection collection : collections) {
         for (CameraTrapPhoto photo : collection.getPhotos()) {
             CameraTrapPhotoMetadata metadata = photo.getMetadata();
             hsession.delete(metadata);

	     for (PhotoSpeciesRecord record: photo.getRecords()) {
	         hsession.delete(record);
	     }

	     hsession.delete(photo);
	 }
	 network.delete(collection);
      }

      // delete all folders for this event
      String root = pageContext.getServletContext().getRealPath("/");

      File imageDir = new File(root, "image-repository");
      File thumbnailDir = new File(root, "thumbnails");

      File projImageDir = new File(imageDir, event.getSite().getName());
      File projThumbnailDir = new File(thumbnailDir, event.getSite().getName());

      File projImageEventDir = new File(projImageDir, event.getEvent());
      File projThumbnailEventDir = new File(projThumbnailDir, event.getEvent());

      if (projImageEventDir.exists()) {
          Util.deleteDir(projImageEventDir);
      }

      if (projThumbnailEventDir.exists()) {
          Util.deleteDir(projThumbnailEventDir);
      }

      network.close();
    
%>