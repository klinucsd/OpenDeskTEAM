

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String arrayId = request.getParameter("array_id");
      String projectId = request.getParameter("project_id");

      Network network = NetworkFactory.newInstance();
      Site site = network.getSiteById(Integer.parseInt(projectId));
      ProtocolFamily pf = network.getProtocolFamilyByName("Terrestrial Vertebrate"); 
      Block block = network.getBlockById(Integer.parseInt(arrayId));	
      List<SamplingUnit> traps = network.getSamplingUnits(block);

      Set<String> trapNames = new TreeSet<String>();
      for (SamplingUnit trap : traps) {  
          trapNames.add(trap.getName()); 
      }

      int index = 1;
      String tmpName = "CT-"+site.getAbbv()+"-"+block.getIndex()+"-"+index;
      while (trapNames.contains(tmpName)) {
          index++;
          tmpName = "CT-"+site.getAbbv()+"-"+block.getIndex()+"-"+index;
      }

      // create a new camera trap in the block
      CameraTrap trap = new CameraTrap();
      Integer minTrapId = network.getMinTrapId();	
      trap.setId(minTrapId);
      trap.setName(tmpName);
      trap.setSite(site);
      trap.setBlock(block);
      trap.setProtocolFamily(pf);
      network.save(trap);

      network.close();    
%>