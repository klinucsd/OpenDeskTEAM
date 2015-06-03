

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
      String projectId = request.getParameter("project_id");
      String arrayId = request.getParameter("array_id");

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(projectId));
      Block block = network.getBlockById(Integer.parseInt(arrayId));

      // process the related collection
      Set<String> events = new TreeSet<String>(); 

      Session hsession = network.getSession();	
      Query query = hsession.createQuery("FROM CameraTrapPhotoCollection as col WHERE col.cameraTrap.block.id="+block.getId());
      List<CameraTrapPhotoCollection> collections = query.list();
      for (CameraTrapPhotoCollection collection : collections) {

         events.add(collection.getEvent());
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

      // delete all traps in the array
      List<SamplingUnit> traps = network.getSamplingUnits(block);
      for (SamplingUnit trap : traps) {
	  network.deleteObject(trap);
      }

      // delete the array
      network.deleteObject(block);

      // delete assocaited folder
      String root = pageContext.getServletContext().getRealPath("/");

      File imageDir = new File(root, "image-repository");
      File thumbnailDir = new File(root, "thumbnails");

      File projImageDir = new File(imageDir, site.getName());
      File projThumbnailDir = new File(thumbnailDir, site.getName());

      for (String event : events) {
          File dir = new File(projImageDir, event+"/Array"+block.getIndex());
	  if (dir.exists()) {
              Util.deleteDir(dir);
          }

          dir = new File(projThumbnailDir, event+"/Array"+block.getIndex());
	  if (dir.exists()) {
              Util.deleteDir(dir);
          }
      }

      network.close();
    
%>