<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>

<%

 try {
     response.setHeader("Cache-Control","no-cache"); 
     response.setHeader("Pragma","no-cache"); 
     response.setDateHeader ("Expires", -1); 

     Network network = NetworkFactory.newInstance();
     Session session1 = network.getSession();
     Transaction tx = session1.beginTransaction();
     Connection conn = session1.connection();     
     Statement statement = conn.createStatement();
     statement.execute("SET foreign_key_checks = 0;");     

     String url = "http://www.teamnetwork.org:8080/CorePortlet/jsp/management/OpenDeskTEAM/config.jsp";
     URL u = new URL(url);
     HttpURLConnection huc = (HttpURLConnection) u.openConnection();
     huc.connect();
     BufferedReader reader = new BufferedReader(new InputStreamReader(huc.getInputStream(), "UTF-8"));
     String line = reader.readLine();

     try {
         while ( line != null) {
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

     statement.execute("SET foreign_key_checks = 1;");     
     conn.close();
     reader.close();
     huc.disconnect();
     network.close();

     // update deskTEAM itself
     int count = 0;
     try {
     	 // get the root of deskTEAM
         String root = pageContext.getServletContext().getRealPath("/");
     
         // get the temporary directory 
         File tmpDir = new File(root+"/WEB-INF/tmp/");
         if (!tmpDir.exists()) {
            tmpDir.mkdirs();
         }
	 
         url = "http://www.teamnetwork.org:8080/CorePortlet/jsp/management/OpenDeskTEAM/version.jsp?version="+
               DeskTEAMProperties.getProperty("deskTEAM.version")+
	       "&os="+java.net.URLEncoder.encode(System.getProperty("os.name"));

         u = new URL(url);
         huc = (HttpURLConnection) u.openConnection();
         huc.connect();

         int i;
         InputStream is = huc.getInputStream();
	 String filename = String.valueOf(new java.util.Date().getTime());
	 File updateFile = new File(tmpDir, filename+".zip");
	 FileOutputStream fos = new FileOutputStream(updateFile);
         while ((i=is.read()) != -1){
            fos.write(i);
	    count++;
         }
	 fos.close();
	 is.close();
         huc.disconnect();

	 // unzip the update package
	 File rootDir = new File(root);
         Util.unzip(updateFile, rootDir);
         updateFile.delete();

	 DeskTEAMProperties.load();

     } catch (Exception ex) {
         //ex.printStackTrace();
     }

     out.println("{");
     out.println("  success: true, update: "+(count > 0));
     out.println("}");

  } catch (Exception ex) {

     ex.printStackTrace();

     out.println("{");
     out.println("  message: '"+ex.getMessage().replace("'", "\"")+"',");
     out.println("  success: false");
     out.println("}");
  }

%>