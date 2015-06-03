

package org.team.sdsc.datamodel;

import java.util.*;
import java.io.*;
import java.text.*;
import java.util.zip.*;
import ch.enterag.utils.zip.*;

public class Util {

    static public TreeMap<Protocol, List<Site>> getSiteIndexByProtocol(List<Site> sites) {

	TreeMap<Protocol, List<Site>> protocol2site = new TreeMap<Protocol, List<Site>>(new ProtocolComparator());

	for (Site site : sites) {
	    Set<Protocol> protocols = site.getProtocols();
	    for (Protocol protocol : protocols) {
		List<Site> pSites;
		if (protocol2site.containsKey(protocol)) {
		    pSites = protocol2site.get(protocol);
		} else {
		    pSites = new ArrayList<Site>();
		}
		pSites.add(site);
		protocol2site.put(protocol, pSites);
	    }
	}

	return protocol2site;
    }


    static public Date getGpx10Date(String filename) {
	
	Date date = null;

	try {
	    BufferedReader reader = new BufferedReader(new FileReader(filename));
	    String line = reader.readLine();
	    while (line != null) {
		line = line.trim();
		if (line.startsWith("<time>") && line.endsWith("</time>")) {
		    line = line.substring(6);
		    line = line.substring(0, line.length()-7);
		    line = line.replace("T", " ");
		    line = line.replace("Z", " ");
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    date = formatter.parse(line);
		    break;
		}
		line = reader.readLine();
	    }
	    reader.close();
	} catch (Exception ex) {
	    //ex.printStackTrace();		
	}

	return date;
    }

    static public void zipDir(String dir2zip, String prefix, String topName, ZipOutputStream zos) throws Exception{ 
    
	//create a new File object based on the directory we have to zip File    
	File zipDir = new File(dir2zip); 

	//get a listing of the directory content 
	String[] dirList = zipDir.list(); 
	byte[] readBuffer = new byte[2156]; 
	int bytesIn = 0; 

	if (dirList.length == 0) {
	    ZipEntry anEntry = new ZipEntry(topName+"/"+dir2zip.substring(prefix.length()).replace("\\", "/")); 
	    System.out.println("add dir entry ----> "+topName+"/"+dir2zip.substring(prefix.length()).replace("\\", "/"));
	    zos.putNextEntry(anEntry);
	}


	//loop through dirList, and zip the files 
	for(int i=0; i<dirList.length; i++) { 
	    File f = new File(zipDir, dirList[i]); 
	    if (f.isDirectory()) { 

		//if the File object is a directory, call this 
		//function again to add its content recursively 
		String filePath = f.getPath(); 
		zipDir(filePath, prefix, topName, zos); 

		//loop again 
		continue; 
	    } 
		
	    //if we reached here, the File object f was not a directory 
	    //create a FileInputStream on top of f 
	    FileInputStream fis = new FileInputStream(f); 
		
	    //create a new zip entry 
	    ZipEntry anEntry = new ZipEntry(topName+"/"+f.getPath().substring(prefix.length()).replace("\\", "/")); 
	    //System.out.println("add entry ----> "+topName+"/"+f.getPath().substring(prefix.length()).replace("\\", "/"));

	    //place the zip entry in the ZipOutputStream object 
	    zos.putNextEntry(anEntry); 
            
	    //now write the content of the file to the ZipOutputStream 
	    while((bytesIn = fis.read(readBuffer)) != -1) { 
		zos.write(readBuffer, 0, bytesIn); 
	    } 
		
	    //close the Stream 
	    fis.close(); 
	} 

    }


    public static final void unzip(File zip, File extractTo) throws IOException {

	ZipFile archive = new ZipFile(zip);
	Enumeration e = archive.entries();
	while (e.hasMoreElements()) {
	    ZipEntry entry = (ZipEntry) e.nextElement();
	    //System.out.println("-----> entry = "+entry.getName()+"     "+entry.isDirectory());
	    File file = new File(extractTo, entry.getName());
	    if (entry.isDirectory() && !file.exists()) {
		file.mkdirs();
	    } else if (entry.isDirectory() && file.exists()) {

	    } else {
		if (!file.getParentFile().exists()) {
		    file.getParentFile().mkdirs();
		}

		InputStream in = archive.getInputStream(entry);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

		byte[] buffer = new byte[8192];
		int read;

		while (-1 != (read = in.read(buffer))) {
		    out.write(buffer, 0, read);
		}

		in.close();
		out.close();
	    }
	}
    }



