<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<p>Checking The Image Repository Consistence......
<%

    Network network = NetworkFactory.newInstance();

    String pad = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    String pad2 = pad+pad;
    String pad3 = pad2+pad;
    String pad4 = pad3+pad;

    String root = pageContext.getServletContext().getRealPath("/");
    File rootDir = new File(root, "image-repository");

    String siteName = (rootDir.list())[0];
    File siteDir = new File(rootDir, siteName);
    out.println("<br>Checking the site <b>"+siteName+"</b>");	

    // check each events
    String[] events = siteDir.list();

    for (int i=events.length-1; i>-1; i--) {

       if (!events[i].equals("2011.01")) {
           out.println("<br>"+pad+"Skipping the event <b>"+events[i]+"</b>");
           continue;				
       } else {
           out.println("<br>"+pad+"Checking the event <b>"+events[i]+"</b>");
       }

       File eventDir = new File(siteDir, events[i]);
       String[] arrays = eventDir.list();

       // check each array
       for (int j=0; j<arrays.length; j++) {
           out.println("<br>"+pad2+"Checking the array <b>"+arrays[j]+"</b>");	

	   // check each camera trap
	   File arrayDir = new File(eventDir, arrays[j]);
	   String[] traps = arrayDir.list();
	   for (int k=0; k<traps.length; k++) {
	      out.println("<br>"+pad3+"Checking the camera trap <b>"+traps[k]+"</b>");	
	      	 
	      List<CameraTrapPhoto> photos = network.getCameraTrapPhoto(traps[k], events[i]);

	      // check each image
	      File trapDir = new File(arrayDir, traps[k]);
	      String[] photoNames = trapDir.list();
	      for (int p=0; p<photoNames.length; p++) {
	          //out.println("<br>"+pad4+"Checking the image <b>"+photoNames[p]+"</b>");

		  if (!photoNames[p].toUpperCase().endsWith("JPG")) continue;

		  File pic = new File(trapDir, photoNames[p]);

		  CameraTrapPhoto photo = null;
		  for (CameraTrapPhoto tmp : photos) {
		     if (photoNames[p].equals(tmp.getRawName())) {
			 photo = tmp;
			 break;
		     }
		  }  

		  if (photo != null) {	
		  
		     // check the taken time
		     String errorMsg = "";
		     if (photo.getTakenTime() == null) {
		         errorMsg += "No taken time for this photo. ";
		     } else if (photo.getMetadata() == null) {
		         errorMsg += "No metadata loaded from the image header of this photo.";		 
		     }

		     if (!errorMsg.equals("")) {
		         out.println("<br>"+pad4+"Checking the image <b>"+photoNames[p]+"</b>");
		         out.println("<br>"+pad4+"<font color=red><b>Error: "+errorMsg+"</b></font>");		
		         out.println("<br>"+pad4+"<font color=green><b>Tring to load the image header</b></font>");

			 CameraTrapPhotoMetadata metadata = new CameraTrapPhotoMetadata(pic);			 
			 Date date = metadata.getJpegCommentDat();
			 if (date == null) date = metadata.getExifDateTime();
			 if (date != null) {

			     out.println("<br>"+pad4+"<font color=green><b>Image header loaded</b></font>");
			     out.println("<br>"+pad4+"<font color=green><b>Taken time is "+date+"</b></font>");
	
			     metadata.setPhoto(photo);
			     network.save(metadata);

                             java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			     photo.setTakenTime(date);
		             photo.setTakenTimeString(formatter.format(date));
		    	     photo.setTeamName(traps[k]+"_"+formatter.format(date)+"_"+photoNames[p]);    
			     network.update(photo);

			     out.println("<br>"+pad4+"<font color=green><b>Prolem fixed</b></font>");

			 } else {
			     out.println("<br>"+pad4+"<font color=green><b>No taken time was loaded</b></font>");
			 }

	             } else {
		         //out.println(": <font color=green>ok</font>");
		     }

		  } else {

		     // no record for this photo 
		     out.println("<br>"+pad4+"Checking the image <b>"+photoNames[p]+"</b>");
		     out.println("<br>"+pad4+"<font color=red><b>Error: No database record for this photo</b></font>");	
		     out.println("<br>"+pad4+"<font color=green><b>Tring to fix the problem</b></font>"); 

		     // create a photo record
		     CameraTrapPhotoCollection collection = network.getCameraTrapPhotoCollection(events[i], traps[k]);
		     
		     if (collection == null) {
			 out.println("<br>"+pad4+"<font color=red><b>SERIOUS ERROR: could not load the data for the camera trap:==="+events[i]+"==="+traps[k]+"===</b></font>");	
			 continue;
		     } 

	    	     photo = new CameraTrapPhoto();
		     photo.setSize(new Integer((int)pic.length()));	    	     
	             photo.setRawName(photoNames[p]);
	             photo.setTeamName(traps[k]+"_123456_"+photoNames[p]);
	             photo.setCollection(collection);
	             network.save(photo);

		     out.println("<br>"+pad4+"<font color=green><b>A record is saved for this photo.</b></font>"); 

	             CameraTrapPhotoMetadata metadata = new CameraTrapPhotoMetadata(pic);			 
	             Date date = metadata.getJpegCommentDat();
		     if (date == null) date = metadata.getExifDateTime();
		     if (date != null) {

			  out.println("<br>"+pad4+"<font color=green><b>Image header loaded</b></font>");
			  out.println("<br>"+pad4+"<font color=green><b>Taken time is "+date+"</b></font>");
	
			  metadata.setPhoto(photo);
			  network.save(metadata);

                          java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			  photo.setTakenTime(date);
		          photo.setTakenTimeString(formatter.format(date));
		    	  photo.setTeamName(traps[k]+"_"+formatter.format(date)+"_"+photoNames[p]);    
			  network.update(photo);

			  out.println("<br>"+pad4+"<font color=green><b>Prolem fixed</b></font>");

	             } else {
			  out.println("<br>"+pad4+"<font color=red><b>No taken time was loaded</b></font>");
		     }
		  }
	      }

	   }

       }

    }    

    network.close();


%>