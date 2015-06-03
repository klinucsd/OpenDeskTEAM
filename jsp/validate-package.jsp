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

     List<String> collections = new ArrayList<String>();
   
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
     if (!extDir.exists()) extDir.mkdirs();    

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
        properties.load(new FileInputStream(manifest));
     } catch (IOException e) {
        e.printStackTrace();
     }

     String siteName = properties.getProperty("export.site");
     String event = properties.getProperty("export.event");
     String array = properties.getProperty("export.array");

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
	     File tmpXmlFile = new File(arrayDataDir, trapNames[j]+"-filter.xml");
	     
	     BufferedReader br = new BufferedReader(new FileReader(xmlFile));
	     PrintWriter pw = new PrintWriter(new FileWriter(tmpXmlFile));
	     String line = br.readLine();	
	     while (line != null) {		 
		 int index1 = line.indexOf("<PhotoSpeciesRecord>");
		 int index2 = line.indexOf("</PhotoSpeciesRecord>");	
		 while (index1 != -1) {
		    int pos1 = line.indexOf("<id>", index1);
		    int pos2 = line.indexOf("</id>", index1);		    
		    if (pos1 != -1) {
		        String before = line.substring(0, pos1);
			String after = line.substring(pos2+5);
			line = before+after;
			index1 = line.indexOf("<PhotoSpeciesRecord>", index2+20);
			index2 = line.indexOf("</PhotoSpeciesRecord>", index2+20);
		    }
		 } 
	
		 pw.println(line);
                 line = br.readLine();

	     }
	     br.close();
	     pw.close();
     
	     // unmarshal xml file
             JAXBElement<CameraTrapPhotoCollection> df = null;
             JAXBContext context = JAXBContext.newInstance(CameraTrapPhotoCollection.class);
             Unmarshaller unmarshaller = context.createUnmarshaller();
             df = unmarshaller.unmarshal(new StreamSource(new FileReader(tmpXmlFile)),  CameraTrapPhotoCollection.class);

	     CameraTrapPhotoCollection collection = df.getValue();
	     collections.add(collection.getCameraTrap().getName());

	     //System.out.println("-----> collection camera trap = "+collection.getCameraTrap().getName());
	     
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

     out.println("  cameratrap: "+collections.size()+",");
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


