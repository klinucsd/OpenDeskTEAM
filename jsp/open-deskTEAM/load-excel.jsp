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
     String arrayId = request.getParameter("arrayId");

     if (path.endsWith("csv")) {
             
         network = NetworkFactory.newInstance();
         Site site = network.getSiteById(new Integer(siteId));	

	 BufferedReader br = new BufferedReader(new FileReader(path));

         // skip the first line
         String line = br.readLine();
         String[] headers = line.split(",");

	 if (headers.length > 0 && !headers[0].toLowerCase().equals("camera trap name")) {
            throw new SQLException("Found no header \"Camera Trap Name\"");
	 }

	 if (headers.length > 1 && !headers[1].toLowerCase().equals("latitude")) {
            throw new SQLException("Found no header \"Latitude\"");
	 }

	 if (headers.length > 2 && !headers[2].toLowerCase().equals("longitude")) {
            throw new SQLException("Found no header \"Longitude\"");
	 }

	 if (headers.length > 3 && !headers[3].toLowerCase().equals("array")) {
            throw new SQLException("Found no header \"Array\"");
	 }

	 boolean hasArrayColumn = false;
	 if (headers.length > 3 && headers[3].toLowerCase().equals("array")) {
             hasArrayColumn = true;	    
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

	     String cameraTrapName = null;
	     String latitude = null;
 	     String longitude = null;
	     String givenArray = null;

	     System.out.println("------------------");
	     for(int i=0; i<cells.length; i++) { 
	         System.out.println(headers[i]+" : "+cells[i]);

		 if (i == 0) {
		      cameraTrapName = cells[i];
		 } else if (i == 1) {
		      latitude = cells[i];
		 } else if (i == 2) {
		      longitude = cells[i]; 
		 } else if (i == 3) {
		      givenArray = cells[i];
		 }
	     }

	     if (cameraTrapName == null) {
		   throw new SQLException("Empty camera trap name at the row "+(r+1));
	     } else {
	           // parse the camera trap name
		   String[] elements = cameraTrapName.split("-");
		   //for (int k=0; k<elements.length; k++) {
		   //    System.out.println("----> "+elements[k]);
		   //}
		   
		   if (elements.length != 4) {
		   
		       if ((arrayId != null && !arrayId.equals("null") && !arrayId.equals("undefined")) || givenArray != null) {
                           ProtocolFamily pf = network.getProtocolFamilyByName("Terrestrial Vertebrate"); 
		           Block array;
			   if (givenArray != null) {
			      array = network.getBlockByName(givenArray);
			      if (array == null) {
			         throw new SQLException("Array \\'"+givenArray+"\\' not found at the row "+(r+1));
			      }
			   } else {
			      array = network.getBlockById(Integer.parseInt(arrayId));	
			   }

		           SamplingUnit trap = network.getSamplingUnitByName(cameraTrapName);
		      	   if (trap == null || trap.getBlock().getIndex() != array.getIndex() ) {
      		              trap = new CameraTrap();
                              Integer minTrapId = network.getMinTrapId();	
      			      trap.setId(minTrapId);
      			      trap.setName(cameraTrapName);
      			      trap.setSite(site);
      			      trap.setBlock(array); 
      			      trap.setProtocolFamily(pf);
			   
      			      if (latitude != null && !latitude.equals("")) {
          		          trap.setLatitude(Double.parseDouble(latitude));
      			      }	
 
			      if (longitude != null && !longitude.equals("")) {
          		          trap.setLongitude(Double.parseDouble(longitude));
      			      }	 

      			      network.save(trap);

		           } else {

      			      if (latitude != null && !latitude.equals("")) {
          		          trap.setLatitude(Double.parseDouble(latitude));
      			      }	
 
			      if (longitude != null && !longitude.equals("")) {
          		          trap.setLongitude(Double.parseDouble(longitude));
      			      }	 

                              network.update(trap);  

			      //throw new SQLException("Duplicate camera trap name \""+cameraTrapName+"\" at the row "+(r+1));	
		           }

		       } else {
		           throw new SQLException("No array specied for the camera trap name at the row "+(r+1));
		       }
		   } else if (!elements[0].equals("CT")) {
		       throw new SQLException("Camera trap name does not state with 'CT' at the row "+(r+1));       
		   } else if (!site.getAbbv().equals(elements[1])) { 		       		       
		       // check project abbreviation
		       throw new SQLException("Wrong project abbreviation \""+elements[1]+"\" at the row "+(r+1));       
		   } else {
		       // check array
		       ProtocolFamily pf = network.getProtocolFamilyByName("Terrestrial Vertebrate"); 
		       String arrayName = "CT-"+elements[1]+"-"+elements[2];
		       if (givenArray != null && !arrayName.equals(givenArray)) {
		           throw new SQLException("Conflict camera trap array name \""+arrayName+"\" and \""+givenArray+"\" at the row "+(r+1));
		       }
 
		       Block array = network.getBlockByName(arrayName);
		       if (array == null) {
		          //throw new SQLException("Wrong camera trap array \""+elements[2]+"\" at the row "+(r+1));  
			  //System.out.println("-----> create an array "+"CT-"+site.getAbbv()+"-"+elements[2]);

                          array = new Block();
                          array.setName("CT-"+site.getAbbv()+"-"+elements[2]);
                          array.setType("ARY");
                          array.setIndex(Integer.parseInt(elements[2]));
                          array.setSite(site);
			  network.saveObject(array);
		       }

		       // check camera trap
		       String trapName = "CT-"+elements[1]+"-"+elements[2]+"-"+elements[3];
		       SamplingUnit trap = network.getSamplingUnitByName(trapName);
		       if (trap == null) {
      		           trap = new CameraTrap();
                           Integer minTrapId = network.getMinTrapId();	
      			   trap.setId(minTrapId);
      			   trap.setName("CT-"+site.getAbbv()+"-"+elements[2]+"-"+elements[3]);
      			   trap.setSite(site);
      			   trap.setBlock(array);
      			   trap.setProtocolFamily(pf);
			   
      			   if (latitude != null && !latitude.equals("")) {
          		       trap.setLatitude(Double.parseDouble(latitude));
      			   }	
 
			   if (longitude != null && !longitude.equals("")) {
          		       trap.setLongitude(Double.parseDouble(longitude));
      			   }	 

      			   network.save(trap);

		       } else {

      			   if (latitude != null && !latitude.equals("")) {
          		       trap.setLatitude(Double.parseDouble(latitude));
      			   }	
 
			   if (longitude != null && !longitude.equals("")) {
          		       trap.setLongitude(Double.parseDouble(longitude));
      			   }	 

                           network.update(trap);  
		       }

		   }
	       }


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
     //network.close();

     Workbook wb = Workbook.getWorkbook(excelFile);
     Sheet[] sheets = wb.getSheets();
     for (Sheet sheet : sheets) {
        int rowNumber = sheet.getRows();
        int columnNumber = sheet.getColumns();
	//System.out.println("row = "+rowNumber);
	//System.out.println("column = "+columnNumber);
 
        for (int r=0; r<rowNumber; r++) {
	    String cameraTrapName = null;
	    String latitude = null;
 	    String longitude = null;
	    String givenArray = null;	
	    for (int c=0; c<columnNumber; c++) {
	     	Cell cell = sheet.getCell(c, r);
		String aStr = cell.getContents().trim();
		//System.out.println(r+", "+c+" = "+aStr);

		if (r == 0 && c == 0 && !aStr.toLowerCase().equals("camera trap name")) {
		   throw new SQLException("Found no header \"Camera Trap Name\"");
		}

		if (r == 0 && c == 1 && !aStr.toLowerCase().equals("latitude")) {
		   throw new SQLException("Found no header \"Latitude\"");
		}

		if (r == 0 && c == 2 && !aStr.toLowerCase().equals("longitude")) {
		   throw new SQLException("Found no header \"Longitude\"");
		}

	        boolean hasArrayColumn = false;
		if (r == 0 && c == 3 && !aStr.toLowerCase().equals("array")) {
                     hasArrayColumn = true;	    
	        }

		if (r > 0) {
		    if (c == 0) {
		        cameraTrapName = aStr;
		    } else if (c == 1) {
		        latitude = aStr;
		    } else if (c == 2) {
		        longitude = aStr; 
		    } else if (c == 3) {
		        givenArray = aStr;
		    }
		}
	    }

	    if (r > 0) {

	      if (cameraTrapName == null) {
		   throw new SQLException("Empty camera trap name at the row "+(r+1));
	      } else {
                 String[] elements = cameraTrapName.split("-");
                 if (elements.length != 4) {  
		        if ((arrayId != null && !arrayId.equals("null") && !arrayId.equals("undefined"))  || givenArray != null ) {
                           ProtocolFamily pf = network.getProtocolFamilyByName("Terrestrial Vertebrate");
		           Block array;
			   if (givenArray != null) {
			      array = network.getBlockByName(givenArray);
			      if (array == null) {
			         throw new SQLException("Array \\'"+givenArray+"\\' not found at the row "+(r+1));
			      }
			   } else {
			      array = network.getBlockById(Integer.parseInt(arrayId));	
			   } 
		           //Block array = network.getBlockById(Integer.parseInt(arrayId));
		           SamplingUnit trap = network.getSamplingUnitByName(cameraTrapName);
		      	   if (trap == null || trap.getBlock().getIndex() != array.getIndex()) {
      		              trap = new CameraTrap();
                              Integer minTrapId = network.getMinTrapId();	
      			      trap.setId(minTrapId);
      			      trap.setName(cameraTrapName);
      			      trap.setSite(site);
      			      trap.setBlock(array); 
      			      trap.setProtocolFamily(pf);
			   
      			      if (latitude != null && !latitude.equals("")) {
          		          trap.setLatitude(Double.parseDouble(latitude));
      			      }	
 
			      if (longitude != null && !longitude.equals("")) {
          		          trap.setLongitude(Double.parseDouble(longitude));
      			      }	 

      			      network.save(trap);

		           } else {

      			      if (latitude != null && !latitude.equals("")) {
          		          trap.setLatitude(Double.parseDouble(latitude));
      			      }	
 
			      if (longitude != null && !longitude.equals("")) {
          		          trap.setLongitude(Double.parseDouble(longitude));
      			      }	 

                              network.update(trap);  
			      //throw new SQLException("Duplicate camera trap name \""+cameraTrapName+"\" at the row "+(r+1));	
		           }
		       } else {
		           throw new SQLException("Wrong camera trap name at the row "+(r+1));
		       }
   
	       } else {
	           // parse the camera trap name
		   //String[] elements = cameraTrapName.split("-");
		   //for (int k=0; k<elements.length; k++) {
		   //    System.out.println("----> "+elements[k]);
		   //}
		   
		   if (elements.length != 4) {
		       throw new SQLException("Wrong camera trap name at the row "+(r+1));
		   } else if (!elements[0].equals("CT")) {
		       throw new SQLException("Camera trap name does not state with 'CT' at the row "+(r+1));       
		   } else if (!site.getAbbv().equals(elements[1])) { 		       		       
		       // check project abbreviation
		       throw new SQLException("Wrong project abbreviation \""+elements[1]+"\" at the row "+(r+1));       
		   } else {
		       // check array
		       ProtocolFamily pf = network.getProtocolFamilyByName("Terrestrial Vertebrate"); 

		       String arrayName = "CT-"+elements[1]+"-"+elements[2];
		       if (givenArray != null && !arrayName.equals(givenArray)) {
		           throw new SQLException("Conflict camera trap array name \""+arrayName+"\" and \""+givenArray+"\" at the row "+(r+1));
		       }

		       Block array = network.getBlockByName(arrayName);
		       if (array == null) {
		          //throw new SQLException("Wrong camera trap array \""+elements[2]+"\" at the row "+(r+1));  
			  //System.out.println("-----> create an array "+"CT-"+site.getAbbv()+"-"+elements[2]);

                          array = new Block();
                          array.setName("CT-"+site.getAbbv()+"-"+elements[2]);
                          array.setType("ARY");
                          array.setIndex(Integer.parseInt(elements[2]));
                          array.setSite(site);
			  network.saveObject(array);
		       }

		       // check camera trap
		       String trapName = "CT-"+elements[1]+"-"+elements[2]+"-"+elements[3];
		       SamplingUnit trap = network.getSamplingUnitByName(trapName);
		       if (trap == null) {
      		           trap = new CameraTrap();
                           Integer minTrapId = network.getMinTrapId();	
      			   trap.setId(minTrapId);
      			   trap.setName("CT-"+site.getAbbv()+"-"+elements[2]+"-"+elements[3]);
      			   trap.setSite(site);
      			   trap.setBlock(array);
      			   trap.setProtocolFamily(pf);
			   
      			   if (latitude != null && !latitude.equals("")) {
          		       trap.setLatitude(Double.parseDouble(latitude));
      			   }	
 
			   if (longitude != null && !longitude.equals("")) {
          		       trap.setLongitude(Double.parseDouble(longitude));
      			   }	 

      			   network.save(trap);

		       } else {

      			   if (latitude != null && !latitude.equals("")) {
          		       trap.setLatitude(Double.parseDouble(latitude));
      			   }	
 
			   if (longitude != null && !longitude.equals("")) {
          		       trap.setLongitude(Double.parseDouble(longitude));
      			   }	 

                           network.update(trap);  
		       }  // else for existing camera trap
		   } // else for regular name 4
	       } // else for not null camera trap name
	     }
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

