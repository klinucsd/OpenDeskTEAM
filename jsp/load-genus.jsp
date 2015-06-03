<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.DatabaseMigration" %>

<%
    Configuration mConfigure = new AnnotationConfiguration();
    mConfigure.configure();
    SessionFactory mFactory = mConfigure.buildSessionFactory();
    Session mSession = mFactory.openSession();
    Connection connection = mSession.connection();
    Statement stmt = connection.createStatement();

    BufferedReader reader = new BufferedReader(new FileReader("/Users/kailin/Desktop/genus_species_tmp.sql"));
    String line = reader.readLine();
    while (line != null) {
        stmt.execute(line);    
        line	= reader.readLine();
    }
    mSession.close();

%>