
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
	 CameraTrap ct = (CameraTrap)network.getSamplingUnitByName(trap);
	 Site site = ct.getSite();
	 Integer pid = site.getPublicId();
	 CameraTrapUploadJob job = network.getCameraTrapUploadJob(ct, event);

	 if (job != null) {
	     CameraTrapPhotoCollection collection = network.getCameraTrapPhotoCollection(event, trap); 
	     Integer cid = collection.getPublicId();
	     if (pid != null && cid != null) {
                 Set<CameraTrapPhoto> photos = collection.getPhotos();
	         for (CameraTrapPhoto photo : photos) {	 
	             Integer poid = photo.getPublicId();
	             if (poid != null) {
	                 CAS.deleteImage(pid.intValue(), cid.intValue(), poid.intValue());
                         photo.setPublicId(null);
		         network.getSession().update(photo);	     
	             }
	         }

	         CAS.deleteDeployment(pid.intValue(), cid.intValue());
                 collection.setPublicId(null);
                 network.getSession().update(collection);
	     }
	     network.getSession().delete(job);
             out.println("{ 'success':true, 'action':true }");
	 } else {
             out.println("{ 'success':true, 'action':false }");
	 }
	 
	 network.close();

     } catch (Exception ex) {
         ex.printStackTrace();
         out.println("{ 'success':false, reason: '"+ex.getMessage()+"' }");
     } finally {
         if (session1 != null) session1.close();
     }
%>
