<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("id");
      String name = request.getParameter("name");
      String abbrev = request.getParameter("abbrev");
      String lat = request.getParameter("lat");
      String lon = request.getParameter("lon");
      String countryId = request.getParameter("country_id");
      String org = request.getParameter("org");
      String objective = request.getParameter("objective");
      String useBait = request.getParameter("useBait");
      String bait = request.getParameter("bait");

      // update the database table
      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(id));
      String oldSiteName = site.getName();
      String oldSiteAbbrev = site.getAbbv();
      site.setName(name);
      site.setAbbv(abbrev);
      if (lat != null && !lat.equals("")) {
          site.setLatitude(Float.parseFloat(lat));
      }	 
      if (lon != null && !lon.equals("")) {
          site.setLongitude(Float.parseFloat(lon));
      }	 
      site.setStatus(objective);

      if (useBait != null && !useBait.equals("")) {
          if (useBait.equals("Yes")) {
             site.setLastEventBy(new Integer(1));
	     site.setInstName(bait);
          } else {
             site.setLastEventBy(new Integer(0));
	     site.setInstName(null);
	  } 
      } 

      if (countryId != null && !countryId.trim().equals("")) {
            Session session1 = network.getSession();
            Query query = session1.createQuery("FROM Country as country WHERE country.id="+countryId);
            Country ctr = (Country)query.list().get(0);
	    site.setCountry(ctr);
      }

      network.updateSite(site);  

      // update the folder names for this project 
      String root = pageContext.getServletContext().getRealPath("/");
      File imageDir = new File(root, "image-repository");
      File thumbnailDir = new File(root, "thumbnails");

      File oldProjImageDir = new File(imageDir, oldSiteName);
      File oldProjThumbnailDir = new File(thumbnailDir, oldSiteName);

      File newProjImageDir = new File(imageDir, name);
      File newProjThumbnailDir = new File(thumbnailDir, name);

      if (oldProjImageDir.exists()) {
          oldProjImageDir.renameTo(newProjImageDir);
      }

      if (oldProjThumbnailDir.exists()) {
          oldProjThumbnailDir.renameTo(newProjThumbnailDir);
      }

      // update units and arrays
      if (!abbrev.equals(oldSiteAbbrev)) {
          List<Block> blocks = network.getBlocks(site);	
	  for (Block block : blocks) {
	      block.setName("CT-"+abbrev+"-"+block.getIndex());	
	      network.update(block);		

              List<SamplingUnit> traps = network.getSamplingUnits(block);	
	      for (SamplingUnit trap : traps) {
                  String trapName = trap.getName();
		  
		  int pos = trapName.indexOf("-");
		  if (pos != -1) {
		      String tmp = trapName.substring(0, pos+1);
		      trapName = trapName.substring(pos+1);

		      tmp += abbrev;
		      pos = trapName.indexOf("-");
		      if (pos != -1) {
		          tmp += trapName.substring(pos);
	                  trap.setName(tmp);
		          network.update(trap);
	              }
		  }	
	      }
	  }

          // update the folders for these camera traps
	  if (newProjImageDir.exists()) {	  
              String[] events = newProjImageDir.list();
              for (int i=0; i<events.length; i++) {
                  File eventDir = new File(newProjImageDir, events[i]);
	          String[] arrays = eventDir.list();
                  for (int j=0; j<arrays.length; j++) {
		       File arrayDir = new File(eventDir, arrays[j]);
	               String[] units = arrayDir.list();
	               for (int k=0; k<units.length; k++) {

		           File unitDir = new File(arrayDir, units[k]);

		           int pos = units[k].indexOf("-");
			   if (pos != -1) {
		               String tmp = units[k].substring(0, pos+1);
		               units[k] = units[k].substring(pos+1);

		               tmp += abbrev;
		               pos = units[k].indexOf("-");
			       if (pos != -1) {
		                   tmp += units[k].substring(pos);
		                   File newDir = new File(arrayDir, tmp);
		                   unitDir.renameTo(newDir);
			       }
			   }
		       }
	          } 
	      }
          }

      }

      network.close();
    
%>