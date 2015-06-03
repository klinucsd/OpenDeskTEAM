<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%

try {
     String path = request.getParameter("path");
     String event = request.getParameter("event");
     String arrayName = request.getParameter("array");
     String size = request.getParameter("size");
     String siteId = request.getParameter("siteId");

     Network network = NetworkFactory.newInstance();
     //Site site = network.getSites().get(0);
     Site site = network.getSiteById(new Integer(siteId));	
     network.close();

     // get the directory for output
     File exportDir = new File(path);

     // get the directory for the image repository
     String root = pageContext.getServletContext().getRealPath("/");
     File rootDir = new File(root, "image-repository");
     
     // get the temporary directory 
     File tmpDir = new File(root+"/WEB-INF/tmp/");

     File exportFile;
     if (arrayName != null && !arrayName.equals("")) {
	 arrayName = arrayName.substring(5);
         String zipPath = rootDir.getAbsolutePath()+"/"+site.getName()+"/"+event+"/Array"+arrayName;
	 String topName = "CT-"+site.getAbbv()+"-"+event+"-Array"+arrayName;
         exportFile = new File(tmpDir, topName+".zip");
     } else {
         String zipPath = rootDir.getAbsolutePath()+"/"+site.getName()+"/"+event;
	 String topName = "CT-"+site.getAbbv()+"-"+event;
         exportFile = new File(tmpDir, topName+".zip");
     }

     boolean start = true;
     if (exportFile.exists()) {
         start = false;
         long length = exportFile.length();
	 long sizeVal = Long.parseLong(size) * 1024 * 1024;
	 
	 double percent = sizeVal == 0 ? 0 : (float)length / (float)sizeVal;	 
	 if (percent > 1) percent = 1; 
	 String percentStr = ""+Math.floor(percent * 100);

         out.println(" { done : false, ");
	 out.println("   percentage: "+percent+", ");
	 out.println("   strPercent: '"+percentStr+"%' ");
	 out.println(" }");        
     } else if (start) {
         out.println(" { done : false, ");
	 out.println("   percentage: 0, ");
	 out.println("   strPercent: 'Starting'");
	 out.println(" }");
     } else {
         out.println(" { done : true, ");
	 out.println("   percentage: 1, ");
	 out.println("   strPercent: '100%'");
	 out.println(" }");
     }

} catch (Exception ex) {
     ex.printStackTrace();
     out.println(" { failed : true }");
}

%>