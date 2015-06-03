<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.zip.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.crypto.*" %>
<%@ page import="javax.xml.bind.*" %> 
<%@ page import="javax.xml.transform.stream.StreamSource" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>

<%
 try {
     // get the root of deskTEAM
     String root = pageContext.getServletContext().getRealPath("/");

     // get the directory for the image repository
     File imgDir = new File(root, "image-repository");
     String[] names = imgDir.list();
     if (names != null) {
         for (int i=0; i<names.length; i++) {
             File tmpDir = new File(imgDir, names[i]);
	     Util.deleteDir(tmpDir);         
         }
     }

     String path = request.getParameter("path");

     // get the directory for output
     File tcfFile = new File(path);

     Network network = NetworkFactory.newInstance();
     //Site site = network.getSites().get(0);	
     Session session1 = network.getSession();
     Transaction tx = session1.beginTransaction();
     Connection conn = session1.connection();     
     Statement statement = conn.createStatement();
     statement.execute("SET foreign_key_checks = 0;");     

     BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tcfFile), "UTF-8"));
     String line = reader.readLine();
     try {
         while (line != null) {
	     line = line.trim();
             if (!line.equals("")) {
	        if (line.startsWith("CREATE ")) {
		    try {
		       statement.execute(line);
		    } catch (Exception error) {}
		} else {
                    statement.execute(line);
		}	
	     }  
	     line = reader.readLine();
         }
     } catch (Exception e) {
         tx.rollback();
	 network.close();
	 throw e;
     }      
     reader.close();

     statement.execute("SET foreign_key_checks = 1;");     
     conn.close();
     network.close();

     //tcfFile.delete();

     out.println("{");
     out.println("  success: true");
     out.println("}");

  } catch (Exception ex) {

     ex.printStackTrace();

     out.println("{");
     out.println("  message: '"+ex.getMessage()+"',");
     out.println("  success: false");
     out.println("}");
  }

%>

