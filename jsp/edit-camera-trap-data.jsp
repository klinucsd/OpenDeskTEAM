
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%

try {
     String trapName = request.getParameter("trapName");
     String event = request.getParameter("event");
     String value = request.getParameter("value");
     String field = request.getParameter("field");  

     //System.out.println(trapName+"   "+event+"    "+value+"     "+field);

     Network network = NetworkFactory.newInstance();
     CameraTrapPhotoCollection collection = network.getCameraTrapPhotoCollection(event, trapName);
     if (field.equals("Memory_Card_Serial_Number")) {
        collection.setMemoryCardSerialNumber(value);
     } else if (field.equals("Notes")) {
        collection.setNotes(value);
     } else if (field.equals("Camera_Serial_Number")) {
        collection.setCameraSerialNumber(value);
     } else if (field.equals("Pick_Person")) {
        Person person = network.getPersonById(new Integer(value));
	if (person != null) collection.setPickPerson(person);
     } else if (field.equals("Set_Person")) {
        Person person = network.getPersonById(new Integer(value));
        if (person != null) collection.setSetPerson(person);
     } else if (field.equals("Follow_Up")) {
        DamagedCameraTrap dct = collection.getDamagedCameraTrap();
	dct.setFollowUpSteps(value);
	network.update(dct);
     } else if (field.equals("Camera_Working_")) {
        collection.setWorking(new Boolean(value));
     } else if (field.equals("Case_Damage_")) {
        collection.setCaseDamaged(new Boolean(value));
     } else if (field.equals("Camera_Damage_")) {
        collection.setCameraDamaged(new Boolean(value));
     } else if (field.equals("Card_Damage_")) {
        collection.setCardDamaged(new Boolean(value));
     } else if (field.equals("Camera_Trap_Missing_")) {
        collection.setTrapMissing(new Boolean(value));	      
     }

     network.update(collection);
     network.close();

     out.println("{ success: true }");

} catch (Exception ex) {

     ex.printStackTrace();

     out.println("{ success: false, message: '"+ex.getMessage()+"}");

}


%>