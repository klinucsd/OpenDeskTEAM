<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
   
<%

     String errMsg = "";

     try {
	 String direction = request.getParameter("dir");
	 String limit = request.getParameter("limit");
	 String sort = request.getParameter("sort");
	 String start = request.getParameter("start");
	
	 int startInt = 0;
	 int limitInt = 0;

	 try {
	     startInt = Integer.parseInt(start);
	     limitInt = Integer.parseInt(limit);
	 } catch (Exception ex) {}

         String trapName = request.getParameter("trapName");
         String event = request.getParameter("event");

	 Network network = NetworkFactory.newInstance();
	 CameraTrap trap = (CameraTrap)network.getSamplingUnitByName(trapName);
	 out.println(network.getJSONCameraTrapData(trap, event, startInt, limitInt, sort, direction));
	 network.close();

     } catch (Exception ex) {
	 ex.printStackTrace();
     }


%>

