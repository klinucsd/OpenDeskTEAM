

<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String projectId = request.getParameter("project_id");
      String name = request.getParameter("name");
      String address = request.getParameter("address");
      String city = request.getParameter("city");
      String state = request.getParameter("state");
      String country = request.getParameter("country");
      String zip = request.getParameter("zip");
      String email = request.getParameter("email");
      String phone = request.getParameter("phone");

      Network network = NetworkFactory.newInstance();
      Session session1 = network.getSession();
      Site site = network.getSiteById(Integer.parseInt(projectId));
      Institution institution = new Institution();
      institution.setName(name);
      institution.setAddress(address);
      institution.setCity(city);
      institution.setState(state);

      if (country != null && !country.trim().equals("")) {
          Query query = session1.createQuery("FROM Country as country WHERE country.id="+country);
          Country ctr = (Country)query.list().get(0);
          institution.setCountry(ctr);
      }
      
      institution.setZipcode(zip);
      institution.setEmail(email);
      institution.setPhoneNumber(phone);
      institution.setSite(site);
      session1.save(institution);
      network.close();

      out.println("{ institution_id: "+institution.getId()+" }"); 
    
%>