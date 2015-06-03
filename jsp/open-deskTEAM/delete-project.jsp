

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
      String id = request.getParameter("id");
      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(id));

      Session hsession = network.getSession();	
      Query query = hsession.createQuery("FROM CameraTrapPhotoCollection as col WHERE col.cameraTrap.site.id="+id);
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

      List<Block> blocks = network.getBlocks(site);
      for (Block block : blocks) {
          List<SamplingUnit> traps = network.getSamplingUnits(block);
          for (SamplingUnit trap : traps) {
	      network.deleteObject(trap);
	  }
          network.deleteObject(block);
      }

      List<SamplingEvent> events = network.getSamplingEvents(site);
      for (SamplingEvent event : events) {
           network.deleteObject(event);
      }
    
      List<Camera> cameras = network.getCameras(site);
      for (Camera camera : cameras) {
           network.deleteObject(camera);
      }

      List<Person> persons = network.getPersons(site);
      for (Person person : persons) {          
          Set<Site> sites = new HashSet<Site>();
          for (Site tmp : person.getSites()) {
              if (!id.equals(tmp.getId()+"")) {
	          sites.add(tmp);
	      }
          }
          person.setSites(sites);
          network.save(person);
      } 
      network.deleteSite(site);  

      List<Institution> institutions = network.getInstitutions(site);	
      for (Institution institution : institutions) {
          network.deleteObject(institution);
      }

      // delete assocaited folder
      String root = pageContext.getServletContext().getRealPath("/");

      File imageDir = new File(root, "image-repository");
      File thumbnailDir = new File(root, "thumbnails");

      File projImageDir = new File(imageDir, site.getName());
      File projThumbnailDir = new File(thumbnailDir, site.getName());

      if (projImageDir.exists()) {
          Util.deleteDir(projImageDir);
      }

      if (projThumbnailDir.exists()) {
          Util.deleteDir(projThumbnailDir);
      }

      network.close();
    
%>