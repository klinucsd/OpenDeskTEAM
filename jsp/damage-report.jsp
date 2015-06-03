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
<%

try {
     String siteId = request.getParameter("siteId");
     Network network = NetworkFactory.newInstance();
     Site aSite = network.getSiteById(new Integer(siteId));	
     //Site aSite = network.getSites().get(0);
     network.close();

     String site = request.getParameter("site");
     String trapId = request.getParameter("trapId");
     String trapName = request.getParameter("trapName");
     String event = request.getParameter("event");
     String cameraSerial = request.getParameter("cameraSerial");    
     String memoryCardSerial = request.getParameter("memoryCardSerial");
     String setperson = request.getParameter("setperson");
     String pickperson = request.getParameter("pickperson");
     String setpersonid = request.getParameter("setpersonid");
     String pickpersonid = request.getParameter("pickpersonid");
     String notes = request.getParameter("notes");         
     String cameraDetail = request.getParameter("cameraDetail");         
     String damageDetail = request.getParameter("damageDetail");
     String start = request.getParameter("start");
     String end = request.getParameter("end");
     String followup = request.getParameter("followup");

     int index = start.indexOf("GMT");
     if (index != -1) start = start.substring(0, index).trim();

     index = end.indexOf("GMT");	
     if (index != -1) end = end.substring(0, index).trim();

     //Tue Feb 09 2010 23:49:08 GMT-0800 
     //java.text.DateFormat format = new java.text.SimpleDateFormat("EEE MMM dd yyyy kk:mm:ss");
     java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

     Date startDate = format.parse(start);
     Date endDate = format.parse(end);

     Configuration mConfigure = new AnnotationConfiguration();
     mConfigure.configure();
     SessionFactory mFactory = mConfigure.buildSessionFactory();
     Session mSession = mFactory.openSession();

     CameraTrap cameraTrap = (CameraTrap)network.getSamplingUnitById(Integer.parseInt(trapId));
     Person setPerson = network.getPersonById(new Integer(setpersonid));
     Person pickPerson = network.getPersonById(new Integer(pickpersonid));

     String root = pageContext.getServletContext().getRealPath("/");
     File rootDir = new File(root, "image-repository");

     StringTokenizer st = new StringTokenizer(trapName, "-");
     st.nextToken();
     String abbrev = st.nextToken();
     int array = Integer.parseInt(st.nextToken());
     int number = Integer.parseInt(st.nextToken());

     String path = aSite.getName()+"/"+event+"/Array"+array+"/"+trapName;;
     File targetDir = new File(rootDir, path);

     if (targetDir.exists()) {
%>

{ 
   failure: true,
   text: 'The collection of <%= trapName %> for the sampling event <%= event %> at <%= site %> already  exists.'
}

<%
     } else {

         targetDir.mkdirs();

         CameraTrapPhotoCollection collection = new CameraTrapPhotoCollection();
     	 collection.setCameraTrap(cameraTrap);
    	 collection.setEvent(event);
     	 collection.setCameraSerialNumber(cameraSerial);
     	 collection.setMemoryCardSerialNumber(memoryCardSerial);
     	 collection.setSetPerson(setPerson);
     	 collection.setPickPerson(pickPerson);
     	 collection.setWorking(new java.lang.Boolean(false));
     	 collection.setNotes(notes);
     	 collection.setStartTime(startDate);
     	 collection.setEndTime(endDate);

	 Transaction tx = mSession.beginTransaction();

     	 if (damageDetail.equals("Trap Missing")) {
             collection.setTrapMissing(new java.lang.Boolean(true));

	     /*
	     DamagedCameraTrap dct = new DamagedCameraTrap();
	     dct.setCollection(collection);
	     dct.setDeployDate(startDate);
	     dct.setFoundDate(endDate);	
	     dct.setFollowUpSteps(followup);
	     mSession.save(dct);
	     */

         } else if (damageDetail.equals("Case Damaged")) {
             collection.setCaseDamaged(new java.lang.Boolean(true));

	     /*
	     DamagedCameraTrap dct = new DamagedCameraTrap();
	     dct.setCollection(collection);
	     dct.setDeployDate(startDate);
	     dct.setFoundDate(endDate);	
	     dct.setFollowUpSteps(followup);
	     mSession.save(dct);
	     */

         } else if (damageDetail.equals("Card Damaged")) {
             collection.setCardDamaged(new java.lang.Boolean(true));

	     /*
	     DamagedCameraTrap dct = new DamagedCameraTrap();
	     dct.setCollection(collection);
	     dct.setDeployDate(startDate);
	     dct.setFoundDate(endDate);	
	     dct.setFollowUpSteps(followup);
	     mSession.save(dct);
	     */

         } else {
             // Camera Damaged
             collection.setCameraDamaged(new java.lang.Boolean(true));

	     DamagedCameraTrap dct = new DamagedCameraTrap();
	     dct.setCollection(collection);
	     dct.setDeployDate(startDate);
	     dct.setFoundDate(endDate);	
 
	     if (cameraDetail.equals("Is Firing")) {
	         dct.setIsFiring(new java.lang.Boolean(true));
	     } else if (cameraDetail.equals("Has Humidity Inside")) {
                 dct.setHumidityInside(new java.lang.Boolean(true));
             } 
	     dct.setFollowUpSteps(followup);
	     mSession.save(dct);
         }
         mSession.save(collection);
	 tx.commit();
         mSession.close();

         out.println("{  success: true }");
     }

} catch (Exception ex) {
     ex.printStackTrace();
     out.println("{  success: false,");
     out.println("   text: '"+ex.getMessage()+"' }");

}

%>
