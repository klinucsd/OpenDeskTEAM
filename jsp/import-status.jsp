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

     Network network = NetworkFactory.newInstance();
     String siteId = request.getParameter("siteId");
     Site aSite = network.getSiteById(new Integer(siteId));	
     //Site aSite = network.getSites().get(0);
     network.close();

     String trapName = request.getParameter("trapName");
     String event = request.getParameter("event");
     String memoryFolder = request.getParameter("memoryFolder");

     StringTokenizer st = new StringTokenizer(trapName, "-");
     st.nextToken();
     String abbrev = st.nextToken();
     int array = Integer.parseInt(st.nextToken());
     int number = Integer.parseInt(st.nextToken());

     // get source directory
     File sourceDir = null;	
     File topDir = null;
     List<File> sourceDirs = new ArrayList<File>();

     String osName = System.getProperty("os.name");
     if (osName.toLowerCase().indexOf("windows") > -1) {

	 int index = memoryFolder.indexOf(":");
	 if (index != -1) {
	        memoryFolder = memoryFolder.substring(0, index+1)+"\\"+memoryFolder.substring(index+1);
                //System.out.println("-------> windows memory folder = "+memoryFolder);
	 }

         topDir = new File(memoryFolder);
     } else {
         topDir = new File(memoryFolder, "DCIM");
	 //topDir = new File("/Users/kailin/Desktop/misc/cards/card1/DCIM");
         //topDir = new File("/Users/kailin/Desktop/misc/cards/card2/DCIM");	
         //topDir = new File("/Users/kailin/Desktop/misc/cards/card3/DCIM");	
     }

     if (topDir != null && topDir.exists()) {
	 String[] sndFolders = topDir.list();
	 for (int i=0; i<sndFolders.length; i++) {
	    if (sndFolders[i].endsWith("RECNX")) {
	       if (sourceDir == null) {
		   sourceDir = new File(topDir, sndFolders[i]);
		   //System.out.println("-----> status: sourceDir = "+sourceDir);
	       } 
	       sourceDirs.add(new File(topDir, sndFolders[i]));
	    } 
	 }
     } 

     // get target directory
     String root = pageContext.getServletContext().getRealPath("/");
     File rootDir = new File(root, "image-repository");

     String path = aSite.getName()+"/"+event+"/Array"+array+"/"+trapName;;
     File targetDir = new File(rootDir, path);

     if (sourceDir != null && targetDir.exists()) {
	 int sourceCount = 0;
         
         for (File tmpDir : sourceDirs) {	
             sourceDir = tmpDir;
             String[] pics = sourceDir.list();
             for (int i=0; i<pics.length; i++) {
	        if (!pics[i].toUpperCase().endsWith("JPG")) continue; 
	        sourceCount++;
	     }
         }
		
	 int targetCount = targetDir.list().length;
	 
	 if (sourceCount == targetCount) {
	     out.println(" { done : true }");
	 } else {
             out.println(" { done : false, ");
	     out.println("   percentage: "+Math.floor(((float)targetCount/(float)sourceCount) * 100)/ 100+", ");
	     out.println("   strPercent: 'Copied "+targetCount+" Files'");
	     out.println(" }");
	 }
	 
     } else {
         out.println(" { done : false, ");
	 out.println("   percentage: 0, ");
	 out.println("   strPercent: 'Starting'");
	 out.println(" }");
     }


} catch (Exception ex) {

     out.println(" { failed : true }");
}

%>