

<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("id");
      String name = request.getParameter("name");
      String abbrev = request.getParameter("abbrev");
      String lat = request.getParameter("lat");
      String lon = request.getParameter("lon");
      String country = request.getParameter("country");
      String objective = request.getParameter("objective");
      String useBait = request.getParameter("useBait");
      String bait = request.getParameter("bait");

      Network network = NetworkFactory.newInstance();
      ProtocolFamily pf = network.getProtocolFamilyByName("Terrestrial Vertebrate"); 

      // create a new site
      Site site = new Site();
      site.setId(new Integer(id));
      site.setName(name);
      site.setAbbv(abbrev);
      site.setStatus("");

      if (lat != null && !lat.equals("")) {
          site.setLatitude(Float.parseFloat(lat));
      }	 

      if (lon != null && !lon.equals("")) {
          site.setLongitude(Float.parseFloat(lon));
      }	 

      Protocol tv = network.getProtocol(4);	
      Set<Protocol> protocols = new HashSet<Protocol>();
      protocols.add(tv);
      site.setProtocols(protocols);
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

      if (country != null && !country.trim().equals("")) {
            Session session1 = network.getSession();
            Query query = session1.createQuery("FROM Country as country WHERE country.id="+country);
            Country ctr = (Country)query.list().get(0);
	    site.setCountry(ctr);
      }

      network.saveSite(site);  

      // create a new block
      Block block = new Block();
      //long minBlockId = network.getMinBlockId();
      //block.setId(new Integer(-1));
      block.setName("CT-"+abbrev+"-1");
      block.setType("ARY");
      block.setIndex(1);
      block.setSite(site);
      network.saveObject(block);

      // create a new camera trap in the block
      CameraTrap trap = new CameraTrap();
      Integer minTrapId = network.getMinTrapId();	
      trap.setId(minTrapId);
      trap.setName("CT-"+abbrev+"-1-1");    
      trap.setSite(site);
      trap.setBlock(block);
      trap.setProtocolFamily(pf);
      network.save(trap);

      network.close();
    
%>