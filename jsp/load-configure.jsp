<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
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

     String path = request.getParameter("path");
     String defaultFlag = request.getParameter("defaultFlag");

     // get the directory for output
     File tcfFile = new File(path);

     // get the root of deskTEAM
     String root = pageContext.getServletContext().getRealPath("/");

     if (defaultFlag != null && defaultFlag.equals("true")) {
        File datahome = new File(root+"/WEB-INF/datahome.txt");
	PrintWriter pw = new PrintWriter(new FileWriter(datahome));
	pw.println(tcfFile.getParent());
	pw.close();
     }
     
     // get the temporary directory 
     File tmpDir = new File(root+"/WEB-INF/tmp/");
     if (!tmpDir.exists()) {
        tmpDir.mkdirs();
     }
     
     // get the directory of the key
     File keyFile = new File(root+"/WEB-INF/key.txt");

     // read the key file            
     DataInputStream dis = new DataInputStream(new FileInputStream(keyFile));
     byte[] rawKey = new byte[(int)keyFile.length()];
     dis.readFully(rawKey);
     dis.close();

     //make a key object
     javax.crypto.spec.SecretKeySpec key1 = new javax.crypto.spec.SecretKeySpec(rawKey, "DES");

     // decrpte the package
     DesEncrypter decrypter = new DesEncrypter(key1);
     File finalFile = new File(tmpDir, tcfFile.getName()+".txt");
     decrypter.decrypt(new FileInputStream(tcfFile), new FileOutputStream(finalFile));

     // validate tcf file
     BufferedReader reader = new BufferedReader(new FileReader(finalFile));
     String line = reader.readLine();
     if (line != null && line.trim().startsWith("DELETE")) {
         out.println("{");
         out.println("  success: true,");
         out.println("  path: '"+finalFile.getAbsolutePath().replace('\\', '/')+"'");
         out.println("}");
     } else {
         out.println("{");
         out.println("  message: 'Invalid Configuration File.',");
         out.println("  success: false");
         out.println("}");
     }

  } catch (Exception ex) {

     ex.printStackTrace();

     out.println("{");
     out.println("  message: '"+ex.getMessage()+"',");
     out.println("  success: false");
     out.println("}");
  }

%>

