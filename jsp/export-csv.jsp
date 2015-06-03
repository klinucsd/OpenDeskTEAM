<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.crypto.*" %>
<%@ page import="javax.xml.bind.*" %> 
<%@ page import="javax.xml.transform.stream.StreamSource" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="ch.enterag.utils.zip.*" %>
<%@ page import="au.com.bytecode.opencsv.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>

<%
     String path = request.getParameter("path");
     String event = request.getParameter("event");
     String arrayName = request.getParameter("array");
     String siteId = request.getParameter("siteId");

     String prefix = "";

     // get the directory for output
     File exportDir = new File(path);

     try {
        Network network = NetworkFactory.newInstance();
        Site site = network.getSiteById(new Integer(siteId));	
	Protocol tv = network.getProtocol(4);
	File exportFile;
	String sql = 
              " SELECT \n"+
	      "        sampling_units.unit_name AS camera_trap, \n"+
	      "        imas.ima_description AS camera_array, \n"+
	      "        sampling_units.latitude AS latitude, \n"+
	      "        sampling_units.longitude AS longitude, \n"+
	      "        tv_photo.id AS photo_id, \n"+
	      "        tv_photo.raw_name AS photo_name, \n"+
	      "        date_format(tv_photo.taken_time, '%Y-%m-%d') AS photo_date, \n"+
	      "        time(tv_photo.taken_time) AS photo_time, \n"+
	      "        tv_photo_type.name AS photo_type, \n"+	
	      "        tv_photo_animal.genus AS genus, \n"+
	      "        tv_photo_animal.species AS species, \n"+
	      "        tv_photo_animal.number_of_animals AS number_of_animals, \n"+
	      "        tv_photo_animal.uncertainty AS uncertainty, \n"+
	      "        tv_photo_metadata.jpeg_comment_Tmp AS temperature, \n"+
	      "        tv_photo_metadata.jpeg_comment_MP AS moon_phase, \n"+
	      "        person.first_name AS first_name_identifying_image, \n"+
	      "        person.last_name AS last_name_identifying_image, \n"+
              "        sites_team.site_name project, \n"+
              "        tv_camera_trap_data.event AS event, \n"+
	      "        tv_camera_trap_data.starttime AS camera_trap_start_time, \n"+
	      "        tv_camera_trap_data.endtime AS camera_trap_end_time, \n"+
	      "        inventory.manufacturer AS camera_manufacturer, \n"+
	      "        inventory.model AS camera_model, \n"+
	      "        tv_camera_trap_data.camera_serial_number AS camera_serial_number, \n"+
	      "        person1.first_name AS first_name_set_camera, \n"+
	      "        person1.last_name AS last_name_set_camera, \n"+
	      "        person2.first_name AS first_name_pick_camera, \n"+
	      "        person2.last_name AS last_name_pick_camera, \n"+
	      "        institution.institution_name AS institution_name \n"+
              "   FROM tv_camera_trap_data, \n"+
              "        tv_photo LEFT JOIN tv_photo_type ON (tv_photo.photo_type_id = tv_photo_type.id) LEFT JOIN tv_photo_animal ON (tv_photo_animal.photo_id=tv_photo.id) LEFT JOIN person ON (tv_photo_animal.identified_by = person.person_id) , \n"+
              "        sampling_units, \n"+
	      "        imas, \n"+
	      "        person person1, \n"+
	      "        person person2, \n"+
	      "        tv_photo_metadata, \n"+
	      "        inventory, \n"+
	      "        sites_team, \n"+
	      "        institution, \n"+
	      "        institution_site \n"+
              "  WHERE tv_photo.camera_trap_data_id = tv_camera_trap_data.id \n"+
              "    AND tv_camera_trap_data.camera_trap_id = sampling_units.id \n"+
              "    AND imas.id = sampling_units.ima_id \n"+
              "    AND sampling_units.site_id="+siteId+" \n"+
	      "    AND sampling_units.site_id=sites_team.site_id \n"+
	      "    AND institution_site.site_id=sites_team.site_id \n"+
	      "    AND institution_site.institution_id=institution.institution_id \n"+
	      "    AND tv_camera_trap_data.set_by = person1.person_id \n"+
	      "    AND tv_camera_trap_data.picked_by = person2.person_id \n"+
	      "    AND tv_photo_metadata.photo_id = tv_photo.id \n"+
              "    AND tv_camera_trap_data.camera_serial_number = inventory.serial_number \n"+
              "    AND tv_camera_trap_data.event='"+event+"' \n";

        if (arrayName != null && !arrayName.equals("")) {
	    arrayName = arrayName.substring(5);
	    String topName = "CT-"+site.getAbbv()+"-"+event+"-Array"+arrayName;
            exportFile = new File(exportDir, topName+".csv");
	    sql += "    AND imas.ima_name="+arrayName+" \n";
        } else {
	    String topName = "CT-"+site.getAbbv()+"-"+event;
            exportFile = new File(exportDir, topName+".csv");
	}

        sql += " ORDER BY unit_name, photo_name";
	//System.out.println(sql);      
	//System.out.println("Export to the directory: "+exportFile.getAbsolutePath());

        FileWriter fw = new FileWriter(exportFile, false);
        CSVWriter out1 = new CSVWriter(fw, ',');

	String[] header = new String[] {
	      "camera_trap",
	      "camera_array",
	      "latitude",
              "longitude",
	      "photo_id",
	      "photo_name",
	      "photo_date",
	      "photo_time",
	      "photo_type",	
	      "genus",
	      "specise",
	      "number_of_animals",
	      "uncertainty",
	      "temperature",
	      "moon_phase",
	      "first_name_identifying_image",	
              "last_name_identifying_image",	
              "project",
              "event",
	      "camera_trap_start_time",
	      "camera_trap_end_time",
	      "camera_manufacturer",
	      "camera_model",
	      "camera_serial_number",
	      "first_name_set_camera",
	      "last_name_set_camera",
	      "first_name_pick_camera",
	      "last_name_pick_camera",
	      "institution_name"	
	};

        out1.writeNext(header); 

	Session hsession = network.getSession();	
        Connection connection = hsession.connection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        out1.writeAll(rs, false);
        out1.close();
        connection.commit();
        statement.close();
        connection.close();

        network.close();

	out.println("{ success: true, name: '"+exportFile.getName()+"'} ");

    } catch (Exception ex) {
        ex.printStackTrace();
	out.println("{ success : false, message: '"+ex.getMessage()+"' } ");
    }
%>