

<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      Network network = NetworkFactory.newInstance();
      Session session1 = network.getSession();
      Connection conn = session1.connection();
      Statement statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("SELECT DISTINCT manufacturer FROM camera_model ORDER BY manufacturer");
      List<String> names = new ArrayList();
      while (rs.next()) {
         names.add(rs.getString(1));
      }

      JsonFactory f = new JsonFactory();
      StringWriter sw = new StringWriter();
      JsonGenerator jg = f.createJsonGenerator(sw);

      jg.writeStartObject();	
      jg.writeArrayFieldStart("results");
      for (String name : names) {
         jg.writeStartObject();
         jg.writeStringField("name", name);
         jg.writeEndObject();
      }
      jg.writeEndArray();	
      jg.writeEndObject();
      network.close();
      jg.close();
      sw.flush();
      out.println(sw.toString());
    
%>