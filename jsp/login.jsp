
<%@ page language="java" session="true" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="org.apache.commons.httpclient.*" %>

<%
     try {
         String username = request.getParameter("loginUsername");
         String password = request.getParameter("loginPassword");
         //System.out.println("----> "+username+"  "+password);

	 session.setAttribute("uploadingUsr", username);
	 session.setAttribute("uploadingPwd", password);

         HttpClient httpclient = new HttpClient();
	 String tgt = CAS.getTGT(httpclient, username, password);
         if (tgt != null) {
	     session.setAttribute("tgt", tgt);
	     session.setAttribute("username", username);
	     session.setAttribute("password", password);
             out.println("{ 'success':true }");
         } else {
             out.println("{ 'success':false, 'reason':'Incorrect username/password' }");
         }
     } catch (Exception ex) {
         out.println("{ 'success':false, 'record': { 'reason': ex.getMessage() } }");
     }
%>
