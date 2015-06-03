
package org.team.sdsc.datamodel.util;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.net.ssl.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.HttpMethodParams.*;
import org.apache.commons.httpclient.cookie.*;
import org.apache.commons.httpclient.protocol.*; 
import javax.net.ssl.HttpsURLConnection;
import org.team.sdsc.datamodel.Site;
import org.team.sdsc.datamodel.Camera;
import org.team.sdsc.datamodel.CameraTrap;
import org.team.sdsc.datamodel.Person;
import org.team.sdsc.datamodel.Institution;
import org.team.sdsc.datamodel.CameraTrapPhoto;
import org.team.sdsc.datamodel.PhotoSpeciesRecord;
import org.team.sdsc.datamodel.CameraTrapPhotoCollection;
import org.team.sdsc.datamodel.Network;


public class CAS {

    public static String username = null; // "TestUser";
    public static String password = null; // "testpassword";
    private static String server = "https://15.125.91.186:8443/cas/v1/tickets";
    private static String service = "https://15.125.127.150:8443/CTFSystem/projects.xml";

    public static String getTGT(HttpClient client, String username, String password) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	    return url;
	}

	return null;
    }



    public static void getProjects() throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects.xml");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    GetMethod get = new GetMethod("https://15.125.127.150:8443/CTFSystem/projects.xml?ticket="+ticket);
	    returnCode = client.executeMethod(get);
	    responseText = get.getResponseBodyAsString();
	    get.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	}

    }



    public static void getProject(int projectId) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+".xml");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    GetMethod get = new GetMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+".xml?ticket="+ticket);
	    returnCode = client.executeMethod(get);
	    responseText = get.getResponseBodyAsString();
	    get.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	}

    }


    public static String createProject(String projectId, 
				       String projectName, 
				       String projectObjective, 
				       String projectStatus, 
				       String projectDataUseAndConstraints, 
				       String publishDate, 
				       String countryCode, 
				       String ownerName, 
				       String ownerEmail, 
				       String principalInvestigatorName,
				       String principalInvestigatorEmail,
				       String organizationName) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");

	    String json = "{\"projectId\": \""+projectId+"\", "+
		" \"projectName\": \""+projectName+"\", "+
		" \"projectObjective\": \""+projectObjective+"\", "+ 
		" \"projectStatus\": \""+projectStatus+"\", "+
		" \"projectDataUseAndConstraints\": \""+projectDataUseAndConstraints+"\", "+
		" \"publishDate\": \""+publishDate+"\", "+
		" \"countryCode\": \""+countryCode+"\", "+
		(ownerName == null ? "" : " \"projectOwnerName\": \""+ownerName+"\", ")+
		(ownerEmail == null ? "" : " \"projectOwnerEmail\": \""+ownerEmail+"\", ")+
		(principalInvestigatorName == null ? "" : " \"principalInvestigatorName\": \""+principalInvestigatorName+"\", ")+
		(principalInvestigatorEmail == null ? "" : " \"principalInvestigatorEmail\": \""+principalInvestigatorEmail+"\", ")+
		(organizationName == null ? "" : " \"organizationName\": \""+organizationName+"\"")+"}";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfProjectId\":\"");
	    pos2 = responseText.indexOf("\",\"projectId\":");
	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+18, pos2);
	    }
	}

	return null;

    }



    public static String updateProject(int pid,
				       String projectId, 
				       String projectName, 
				       String projectObjective, 
				       String projectStatus, 
				       String projectDataUseAndConstraints, 
				       String publishDate, 
				       String countryCode, 
				       String ownerName, 
				       String ownerEmail, 
				       String principalInvestigatorName,
				       String principalInvestigatorEmail,
				       String organizationName) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);

	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+pid);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+pid+"?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");

	    String json = "{\"projectId\": \""+projectId+"\", "+
		" \"projectObjective\": \""+projectObjective+"\", "+ 
		" \"projectStatus\": \""+projectStatus+"\", "+
		" \"projectDataUseAndConstraints\": \""+projectDataUseAndConstraints+"\", "+
		" \"publishDate\": \""+publishDate+"\", "+
		" \"countryCode\": \""+countryCode+"\", "+
		(ownerName == null ? "" : " \"projectOwnerName\": \""+ownerName+"\", ")+
		(ownerEmail == null ? "" : " \"projectOwnerEmail\": \""+ownerEmail+"\", ")+
		(principalInvestigatorName == null ? "" : " \"principalInvestigatorName\": \""+principalInvestigatorName+"\", ")+
		(principalInvestigatorEmail == null ? "" : " \"principalInvestigatorEmail\": \""+principalInvestigatorEmail+"\", ")+
		(organizationName == null ? "" : " \"organizationName\": \""+organizationName+"\", ")+
		" \"projectName\": \""+projectName+"\" "+"}";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfProjectId\":\"");
	    pos2 = responseText.indexOf("\",\"projectId\":");
	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+18, pos2);
	    } else {
		pos1 = responseText.indexOf("\"errorCode\":\"ERROR\",\"description\":\"");
		pos2 = responseText.indexOf("\"}],\"projects\":");
		if (pos1 != -1 && pos2 != 1) {
		    String error = responseText.substring(pos1+35, pos2);
		    throw new java.sql.SQLException("Encounter the following error when saving the image "+projectId+": "+error+".");
		}
	    }

	}

	return null;

    }


    public static void updateProject(Network network, Site site) throws Exception {

	Integer projectId = site.getPublicId();
	if (projectId != null) {

	    String objective = site.getStatus();
	    if (objective == null) objective = "Unknown";

	    Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");     
            
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

	    CAS.updateProject(projectId.intValue(),
			      site.getAbbv(),
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
	}

    }


    public static void deleteProject(int projectId) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    DeleteMethod delete = new DeleteMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"?ticket="+ticket);
	    //delete.addRequestHeader("X-Auth-Token", ticket);
	    delete.getParams().setParameter("ticket", ticket); 
	    returnCode = client.executeMethod(delete);
	    responseText = delete.getResponseBodyAsString();
	    delete.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	}

    }


    ///////////////////////////////////////////////////////////////
    //
    // event
    //
    ///////////////////////////////////////////////////////////////
        

    public static String createEvent(int projectId, String eventName) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/events");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/events?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"eventName\": \""+eventName+"\", "+
		" \"prctfEventId\": "+projectId+", "+
		" \"projectId\": \"OT1\"} ";

	    System.out.println("==================================");
	    System.out.println("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/events?ticket="+ticket);
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfEventId\":");
	    pos2 = responseText.indexOf(",\"eventName\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+15, pos2);
	    }
	    
	    
	}

	return null;

    }


    public static String updateEvent(int projectId, 
				     int eventId, 
				     String eventName) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/events/"+eventId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/events/"+eventId+"?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"eventName\": \""+eventName+"\", "+
		" \"prctfEventId\": "+projectId+", "+
		" \"projectId\": \"OT1\"} ";

	    System.out.println("==================================");
	    System.out.println("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/events/"+eventId+"?ticket="+ticket);
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfEventId\":");
	    pos2 = responseText.indexOf(",\"eventName\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+15, pos2);
	    }
	    
	    
	}

	return null;

    }



    public static void deleteEvent(int projectId, int eventId) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/events/"+eventId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    DeleteMethod delete = new DeleteMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/events/"+eventId+"?ticket="+ticket);
	    //delete.addRequestHeader("X-Auth-Token", ticket);
	    //delete.getParams().setParameter("ticket", ticket); 
	    returnCode = client.executeMethod(delete);
	    responseText = delete.getResponseBodyAsString();
	    delete.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	}

    }



    ///////////////////////////////////////////////////////////////
    //
    // camera
    //
    ///////////////////////////////////////////////////////////////
        

    public static String createCamera(int projectId, 
				      String cameraMake, 
				      String cameraModel, 
				      String serialNo) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/cameras");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/cameras?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"cameraMake\": \""+cameraMake+"\", "+
		" \"cameraModel\": \""+cameraModel+"\", "+
		" \"cameraId\": \""+serialNo+"\", "+
		" \"serialNo\": \""+serialNo+"\"} ";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfCameraId\":");
	    pos2 = responseText.indexOf(",\"cameraId\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+16, pos2);
	    }
	    
	    
	}

	return null;

    }


    public static String updateCamera(int projectId,
				      int cameraId,
				      String cameraMake, 
				      String cameraModel, 
				      String serialNo) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/cameras/"+cameraId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/cameras/"+cameraId+"?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"cameraMake\": \""+cameraMake+"\", "+
		" \"cameraModel\": \""+cameraModel+"\", "+
		" \"cameraId\": \""+serialNo+"\", "+
		" \"serialNo\": \""+serialNo+"\"} ";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfCameraId\":");
	    pos2 = responseText.indexOf(",\"cameraId\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+16, pos2);
	    }
	    
	    
	}

	return null;

    }



    public static void deleteCamera(int projectId, int cameraId) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/cameras/"+cameraId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    DeleteMethod delete = new DeleteMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/cameras/"+cameraId+"?ticket="+ticket);
	    //delete.addRequestHeader("X-Auth-Token", ticket);
	    //delete.getParams().setParameter("ticket", ticket); 
	    returnCode = client.executeMethod(delete);
	    responseText = delete.getResponseBodyAsString();
	    delete.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	}

    }



    ///////////////////////////////////////////////////////////////
    //
    // deployment
    //
    ///////////////////////////////////////////////////////////////
        

    public static String createDeployment(int projectId, 
					  String deploymentId, 
					  String deploymentArrayName, 
					  String deploymentBeginDate, 
					  String deploymentEndDate, 
					  Double latitude, 
					  Double longitude,
					  Integer cameraId,
					  String eventName) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/deployments?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"deploymentId\": \""+deploymentId+"\", "+
		" \"deploymentArrayName\": \""+deploymentArrayName+"\", "+
		" \"deploymentBeginDate\": \""+deploymentBeginDate+"\", "+ 
		" \"deploymentEndDate\": \""+deploymentEndDate+"\", "+
		" \"eventName\": \""+eventName+"\", "+
		(latitude != null ? " \"latitude\": "+latitude+", " : "")+
		(longitude != null ? " \"longitude\": "+longitude+", " : "")+
		" \"baitType\":\"0\", "+
		" \"baitDesc\":\"No\", "+
		" \"featureType\":\"0\", "+
		" \"featureTypeMethodology\":\"0\", "+
		" \"quitePeriodSetting\":\"0\", "+
		" \"cameraId\": "+cameraId+"}";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfDeploymentId\":\"");
	    pos2 = responseText.indexOf("\",\"deploymentId\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+21, pos2);
	    } else {
		pos1 = responseText.indexOf("\"errorCode\":\"Error\",\"description\":\"");
		pos2 = responseText.indexOf("\"}],\"projects\":");
		if (pos1 != -1 && pos2 != 1) {
		    String error = responseText.substring(pos1+35, pos2);
		    throw new java.sql.SQLException("Encounter the following error when saving the deployment "+deploymentId+": "+error+".");
		}
	    }

	}

	return null;

    }


    public static String updateDeployment(int projectId, 
					  int dId,
					  String deploymentId, 
					  String deploymentArrayName, 
					  String deploymentBeginDate, 
					  String deploymentEndDate, 
					  Double latitude, 
					  Double longitude,
					  Integer cameraId,
					  String eventName) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+dId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/deployments/"+dId+"?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"deploymentId\": \""+deploymentId+"\", "+
		" \"deploymentArrayName\": \""+deploymentArrayName+"\", "+
		" \"deploymentBeginDate\": \""+deploymentBeginDate+"\", "+ 
		" \"deploymentEndDate\": \""+deploymentEndDate+"\", "+
		" \"eventName\": \""+eventName+"\", "+
		(latitude != null ? " \"latitude\": "+latitude+", " : "")+
		(longitude != null ? " \"longitude\": "+longitude+", " : "")+
		" \"baitType\":\"0\", "+
		" \"baitDesc\":\"No\", "+
		" \"featureType\":\"0\", "+
		" \"featureTypeMethodology\":\"0\", "+
		" \"quitePeriodSetting\":\"0\", "+
		" \"cameraId\": "+cameraId+"}";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfDeploymentId\":\"");
	    pos2 = responseText.indexOf("\",\"deploymentId\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+21, pos2);
	    } else {
		pos1 = responseText.indexOf("\"errorCode\":\"Error\",\"description\":\"");
		pos2 = responseText.indexOf("\"}],\"projects\":");
		if (pos1 != -1 && pos2 != 1) {
		    String error = responseText.substring(pos1+35, pos2);
		    throw new java.sql.SQLException("Encounter the following error when saving the deployment "+deploymentId+": "+error+".");
		}
	    }

	}

	return null;

    }


    public static void updateDeployment(Network network, CameraTrapPhotoCollection collection) throws Exception {

	CameraTrap trap = collection.getCameraTrap();
	Site site = trap.getSite();
	List<Camera> cameras = network.getCameras(site);

	// find camera id
	Integer cameraId = null;
	for (Camera camera : cameras) {
	    if (camera.getSerialNumber().equals(collection.getCameraSerialNumber())) {
		cameraId = camera.getPublicId();
		break;
	    }
	}

	Integer cid = collection.getPublicId();
	if ( cid != null) {
	    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    Date b = collection.getStartTime();
	    Date e = collection.getEndTime();

	    CAS.updateDeployment(site.getPublicId().intValue(), 
				 cid.intValue(),
				 collection.getEvent()+"_"+collection.getCameraTrap().getName(),
				 collection.getCameraTrap().getBlock().getName(),
				 format.format(b),
				 format.format(e),
				 trap.getLatitude(),
				 trap.getLongitude(),
				 cameraId,
				 collection.getEvent());     

	}   

    }



    public static void deleteDeployment(int projectId, int deploymentId) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    DeleteMethod delete = new DeleteMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"?ticket="+ticket);
	    //delete.addRequestHeader("X-Auth-Token", ticket);
	    //delete.getParams().setParameter("ticket", ticket); 
	    returnCode = client.executeMethod(delete);
	    responseText = delete.getResponseBodyAsString();
	    delete.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	}

    }



    ///////////////////////////////////////////////////////////////////////
    //
    // Image
    //
    ///////////////////////////////////////////////////////////////////////

    public static String createImage(int projectId,
				     int deploymentId,
				     String imageId, 
				     //String eventName, 
				     String timeCaptured,
				     String photoType,
				     String photoTypeIdentifiedBy,
				     String genusSpecies,
				     String animalRecognizable) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);
	    System.out.println("https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images");

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"imageId\": \""+imageId+"\", "+
		//" \"eventName\": \""+eventName+"\", "+
		(photoType != null ? " \"photoType\": \""+photoType+"\", " : "")+
		(photoTypeIdentifiedBy != null ? " \"photoTypeIdentifiedBy\": \""+photoTypeIdentifiedBy+"\", " : "")+
		(genusSpecies != null ? " \"genusSpecies\": \""+genusSpecies+"\", " : "")+
		" \"animalRecognizable\": \""+animalRecognizable+"\", "+
		" \"restrictionsOnAccess\": \"No Restrictions\", "+
	        " \"timeCaptured\": \""+timeCaptured+"\" }";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfImageId\":");
	    pos2 = responseText.indexOf(",\"imageId\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+15, pos2);
	    } else {
		pos1 = responseText.indexOf("\"errorCode\":\"ERROR\",\"description\":\"");
		pos2 = responseText.indexOf("\"}],\"projects\":");
		if (pos1 != -1 && pos2 != 1) {
		    String error = responseText.substring(pos1+35, pos2);
		    throw new java.sql.SQLException("Encounter the following error when saving the image "+imageId+": "+error+".");
		}
	    }
	    
	}

	return null;

    }


    public static void updateImage(CameraTrapPhoto photo) throws Exception{

	Integer photoId = photo.getPublicId();
	if (photoId != null) {

	    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	    String typeName = typeName = photo.getType().getName();
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

	    updateImage(photo.getCollection().getCameraTrap().getSite().getPublicId().intValue(),
			photo.getCollection().getPublicId().intValue(),
			photoId.intValue(),
			photo.getRawName(),
			//collection.getEvent(),
			format.format(photo.getTakenTime()),
			typeName,
			idName,
			gs,
			uncertainty);
	}

    }


    public static String updateImage(int projectId,
				     int deploymentId,
				     int imageId, 
				     String imageName,
				     String timeCaptured,
				     String photoType,
				     String photoTypeIdentifiedBy,
				     String genusSpecies,
				     String animalRecognizable) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);
	    System.out.println("https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId);

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId+"?ticket="+ticket);
	    post.addRequestHeader("Content-Type", "application/json");
	    String json = "{\"imageId\": \""+imageName+"\", "+
		(photoType != null ? " \"photoType\": \""+photoType+"\", " : "")+
		(photoTypeIdentifiedBy != null ? " \"photoTypeIdentifiedBy\": \""+photoTypeIdentifiedBy+"\", " : "")+
		(genusSpecies != null ? " \"genusSpecies\": \""+genusSpecies+"\"," : "")+
		(animalRecognizable != null ? " \"animalRecognizable\": \""+animalRecognizable+"\", " : "")+
		" \"restrictionsOnAccess\": \"No Restrictions\", "+
	        " \"timeCaptured\": \""+timeCaptured+"\" }";

	    System.out.println("=================================");
	    System.out.println(json);

	    post.setRequestBody(json);

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"prctfImageId\":");
	    pos2 = responseText.indexOf(",\"imageId\":");

	    System.out.println("pos1 = "+pos1);
	    System.out.println("pos2 = "+pos2);

	    if (pos1 != -1 && pos2 != -1) {
		return responseText.substring(pos1+15, pos2);
	    } else {
		pos1 = responseText.indexOf("\"errorCode\":\"Error\",\"description\":\"");
		pos2 = responseText.indexOf("\"}],\"projects\":");
		if (pos1 != -1 && pos2 != 1) {
		    String error = responseText.substring(pos1+35, pos2);
		    throw new java.sql.SQLException("Encounter the following error when saving the image "+imageId+": "+error+".");
		}
	    }
	    	    
	}

	return null;

    }



    public static void deleteImage(int projectId, int deploymentId, int imageId) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId);
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);

	    DeleteMethod delete = new DeleteMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId+"?ticket="+ticket);
	    //delete.addRequestHeader("X-Auth-Token", ticket);
	    //delete.getParams().setParameter("ticket", ticket); 
	    returnCode = client.executeMethod(delete);
	    responseText = delete.getResponseBodyAsString();
	    delete.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	}

    }



    public static String uploadImage(int projectId,
				     int deploymentId,
				     int imageId,
				     File fileToUpload) throws Exception {

	// ignore ssl verification
	Protocol lEasyHttps = new Protocol("https", new EasySslProtocolSocketFactory(), 8443);
	Protocol.registerProtocol("https", lEasyHttps);

	// get ticket
	HttpClient client = new HttpClient();   
	PostMethod post = new PostMethod(server);
	post.addParameter("username", username);
	post.addParameter("password", password);
	int returnCode = client.executeMethod(post);
	String responseText = post.getResponseBodyAsString();
	post.releaseConnection();

	System.out.println("=================================");
	System.out.println(responseText);

	int pos1 = responseText.indexOf(" action=\"");
	int pos2 = responseText.indexOf("\" method=\"POST\">");
	if (pos1 != -1 && pos2 != -1) {
	    String url = responseText.substring(pos1+9, pos2);
	
	    System.out.println("=================================");
	    System.out.println(url);

	    post = new PostMethod(url);
	    post.addParameter("service", "https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId+"/file");
	    returnCode = client.executeMethod(post);
	    String ticket = post.getResponseBodyAsString();
	    post.releaseConnection();

	    System.out.println("==================================");
	    System.out.println(ticket);
	    System.out.println("https://15.125.127.150/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId+"/file");

	    post = new PostMethod("https://15.125.127.150:8443/CTFSystem/projects/"+projectId+"/deployments/"+deploymentId+"/images/"+imageId+"/file?ticket="+ticket);
	    //fileToUpload = new File("/tmp/test.jpg");
	    Part[] parts = {
		new FilePart("file", fileToUpload)
	    };
	    post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));

	    returnCode = client.executeMethod(post);
	    responseText = post.getResponseBodyAsString();
	    post.releaseConnection();	

	    System.out.println("=================================");
	    System.out.println(responseText);

	    pos1 = responseText.indexOf("\"Success\":");

	    if (pos1 != -1) {
		return "Success";
	    }
	    
	    
	}

	return null;

    }


        
}

