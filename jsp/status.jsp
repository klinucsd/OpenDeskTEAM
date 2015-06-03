<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>



<%
  
try {  

     String siteId = request.getParameter("siteId");
     Network network = NetworkFactory.newInstance();

     Session hsession = network.getSession();	
     Query query = hsession.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo WHERE photo.collection.cameraTrap.site.id="+siteId);
     List list = query.list();
     int totalImageNumber = ((Long)list.iterator().next()).intValue();

     query = hsession.createQuery("SELECT count(*) FROM CameraTrapPhoto as photo WHERE NOT photo.type = NULL AND photo.collection.cameraTrap.site.id="+siteId);
     list = query.list();			       
     int processedImageNumber = ((Long)list.iterator().next()).intValue();

     network.close();

     out.println("{");
     out.println("   success: true,");
     out.println("   total: "+totalImageNumber+",");
     out.println("   annotated: "+processedImageNumber+",");
     out.println("}");

} catch (Exception e) {

     out.println("{");
     out.println("   success: false");
     out.println("}");
}
%>