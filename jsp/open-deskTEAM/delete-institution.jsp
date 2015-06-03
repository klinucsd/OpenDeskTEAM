

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>

<%
      String institutionId = request.getParameter("institution_id");
      String projectId = request.getParameter("project_id");

      Network network = NetworkFactory.newInstance();
      Session session1 = network.getSession();
      Query query = session1.createQuery("FROM Institution as institution WHERE institution.id="+institutionId);
      List<Institution> institutions = query.list();
      for (Institution institution : institutions) {
          session1.delete(institution);
      }
      network.close();
    
%>