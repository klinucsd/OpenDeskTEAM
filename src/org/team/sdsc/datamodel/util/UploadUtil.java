package org.team.sdsc.datamodel.util;

import java.math.*; 
import java.util.*; 
import java.util.zip.*;
import java.io.*; 
import java.text.*;
import javax.xml.bind.*; 
import java.util.regex.*;

import javax.xml.parsers.*; 
import org.xml.sax.*; 
import org.w3c.dom.*; 
import javax.xml.namespace.*; 
import jxl.*; 
import au.com.bytecode.opencsv.*;

import org.apache.http.*;
import org.apache.http.message.*; 
import org.apache.http.protocol.*; 
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.mime.HttpMultipartMode;

import org.hibernate.*;
import org.hibernate.cfg.*;

import org.team.sdsc.datamodel.*;


public class UploadUtil {

   
    public static boolean login(DefaultHttpClient httpclient, String username, String password) throws Exception {

	boolean result = false;
	HttpPost httpost = new HttpPost("http://www.teamnetwork.org/user");

	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	nvps.add(new BasicNameValuePair("name", username));
	nvps.add(new BasicNameValuePair("pass", password));	
	nvps.add(new BasicNameValuePair("form_id", "user_login"));	
	nvps.add(new BasicNameValuePair("op", "Log in"));	
	httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

	HttpResponse response = httpclient.execute(httpost);
	HttpEntity entity = response.getEntity();
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	String line = reader.readLine();
	if (line == null) {
	    result = true;
	} 
	entity.consumeContent();

	return result;
    }


    public static boolean getUserInfo(DefaultHttpClient httpclient) throws Exception {

	HttpGet httpget = new HttpGet("http://www.teamnetwork.org/about/people/klin");
	HttpResponse response = httpclient.execute(httpget);
	HttpEntity entity = response.getEntity();
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	String line = reader.readLine();
	while (line != null) {
	    //System.out.println(line);
	    line = reader.readLine();
	}
	entity.consumeContent();
	return true;
    }


    public static CameraTrapUploadJob newRemoteCameraTrapUploadJob(DefaultHttpClient httpclient, 
								   CameraTrap cameraTrap, 
								   String event,
								   Person uploader,
								   int totalNumber) throws Exception{

	String url = "http://www.teamnetwork.org/CorePortlet/jsp/management/deskTEAM/upload/new-job.jsp";
	HttpPost post = new HttpPost(url);
	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	nvps.add(new BasicNameValuePair("cameraTrapId", ""+cameraTrap.getId()));
	nvps.add(new BasicNameValuePair("event", ""+event));	
	nvps.add(new BasicNameValuePair("uploaderId", ""+uploader.getId()));		
	nvps.add(new BasicNameValuePair("total", ""+totalNumber));	
	post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

	HttpResponse response = httpclient.execute(post);
	HttpEntity entity = response.getEntity();
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	String line = reader.readLine();	
	boolean failed = false;
	while (line != null) {
	    //System.out.println("------>"+line);
	    if (line.indexOf("failed") != -1) failed = true;
	    line = reader.readLine();
	}
	entity.consumeContent();	

	CameraTrapUploadJob job = failed? null : new CameraTrapUploadJob(cameraTrap, event, uploader, totalNumber);
	return job;
    }


    public static CameraTrapUploadJob newRemoteCameraTrapUploadJob(String username,
								   String password, 
								   CameraTrap cameraTrap, 
								   String event,
								   Person uploader,
								   int totalNumber) throws Exception{
	DefaultHttpClient httpclient = new DefaultHttpClient();
	if (login(httpclient, username, password)) {
	    return newRemoteCameraTrapUploadJob(httpclient, cameraTrap, event, uploader, totalNumber);
	} else {
	    return null;
	}
    }


    public static void saveUploadJob(DefaultHttpClient httpclient, CameraTrapUploadJob uploadJob) {

    }


