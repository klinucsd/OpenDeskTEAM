<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.crypto.*" %>
<%@ page import="javax.xml.bind.*" %> 
<%@ page import="javax.xml.transform.stream.StreamSource" %>

<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="ch.enterag.utils.zip.*" %>

<%
     String path = request.getParameter("path");
     String event = request.getParameter("event");
     String arrayName = request.getParameter("array");
     String valid = request.getParameter("valid");
     String siteId = request.getParameter("siteId");

     String prefix = "";
     if (valid != null && valid.equals("false")) {
         prefix = "incomplete-";
     }

     // get the directory for output
     File exportDir = new File(path);
     //System.out.println("exportDir="+path);

     // get the directory for the image repository
     String root = pageContext.getServletContext().getRealPath("/");
     File rootDir = new File(root, "image-repository");
     
     // get the temporary directory 
     File tmpDir = new File(root+"/WEB-INF/tmp/");
     if (!tmpDir.exists()) {
        tmpDir.mkdirs();
     }
     
     // get the directory of the key
     File keyFile = new File(root+"/WEB-INF/key.txt");
     System.out.println("Fetch the key file for exporting data: "+keyFile.getAbsolutePath());

     try {

        Network network = NetworkFactory.newInstance();
        //Site site = network.getSites().get(0);
        Site site = network.getSiteById(new Integer(siteId));	

	Protocol tv = network.getProtocol(4);

        if (arrayName != null && !arrayName.equals("")) {

	    arrayName = arrayName.substring(5);

            String zipPath = rootDir.getAbsolutePath()+"/"+site.getName()+"/"+event+"/Array"+arrayName;
	    String topName = "CT-"+site.getAbbv()+"-"+event+"-Array"+arrayName;

            File exportFile = new File(tmpDir, topName+".zip");
	    if (exportFile.exists()) {
	       exportFile.delete();
	    }
	    Zip64File zip64 = new Zip64File(exportFile, false);

	    System.out.println("Export to the directory: "+exportFile.getAbsolutePath());
	    System.out.println("Working relative Root: "+root);
	    System.out.println("Working zip file: "+zipPath);

            List<CameraTrapPhotoCollection> collections = network.getCameraTrapPhotoCollections(site, event, arrayName);	    
	    System.out.println("Collections size: "+collections.size());

            JAXBContext jc = JAXBContext.newInstance( CameraTrapPhotoCollection.class);
            Marshaller m = jc.createMarshaller();

	    List<String> misfired = new ArrayList<String>(); 

	    // create xml files
            for (CameraTrapPhotoCollection collection : collections) {

	        String cameraTrapName = collection.getCameraTrap().getName();

		// check misfired photos
		//System.out.println("----> check misfired photos here");
		for (CameraTrapPhoto photo : collection.getPhotos()) {
		    if (photo.getType() != null && photo.getType().getName().equals("Misfired")) {
		        misfired.add(rootDir.getAbsolutePath()+File.separator+site.getName()+File.separator+event+File.separator+"Array"+arrayName+File.separator+cameraTrapName+File.separator+photo.getRawName());				   
		    }
		}

	        // create xml file for this data collection of a camera trap
	        File xmlFile = new File(root+"/WEB-INF/tmp/"+collection.getCameraTrap().getName()+".xml");
	        FileOutputStream fos = new FileOutputStream(xmlFile);
                m.marshal(collection, fos);
	        fos.close();

		try {
		    Util.addFile(zip64, 
			         xmlFile, 
		                 topName+"/data/"+site.getName()+"/"+event+"/Array"+arrayName+"/"+collection.getCameraTrap().getName()+".xml");
		} catch (Exception exc) {
		     Thread.sleep(10);	
		     Util.addFile(zip64, 
			          xmlFile, 
	                	  topName+"/data/"+site.getName()+"/"+event+"/Array"+arrayName+"/"+collection.getCameraTrap().getName()+
				  "-opt-"+((new java.util.Date()).getTime())+".xml"); 
		}
		xmlFile.delete();
		
            }

            Util.zip64Dir(zipPath, root, topName, zip64, misfired);	

	    //  create manifest file
	    File manifest = new File(root+"/WEB-INF/tmp/manifest.properties");
	    PrintWriter bw = new PrintWriter(new FileWriter(manifest));
	    bw.println("export.site="+site.getName());
	    bw.println("export.event="+event);
            bw.println("export.array="+arrayName);	
            bw.println("export.valid="+valid);	
	    bw.close();

	    Util.addFile(zip64, 
			 manifest, 
			 topName+"/manifest.properties"); 	    
	    manifest.delete();

	    // close zip stream
	    zip64.close();	

	    /*
            DesEncrypter encrypter;
	    if (keyFile.exists()) {
	        // read the key file
	        DataInputStream dis = new DataInputStream(new FileInputStream(keyFile));
                byte[] rawKey = new byte[(int)keyFile.length()];
		dis.readFully(rawKey);
                dis.close();

		//make a key object
                javax.crypto.spec.SecretKeySpec key = new javax.crypto.spec.SecretKeySpec(rawKey, "DES");
	        encrypter = new DesEncrypter(key);
	    } else {
	        // encrypt the package
	        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
	        encrypter = new DesEncrypter(key);

		byte[] rawKey = key.getEncoded();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(keyFile));
            	dos.write(rawKey, 0, rawKey.length);
            	dos.close();
	    }

	    File encryptFile = new File(exportDir, prefix+topName+"-64.tpk");
	    encrypter.encrypt(new FileInputStream(exportFile), new FileOutputStream(encryptFile));
	    zip64.close();
	    exportFile.delete();
	    */	   

	    File encryptFile = new File(exportDir, prefix+topName+"-64-s.tpk");
	    exportFile.renameTo(encryptFile);

        } else {

            String zipPath = rootDir.getAbsolutePath()+"/"+site.getName()+"/"+event;
	    String topName = "CT-"+site.getAbbv()+"-"+event;

	    //File exportFile = new File(exportDir, topName+".zip");
            File exportFile = new File(tmpDir, topName+".zip");
	    if (exportFile.exists()) {
	       exportFile.delete();
	    }
	    Zip64File zip64 = new Zip64File(exportFile.getAbsolutePath());

	    System.out.println("Export to the directory: "+exportFile.getAbsolutePath());
	    System.out.println("Exporting root: "+root);
	    System.out.println("Exporting zip: "+zipPath);

            //Util.zipDir(zipPath, root, topName, zos); 

            List<CameraTrapPhotoCollection> collections = network.getCameraTrapPhotoCollections(site, event);	    
	    System.out.println("Exporting collections size: "+collections.size());

            JAXBContext jc = JAXBContext.newInstance( CameraTrapPhotoCollection.class);
            Marshaller m = jc.createMarshaller();

            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;

	    List<String> misfired = new ArrayList<String>(); 

            for (CameraTrapPhotoCollection collection : collections) {

	        File xmlFile = new File(root+"/WEB-INF/tmp/"+collection.getCameraTrap().getName()+".xml");

	        FileOutputStream fos = new FileOutputStream(xmlFile);
                m.marshal(collection, fos);
	        fos.close();

		String trapName = collection.getCameraTrap().getName();
		StringTokenizer st = new StringTokenizer(trapName, "-");
		st.nextToken();
		st.nextToken();
		arrayName = st.nextToken();

		// check misfired photos
		//System.out.println("----> check misfired photos here");
		for (CameraTrapPhoto photo : collection.getPhotos()) {
		    if (photo.getType() != null && photo.getType().getName().equals("Misfired")) {
		        misfired.add(rootDir.getAbsolutePath()+File.separator+site.getName()+File.separator+event+File.separator+"Array"+arrayName+File.separator+trapName+File.separator+photo.getRawName());				   
		    }
		}

		try {
		    Util.addFile(zip64, 
			         xmlFile, 
			         topName+"/data/"+site.getName()+"/"+event+"/Array"+arrayName+"/"+collection.getCameraTrap().getName()+".xml"); 
		} catch (Exception exc) {
		     Thread.sleep(10);	
		     Util.addFile(zip64, 
			          xmlFile, 
	                	  topName+"/data/"+site.getName()+"/"+event+"/Array"+arrayName+"/"+collection.getCameraTrap().getName()+
				  "-opt-"+((new java.util.Date()).getTime())+".xml"); 
		}
		
		xmlFile.delete();
            }

            Util.zip64Dir(zipPath, root, topName, zip64, misfired);	

	    //  create manifest file
	    File manifest = new File(root+"/WEB-INF/tmp/manifest.properties");
	    PrintWriter bw = new PrintWriter(new FileWriter(manifest));
	    bw.println("export.site="+site.getName());
	    bw.println("export.event="+event);
            bw.println("export.valid="+valid);	
	    bw.close();

	    Util.addFile(zip64, 
			 manifest, 
			 topName+"/manifest.properties"); 	    

	    manifest.delete();

	    zip64.close();	

	    /*
            DesEncrypter encrypter;
	    if (keyFile.exists()) {
	        // read the key file
	        DataInputStream dis = new DataInputStream(new FileInputStream(keyFile));
                byte[] rawKey = new byte[(int)keyFile.length()];
		dis.readFully(rawKey);
                dis.close();

		//make a key object
                javax.crypto.spec.SecretKeySpec key = new javax.crypto.spec.SecretKeySpec(rawKey, "DES");
	        encrypter = new DesEncrypter(key);
	    } else {
	        // encrypt the package
	        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
	        encrypter = new DesEncrypter(key);

		byte[] rawKey = key.getEncoded();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(keyFile));
            	dos.write(rawKey, 0, rawKey.length);
            	dos.close();
	    }

	    File encryptFile = new File(exportDir, prefix+topName+"-64.tpk");
	    encrypter.encrypt(new FileInputStream(exportFile), new FileOutputStream(encryptFile));
	    zip64.close();
	    exportFile.delete();
	    */

	    File encryptFile = new File(exportDir, prefix+topName+"-64-s.tpk");
	    exportFile.renameTo(encryptFile);

	}
 
        network.close();

	out.println("{ success : true } ");

    } catch (Exception ex) {
        ex.printStackTrace();
	out.println("{ success : false, message: '"+ex.getMessage()+"' } ");
    }
%>