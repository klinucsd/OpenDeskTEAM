<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
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

<%
 try {

     String path = request.getParameter("path");
     String siteId = request.getParameter("siteId");

     // get the directory for output
     File tpkFile = new File(path);

     boolean isZip64 = true;
     boolean simpleEncrypted = false;
     if (!tpkFile.getName().endsWith("-64.tpk") && !tpkFile.getName().endsWith("-64-s.tpk")) {
        isZip64 = false;
     }

     if (tpkFile.getName().endsWith("-64-s.tpk")) {
        simpleEncrypted = true;
     }

     // get the root of deskTEAM
     String root = pageContext.getServletContext().getRealPath("/");

     // get the directory for the image repository
     File imgDir = new File(root, "image-repository");
     
     // get the temporary directory 
     File tmpDir = new File(root+"/WEB-INF/tmp/");
     if (!tmpDir.exists()) {
        tmpDir.mkdirs();
     }
     
     // get the directory of the key
     File keyFile = new File(root+"/WEB-INF/key.txt");

     // read the key file            
     DataInputStream dis = new DataInputStream(new FileInputStream(keyFile));
     byte[] rawKey = new byte[(int)keyFile.length()];
     dis.readFully(rawKey);
     dis.close();

     //make a key object
     javax.crypto.spec.SecretKeySpec key1 = new javax.crypto.spec.SecretKeySpec(rawKey, "DES");

     // decrpte the package
     System.out.println("---------------------------");
     System.out.println("Decrypting the package ......");
     DesEncrypter decrypter = new DesEncrypter(key1);
     File finalFile = new File(tmpDir, tpkFile.getName()+".zip");
     if (simpleEncrypted) {
	 InputStream fin = new FileInputStream(tpkFile);
	 OutputStream fout = new FileOutputStream(finalFile);
	 byte[] fbuf = new byte[1024];
	 int flen;
	 while ((flen = fin.read(fbuf)) > 0) {
   	       fout.write(fbuf, 0, flen);
	 }
	 fin.close();
	 fout.close(); 
     } else {
         decrypter.decrypt(new FileInputStream(tpkFile), new FileOutputStream(finalFile));
     }

     // extract the package
     File extDir = new File(tmpDir, (new Date().getTime())+"");
     //File extDir = new File(tmpDir, "1308800431443");	
     //Util.unzip(finalFile, extDir);
     if (!extDir.exists()) extDir.mkdirs(); 
    
     System.out.println("---------------------------");
     System.out.println("Unziping the package ......");
     if (isZip64) {
         Util.unzip64(finalFile.getAbsolutePath(), extDir.getAbsolutePath());	
     } else {
         Util.unzip(finalFile, extDir);
     }

     // get the top directory
     File topDir = new File(extDir, extDir.list()[0]);

     // load the manifest file
     File manifest = new File(topDir, "manifest.properties");
     Properties properties = new Properties();
     try {
        FileInputStream tmpFis = new FileInputStream(manifest);
        properties.load(tmpFis);
	tmpFis.close();
     } catch (IOException e) {
        e.printStackTrace();
     }

     String siteName = properties.getProperty("export.site");
     String event = properties.getProperty("export.event");
     String array = properties.getProperty("export.array");

     Network network = NetworkFactory.newInstance();
     Site site = network.getSiteById(new Integer(siteId));	
     //Site site = network.getSites().get(0);
     network.close();

     if (!site.getName().trim().equals(siteName.trim())) {
         // delete zip file
	 // delete zipped files
         finalFile.delete();
         Util.deleteDir(extDir);         
         throw new java.sql.SQLException("The package is for the project: \""+siteName+"\"");
     }

     File dataDir = new File(topDir, "data");
     File siteDataDir = new File(dataDir, (dataDir.list())[0]);
     File eventDataDir = new File(siteDataDir, event);

     File imgRepDir = new File(topDir, "image-repository");
     File siteImgDir = new File(imgRepDir, (imgRepDir.list())[0]);
     File eventImgDir = new File(siteImgDir, event);

     // go through each camera trap
     //Network network = NetworkFactory.newInstance();
     Configuration mConfigure = new AnnotationConfiguration();
     mConfigure.configure();
     SessionFactory mFactory = mConfigure.buildSessionFactory();
     Session mSession = mFactory.openSession();

     String[] arrayNames = eventImgDir.list();
     for (int i=0; i<arrayNames.length; i++) {

         File arrayImgDir = new File(eventImgDir, arrayNames[i]);
         File arrayDataDir = new File(eventDataDir, arrayNames[i]);

         String[] trapNames = arrayImgDir.list();
	 for (int j=0; j<trapNames.length; j++) {

             File xmlFile = new File(arrayDataDir, trapNames[j]+".xml");
     
	     // unmarshal xml file
             JAXBElement<CameraTrapPhotoCollection> df = null;
             JAXBContext context = JAXBContext.newInstance(CameraTrapPhotoCollection.class);
             Unmarshaller unmarshaller = context.createUnmarshaller();
             df = unmarshaller.unmarshal(new StreamSource(new FileReader(xmlFile)),  CameraTrapPhotoCollection.class);

	     CameraTrapPhotoCollection collection = df.getValue();
	     System.out.println("Saving the data for the camera trap: "+collection.getCameraTrap().getName());
	     
	     // check whether the camera trap names are same or not
             Query query = mSession.createQuery("FROM SamplingUnit as trap "+
	     	   	                        "WHERE trap.name='"+collection.getCameraTrap().getName()+"' "+
						"  AND trap.site.id="+site.getId());
	     List<SamplingUnit> trapList = query.list();
	     if (trapList.isEmpty()) {

	         Block block = collection.getCameraTrap().getBlock();
		 
		 // check whether the local db has a block with the same name
                 query = mSession.createQuery("FROM Block as array "+
	     	   	                      "WHERE array.name='"+block.getName()+"' "+
				              "  AND array.site.id="+site.getId());		 						
		 List<Block> blockList = query.list();
	         if (blockList.isEmpty()) {	
		     // create this block
		     block.setId(null);
		     block.setSite(site);
		     mSession.save(block);
		 } else {
		     block = blockList.get(0);
		 }			

		 // found no camera trap with the same name; then create one
		 SamplingUnit trap = collection.getCameraTrap();
		 trap.setSite(site);
                 trap.setBlock(block);

                 query = mSession.createQuery("SELECT min(unit.id)-1 FROM SamplingUnit as unit");
                 Object object = query.list().get(0);
                 Integer minTrapId = new Integer(-1);	
                 if (object != null) {
                    minTrapId = (Integer)object;
                 }
                 trap.setId(minTrapId);
		 mSession.save(trap);

		 collection.setCameraTrap((CameraTrap)trap);
	     } else {
	         collection.setCameraTrap((CameraTrap)(trapList.get(0)));
	     } 				

	     // check person
	     Person setPerson = collection.getSetPerson();
	     setPerson.setId(null);

	     Person pickPerson = collection.getPickPerson();	     
	     pickPerson.setId(null);

	     query = mSession.createQuery("FROM Person as person WHERE person.email='"+setPerson.getEmail()+"'");
	     List<Person> persons = query.list();
	     if (persons.isEmpty()) {
	        // save person
                Integer minPersonId = new Integer(-1);	
                query = mSession.createQuery("SELECT min(person.id)-1 FROM Person as person");
                Object object = query.list().get(0);
                if (object != null) {
                   minPersonId = (Integer)object;
                }

                setPerson.setId(minPersonId);
                Set<Site> sites = new HashSet<Site>();
                sites.add(site);
                setPerson.setSites(sites);
		mSession.saveOrUpdate(setPerson);
		mSession.flush();

		collection.setSetPerson(setPerson);
	     } else {
	        setPerson = persons.get(0);
		collection.setSetPerson(setPerson);
	     }

	     if (!pickPerson.getEmail().equals(setPerson.getEmail())) {
	         query = mSession.createQuery("FROM Person as person WHERE person.email='"+pickPerson.getEmail()+"'");
	         persons = query.list();
	         if (persons.isEmpty()) {
	             // save person
                     Integer minPersonId = new Integer(-1);	
                     query = mSession.createQuery("SELECT min(person.id)-1 FROM Person as person");
                     Object object = query.list().get(0);
                     if (object != null) {
                         minPersonId = (Integer)object;
                     }

                     pickPerson.setId(minPersonId);
                     Set<Site> sites = new HashSet<Site>();
                     sites.add(site);
                     pickPerson.setSites(sites);
		     mSession.saveOrUpdate(pickPerson);
		     mSession.flush();

		     collection.setPickPerson(pickPerson);
	          } else {
	             pickPerson = persons.get(0);	
		     collection.setPickPerson(pickPerson);
	          }
	     } else {
	         pickPerson = setPerson;
	         collection.setPickPerson(pickPerson);
	     }
	     	   
	     // handle event
	     String eventName = collection.getEvent();
	     query = mSession.createQuery("FROM SamplingEvent as event WHERE event.event='"+eventName+"'");
	     List<SamplingEvent> events = query.list();
	     if (events.isEmpty()) {
                 SamplingEvent se = new SamplingEvent();
                 se.setEvent(eventName);
                 se.setSite(site);
		 mSession.saveOrUpdate(se);
		 mSession.flush();
	     }

	     // handle camera 
		   
  
	     // check whether there is a collection for the same event and camera trap 
	     query = mSession.createQuery("FROM CameraTrapPhotoCollection as collection "+
  	   	   			  "WHERE collection.event='"+collection.getEvent()+"' AND "+
                                          "collection.cameraTrap.name='"+collection.getCameraTrap().getName()+"'");
             List tmpList = query.list();
	     if (tmpList != null && !tmpList.isEmpty()) {
	         System.out.println("The collection for the event "+collection.getEvent()+" and the camera trap "+collection.getCameraTrap().getName()+" exists. The system skipped this collection.");
		 continue;
	     }

	     // save the collection
	     Set<CameraTrapPhoto> photos = collection.getPhotos();
	     if (photos != null) {
	         collection.setPhotos(null);
		 mSession.save(collection); 
                 //mSession.flush();

	         // save photos

		 Map<CameraTrapPhoto, Set<PhotoSpeciesRecord>> map = new HashMap <CameraTrapPhoto, Set<PhotoSpeciesRecord>>();
		 for (CameraTrapPhoto photo : photos) {

		     Person idPerson = photo.getTypeIdentifiedBy();
		     if (idPerson != null) {
		         idPerson.setId(null);

	                 query = mSession.createQuery("FROM Person as person WHERE person.email='"+idPerson.getEmail()+"'");
	                 persons = query.list();
	                 if (persons.isEmpty()) {
	                     // save person
                             Integer minPersonId = new Integer(-1);	
                             query = mSession.createQuery("SELECT min(person.id)-1 FROM Person as person");
                             Object object = query.list().get(0);
                             if (object != null) {
                                 minPersonId = (Integer)object;
                             }

                             idPerson.setId(minPersonId);
                             Set<Site> sites = new HashSet<Site>();
                             sites.add(site);
                             idPerson.setSites(sites);
		             mSession.saveOrUpdate(idPerson);
		             mSession.flush();

			     photo.setTypeIdentifiedBy(idPerson);
	                 } else {
                             idPerson = persons.get(0);
		             photo.setTypeIdentifiedBy(idPerson);
		         } 
		     }

		     photo.setCollection(collection);
		     
		     Set<PhotoSpeciesRecord> records = photo.getRecords();
		     if (records != null) {
		     	map.put(photo, records);
		     }

		     photo.setRecords(null);
		     mSession.save(photo);  
		 }

		 // save metadata
		 for (CameraTrapPhoto photo : photos) {
                     CameraTrapPhotoMetadata metadata = photo.getMetadata();
		     if (metadata != null) {
		         metadata.setPhoto(photo);
		         mSession.save(metadata);
		     }		 
	         }

		 // save species
		 for (CameraTrapPhoto photo : map.keySet()) {
		      Set<PhotoSpeciesRecord> records = map.get(photo);
		      if (records != null) {
		         for (PhotoSpeciesRecord record : records) {

		             Person idPerson = record.getIdentifiedBy();
		             if (idPerson != null) {
		                 idPerson.setId(null);

	                         query = mSession.createQuery("FROM Person as person WHERE person.email='"+idPerson.getEmail()+"'");
	                         persons = query.list();
	                         if (persons.isEmpty()) {
	                            // save person
                                    Integer minPersonId = new Integer(-1);	
                                    query = mSession.createQuery("SELECT min(person.id)-1 FROM Person as person");
                                    Object object = query.list().get(0);
                                    if (object != null) {
                                        minPersonId = (Integer)object;
                                    }

                                    idPerson.setId(minPersonId);
                                    Set<Site> sites = new HashSet<Site>();
                                    sites.add(site);
                                    idPerson.setSites(sites);
		                    mSession.saveOrUpdate(idPerson);
		                    mSession.flush();

			            record.setIdentifiedBy(idPerson);
	                         } else {
                                    idPerson = persons.get(0);
		                    record.setIdentifiedBy(idPerson);
		                 } 
		             }

		             record.setCameraTrapPhoto(photo);
		             mSession.save(record);
		         }
		      }
		 } 		                  
	
	     }

	     if (photos != null) {

	        // copy images
	        File trapImgDir = new File(arrayImgDir, trapNames[j]);
	        String siteString = site.getName();

		/*
	        String siteString = null;
	        String[] strings = imgDir.list();
	        for (int p=0; p<strings.length; p++) {
	            if (strings[p].startsWith(".")) {
		        continue;
		    } else {
		        siteString = strings[p];
		        break;
		    }
	        }
		*/

	        File destDir = new File(imgDir.getAbsolutePath()+"/"+siteString+"/"+event+"/"+arrayNames[i]+"/"+trapNames[j]);
	        if (!destDir.exists()) {
	            destDir.mkdirs();
		    Util.copyDirectory(trapImgDir, destDir);
	        }

	     } else {
	         mSession.save(collection); 

		 //File destDir = new File(imgDir.getAbsolutePath()+"/"+(imgRepDir.list())[0]+"/"+event+"/"+arrayNames[i]+"/"+trapNames[j]);

                 File destDir = new File(imgDir.getAbsolutePath()+"/"+site.getName()+"/"+event+"/"+arrayNames[i]+"/"+trapNames[j]); 
		 if (!destDir.exists()) {
                     destDir.mkdirs();
                 }

	         DamagedCameraTrap dct = collection.getDamagedCameraTrap();
	         if (dct != null) {
	             dct.setCollection(collection);
		     mSession.save(dct);
	         }

	     }

	 }
     }

     mSession.flush();
     mSession.close();

     finalFile.delete();
     Util.deleteDir(extDir);         

     out.println("{");
     out.println("  event: '"+event+"',");
     if (array != null) {
         out.println("  array: '"+array+"',");	
     }
     out.println("  success: true");
     out.println("}");

  } catch (Exception ex) {

     ex.printStackTrace();

     out.println("{");
     out.println("  message: '"+ex.getMessage()+"',");
     out.println("  success: false");
     out.println("}");
  }

%>

