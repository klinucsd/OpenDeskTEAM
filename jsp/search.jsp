
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%
     String sites = request.getParameter("sites");
     String startTime = request.getParameter("startTime");
     String endTime = request.getParameter("endTime");
     String photoType = request.getParameter("photoType");
     String genus = request.getParameter("genus");
     String species = request.getParameter("species");
     
     String text = request.getParameter("text");
     String node = request.getParameter("node");
     String path = request.getParameter("path");

     /*
     System.out.println("------------------------------");
     System.out.println("text="+text);
     System.out.println("node="+node);
     System.out.println("path="+path);
     */

     String[] pathes = path.split("/");

     /*
     System.out.println("pathes size = "+pathes.length);
     for (int i=0; i<pathes.length; i++) {
        System.out.println("-----> i="+i+"  "+pathes[i]);
     }
     */

     Network network = NetworkFactory.newInstance();
     Session session1 = network.getSession();

     if (pathes.length == 2) {
  
        out.println("[\n");
        boolean first = true;
        //List<Site> siteList = network.getPhotoSites(sites,startTime,endTime,photoType,genus,species);

        String hql = "SELECT DISTINCT collection.cameraTrap.site FROM CameraTrapPhotoCollection as collection ";

        boolean joinPhoto = false;
        boolean joinRecord = false;
        boolean joinType = false;

        if ( photoType != null && !photoType.trim().equals("") ) {
            hql += " INNER JOIN collection.photos as photo ";
            hql += " INNER JOIN photo.type as type ";

            joinPhoto = true;
            joinType = true;
        }

        if (genus != null && !genus.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        if (species != null && !species.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        if (startTime != null && !startTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
        }

        if (endTime != null && !endTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
        }

        boolean cond = false;
        if (sites != null && !sites.trim().equals("")) {
            String[] abbvs = sites.split(",");
            String siteCond = " ( ";
            for (int i=0; i<abbvs.length; i++) {
                if (i != 0) {
                    siteCond += " OR ";
                }
                siteCond += "collection.cameraTrap.site.abbv='"+abbvs[i]+"'";
            }
            siteCond += " ) ";
            hql += " WHERE "+siteCond;
            cond = true;
        }

        if ( photoType != null && !photoType.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " type.name='"+photoType+"' ";
            cond = true;
        }

        if (genus != null && !genus.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " lower(record.genus)='"+genus.toLowerCase()+"' ";
            cond = true;
        }

        if (species != null && !species.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " lower(record.species)='"+species.toLowerCase()+"' ";
            cond = true;
        }

        if (startTime != null && !startTime.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " photo.takenTime >= '"+startTime+"' ";
            cond = true;
        }

        if (endTime != null && !endTime.trim().equals("") ) {
            if (!cond) {
                hql += " WHERE ";
            } else {
                hql += " AND ";
            }
            hql += " photo.takenTime <= '"+endTime+"' ";
            cond = true;
        }

        hql += "ORDER BY collection.cameraTrap.site.shortName";
        Query query = session1.createQuery(hql);
        List<Site> siteList = query.list();

	for (Site site : siteList) {
            if (first) {
                first = false;
            } else {
	        out.println("    ,");
	    }
            out.println("   { text:'"+site.getName()+"', path:'"+ path +"/"+site.getId()+"', leaf:false}");
	}
	out.println("]\n");

     } else if (pathes.length == 3){

	Site site = network.getSiteById(Integer.parseInt(pathes[2]));
	out.println("[\n");
        boolean first = true;
        //List<String> eventList = network.getPhotoEvents(site.getAbbv(),startTime,endTime,photoType,genus,species);

        String hql = "SELECT DISTINCT collection.event FROM CameraTrapPhotoCollection as collection ";

        boolean joinPhoto = false;
        boolean joinRecord = false;
        boolean joinType = false;

        // handle photo type                                                                                                                           
        if ( photoType != null && !photoType.trim().equals("") ) {
            hql += " INNER JOIN collection.photos as photo ";
            hql += " INNER JOIN photo.type as type ";

            joinPhoto = true;
            joinType = true;
        }

        // handle genus                                                                                                                                
        if (genus != null && !genus.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle species                                                                                                                              
        if (species != null && !species.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle start                                                                                                                                
        if (startTime != null && !startTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
        }

        // handle end                                                                                                                                  
        if (endTime != null && !endTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
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
                siteCond += "collection.cameraTrap.site.abbv='"+abbvs[i]+"'";
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
            hql += " type.name='"+photoType+"' ";
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

        hql += "ORDER BY collection.event";

        System.out.println(hql);

        Query query = session1.createQuery(hql);
        List<String> eventList = query.list();
	for (String event : eventList) {
            if (first) {
                first = false;
            } else {
	        out.println("    ,");
	    }
            out.println("   { text:'"+event+"', path:'"+ path +"/"+event+"', leaf:false}");
	}
	out.println("]\n");

     } else if (pathes.length == 4){

	Site site = network.getSiteById(Integer.parseInt(pathes[2]));
	String event = pathes[3];

	out.println("[\n");
        boolean first = true;
        //List<String> arrayList = network.getPhotoArrays(event, site.getAbbv(),startTime,endTime,photoType,genus,species);
        //List<String> arrayList = new ArrayList<String>();

        String hql = "SELECT DISTINCT collection.cameraTrap.name FROM CameraTrapPhotoCollection as collection ";

        boolean joinPhoto = false;
        boolean joinRecord = false;
        boolean joinType = false;

        // handle photo type                                                                                                                           
        if ( photoType != null && !photoType.trim().equals("") ) {
            hql += " INNER JOIN collection.photos as photo ";
            hql += " INNER JOIN photo.type as type ";

            joinPhoto = true;
            joinType = true;
        }

        // handle genus                                                                                                                                
        if (genus != null && !genus.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle species                                                                                                                              
        if (species != null && !species.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle start                                                                                                                                
        if (startTime != null && !startTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
        }

        // handle end                                                                                                                                  
        if (endTime != null && !endTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
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
                siteCond += "collection.cameraTrap.site.abbv='"+abbvs[i]+"'";
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
            hql += " type.name='"+photoType+"' ";
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

        if (!cond) {
            hql += " WHERE ";
        } else {
            hql += " AND ";
        }
        hql += " collection.event = '"+event+"' ";

        System.out.println(hql);

        Query query = session1.createQuery(hql);
        List<String> trapNames = query.list();

        TreeSet<String> stringSet = new TreeSet<String>();
        for (String name : trapNames) {
            stringSet.add(name.split("-")[2]);
        }

        List<String> arrayList = new ArrayList<String>();
        arrayList.addAll(stringSet);

	for (String array : arrayList) {
            if (first) {
                first = false;
            } else {
	        out.println("    ,");
	    }
            out.println("   { text:'Array"+array+"', path:'"+ path +"/"+array+"', leaf:false}");
	}
	out.println("]\n");

     } else if (pathes.length == 5){

	Site site = network.getSiteById(Integer.parseInt(pathes[2]));
	String event = pathes[3];
	String array = pathes[4];

	out.println("[\n");
        boolean first = true;
        //List<String> trapList = network.getPhotoCameraTraps(event, array, site.getAbbv(),startTime,endTime,photoType,genus,species);
        //List<String> trapList = new ArrayList<String>();

        String hql = "SELECT DISTINCT collection.cameraTrap.name FROM CameraTrapPhotoCollection as collection ";

        boolean joinPhoto = false;
        boolean joinRecord = false;
        boolean joinType = false;

        // handle photo type                                                                                                                           
        if ( photoType != null && !photoType.trim().equals("") ) {
            hql += " INNER JOIN collection.photos as photo ";
            hql += " INNER JOIN photo.type as type ";

            joinPhoto = true;
            joinType = true;
        }

        // handle genus                                                                                                                                
        if (genus != null && !genus.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle species                                                                                                                              
        if (species != null && !species.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle start                                                                                                                                
        if (startTime != null && !startTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
        }

        // handle end                                                                                                                                  
        if (endTime != null && !endTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
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
                siteCond += "collection.cameraTrap.site.abbv='"+abbvs[i]+"'";
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
            hql += " type.name='"+photoType+"' ";
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

        if (!cond) {
            hql += " WHERE ";
        } else {
            hql += " AND ";
        }
        hql += " collection.cameraTrap.name like 'CT-%-"+array+"-%' ";
        hql += " AND collection.event = '"+event+"' ";

        System.out.println(hql);

        Query query = session1.createQuery(hql);
        List<String> trapList = query.list();

        TreeSet<String> tNames = new TreeSet<String>(new CameraTrapNameComparator());
        tNames.addAll(trapList);

        trapList = new ArrayList<String>();
        trapList.addAll(tNames);

	for (String trap : trapList) {
            if (first) {
                first = false;
            } else {
	        out.println("    ,");
	    }
            out.println("   { text:'"+trap+"', path:'"+ path +"/"+trap+"', leaf:false}");
	}
	out.println("]\n");

     } else if (pathes.length == 6){

	Site site = network.getSiteById(Integer.parseInt(pathes[2]));
	String event = pathes[3];
	String array = pathes[4];
	String trap = pathes[5];

	out.println("[\n");
        boolean first = true;
        //List<String> nameList = network.getPhotoNames(event, array, trap, site.getAbbv(),startTime,endTime,photoType,genus,species);
        //List<String> nameList = new ArrayList<String>();

	String hql = "SELECT photo.rawName FROM CameraTrapPhotoCollection as collection ";
        String trapName = trap;

        boolean joinPhoto = false;
        boolean joinRecord = false;
        boolean joinType = false;

        // handle photo type                                                                                                                           
        if ( photoType != null && !photoType.trim().equals("") ) {
            hql += " INNER JOIN collection.photos as photo ";
            hql += " INNER JOIN photo.type as type ";

            joinPhoto = true;
            joinType = true;
        }

        // handle genus                                                                                                                                
        if (genus != null && !genus.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle species                                                                                                                              
        if (species != null && !species.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
            if ( !joinRecord ) {
                hql += " INNER JOIN photo.records as record ";
                joinRecord = true;
            }
        }

        // handle start                                                                                                                                
        if (startTime != null && !startTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
            }
        }

        // handle end                                                                                                                                  
        if (endTime != null && !endTime.trim().equals("") ) {
            if ( !joinPhoto ) {
                hql += " INNER JOIN collection.photos as photo ";
                joinPhoto = true;
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
                siteCond += "collection.cameraTrap.site.abbv='"+abbvs[i]+"'";
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
            hql += " type.name='"+photoType+"' ";
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

        if (!cond) {
            hql += " WHERE ";
        } else {
            hql += " AND ";
        }
        hql += " collection.event = '"+event+"' ";
        hql += " AND collection.cameraTrap.name = '"+trapName+"' ";


        hql += " ORDER BY photo.rawName";

        System.out.println(hql);

        Query query = session1.createQuery(hql);
        List<String> nameList = query.list();

	for (String name : nameList) {
            if (first) {
                first = false;
            } else {
	        out.println("    ,");
	    }
            out.println("   { text:'"+name+"', path:'"+ path +"/"+name+"', leaf:true, array: 'CT-"+site.getAbbv()+"-"+array+"', site: '"+site.getName()+"'}");
	}
	out.println("]\n");
	

     }

     network.close();
%>

