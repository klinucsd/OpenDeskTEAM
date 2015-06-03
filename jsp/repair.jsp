
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

     Network network = NetworkFactory.newInstance();
     Session session1 = network.getSession();
     Connection conn = session1.connection();     
     Statement statement = conn.createStatement();
     ResultSet rs = statement.executeQuery("REPAIR TABLE tv_photo");
     if (rs.next()) {
        String msg = rs.getString(1);
        out.println("<br><b>Table:</b> "+msg);

        msg = rs.getString(2);
        out.println("<br><b>Operation:</b> "+msg);

        msg = rs.getString(3);
        out.println("<br><b>Message Type:</b> "+msg);

        msg = rs.getString(4);
        out.println("<br><b>Message Text:</b> "+msg);
     }     

     out.println("<br><br><hr width=400 align=left>");

     rs = statement.executeQuery("CHECK TABLE tv_photo");
     if (rs.next()) {
        String msg = rs.getString(1);
        out.println("<br><b>Table:</b> "+msg);

        msg = rs.getString(2);
        out.println("<br><b>Operation:</b> "+msg);

        msg = rs.getString(3);
        out.println("<br><b>Message Type:</b> "+msg);

        msg = rs.getString(4);
        out.println("<br><b>Message Text:</b> "+msg);
     }     
     conn.close();
     network.close();

%>