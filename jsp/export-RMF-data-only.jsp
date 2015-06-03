<%@ page import="java.io.*" %><%@ page import="java.util.*" %><%@ page import="java.util.zip.*" %><%@ page import="java.text.*" %><%@ page import="javax.crypto.*" %><%@ page import="javax.xml.bind.*" %> <%@ page import="javax.xml.transform.stream.StreamSource" %><%@ page import="org.team.sdsc.datamodel.*" %><%@ page import="org.team.sdsc.datamodel.util.*" %>

<%

// get the directory for the image repository
String root = pageContext.getServletContext().getRealPath("/");
     
// get the temporary directory 
File tmpDir = new File(root+"/WEB-INF/tmp/");
if (!tmpDir.exists()) {
    tmpDir.mkdirs();
}
     
try {
    Network network = NetworkFactory.newInstance();
    Site site = network.getSites().get(0);
    Protocol tv = network.getProtocol(4);

    String event = "2010.01";

    String topName = "CT-"+site.getAbbv()+"-"+event;
    File exportFile = new File(tmpDir, topName+".zip");
    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(exportFile));

    String[] arrayNames = new String[] { "1" };
    for (String arrayName : arrayNames) {
	System.out.println("==============================");
	System.out.println("arrayName="+arrayName);
        System.out.println("event="+event);	
        System.out.println("site="+site.getName());	

        List<CameraTrapPhotoCollection> collections = network.getCameraTrapPhotoCollections(site, event);
	System.out.println("==============================");
	System.out.println("Found "+collections.size()+" collection(s) in this array.");

        JAXBContext jc = JAXBContext.newInstance( CameraTrapPhotoCollection.class);
        Marshaller m = jc.createMarshaller();

        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;

	// create xml files
        for (CameraTrapPhotoCollection collection : collections) {

	     System.out.println("------------------------------");
	     System.out.println("Generating an XML file for "+collection.getCameraTrap().getName());
	     File xmlFile = new File(root+"/WEB-INF/tmp/"+collection.getCameraTrap().getName()+"-"+collection.getId()+".xml");
	     FileOutputStream fos = new FileOutputStream(xmlFile);
             m.marshal(collection, fos);
	     fos.close();
	     System.out.println("The XML file was created");

	     FileInputStream fis = new FileInputStream(xmlFile);	
	     ZipEntry anEntry = new ZipEntry(topName+"/"+collection.getCameraTrap().getName()+"-"+collection.getId()+".xml"); 
	     zos.putNextEntry(anEntry); 

	     //now write the content of the file to the ZipOutputStream 
	     while((bytesIn = fis.read(readBuffer)) != -1) { 
		zos.write(readBuffer, 0, bytesIn); 
	     } 
		
	     //close the Stream 
	     fis.close(); 
             xmlFile.delete();

        }

    }

    zos.close();

    response.setContentType("APPLICATION/OCTET-STREAM");
    response.setHeader("Content-Disposition", "attachment; filename=\""+topName+".zip\";");

    FileInputStream fileInputStream = new java.io.FileInputStream(exportFile);
    int i;
    while ((i=fileInputStream.read()) != -1){
        out.write(i);
    }
    fileInputStream.close();

    // remove the download file
    exportFile.delete();


} catch (Exception ex) {
    ex.printStackTrace();
    out.println("Not able to create a data package.");
}
%>