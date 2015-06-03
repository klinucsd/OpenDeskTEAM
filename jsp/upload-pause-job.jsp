
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
	 job.setStatus("pause");
	 network.update(job);
         out.println(" { success : true }");

     } catch (Exception ex) {
         ex.printStackTrace();
         out.println("{ 'success':false, resson: '"+ex.getMessage()+"' }");
     } finally {
         if (network != null)  network.close();
     }
%>
