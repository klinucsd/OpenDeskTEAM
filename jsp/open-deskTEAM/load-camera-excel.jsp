<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.zip.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.crypto.*" %>
<%@ page import="javax.xml.bind.*" %> 
<%@ page import="javax.xml.transform.stream.StreamSource" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="jxl.*" %>

<%
 
 Network network = null;
 
 try {

     String path = request.getParameter("path");
     String siteId = request.getParameter("projectId");

     if (path.endsWith("csv")) {
             
         network = NetworkFactory.newInstance();
         Site site = network.getSiteById(new Integer(siteId));	

	 // get all serial numbers
         Session session1 = network.getSession();
         Connection conn = session1.connection();
         Statement statement = conn.createStatement();
         ResultSet rs = statement.executeQuery("SELECT serial_number FROM inventory");
         List<String> names = new ArrayList();
         while (rs.next()) {
             names.add(rs.getString(1));
         }

	 BufferedReader br = new BufferedReader(new FileReader(path));

         // skip the first line
         String line = br.readLine();
         String[] headers = line.split(",");

	 if (headers.length > 0 && !headers[0].toLowerCase().equals("manufacturer")) {
            throw new SQLException("Found no header \"Manufacturer\"");
	 }

	 if (headers.length > 1 && !headers[1].toLowerCase().equals("model")) {
            throw new SQLException("Found no header \"Modle\"");
	 }

	 if (headers.length > 2 && !headers[2].toLowerCase().equals("serial number")) {
            throw new SQLException("Found no header \"Serial Number\"");
	 }

	 line = br.readLine();
	 int r = 0;
	 while (line != null) {
             if (line.trim().equals("")) {
                 line = br.readLine();
		 r++;
                 continue;
             }
	 
             String[] cells = line.split(",");

	     String manufacturer = null;
	     String model = null;
 	     String serial = null;

	     System.out.println("------------------");
	     for(int i=0; i<cells.length; i++) { 
	         System.out.println(headers[i]+" : "+cells[i]);

		 if (i == 0) {
		      manufacturer = cells[i];
		 } else if (i == 1) {
		      model = cells[i];
		 } else if (i == 2) {
		      serial = cells[i]; 
		 }
	     }

	     if (manufacturer == null) {
                 throw new SQLException("Blank \"Manufacturer\" at the row "+(r+1)+".");	
	     }

	     if (model == null) {
                 throw new SQLException("Blank \"Model\" at the row "+(r+1)+".");	
	     }

	     if (serial == null) {
                 throw new SQLException("Blank \"Serial Number\" at the row "+(r+1)+".");	
	     }

	     if (names.contains(serial)) {
	         throw new SQLException("Duplicate serial Number \""+serial+"\" at the row "+(r+1)+".");	
	     }
 
             // create a camera
             Camera camera = new Camera();
             camera.setType("Camera");
             camera.setManufacturer(manufacturer);
             camera.setModel(model);
             camera.setSerialNumber(serial);
             camera.setSite(site);
             network.save(camera);

	     line = br.readLine();
	     r++;
	 }

         out.println("{");
         out.println("  success: true");
         out.println("}");	 

	 return;

     }

     // get the directory for output
     File excelFile = new File(path);

     network = NetworkFactory.newInstance();
     Site site = network.getSiteById(new Integer(siteId));	

     // get all serial numbers
     Session session1 = network.getSession();
     Connection conn = session1.connection();
     Statement statement = conn.createStatement();
     ResultSet rs = statement.executeQuery("SELECT serial_number FROM inventory");
     List<String> names = new ArrayList();
     while (rs.next()) {
        names.add(rs.getString(1));
     }

     Workbook wb = Workbook.getWorkbook(excelFile);
     Sheet[] sheets = wb.getSheets();
     for (Sheet sheet : sheets) {
        int rowNumber = sheet.getRows();
        int columnNumber = sheet.getColumns();
	//System.out.println("row = "+rowNumber);
	//System.out.println("column = "+columnNumber);
 
        for (int r=0; r<rowNumber; r++) {
	    String manufacturer = null;
	    String model = null;
 	    String serial = null;
	    for (int c=0; c<columnNumber; c++) {
	     	Cell cell = sheet.getCell(c, r);
		String aStr = cell.getContents().trim();
		//System.out.println(r+", "+c+" = "+aStr);

		if (r == 0 && c == 0 && !aStr.toLowerCase().equals("manufacturer")) {
		   throw new SQLException("Found no header \"Manufacturer\"");
		}

		if (r == 0 && c == 1 && !aStr.toLowerCase().equals("model")) {
		   throw new SQLException("Found no header \"Model\"");
		}

		if (r == 0 && c == 2 && !aStr.toLowerCase().equals("serial number")) {
		   throw new SQLException("Found no header \"Serial Number\"");
		}

		if (r > 0) {
		    if (c == 0) {
		        manufacturer = aStr;
		    } else if (c == 1) {
		        model = aStr;
		    } else if (c == 2) {
		        serial = aStr; 
		    }

		}
	    }

	    if (r > 0) {

	        if (manufacturer == null) {
                    throw new SQLException("Blank \"Manufacturer\" at the row "+(r+1)+".");	
	        }

	        if (model == null) {
                    throw new SQLException("Blank \"Model\" at the row "+(r+1)+".");	
	        }

	        if (serial == null) {
                    throw new SQLException("Blank \"Serial Number\" at the row "+(r+1)+".");	
	        }

	        if (names.contains(serial)) {
	            throw new SQLException("Duplicate serial Number \""+serial+"\" at the row "+(r+1)+".");	
	        }

                // create a camera
                Camera camera = new Camera();
                camera.setType("Camera");
                camera.setManufacturer(manufacturer);
                camera.setModel(model);
                camera.setSerialNumber(serial);
                camera.setSite(site);
                network.save(camera);
	    }

	}

     }
 
     out.println("{");
     out.println("  success: true");
     out.println("}");

  } catch (Exception ex) {

     ex.printStackTrace();

     out.println("{");
     out.println("  message: '"+ex.getMessage()+"',");
     out.println("  success: false");
     out.println("}");
  } finally {
     if (network != null) network.close();
  }

%>

