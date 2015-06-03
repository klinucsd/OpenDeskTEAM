<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

{
   results: [
<%
     String genus = request.getParameter("genus");
     
     Network network = NetworkFactory.newInstance();
     Session session1 = network.getSession();

     Query query = session1.createQuery("SELECT DISTINCT species FROM PhotoSpeciesRecord as record "+
                                          (genus==null? "" : "WHERE lower(record.genus)='"+genus.toLowerCase()+"' ")+
                                          "Order By species");
     List records = query.list();
     List<String> result = new ArrayList();
     for (Iterator i=records.iterator(); i.hasNext(); ) {
            String species = (String)i.next();
            species = species.substring(0,1).toUpperCase()+species.substring(1).toLowerCase();
            result.add(species);
     }

     //List<String> species = network.getPhotoSpecies(genus);
     for (int i=0; i<result.size(); i++) {
        if (i > 0) out.println(",");
        out.println("{  name : '"+result.get(i).toLowerCase()+"' }");
     }
     network.close();
%>
  ]
}