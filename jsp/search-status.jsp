<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>


<%
  
try {  
     String sites = request.getParameter("sites");
     String startTime = request.getParameter("startTime");
     String endTime = request.getParameter("endTime");
     String photoType = request.getParameter("photoType");
     String genus = request.getParameter("genus");
     String species = request.getParameter("species");

     Network network = NetworkFactory.newInstance();
     Session session1 = network.getSession();

     String hql = "SELECT count(*) FROM CameraTrapPhoto as photo ";
     boolean joinRecord = false;

     // handle genus                                                                   
     if (genus != null && !genus.trim().equals("") ) {
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
     }

     // handle species                                                                                       
     if (species != null && !species.trim().equals("") ) {
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
     }

     boolean cond = false;

     // handle site condition                                                                                     
     if (sites != null && !sites.trim().equals("")) {
            String[] abbvs = sites.split(",");
            String siteCond = " ( ";
            for (int i=0; i<abbvs.length; i++) {
                if (i != 0) {
                    siteCond += " OR ";
                }
                siteCond += "photo.collection.cameraTrap.site.abbv='"+abbvs[i]+"'";
            }
            siteCond += " ) ";
            hql += " WHERE "+siteCond;
            cond = true;
     }

     // handle photo type
     if ( photoType != null && !photoType.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " photo.type.name='"+photoType+"' ";
            cond = true;
     }

     // handle genus
     if (genus != null && !genus.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " lower(record.genus)='"+genus.toLowerCase()+"' ";
            cond = true;
     }

     // handle species                                                                                              
     if (species != null && !species.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " lower(record.species)='"+species.toLowerCase()+"' ";
            cond = true;
     }

     // handle start
     if (startTime != null && !startTime.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " photo.takenTime >= '"+startTime+"' ";
            cond = true;
     }

     // handle end
     if (endTime != null && !endTime.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " photo.takenTime <= '"+endTime+"' ";
            cond = true;
    }

    System.out.println(hql);

    Query query = session1.createQuery(hql);
    List counts = query.list();
    int totalImageNumber = ((Long)counts.get(0)).intValue();

     out.println("{");
     out.println("   total: "+totalImageNumber+",");
     out.println("   success: true");

     out.println("}");
     network.close();

} catch (Exception e) {

     e.printStackTrace();

     out.println("{");
     out.println("   success: false");
     out.println("}");
}
%>