
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="com.fasterxml.jackson.core.*" %>

<%
      String institutionId = request.getParameter("institution_id");
      String name = request.getParameter("name");
      String address = request.getParameter("address");
      String city = request.getParameter("city");
      String state = request.getParameter("state");
      String country = request.getParameter("country_id");
      String zip = request.getParameter("zip");
      String email = request.getParameter("email");
      String phone = request.getParameter("phone");

      Network network = NetworkFactory.newInstance();
      Session session1 = network.getSession();

      Query query = session1.createQuery("FROM Institution as institution WHERE institution.id="+institutionId);
      List<Institution> institutions = query.list();
      for (Institution institution : institutions) {
          institution.setName(name);
          institution.setAddress(address);
          institution.setCity(city);
          institution.setState(state);

          if (country != null && !country.trim().equals("")) {
              Query query1 = session1.createQuery("FROM Country as country WHERE country.id="+country);
              Country ctr = (Country)query1.list().get(0);
              institution.setCountry(ctr);
          }
      
          institution.setZipcode(zip);
          institution.setEmail(email);
          institution.setPhoneNumber(phone);

          session1.update(institution);
      }
      network.close();
    
%>