

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
      String trapId = request.getParameter("trap_id");
      Network network = NetworkFactory.newInstance();
      SamplingUnit trap = network.getSamplingUnitById(Integer.parseInt(trapId));	

      // process the related collection
      List<String> events = new ArrayList<String>(); 
      List<String> arrays = new ArrayList<String>(); 
      List<String> traps = new ArrayList<String>(); 

      Session hsession = network.getSession();	
      Query query = hsession.createQuery("FROM CameraTrapPhotoCollection as col WHERE col.cameraTrap.id="+trap.getId());
      List<CameraTrapPhotoCollection> collections = query.list();
      for (CameraTrapPhotoCollection collection : collections) {

         events.add(collection.getEvent());
         arrays.add("Array"+collection.getCameraTrap().getBlock().getIndex());
	 traps.add(collection.getCameraTrap().getName());	 

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

      network.deleteObject(trap);

      // delete assocaited folder
      String root = pageContext.getServletContext().getRealPath("/");

      File imageDir = new File(root, "image-repository");
      File thumbnailDir = new File(root, "thumbnails");

      File projImageDir = new File(imageDir, trap.getSite().getName());
      File projThumbnailDir = new File(thumbnailDir, trap.getSite().getName());

      for (int i=0; i<events.size(); i++) {
          File dir = new File(projImageDir, events.get(i)+"/"+arrays.get(i)+"/"+traps.get(i));
	  if (dir.exists()) {
              Util.deleteDir(dir);
          }

          dir = new File(projThumbnailDir, events.get(i)+"/"+arrays.get(i)+"/"+traps.get(i));
	  if (dir.exists()) {
              Util.deleteDir(dir);
          }
      }

      network.close();
    
%>