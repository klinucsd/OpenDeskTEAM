

<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String id = request.getParameter("project_id");
      Network network = NetworkFactory.newInstance();
      ProtocolFamily pf = network.getProtocolFamilyByName("Terrestrial Vertebrate"); 
      Site site = network.getSiteById(Integer.parseInt(id));
      List<Block> blocks = network.getBlocks(site);
      int maxIndex = 0;
      for (Block block : blocks) {
      	  maxIndex = Math.max(maxIndex, block.getIndex());
      }
      maxIndex++;

      // create a new block
      Block block = new Block();
      //long minBlockId = network.getMinBlockId();
      //block.setId(new Integer(-1));
      block.setName("CT-"+site.getAbbv()+"-"+maxIndex);
      block.setType("ARY");
      block.setIndex(maxIndex);
      block.setSite(site);
      network.saveObject(block);

      // create a new camera trap in the block
      CameraTrap trap = new CameraTrap();
      Integer minTrapId = network.getMinTrapId();	
      trap.setId(minTrapId);
      trap.setName("CT-"+site.getAbbv()+"-"+maxIndex+"-1");    
      trap.setSite(site);
      trap.setBlock(block);
      trap.setProtocolFamily(pf);
      network.save(trap);

      network.close();    
%>