<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>


<%
     String event = request.getParameter("event");
     String array = request.getParameter("array");
     String trap = request.getParameter("trap");
     String siteId = request.getParameter("siteId");

     try {    
        Network network = NetworkFactory.newInstance();
        Site site = network.getSiteById(new Integer(siteId));	
        //Site site = network.getSites().get(0);
        if (trap != null && !trap.equals("")) {
           network.deleteCameraTrapPhotos(site.getAbbv(), event, array, trap);
        } else if (array != null && !array.equals("")) {
	   System.out.println("----> array = "+array);
           network.deleteCameraTrapPhotos(site.getAbbv(), event, array, null);
        } else {
           network.deleteCameraTrapPhotos(site.getAbbv(), event, null, null);
        }
        network.close();

	// remove photos from the repository
        String root = pageContext.getServletContext().getRealPath("/");
        File rootDir = new File(root, "image-repository");
        File thumbnailDir = new File(root, "thumbnails");

        String path = site.getName()+"/"+event;

	if (array != null && !array.equals("")) {
           path += "/"+array;
        } 

	if (trap != null && !trap.equals("")) {
           path += "/"+trap;
        } 

	//System.out.println("----> delete: "+new File(rootDir, path).getAbsolutePath());

	Util.deleteDir(new File(rootDir, path));
	Util.deleteDir(new File(thumbnailDir, path));
	out.println(" { success : true } ");

    } catch (Exception ex) {
        ex.printStackTrace();
	out.println("{ success : false, message: '"+ex.getMessage()+"' } ");
    }
%>



