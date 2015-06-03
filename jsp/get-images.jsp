<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="net.coobird.thumbnailator.Thumbnails" %>

<%
	/*
        int mb = 1024*1024;
         
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
         
        System.out.println("##### Heap utilization statistics [MB] #####");
         
        //Print used memory
        System.out.println("Used Memory:"
            + (runtime.totalMemory() - runtime.freeMemory()) / mb);
 
        //Print free memory
        System.out.println("Free Memory:"
            + runtime.freeMemory() / mb);
         
        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb);
 
        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb);
	*/

     out.println("{ images: [");

     String trapName = request.getParameter("trapName");
     String event = request.getParameter("event");
     String raw = request.getParameter("raw");
     String path = request.getParameter("path");
     //String array = request.getParameter("array");     
     //if (array != null) array = array.substring(5);

     String root = pageContext.getServletContext().getRealPath("/");
     File thumbnailDir = new File(root, "thumbnails");
     File rootDir = new File(root, "image-repository");

     Network network = NetworkFactory.newInstance();
     CameraTrapPhoto photo = network.getCameraTrapPhoto(trapName, event, raw);

     /*
     Session session1 = network.getSession();		    
     Query query = session1.createQuery("FROM CameraTrapPhoto as photo "+
     	   	   			"WHERE photo.collection.cameraTrap.name='"+trapName+"'"+
					" AND photo.collection.cameraTrap.block.index="+array+	
                                        " AND photo.collection.event='"+event+"'"+
                                        " AND photo.rawName='"+raw+"'");
     List<CameraTrapPhoto> list = query.list();
     CameraTrapPhoto photo = null;
     if (!list.isEmpty()) photo = list.get(0);
     */

     Set<CameraTrapPhoto> gallery = new HashSet<CameraTrapPhoto>();
     if (photo != null) {
     	 gallery = network.getCameraTrapPhotoCluster(photo);
	 int k = 0;
	 for (CameraTrapPhoto tmp : gallery) {

	     CameraTrapPhotoType type = tmp.getType();
	     Person person = tmp.getTypeIdentifiedBy();
	     String typeName = "";
             if (type != null && person == null) {
	     	typeName = type.getName();
	     } 

	     if (k > 0) {
	        out.println(","); 
	     } else {
	        k++;
	     }

	     /*
             out.println("{name:'"+tmp.getRawName()+"', "+
	     		 "size:500, "+
			 "lastmod:'"+tmp.getTakenTime()+"', "+
			 "annotated:"+(tmp.getType() != null)+", "+
			 "deleted: false, "+
			 "event: '"+event+"', "+
			 "trap: '"+trapName+"', "+
			 "type: '"+typeName+"', "+
			 "url:'image-repository"+path.replaceAll(raw, tmp.getRawName())+"'}");
	      */


             out.println("{name:'"+tmp.getRawName()+"', "+
	     		 "size:500, "+
			 "lastmod:'"+tmp.getTakenTime()+"', "+
			 "annotated:"+(tmp.getType() != null)+", "+
			 "deleted: false, "+
			 "event: '"+event+"', "+
			 "trap: '"+trapName+"', "+
			 "type: '"+typeName+"', "+
			 "url:'thumbnails"+path.replaceAll(raw, tmp.getRawName())+"'}");

	      String[] elements = path.replaceAll(raw, tmp.getRawName()).split("/");
	      File thumbnail = thumbnailDir;
	      File photoFile = rootDir;
	      File tmpDir = thumbnail;
              for (int x=0; x<elements.length; x++) {
	          //System.out.println(x+" ---> "+elements[x]+"   "+java.net.URLDecoder.decode(elements[x]));
		  thumbnail = new File(thumbnail, java.net.URLDecoder.decode(elements[x]));
		  photoFile = new File(photoFile, java.net.URLDecoder.decode(elements[x]));

		  if (x < elements.length-1) {
		      tmpDir = thumbnail;
		  }
	      }

	      if (!thumbnail.exists()) {
	          if (!tmpDir.exists()) tmpDir.mkdirs(); 
	          
	          // create the thumbnail
	          Thumbnails.of(photoFile).size(100, 100).toFile(new File(tmpDir, thumbnail.getName()));
	      }

	 }
     }
     network.close();

     out.println("]}");

%>



