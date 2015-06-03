<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>


<%
     String trapName = request.getParameter("trap");
     String event = request.getParameter("event");
     String raw = request.getParameter("raw");

     if (trapName != null && event != null && raw != null) {    
         Network network = NetworkFactory.newInstance();
         CameraTrapPhoto photo = network.getCameraTrapPhoto(trapName, event, raw);
	 photo = network.getNextCameraTrapPhoto(photo);
	 if (photo != null) {

	     StringTokenizer st = new StringTokenizer(photo.getCollection().getCameraTrap().getName(), "-");
	     st.nextToken();
             st.nextToken();	

	     out.println("{");
	     out.println("   success: true,");
	     out.println("   img_trap: '"+photo.getCollection().getCameraTrap().getName()+"',");
	     out.println("   img_event: '"+photo.getCollection().getEvent()+"',");
	     out.println("   img_raw: '"+photo.getRawName()+"',");
             out.println("   img_array: 'Array"+st.nextToken()+"',");

             //java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             //String takenTime = formatter.format(photo.getTakenTime());
	     //out.println("   takentime: '"+takenTime+"', ");
             out.println("   takentime: '"+photo.getTakenTimeString()+"', ");

	     String type = "";
	     if (photo.getType() != null) type = photo.getType().getName();
             out.println("   img_type: '"+type+"',");	

	     String notes = "";
	     if (photo.getNote() != null) notes = photo.getNote();
             out.println("   notes: '"+notes+"',");	
	    
	     if (!type.equals("")) {
		if (type.equals("Animal")) {
		    Set<PhotoSpeciesRecord> records = photo.getRecords();
		    PhotoSpeciesRecord record = records.iterator().next();
		    out.println("   img_genus: '"+record.getGenus()+"',");		    
		    out.println("   img_species: '"+record.getSpecies()+"',");
		    out.println("   img_number: '"+record.getNumberOfAnimals()+"',");		    		    
		}

		if (photo.getTypeIdentifiedBy() != null) {
		    out.println("   img_identified_by: '"+photo.getTypeIdentifiedBy().getFirstName()+" "+photo.getTypeIdentifiedBy().getLastName()+"',");
		}
	     }

	     CameraTrapPhotoMetadata metadata = photo.getMetadata();
	     if (metadata != null) {

		String trg = metadata.getJpegCommentTrg();
		if (trg != null) {
		    trg = trg.trim();
		} else {
		    trg = "Not found";
		}
		out.println("trg: '"+trg+"', ");

		String flash = metadata.getExifFlashName();
		if (flash != null) {
		    flash = flash.trim();
		} else {
		    flash = "Not found";
		}
		out.println("flash: '"+flash+"', ");

		String exposure = metadata.getExifExposureTime();
		if (exposure != null) {
		    exposure = exposure.trim();
		} else {
		    exposure = "Not found";
		}
		out.println("exposure: '"+exposure+"', ");

		String temperature = metadata.getJpegCommentTmp();
		if (temperature != null) {
		    temperature = temperature.trim();
		} else {
		    temperature = "Not found";
		}
		out.println("temperature: '"+temperature+"'");
	     }

	     out.println("}");
	 }

	 network.close();
     }
%>
