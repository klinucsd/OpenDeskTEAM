<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="net.coobird.thumbnailator.Thumbnails" %>
<%

try {

     String siteId = request.getParameter("siteId");
     String trapId = request.getParameter("trapId");

     Network network = NetworkFactory.newInstance();
     Site aSite = network.getSiteById(new Integer(siteId));	
     SamplingUnit trap = network.getSamplingUnitById(Integer.parseInt(trapId));
     network.close();

     String site = request.getParameter("site");
     String trapName = request.getParameter("trapName");
     String memoryFolder = request.getParameter("memoryFolder");
     String event = request.getParameter("event");
    
     String cameraSerial = request.getParameter("cameraSerial");    
     String memoryCardSerial = request.getParameter("memoryCardSerial");    
     //String start = request.getParameter("start");    
     //String end = request.getParameter("end");    
     String setperson = request.getParameter("setperson");
     String pickperson = request.getParameter("pickperson");
     String setpersonid = request.getParameter("setpersonid");
     String pickpersonid = request.getParameter("pickpersonid");
     String notes = request.getParameter("notes");     

     Date startDate = null;
     Date endDate = null;

     CameraTrapPhoto firstPhoto = null;
     CameraTrapPhoto lastPhoto = null;
     Date firstDate = null;
     Date lastDate = null;

     String recordedCameraSerial = null;
     String recordedTrapName = null;

     String root = pageContext.getServletContext().getRealPath("/");
     File rootDir = new File(root, "image-repository");
     
     File thumbnailDir = new File(root, "thumbnails");

     String abbrev = aSite.getAbbv();
     int array = trap.getBlock().getIndex();

     String path = aSite.getName()+"/"+event+"/Array"+array+"/"+trapName;;
     File targetDir = new File(rootDir, path);

     File targetThumbnailDir = new File(thumbnailDir, path);

     if (targetDir.exists()) {
%>

{ 
   failure: true,
   text: 'The collection of <%= trapName %> for the sampling event <%= event %> at <%= site %> already  exists.'
}

<%
     } else {

        //network = NetworkFactory.newInstance();

        Configuration mConfigure = new AnnotationConfiguration();
        mConfigure.configure();
        SessionFactory mFactory = mConfigure.buildSessionFactory();
        Session mSession = mFactory.openSession();

        CameraTrap cameraTrap = (CameraTrap)network.getSamplingUnitById(Integer.parseInt(trapId));
        Person setPerson = network.getPersonById(new Integer(setpersonid));
        Person pickPerson = network.getPersonById(new Integer(pickpersonid));

        CameraTrapPhotoCollection collection = new CameraTrapPhotoCollection();
        collection.setCameraTrap(cameraTrap);
        collection.setEvent(event);
        collection.setCameraSerialNumber(cameraSerial);
        collection.setMemoryCardSerialNumber(memoryCardSerial);
        //collection.setStartTime(startDate);
        //collection.setEndTime(endDate);
        collection.setSetPerson(setPerson);
        collection.setPickPerson(pickPerson);
        collection.setWorking(new java.lang.Boolean(true));
        collection.setNotes(notes);
        //network.save(collection);
	
        mSession.save(collection);
	//System.out.println("------> collection id = "+collection.getId());

        targetDir.mkdirs();
        //System.out.println("-------> memory folder 10 = "+memoryFolder);

	targetThumbnailDir.mkdirs();
      
	//File sourceDir = new File(memoryFolder, "/DCIM/100RECNX");
        File sourceDir = null;	
	File topDir = null;
	List<File> sourceDirs = new ArrayList<File>();

        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {

	   if (memoryFolder.startsWith("//")) {
	      memoryFolder = memoryFolder.substring(2);
	   }
 	   //System.out.println("-------> windows memory folder 1 = "+memoryFolder);

           topDir = new File(memoryFolder);
	} else {
           topDir = new File(memoryFolder);	
	}

	if (topDir != null && topDir.exists()) {
	    sourceDir = topDir;;
	    sourceDirs.add(topDir);	
	} else {
	    System.out.println("-----> wrong top folder "+topDir);
	}	

	if (sourceDir == null) {
	   targetDir.delete();
           out.println("{ failure: true, text: 'Do not understand the folder hierarchy in this memory card.'} ");
	   return; 
	}


	
     int count = 0;
     for (File tmpDir : sourceDirs) {	

        sourceDir = tmpDir;
	String dirhead = "";

	if (sourceDirs.size() > 1) {
	    dirhead = sourceDir.getName();
	    dirhead = dirhead.substring(0, dirhead.length()-5)+"-";
        }
	   
        String[] pics = sourceDir.list();
        for (int i=0; i<pics.length; i++) {

	    if (!pics[i].toUpperCase().endsWith("JPG")) continue; 
	    //System.out.println("-----> load pic "+pics[i]);
	    count++;

	    File pic = new File(sourceDir, pics[i]);
	    File tgt = new File(targetDir, dirhead+pics[i]);	
	    InputStream in1 = new FileInputStream(pic);
            OutputStream out1 = new FileOutputStream(tgt);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in1.read(buf)) > 0) {
                out1.write(buf, 0, len);
            }
            in1.close();
            out1.close();

	    // create the thumbnail
	    // Thumbnails.of(pic).size(100, 100).toFile(new File(targetThumbnailDir, dirhead+pics[i]));
	    
	    Date picDate = new Date(pic.lastModified());
            java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String picDateStr = formatter.format(picDate);	

	    CameraTrapPhoto photo = new CameraTrapPhoto();
	    photo.setTakenTime(picDate);
	    photo.setSize(new Integer((int)pic.length()));
	    photo.setTakenTimeString(picDateStr);
	    photo.setRawName(dirhead+pics[i]);
	    photo.setTeamName(trapName+"_"+picDateStr+"_"+dirhead+pics[i]);
	    //photo.setNotes();
	    photo.setCollection(collection);
	    mSession.save(photo);
	      
	    try {
	        CameraTrapPhotoMetadata metadata = new CameraTrapPhotoMetadata(tgt);
	        metadata.setPhoto(photo);
	        mSession.save(metadata);

     		if (recordedCameraSerial == null) recordedCameraSerial = metadata.getJpegCommentSN();

		if (recordedTrapName == null) {
		    recordedTrapName = metadata.getJpegCommentLbl();
		    if (recordedTrapName != null) recordedTrapName = recordedTrapName.trim();
		}

		Date date = metadata.getJpegCommentDat();
		if (date == null) date = metadata.getExifDateTimeOriginal();
		if (date != null) {
		    photo.setTakenTime(date);
		    photo.setTakenTimeString(formatter.format(date));
		    photo.setTeamName(trapName+"_"+formatter.format(date)+"_"+pics[i]);
		    mSession.update(photo);
		}

		if (date != null) {
                   if (firstPhoto == null) {
		      firstPhoto = photo;
		      firstDate = date;
		   } else if (firstDate.getTime() > date.getTime()) {
		      firstPhoto = photo;
		      firstDate = date;
		   }

		   if (lastPhoto == null) {
		      lastPhoto = photo;
		      lastDate = date;
		   } else if (lastDate.getTime() < date.getTime()) {
		      lastPhoto = photo;
		      lastDate = date;
		   }
		}
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        System.out.println("-----> error read image header = "+tgt.getAbsolutePath()+"    "+ex.getMessage());	
	    }

       } 

    }

    //network.close();
    mSession.flush();
    mSession.evict(collection);
    mSession.close();

    if (count > 0) {
       network = NetworkFactory.newInstance();       
       Set<CameraTrapPhoto> gallery = network.getCameraTrapPhotoCluster(firstPhoto);       
       for (CameraTrapPhoto tmp : gallery) {
          firstPhoto = tmp;
       }
       firstPhoto.setType(network.getCameraTrapPhotoType("Start"));
       network.update(firstPhoto);

       gallery = network.getCameraTrapPhotoCluster(lastPhoto);       
       if (!gallery.isEmpty()) {
          lastPhoto = gallery.iterator().next();
       }
       lastPhoto.setType(network.getCameraTrapPhotoType("End"));
       network.update(lastPhoto);
       network.close();

       network = NetworkFactory.newInstance();
       try {
           collection.setStartTime(firstPhoto.getMetadata().getExifDateTime());
           collection.setEndTime(lastPhoto.getMetadata().getExifDateTime());
           network.update(collection);
       } catch (Exception ex) {}
       network.close();
   } else {
       network = NetworkFactory.newInstance();
       network.delete(collection);	
       network.close();

       targetDir.delete();

       out.println("{  failure: true,");
       out.println("   text: 'No images found on the memory card.' }");
       return;
   }

%>

{ 
   success: true,
   counter: '<%= count %>',
   camera_serial_confirm: <%= recordedCameraSerial != null && !recordedCameraSerial.trim().equals(cameraSerial.trim()) %>,
   input_camera_serial: '<%= cameraSerial %>',
   recorded_camera_serial: '<%= recordedCameraSerial == null ? "" : recordedCameraSerial.trim() %>',
   trap_confirm: <%= recordedTrapName != null && !recordedTrapName.equals(trapName) %>,
   input_trap: '<%= trapName %>',
   array_index: '<%= trap.getBlock().getIndex() %>',
   recorded_trap: '<%= recordedTrapName == null ? "" : recordedTrapName.trim() %>'
}

<%
     }
    
     //System.out.println("camera trap id = "+trapName+"    "+site+"    "+memoryFolder+"   "+event);
     //System.out.println("----> path = "+targetDir.getAbsolutePath());

} catch (Exception ex) {

   ex.printStackTrace();

   out.println("{  failure: true,");
   out.println("   text: '"+ex.getMessage()+"' }");

}

%>

