
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="org.apache.http.*" %>
<%@ page import="org.apache.http.message.*" %>
<%@ page import="org.apache.http.protocol.*" %>
<%@ page import="org.apache.http.client.*" %>
<%@ page import="org.apache.http.client.entity.*" %>
<%@ page import="org.apache.http.cookie.*" %>
<%@ page import="org.apache.http.impl.client.*" %>
<%@ page import="org.apache.http.client.methods.*" %>


<%
     Network network = null;
     try {
         String username = request.getParameter("username");
         String password = request.getParameter("password");
         String trap = request.getParameter("trap");
         String event = request.getParameter("event");

	 network = NetworkFactory.newInstance();
	 CameraTrap ct = (CameraTrap)network.getSamplingUnitByName(trap);
	 Site site = ct.getSite();
	 CameraTrapUploadJob job = network.getCameraTrapUploadJob(ct, event);
         job.setStatus(null);
  
         // get the directory for the image repository
         String appDir = pageContext.getServletContext().getRealPath("/");
         File imgRoot = new File(appDir, "image-repository");
	 String blockIndex = ct.getBlock().getIndex()+"";
         String imgPath = imgRoot.getAbsolutePath()+"/"+ct.getSite().getName()+"/"+event+"/Array"+blockIndex+"/"+ct.getName();
	 File imgDir = new File(imgPath);

	 CAS.username = username;
	 CAS.password = password;

	 /////////////////////////////////////////////////
         // 
         // handle project
         //
         ////////////////////////////////////////////////
	 Integer pid = site.getPublicId();
	 if ( pid == null) {
	     // upload this project
	     System.out.println("upload the project: "+site.getName());

	     String objective = site.getStatus();
	     if (objective == null) objective = "Unknown";

	     Date date = new Date();
	     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	     
            
 	     List<Person> persons = network.getPersons(site);
	     String piName = null;
	     String piEmail = null;
	     for (Person person : persons) {
	         String[] roles = person.getRoles();
		 for (int i=0; i<roles.length; i++) {
		     if (roles[i].equals("principal investigator")) {
		        piName = person.getFirstName()+" "+person.getLastName();
			piEmail = person.getEmail();
			break;
		     }
		 }
		 if (piName != null) break;
	     }
	    
	     String insName = null; 
             List<Institution> institutions = network.getInstitutions(site); 
	     for (Institution ins : institutions) {
	        insName = ins.getName();
	     }	     

             String projectId = CAS.createProject(site.getAbbv(),
					          site.getName(),
                                                  objective,
                                                  "active",
                                                  "Free to use",
                                                  format.format(date),
                               		          site.getCountry().getCode(),
                               		          piName,
                               		          piEmail,
                               		          piName,
                               		          piEmail,
                               		          insName);

	     if (projectId != null) {
	         pid = new Integer(projectId);
	         site.setPublicId(pid);
		 network.getSession().update(site);
	     }
	 }	 	 
	 
	 /////////////////////////////////////////////////
         // 
         // handle events
         //
         ////////////////////////////////////////////////
         
	 List<SamplingEvent> events = network.getSamplingEvents(site);
	 for (SamplingEvent evt : events) {
	     Integer eid = evt.getPublicId();
	     if (eid == null) {
	         System.out.println("New Event "+evt.getEvent()+" not in the repository");
                 String eventId = CAS.createEvent(pid.intValue(), evt.getEvent());
		 if (eventId != null) {
		     evt.setPublicId(new Integer(eventId));
		     network.getSession().update(evt);
		 }
		 
	     } 
	 }

	 /////////////////////////////////////////////////
         // 
         // handle cameras
         //
         ////////////////////////////////////////////////
         
	 List<Camera> cameras = network.getCameras(site);
	 for (Camera camera : cameras) {
	     Integer cid = camera.getPublicId();
	     if (cid == null) {
                 String cameraId = CAS.createCamera(pid.intValue(), 
		 		   		    camera.getManufacturer(),
						    camera.getModel(),
						    camera.getSerialNumber());
		 if (cameraId != null) {
		     camera.setPublicId(new Integer(cameraId));
		     network.getSession().update(camera);
		 }		 
	     } 
	 }

	 /////////////////////////////////////////////////
         // 
         // handle deployments
         //
         ////////////////////////////////////////////////

	 CameraTrapPhotoCollection collection = network.getCameraTrapPhotoCollection(event, trap); 

	 // find camera id
	 Integer cameraId = null;
	 for (Camera camera : cameras) {
	    if (camera.getSerialNumber().equals(collection.getCameraSerialNumber())) {
	       cameraId = camera.getPublicId();
	       break;
	    }
	 }

	 // check latitude and longitude
	 if ( collection.getCameraTrap().getLatitude() == null ) {
	     throw new java.sql.SQLException("Latitude is required for the camera trap "+collection.getCameraTrap().getName());
	 }
	
	 if ( collection.getCameraTrap().getLongitude() == null ) {
	     throw new java.sql.SQLException("Longitude is required for the camera trap "+collection.getCameraTrap().getName());
	 }

	 Integer cid = collection.getPublicId();
	 if ( cid == null) {
	     // upload this deployment
	     System.out.println("upload the deployment for the collection: "+collection.getId());

    	     SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	     Date b = collection.getStartTime();
    	     Date e = collection.getEndTime();

    	     String deploymentId = CAS.createDeployment(pid.intValue(),
                                                        collection.getEvent()+"_"+collection.getCameraTrap().getName(),
                                                        collection.getCameraTrap().getBlock().getName(),
                                                        format.format(b),
                                                        format.format(e),
                                                        collection.getCameraTrap().getLatitude(),
						        collection.getCameraTrap().getLongitude(),
                                                        cameraId,
							collection.getEvent());	     

	     if (deploymentId != null) {	
		 collection.setPublicId(new Integer(deploymentId));
		 network.getSession().update(collection);
		 cid = new Integer(deploymentId);
	     } 		 

	 }	 	 	 

	 /////////////////////////////////////////////////
         // 
         // handle images
         //
         ////////////////////////////////////////////////

	 Set<CameraTrapPhoto> photos = collection.getPhotos();
	 int notSaved = 0;
	 int counter = 0;
	 for (CameraTrapPhoto photo : photos) {	 
	     Integer poid = photo.getPublicId();
	     if (poid == null) {
	        //System.out.println("----> "+photo.getRawName());
		/*
		if (photo.getRawName().equals("100-IMG_0008.JPG")) {
		    continue;
		}
		*/

    	        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		String typeName = null;
		if (photo.getType() == null) {   // || !photo.getType().getName().equals("Animal")) {
		    notSaved++;
	            continue;
		}
		typeName = photo.getType().getName();

	        Person idPerson = photo.getTypeIdentifiedBy();	
		String idName = null;
		if ( idPerson != null ) {
		    idName = idPerson.getFirstName()+" "+idPerson.getLastName(); 
		}

		String gs = null;
		String uncertainty = null;
		Set<PhotoSpeciesRecord> records = photo.getRecords();
		for (PhotoSpeciesRecord record : records) {
		    gs = record.getGenus()+" "+record.getSpecies();
		    uncertainty = record.getUncertainty(); 
		    break;
		}	

		String photoId = CAS.createImage(pid.intValue(),
						 cid.intValue(),
                            			 photo.getRawName(),
                            			 //collection.getEvent(),
                            			 format.format(photo.getTakenTime()),
                            			 typeName,
                            			 idName,
                            			 gs,
                            			 uncertainty);
	        if (photoId != null) {
		    photo.setPublicId(new Integer(photoId));
		    network.getSession().update(photo);
		    
		    // upload image
		    counter++;
		    job.setCurrentIndex(counter);
		    network.update(job);

	     	} else {
		    notSaved++;
		}		 
	     } else {
	        counter++;
	     }
	 }

	 DefaultHttpClient httpclient = new DefaultHttpClient();
         String reason = UploadUtil.uploadData(httpclient, job); 
	 if ( reason == null && notSaved == 0) {
             job.setEndTime(new Date());
	     network.update(job);
	     out.println("{ 'success':true }");
         } else if (reason == null && notSaved != 0) {
	     out.println("{ 'success':true }");
	 } else if (reason != null) {
	     out.println("{ 'success':false, reason: 'Failed to upload metadata.' }");
         }
	 network.close();

	 /*
	         // upload images
	         String[] imgNames = imgDir.list();
	         for (int i=job.getCurrentIndex(); i<imgNames.length; i++) {
	             //System.out.println("----> "+imgNames[i]);
		     //Thread.sleep(3000);
		     if (UploadUtil.uploadImage(httpclient, job, new File(imgDir, imgNames[i]), i)) {

		         network = NetworkFactory.newInstance();
			 job = network.getCameraTrapUploadJob(ct, event);

	     		 String status = job.getStatus();
	     		 if (status != null && status.equals("pause")) {
                  	     out.println("{ 'success': true, done: 'paused' }");
		  	     return;
	     		 }

		     	 job.setCurrentIndex(job.getCurrentIndex()+1);
		     	 network.update(job);
			 network.close();
		     } else {
		         out.println("{ 'success':false, resaon: 'Failed to upload the image "+imgNames[i]+".' }");
			 return;
		     }
	         }
	*/


     } catch (Exception ex) {
         ex.printStackTrace();
         out.println("{ 'success':false, reason: 'Internal Error: "+ex.getMessage()+"' }");
     } finally {
         if (network != null) 
	    try {
	           network.close();
            } catch (Exception e) {}
     }
%>