    // If targetLocation does not exist, it will be created.
    static public void copyDirectory(File sourceLocation , File targetLocation)
	throws IOException {
        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
			      new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }



    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }



    static public void zipDir(String dir2zip, String prefix, String topName, ZipOutputStream zos, List<String> misfired) throws Exception{ 
    
	//create a new File object based on the directory we have to zip File    
	File zipDir = new File(dir2zip); 

	//get a listing of the directory content 
	String[] dirList = zipDir.list(); 
	byte[] readBuffer = new byte[2156]; 
	int bytesIn = 0; 

	if (dirList.length == 0) {
	    ZipEntry anEntry = new ZipEntry(topName+"/"+dir2zip.substring(prefix.length()).replace("\\", "/")); 
	    //System.out.println("add dir entry ----> "+topName+"/"+dir2zip.substring(prefix.length()).replace("\\", "/"));
	    zos.putNextEntry(anEntry);
	}


	//loop through dirList, and zip the files 
	for(int i=0; i<dirList.length; i++) { 
	    File f = new File(zipDir, dirList[i]); 
	    if (f.isDirectory()) { 

		//if the File object is a directory, call this 
		//function again to add its content recursively 
		String filePath = f.getPath(); 
		zipDir(filePath, prefix, topName, zos, misfired); 

		//loop again 
		continue; 
	    } 

	    if (misfired != null && misfired.contains(f.getAbsolutePath())) {
		continue;
	    }

	    //if we reached here, the File object f was not a directory 
	    //create a FileInputStream on top of f 
	    FileInputStream fis = new FileInputStream(f); 
		
	    //create a new zip entry 
	    ZipEntry anEntry = new ZipEntry(topName+"/"+f.getPath().substring(prefix.length()).replace("\\", "/")); 
	    //System.out.println("add entry ----> "+topName+"/"+f.getPath().substring(prefix.length()).replace("\\", "/"));

	    //place the zip entry in the ZipOutputStream object 
	    zos.putNextEntry(anEntry); 
            
	    //now write the content of the file to the ZipOutputStream 
	    while((bytesIn = fis.read(readBuffer)) != -1) { 
		zos.write(readBuffer, 0, bytesIn); 
	    } 
		
	    //close the Stream 
	    fis.close(); 
	} 

    }

     
    static public void zip64Dir(String dir2zip, String prefix, String topName, Zip64File zf, List<String> misfired) throws Exception{

	//System.out.println("-----------------------------------");
	//System.out.println("dir2zip="+dir2zip);

        //create a new File object based on the directory we have to zip File                                                          
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();

        //loop through dirList, and zip the files                                                                                     
        for(int i=0; i<dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
	    //System.out.println("----> found="+f.getPath());

            if (f.isDirectory()) {

		//System.out.println("    add folder="+topName+"/"+f.getPath().substring(prefix.length()).replace("\\", "/")+"/");
		EntryOutputStream eos = zf.openEntryOutputStream(topName+"/"+f.getPath().substring(prefix.length()).replace("\\", "/")+"/", 
								 FileEntry.iMETHOD_STORED, 
								 new Date(f.lastModified()));
		eos.close();

                //if the File object is a directory, call this function again to add its content recursively  
                String filePath = f.getPath();
                zip64Dir(filePath, prefix, topName, zf, misfired);

                //loop again                                                                                         
                continue;
            }


	    if (misfired != null && misfired.contains(f.getAbsolutePath())) {
		continue;
	    }

            //if we reached here, the File object f was not a directory    
	    addFile(zf, f, topName+"/"+f.getPath().substring(prefix.length()).replace("\\", "/"));

        }

    }


    public static void addFile(Zip64File zf, File f, String name) throws Exception{
	//System.out.println("    add file="+name);
	EntryOutputStream eos = zf.openEntryOutputStream(name, 
							 FileEntry.iMETHOD_STORED, 
							 new Date(2000*(long)Math.ceil(f.lastModified()/2000.0)));
	FileInputStream fis = new FileInputStream(f);
	byte[] buffer = new byte[2156];
	for (int iRead = fis.read(buffer); iRead >= 0; iRead = fis.read(buffer)) {
	    eos.write(buffer, 0, iRead);
	}
	fis.close();
	eos.close();
    }


    public static final void unzip64(String zipFileName, String extractTo) throws Exception {

        System.out.println("Extracting from "+zipFileName+" ...\n");
        Zip64File zf = new Zip64File(zipFileName);
        List<FileEntry> listFe = zf.getListFileEntries();
        long lExtractions = 0;
        for (Iterator<FileEntry> iterFe = listFe.iterator(); iterFe.hasNext(); ) {
            FileEntry fe = iterFe.next();
            extractFileEntry(zf, fe, extractTo);
            lExtractions++;
        }

        zf.close();
        System.out.println(lExtractions+" matching file entries extracted.");
    }



    static private boolean extractFileEntry(Zip64File zf, FileEntry fe, String extractTo) throws Exception{

        boolean bExtracted = false;

        String sFileName = extractTo;;
        if (sFileName.endsWith(".")) {
            sFileName = sFileName.substring(0,sFileName.length()-1);
        }

        if (sFileName.endsWith(File.separator)) {
            sFileName = sFileName.substring(0,sFileName.length()-1);
        }

        sFileName = sFileName + File.separator + fe.getName().replace('/', File.separatorChar);
        //System.out.println("extract to "+sFileName);
        File fileOutput = new File(sFileName);

        /* create all missing folders */
        fileOutput.getParentFile().mkdirs();
        if (fe.isDirectory()) {
            bExtracted = fileOutput.mkdir();
            //System.out.println("   ---> mkdir");
        } else {
            if (!fileOutput.exists()) {
                /* extract the file entry */
                FileOutputStream fos = new FileOutputStream(fileOutput);
                EntryInputStream eis = zf.openEntryInputStream(fe.getName());
                byte[] buffer = new byte[2048];
                for (int iRead = eis.read(buffer); iRead >= 0;iRead = eis.read(buffer)) {
                    fos.write(buffer, 0, iRead);
                }
                eis.close();
                fos.close();
                bExtracted = true;
            } else {
                System.err.println("File "+fileOutput.getAbsolutePath()+" already exists and is left unreplaced!");
            }
        }
        return bExtracted;
    } /* extractFileEntry */





}