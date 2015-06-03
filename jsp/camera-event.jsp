
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>

<%
     String siteId = request.getParameter("siteId");
     Network network = NetworkFactory.newInstance();

     Site site = network.getSiteById(new Integer(siteId));	
     List<SamplingEvent> samplingEvents = network.getSamplingEvents(site);
     TreeSet<String> events = new TreeSet<String>();
     for (SamplingEvent tmp : samplingEvents) {
         events.add(tmp.getEvent());
     }
     network.close();

%>

{
   results: [

<%
    int k = 0;
    for (Iterator i=events.iterator(); i.hasNext(); ) {
	if (k > 0) {
	    out.println("      ,");
	}
	out.println("      {  name : '"+i.next()+"' }" ); 
	k++;
    }
%>


  ]
}