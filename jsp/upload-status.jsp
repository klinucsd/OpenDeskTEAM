
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
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


<%
     Network network = null;
     try {
         String username = request.getParameter("username");
         String password = request.getParameter("password");
         String trap = request.getParameter("trap");
         String event = request.getParameter("event");

	 network = NetworkFactory.newInstance();
	 CameraTrap ct = (CameraTrap)network.getSamplingUnitByName(trap);
	 CameraTrapUploadJob job = network.getCameraTrapUploadJob(ct, event);

	 if (job.getCurrentIndex() < job.getTotalNumber()) {

             // get the directory for the image repository
             String appDir = pageContext.getServletContext().getRealPath("/");
             File imgRoot = new File(appDir, "image-repository");

	     int pos = trap.lastIndexOf("-");
	     trap = trap.substring(0, pos);
	     pos = trap.lastIndexOf("-");
	     String blockIndex = trap.substring(pos+1);	  

             String imgPath = imgRoot.getAbsolutePath()+"/"+ct.getSite().getName()+"/"+event+"/Array"+blockIndex+"/"+ct.getName();
	     File imgDir = new File(imgPath);
	     String[] imgNames = imgDir.list();

             out.println(" { done : false, ");
	     out.println("   percentage: "+Math.floor(((float)job.getCurrentIndex()/(float)job.getTotalNumber()) * 100)/ 100+", ");
	     out.println("   strPercent: 'Uploaded "+job.getCurrentIndex()+"/"+job.getTotalNumber()+" Files',");
	     out.println("   fileName: '"+imgNames[job.getCurrentIndex()]+"'");
	     out.println(" }");
         } else {
             out.println(" { done : true }");
	 }
     } catch (Exception ex) {
         ex.printStackTrace();
         out.println("{ 'failed':true, resson: '"+ex.getMessage()+"' }");
     } finally {
         if (network != null)  network.close();
     }
%>
