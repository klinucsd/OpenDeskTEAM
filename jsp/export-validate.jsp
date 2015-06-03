<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%

     String event = request.getParameter("event");
     String arrayName = request.getParameter("array");
     String siteId = request.getParameter("siteId");

     try {    
        String msg = "";
	long size = -1;
        Network network = NetworkFactory.newInstance();
        Site site = network.getSiteById(new Integer(siteId));	
        //Site site = network.getSites().get(0);
        if (arrayName != null && !arrayName.equals("")) {
	    Protocol tv = network.getProtocol(4);
	    arrayName = arrayName.substring(5);
	    size = network.getCameraTrapPhotoFileSize(site, event, arrayName);

	    List<SamplingUnit> units = network.getSamplingUnits(site, tv);
	    TreeSet<SamplingUnit> traps = new TreeSet<SamplingUnit>(new SamplingUnitNameComparator());
	    traps.addAll(units);

	    for (SamplingUnit trap : traps) {
	        if (trap.getName().startsWith("CT-"+site.getAbbv()+"-"+arrayName+"-")) {
	            //System.out.println("----> trap = "+trap.getId()+"   "+trap.getName());
		    String tmp = network.validateExportCameraTrapPhotos(event, (CameraTrap)trap);
		    if (tmp != null) {
		       if (!msg.equals("")) msg += ",\n"; 
                       msg += tmp;
		    }
		}
            }

        } else {
	    size = network.getCameraTrapPhotoFileSize(site, event);
	    Protocol tv = network.getProtocol(4);
	    List<SamplingUnit> units = (List<SamplingUnit>)network.getSamplingUnits(site, tv);
	    TreeSet<SamplingUnit> traps = new TreeSet<SamplingUnit>(new SamplingUnitNameComparator());
	    traps.addAll(units);

            for (SamplingUnit trap : traps) {
		//System.out.println("----> trap = "+trap.getName());
		String tmp = network.validateExportCameraTrapPhotos(event, (CameraTrap)trap);
		if (tmp != null) {
                    if (!msg.equals("")) msg += ",\n"; 
                    msg += tmp;
		}
            }
        }
        network.close();

	size = (size / 1024) / 1024;

	if (!msg.equals("")) {
            out.println("{ valid : false, message: [\n"+msg+"\n], size: "+size+" } ");
	} else {
	    out.println("{ valid : true , size: "+size+" } ");
	}

    } catch (Exception ex) {
        ex.printStackTrace();
	out.println("{ valid : false, message: '"+ex.getMessage()+"' } ");
    }
%>
