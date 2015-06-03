

<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.geongrid.sdsc.portlets.search.*" %>

<%
     String site = request.getParameter("site");
     String protocol = request.getParameter("protocol");
     String id = request.getParameter("id");
     String filename = request.getParameter("filename");

     String columnName = request.getParameter("columnname");
     String value = request.getParameter("value");
     String oldvalue = request.getParameter("oldvalue");	
     String username = request.getParameter("username");
     String row = request.getParameter("row");
     String column = request.getParameter("column");
     String sheet = request.getParameter("sheet");

     System.out.println("--------------------------");
     System.out.println("site = "+site);
     System.out.println("protocol = "+protocol);
     System.out.println("id = "+id);
     System.out.println("filename = "+filename);

     System.out.println("columnName = "+columnName);
     System.out.println("value = "+value);
     System.out.println("oldvalue = "+oldvalue);
     System.out.println("username = "+username);
     System.out.println("row = "+row);
     System.out.println("column = "+column);
     System.out.println("session id = "+session.getId());

     //TeamSiteData.saveDataLog(username, session.getId(), site, protocol, block, event, id, column, value, oldvalue);

     String path = pageContext.getServletContext().getRealPath("WEB-INF");
     String dirName = path + "/data/excels/" + id;

     if (protocol.equals("Climate")) {
         out.println(TeamSiteData.updateClimateExcel(dirName+"/"+filename, Integer.parseInt(row), Integer.parseInt(column),  value));
     } else if (protocol.equals("Vegetation : Tree")) {
         out.println(TeamSiteData.updateTreeExcel(dirName+"/"+filename, Integer.parseInt(row), Integer.parseInt(column),  value, Integer.parseInt(sheet)));
     } else if (protocol.equals("Vegetation : Liana")) {
         out.println(TeamSiteData.updateLianaExcel(dirName+"/"+filename, Integer.parseInt(row), Integer.parseInt(column),  value, Integer.parseInt(sheet)));
     } else if (protocol.equals("Camera Trapping")) {
         out.println(TeamSiteData.updateCameraExcel(dirName+"/"+filename, Integer.parseInt(row), Integer.parseInt(column),  value, Integer.parseInt(sheet)));
     } else {
	 System.out.println("=====> Nothing");
     }

%>

