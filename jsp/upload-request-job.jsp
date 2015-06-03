
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="org.apache.http.*" %>
<%@ page import="org.apache.http.message.*" %>
<%@ page import="org.apache.http.protocol.*" %>
<%@ page import="org.apache.http.client.*" %>
<%@ page import="org.apache.http.client.entity.*" %>
<%@ page import="org.apache.http.cookie.*" %>
<%@ page import="org.apache.http.impl.client.*" %>
<%@ page import="org.apache.http.client.methods.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>


<%
     Network network = null;
     Session session1 = null;
     try {
         String username = request.getParameter("username");
         String password = request.getParameter("password");
         String trap = request.getParameter("trap");
         String event = request.getParameter("event");

         //System.out.println("----> "+username+"  "+password+"   "+trap+"   "+event);

	 network = NetworkFactory.newInstance();
         session1 = network.getSession();
	 CameraTrap ct = (CameraTrap)network.getSamplingUnitByName(trap);
	 CameraTrapUploadJob job = network.getCameraTrapUploadJob(ct, event);
	 Person uploader = network.getPersonByUsername(username);

	 /*
	 String tmp = network.validateExportCameraTrapPhotos(event, ct);
	 if (tmp != null) {
	    String msg = "";
	    if (tmp.indexOf("start: false") != -1) {
	        msg = "No start image";
	    } else if (tmp.indexOf("unprocessed") != -1) {
	        msg = "Could not upload the data that have images not annotated.";
	    }
	    
	    out.println("{ 'success':false, reason: '"+msg+"' }");
	    return;
         }
	 */

	 int totalNumber = network.getCameraTrapPhotoTotalSize1(ct, event);

	 /*
	 int totalNumber = 0;
	 CameraTrapPhotoCollection collection = network.getCameraTrapPhotoCollection(event, trap);	
	 Set<CameraTrapPhoto> photos = collection.getPhotos();
	 for (CameraTrapPhoto photo : photos) {	 
	     Integer poid = photo.getPublicId();
	     if (poid == null && photo.getType() != null) {
	         totalNumber++;
	     }
         }
	 */

	 if (job == null) {
	     //job = UploadUtil.newRemoteCameraTrapUploadJob(username, password, ct, event, uploader, totalNumber);
	     job = new CameraTrapUploadJob(ct, event, uploader, totalNumber);
	     if (job != null) {
	         session1.save(job);
                 out.println("{ 'success':true, type:'new' }");
	     } else {
                 out.println("{ 'success':false, reason: 'Could not upload the data. Maybe the data is uploading/uploaded.' }");
	     }
	 } else if (job.isFinished()) {
	     out.println("{ 'success':true, type:'finished' }");
	 } else {
	     out.println("{ 'success':true, type:'old' }");
	 }

     } catch (Exception ex) {
         ex.printStackTrace();
         out.println("{ 'success':false, reason: '"+ex.getMessage()+"' }");
     } finally {
         if (session1 != null) session1.close();
     }
%>
