<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%
     String text = request.getParameter("text");
     String node = request.getParameter("node");
     String path = request.getParameter("path");
     String root = pageContext.getServletContext().getRealPath("/");

     String event = null;
     String array = null;
     String trap = null;

     StringTokenizer st = new StringTokenizer(path, "/");
     if (st.hasMoreTokens()) {
         // skip the site
	 st.nextToken();
     }

     if (st.hasMoreTokens()) {
         event = st.nextToken();
     }

     if (st.hasMoreTokens()) {
         array = st.nextToken();
     }
     
     if (st.hasMoreTokens()) {
         trap = st.nextToken();
     }

     Map<String, CameraTrapPhoto> name2photos = new HashMap<String, CameraTrapPhoto>();
     if (trap != null) {
         Network network = NetworkFactory.newInstance();
	 List<CameraTrapPhoto> photos = network.getCameraTrapPhoto(trap, event);
	 for (CameraTrapPhoto photo : photos) {
	     name2photos.put(photo.getRawName(), photo);
	 }
	 network.close();
     }
     //System.out.println("name2photos = "+name2photos.size());

     File rootDir = new File(root, "image-repository");
     File pathDir = new File(rootDir, path);

     //System.out.println("======> path dir = "+pathDir);
     if (!pathDir.exists()) {
         pathDir.mkdirs();
     }

     String[] files = pathDir.list();

     List<String> uploaded = new ArrayList<String>();
     List<String> uploading = new ArrayList<String>();
     Network network = NetworkFactory.newInstance();
     if (pathDir.getName().startsWith("Array")) {
         TreeSet<String> nameSet = new TreeSet<String>(new StringComparator());
         for (int i=0; i<files.length; i++) {	       
	     if (files[i].startsWith(".")) continue;
	     nameSet.add(files[i]);

	     CameraTrap ct = (CameraTrap)network.getSamplingUnitByName(files[i]);
	     CameraTrapUploadJob job = network.getCameraTrapUploadJob(ct, event);
	     //CameraTrapUploadJob job = null;
	     if (job != null) {
	         if (job.isFinished()) {
		    uploaded.add(files[i]);
		 } else {
		    uploading.add(files[i]);
		 }
	     } 
         }
	 int i = 0;
	 for (String tmp : nameSet) {
	    files[i++] = tmp;
	 }
     }
     network.close();

     out.println("[");
     boolean first = true;
     int count = 0;
     for (int i=0; i<files.length; i++) {
	if (files[i].startsWith(".")) continue;
	File tmp = new File(pathDir, files[i]);
	if (first) {
	    first = false;
	} else {
	    out.print(",");
	} 

	boolean annotated = false;
	boolean inHP = false;
	if (!tmp.isDirectory()) {
	    CameraTrapPhoto photo = name2photos.get(files[i]);
	    if (photo != null && photo.getType() != null) {
	       annotated = true;
	    }
	    inHP = photo.getPublicId() != null;
	} else {
	    String[] tmpFiles = tmp.list();
	    for (int j=0; j<tmpFiles.length; j++) {
	        if (tmpFiles[j].startsWith(".")) continue;
		count++;
	    }
	}

	if (annotated) {
	    if (inHP) {
                out.println("{ text:'"+files[i]+"', path:'"+path+"/"+files[i]+"', leaf:"+!tmp.isDirectory()+", cls: 'annotated', icon: '/deskTEAM/images/icons/picture_save.png' }");
	    } else {
	        out.println("{ text:'"+files[i]+"', path:'"+path+"/"+files[i]+"', leaf:"+!tmp.isDirectory()+", cls: 'annotated' }");
	    }	
	} else if (tmp.isDirectory() && count == 0 && path.split("/").length == 4) {
	    out.println("{ text:'"+files[i]+"', path:'"+path+"/"+files[i]+"', leaf:"+!tmp.isDirectory()+", damaged:true, cls: 'damaged' }");	
	} else {
	    out.println("{ text:'"+files[i]+"', path:'"+path+"/"+files[i]+"', leaf:"+!tmp.isDirectory()+
	                 ( uploaded.contains(files[i]) ? 
			     ", icon: '/deskTEAM/images/icons/database_save.png'" : 
			     ( uploading.contains(files[i]) ? ", icon: '/deskTEAM/images/icons/folder_go.png'" : "")
			 )+"}");	
        }
     }
     out.println("]");

%>

