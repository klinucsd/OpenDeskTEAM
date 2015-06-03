

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

         String site = request.getParameter("site");
         String protocol = request.getParameter("protocol");
         String id = request.getParameter("id");
	 String fileName = request.getParameter("filename");

         String path = pageContext.getServletContext().getRealPath("WEB-INF");
         String dirName = path + "/data/excels/" + id;

	 out.println(CameraTrapUtil.validatePhotoDataExcel(site, dirName+"/"+fileName, startInt, limitInt));

     } catch (Exception ex) {
	ex.printStackTrace();
     }


%>

