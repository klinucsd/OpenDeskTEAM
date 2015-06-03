
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%

     String site = request.getParameter("site");
     String path = request.getParameter("path");

     StringTokenizer st = new StringTokenizer(path, "/");
     st.nextToken();
     String event = st.nextToken();
     st.nextToken();
     String trapId = st.nextToken();
     String raw = st.nextToken();
     
     String genus = null;    
     String species = null;    
     Integer number = null;
     String uncertainty = null;

     String genus2 = null;    
     String species2 = null;    
     Integer number2 = null;
     String uncertainty2 = null;

     String idperson = null;
     String photoType = "unknown";

     String trg = "";
     String flash = "";
     String exposure = "";
     String temperature = "";
     String takenTime = "";

     String notes = "";

     Network network = NetworkFactory.newInstance();
     CameraTrapPhoto photo = network.getCameraTrapPhoto(trapId, event, raw);

     if (photo == null) {
         out.println("{");
	 out.println("    success: false,");
	 out.println("    message: 'Found no photo for "+path+"'");
	 out.println("}");
	 network.close();
	 return;
     }

     if (photo.getNote() != null) {
        notes = photo.getNote();
     }

     CameraTrapPhotoMetadata metadata = photo.getMetadata();

     if (metadata != null) {
         trg = metadata.getJpegCommentTrg();
	 if (trg != null) trg = trg.trim();

         flash = metadata.getExifFlashName();
	 if (flash != null) flash = flash.trim();
	 
	 exposure = metadata.getExifExposureTime();
	 if (exposure != null) exposure = exposure.trim();

	 temperature = metadata.getJpegCommentTmp();
	 if (temperature != null) temperature = temperature.trim();	 
     }

     //java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     //takenTime = formatter.format(photo.getTakenTime());

     takenTime = photo.getTakenTimeString();

     if (photo.getType() != null) {
         photoType = photo.getType().getName();
	 if (photoType.equals("Animal")) {
             Set<PhotoSpeciesRecord> records = photo.getRecords();
             if (records != null && !records.isEmpty()) {
	     	     
		 Iterator iter = records.iterator();
                 PhotoSpeciesRecord record = (PhotoSpeciesRecord)iter.next();
	         genus = record.getGenus();
	         species = record.getSpecies();
	         number = record.getNumberOfAnimals();
		 uncertainty = record.getUncertainty();
		 if (records.size() == 2) {
		     record = (PhotoSpeciesRecord)iter.next();
	             genus2 = record.getGenus();
	             species2 = record.getSpecies();
	             number2 = record.getNumberOfAnimals();
		     uncertainty2 = record.getUncertainty();	
		 }

	         idperson = record.getIdentifiedBy().getFirstName()+" "+record.getIdentifiedBy().getLastName();
             }
	 } else if (photo.getTypeIdentifiedBy() != null){
	     idperson = photo.getTypeIdentifiedBy().getFirstName()+" "+photo.getTypeIdentifiedBy().getLastName();
	 } else {
	     idperson = "";
	 }
     }
	
     network.close();
%>

{
     success : <%= !photoType.equals("unknown") || (genus != null && species != null && number != null && idperson != null)  %>,
     type: '<%= photoType %>',
     notes: '<%= notes != null ? notes.replace("'", "\\'") : ""%>',
     genus: '<%= genus %>',
     species: '<%= species %>',
     number: '<%= number %>',
     uncertainty: '<%= uncertainty %>',
<% if (genus2 != null) {  %>
     genus2: '<%= genus2 %>',
     species2: '<%= species2 %>',
     number2: '<%= number2 %>',
     uncertainty2: '<%= uncertainty2 %>',
<% } %>
     idperson: '<%= idperson != null ? idperson.replace("'", "\\'") : ""%>',    
     trg: '<%= trg != null ? trg : "Not found" %>',
     flash: '<%= flash != null ?  flash : "Not found" %>',
     exposure: '<%= exposure != null ? exposure : "Not found"%>',
     takentime: '<%= takenTime %>',
     temperature: '<%= temperature != null ? temperature : "Not found" %>'
}