    public static void uploadImages(DefaultHttpClient httpclient, CameraTrapUploadJob uploadJob) throws Exception{

	String url = "http://www.teamnetwork.org/CorePortlet/jsp/management/deskTEAM/upload/upload.jsp";
	HttpPost post = new HttpPost(url);

	MultipartEntity mentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);	
	mentity.addPart("job", new StringBody("123"));
	File file = new File("/export/home/klin/CONNECT/src/edu/sdsc/connect/test/Test.java");
	mentity.addPart("file", new FileBody(file));

	post.setEntity(mentity);
	HttpResponse response = httpclient.execute(post);
	HttpEntity entity = response.getEntity();
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	String line = reader.readLine();
	while (line != null) {
	    System.out.println(line);
	    line = reader.readLine();
	}
	entity.consumeContent();
    }


    public static boolean uploadImage(DefaultHttpClient httpclient, 
				   CameraTrapUploadJob job,
				   File file,
				   int index) throws Exception{

	String url = "http://www.teamnetwork.org/CorePortlet/jsp/management/deskTEAM/upload/upload.jsp";
	HttpPost post = new HttpPost(url);

	MultipartEntity mentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	mentity.addPart("trap", new StringBody(job.getCameraTrap().getName()));
	mentity.addPart("event", new StringBody(job.getEvent()));	
	mentity.addPart("index", new StringBody(index+""));	
	mentity.addPart("file", new FileBody(file));

	post.setEntity(mentity);
	HttpResponse response = httpclient.execute(post);
	HttpEntity entity = response.getEntity();
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	boolean failed = false;
	String line = reader.readLine();
	while (line != null) {
	    //System.out.println(line);
	    if (line.indexOf("failed") != -1) failed = true;	    
	    line = reader.readLine();
	}
	entity.consumeContent();

	return !failed;
    }


    public static String uploadData(DefaultHttpClient httpclient, CameraTrapUploadJob job) throws Exception{

        JAXBContext jc = JAXBContext.newInstance( CameraTrapPhotoCollection.class);
        Marshaller m = jc.createMarshaller();

        Network network = NetworkFactory.newInstance();
	CameraTrapPhotoCollection collection = network.getCameraTrapPhotoCollection(job.getEvent(), job.getCameraTrap().getName());;
	
	StringWriter stringWriter = new StringWriter(); 
        m.marshal(collection, stringWriter);
	stringWriter.close();
	network.close();

	String url = "http://www.teamnetwork.org/CorePortlet/jsp/management/deskTEAM/upload/upload-data.jsp";
	HttpPost httpost = new HttpPost(url);

	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	nvps.add(new BasicNameValuePair("trap", job.getCameraTrap().getName()));	
	nvps.add(new BasicNameValuePair("event", job.getEvent()));	
	nvps.add(new BasicNameValuePair("data", stringWriter.toString()));	
	httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

	boolean failed = false;
        String reason = null;
	HttpResponse response = httpclient.execute(httpost);
	HttpEntity entity = response.getEntity();
	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	String line = reader.readLine();
	while (line != null) {
	    //System.out.println("====> "+line);
	    if (line.indexOf("failed") != -1 || line.indexOf("Error") != -1) {
		failed = true;
	    }
	    
	    if (line.startsWith("failed")) {
		reason = line.substring(7);
	    }
	    line = reader.readLine();
	} 
	entity.consumeContent();

	//System.out.println("----> saveData return = "+!failed);

	return reason;
	
    }


    public static void main(String[] args) throws Exception {

	/*
	DefaultHttpClient httpclient = new DefaultHttpClient();

	// login
	boolean result = login(httpclient);
	System.out.println("login: "+result);

	// get user information
	getUserInfo(httpclient);

	// get an upload job
	CameraTrap cameraTrap = null;
	Person uploader = null;
	String event = "2012.01";
	CameraTrapUploadJob uploadJob = getLocalUploadJob(cameraTrap, event);
	if (uploadJob == null) {
	    uploadJob = newRemoteCameraTrapUploadJob(httpclient, cameraTrap, event, uploader);
	    saveUploadJob(httpclient, uploadJob);
	} 

	if (!uploadJob.isFinished()) {
	    uploadImages(httpclient, uploadJob);
	    uploadMetadata(httpclient, uploadJob);
	}
	*/

    }


}
