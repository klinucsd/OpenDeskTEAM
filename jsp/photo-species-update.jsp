
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import="org.geongrid.sdsc.portlets.search.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>

<%
     String site = request.getParameter("site");
     String trapId = request.getParameter("trapId");
     String event = request.getParameter("event");
     String raw = request.getParameter("raw");

     String type = request.getParameter("type");        
     String genus = request.getParameter("genus");    
     String species = request.getParameter("species");    
     String number = request.getParameter("number");
     String uncertainty = request.getParameter("uncertainty");

     String genus2 = request.getParameter("genus2");    
     String species2 = request.getParameter("species2");    
     String number2 = request.getParameter("number2");
     String uncertainty2 = request.getParameter("uncertainty2");

     String firstname = request.getParameter("firstname");    
     String lastname = request.getParameter("lastname");
     String personId = request.getParameter("personid");
     String applytogroup = request.getParameter("applytogroup");
     String delete = request.getParameter("delete");
     String notes = request.getParameter("notes");

     Network network = NetworkFactory.newInstance();
     
     if (type.equals("Animal") && !network.hasGenus(genus)) {
         out.println(" { success: false, text: '"+genus+" is not a correct genus name.'}");
         return;         
     }

     if (type.equals("Animal") && !species.equals("unknown") && !network.hasSpecies(genus, species)) {
         out.println(" { success: false, text: '"+species+" is not a correct species name.'}");
         return;         
     }


     if (type.equals("Animal") && genus2 != null && !genus2.equals("") && !network.hasGenus(genus2)) {
         out.println(" { success: false, text: '"+genus2+" is not a correct genus name.'}");
         return;         
     }

     if (type.equals("Animal") &&  genus2 != null && !genus2.equals("") && !species2.equals("unknown") && !network.hasSpecies(genus2, species2)) {
         out.println(" { success: false, text: '"+species2+" is not a correct species name.'}");
         return;         
     }


     CameraTrapPhoto photo = network.getCameraTrapPhoto(trapId, event, raw); 
     CameraTrapPhoto changedPhoto = null;

     Set<CameraTrapPhoto> gallery = new HashSet<CameraTrapPhoto>();	
     if (applytogroup != null && applytogroup.equals("true")) {
     	gallery = network.getCameraTrapPhotoCluster(photo);

	if (delete != null && !delete.equals("")) {
	    Set<String> delPhotoNames = new HashSet<String>();
	    StringTokenizer st = new StringTokenizer(delete, ",");
	    while (st.hasMoreTokens()) {
	       delPhotoNames.add(st.nextToken());
	    }

	    Set<CameraTrapPhoto> tmpGallery = new HashSet<CameraTrapPhoto>();
	    for (CameraTrapPhoto tmpPhoto : gallery) {
	       if (!delPhotoNames.contains(tmpPhoto.getRawName())) {
	           tmpGallery.add(tmpPhoto);
	       }
	    }
	    
	    gallery = tmpGallery;
	}

     } else {         
        gallery.add(photo);
     }

     if (type.equals("Misfired")) {
         gallery.addAll(network.getLaterCameraTrapPhotos(trapId, event, photo.getId()));
     }

     Person person = network.getPersonById(new Integer(personId));
     CameraTrapPhotoType photoType = network.getCameraTrapPhotoType(type);

     for (CameraTrapPhoto tmp : gallery) {
         if (type.equals("Animal")) {
             List<PhotoSpeciesRecord> records = network.getPhotoSpeciesRecords(tmp);
             if (records.isEmpty()) {
                 System.out.println("create a new record");

		 // create a new record
                 PhotoSpeciesRecord record = new PhotoSpeciesRecord(tmp, genus, species, new Integer(number), person);
		 record.setUncertainty(uncertainty);
                 network.save(record);

		 if (genus2 != null && !genus2.equals("")) {
		     record = new PhotoSpeciesRecord(tmp, genus2, species2, new Integer(number2), person);
		     record.setUncertainty(uncertainty2);
                     network.save(record);
		 } 

             } else {
                 PhotoSpeciesRecord record = records.get(0);
                 record.setGenus(genus);
	         record.setSpecies(species);
	         record.setNumberOfAnimals(new Integer(number));
		 record.setUncertainty(uncertainty);

	         record.setIdentifiedBy(person);
	         record.setCameraTrapPhoto(tmp);
                 network.update(record);

		 if (genus2 != null && !genus2.equals("")) {
		     if (records.size() < 2) {
		         record = new PhotoSpeciesRecord(tmp, genus2, species2, new Integer(number2), person);
			 record.setUncertainty(uncertainty2);
                         network.save(record);
		     } else {
		         record = records.get(1);
                 	 record.setGenus(genus2);
	         	 record.setSpecies(species2);
	         	 record.setNumberOfAnimals(new Integer(number2));
		         record.setUncertainty(uncertainty2);   
	         	 record.setIdentifiedBy(person);
	         	 record.setCameraTrapPhoto(tmp);
                 	 network.update(record);
		     }
		 } else {
		     if (records.size() == 2) {
		        record = records.get(1);
			network.delete(record);
		     }
		 }
             }
	 } else if (type.equals("Blank") || type.equals("Start") || type.equals("End") || type.equals("Setup")) {

	     if (type.equals("Start")) {

	        // validate it
		CameraTrapPhoto endPhoto = network.getEndPhoto(trapId, event);
	        if (endPhoto != null && endPhoto.getTakenTime().getTime() <= tmp.getTakenTime().getTime()) {
		    out.println(" { success: false, text: 'The Start time is not later than the End time.'}");
		    return;
		}

	        // remove the existing "Start" in the same collection
                CameraTrapPhoto startPhoto = network.getStartPhoto(trapId, event);
		if (startPhoto != null) {
		    startPhoto.setType(null);
		    startPhoto.setTypeIdentifiedBy(null);
		    changedPhoto = startPhoto;
		}
		
	     } else if (type.equals("End")) {

	        // validate it
		CameraTrapPhoto startPhoto = network.getStartPhoto(trapId, event);
	        if (startPhoto != null && tmp.getTakenTime().getTime() <= startPhoto.getTakenTime().getTime()) {
		    out.println(" { success: false, text: 'The Start time is not later than the End time.' }");
		    return;
		}

	        // remove the existing "End" in the same collection
		CameraTrapPhoto endPhoto = network.getEndPhoto(trapId, event);
		if (endPhoto != null) {
		    endPhoto.setType(null);
		    endPhoto.setTypeIdentifiedBy(null);
		    changedPhoto = endPhoto;
		}
	     }

	     List<PhotoSpeciesRecord> records = network.getPhotoSpeciesRecords(tmp);
	     for (PhotoSpeciesRecord record : records) {
	         network.delete(record);
             }	 

	 } 

	 tmp.setType(photoType);
	 tmp.setTypeIdentifiedBy(person); 
	 tmp.setNote(notes);
	 network.update(tmp);

	 if (tmp.getPublicId() != null) {
	     int pid = tmp.getCollection().getCameraTrap().getSite().getPublicId();
	     int cid = tmp.getCollection().getPublicId();
	     int iid = tmp.getPublicId();

    	     SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	     String typeName = tmp.getType().getName();
	     Person idPerson = tmp.getTypeIdentifiedBy();	
	     String idName = idPerson.getFirstName()+" "+idPerson.getLastName(); 

	     String gs = null;
	     String uncertainty1 = null;
	     Set<PhotoSpeciesRecord> records = tmp.getRecords();
	     for (PhotoSpeciesRecord record : records) {
		 gs = record.getGenus()+" "+record.getSpecies();
		 uncertainty1 = record.getUncertainty(); 
		 break;
	     }	

             CAS.updateImage(pid,
			     cid,
			     iid,
                             photo.getRawName(),
                             format.format(photo.getTakenTime()),
                             typeName,
                             idName,
                             gs,
                             uncertainty1);

	 }

     }

     if (type.equals("Misfired")) {
         CameraTrapPhoto aPhoto = network.getCameraTrapPhotoBefore(trapId, event, photo.getId());
	 if (aPhoto != null) {
             gallery.add(aPhoto);

	     CameraTrapPhoto endPhoto = network.getEndPhoto(trapId, event);
	     if (endPhoto == null) {
		 aPhoto.setType(network.getCameraTrapPhotoType("End"));
		 aPhoto.setTypeIdentifiedBy(person);
		 network.update(aPhoto);
	     }
	 }
     }     

     network.close();
    	
     out.println("{ success : true,");
     out.println("  line : 1,");
     out.println("  update : [");

     boolean first = true;
     for (CameraTrapPhoto tmp : gallery) {
        if (first) {
	    first = false;
	} else {
	    out.println(",");
	} 
        out.println("           { trap: '"+tmp.getCollection().getCameraTrap().getName()+"', event: '"+tmp.getCollection().getEvent()+"', raw: '"+tmp.getRawName()+"'}");  
     }

     out.println("   ]");

     if (changedPhoto != null) {
        out.println("  ,");
        out.println("  remove : { trap: '"+changedPhoto.getCollection().getCameraTrap().getName()+"', event: '"+changedPhoto.getCollection().getEvent()+"', raw: '"+changedPhoto.getRawName()+"'}");  
     }

     out.println("}");
%>
