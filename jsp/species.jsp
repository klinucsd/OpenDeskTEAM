
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

{
   results: [

<%
    String query = request.getParameter("query");
    String genus = request.getParameter("genus");

    Network network = NetworkFactory.newInstance();
    List list = network.getSpeciesNames(genus, query);
    if ("unknown".startsWith(query) && !list.contains("unknown")) {
        list.add(0, "unknown");
    }
    for (int i=0; i<list.size(); i++) {
       if (i > 0) out.println(",");
       out.println("{  name : '"+list.get(i)+"' }");
    }
    network.close();
%>

  ]
}

