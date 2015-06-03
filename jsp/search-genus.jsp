<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

{
   results: [
<%

     String sites = request.getParameter("sites");

     Network network = NetworkFactory.newInstance();
     Session session1 = network.getSession();

     String hql = "SELECT DISTINCT genus FROM PhotoSpeciesRecord as record ";
     if (sites != null && !sites.trim().equals("")) {
         String[] abbvs = sites.split(",");
         String siteCond = " ( ";
         for (int i=0; i<abbvs.length; i++) {
             if (i != 0) {
                    siteCond += " OR ";
             }
             siteCond += "record.photo.collection.cameraTrap.site.abbv='"+abbvs[i]+"'";
         }
         siteCond += " ) ";
         hql += " WHERE "+siteCond;
     }

     hql += " ORDER BY genus";

     System.out.println(hql);

     Query query = session1.createQuery(hql);
     List records = query.list();
     List<String> genuses = new ArrayList();
     for (Iterator i=records.iterator(); i.hasNext(); ) {
         String genus = (String)i.next();
         genus = genus.substring(0,1).toUpperCase()+genus.substring(1).toLowerCase();
         genuses.add(genus);
     }

     Set<String> set = new TreeSet<String>();
     set.addAll(genuses);
     genuses = new ArrayList<String>();
     genuses.addAll(set);

     for (int i=0; i<genuses.size(); i++) {
        if (i > 0) out.println(",");
        out.println("{  name : '"+genuses.get(i)+"' }");
     }


     network.close();
%>
  ]
}