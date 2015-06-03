
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%
     String event = request.getParameter("event");
     String siteId = request.getParameter("siteId");	

     Network network = NetworkFactory.newInstance();
     Site site = network.getSiteById(new Integer(siteId));	
     Protocol protocol = network.getProtocol(4);

     String root = pageContext.getServletContext().getRealPath("/");
     File rootDir = new File(root, "image-repository");

     // get all camera traps
     List<SamplingUnit> units = network.getSamplingUnits(site, protocol);

     // get all collections
     List<SamplingUnit> iTraps = new ArrayList<SamplingUnit>();
     if (event != null && !event.equals("")) { 
     	List<CameraTrapPhotoCollection> collections = network.getCameraTrapPhotoCollections(site, event);
     	for (CameraTrapPhotoCollection collection : collections) {
	    if (collection.getPhotos() == null || collection.getPhotos().isEmpty()) {
	        if (collection.getDamagedCameraTrap() == null) {

		    String trapName = collection.getCameraTrap().getName();

		    /*
		    StringTokenizer st = new StringTokenizer(trapName, "-");
     		    st.nextToken();
     		    String abbrev = st.nextToken();
     		    int array = Integer.parseInt(st.nextToken());
     		    int number = Integer.parseInt(st.nextToken());
		    */

     		    String abbrev = site.getAbbv();
     		    int array = collection.getCameraTrap().getBlock().getIndex();

     		    String path = site.getName()+"/"+event+"/Array"+array+"/"+trapName;;
     		    File targetDir = new File(rootDir, path);
     		    if (targetDir.exists()) {
                        iTraps.add(collection.getCameraTrap());
		    } else {
	                network.delete(collection);
		    }
		} else {
                    iTraps.add(collection.getCameraTrap());
		}
	    } else {
                iTraps.add(collection.getCameraTrap());
            }
     	}
     }

     TreeSet<SamplingUnit> traps = new TreeSet<SamplingUnit>(new SamplingUnitNameComparator2());
     for (SamplingUnit unit : units) {
        boolean found = false;
        for (SamplingUnit trap : iTraps) {
	   if (unit.getId().intValue() == trap.getId().intValue()) {
	      found = true;
	      break;
	   }
	}
	if (!found) traps.add(unit);
     }

     //traps.addAll(units);

%>

{
   results: [

<%
    int k = 0;
    for (SamplingUnit unit : traps ) {
        if (unit instanceof CameraTrap) {
	    if (k > 0) {
	        out.println("      ,");
	    }

	    //out.println("      { id: "+unit.getId()+", name: '"+unit.getName()+" in "+unit.getBlock().getName()+"' }" ); 
            out.println("      { id: "+unit.getId()+", name: '"+unit.getName()+"' }" ); 
	    k++;
	}
    }

    network.close();

%>


  ]
}

