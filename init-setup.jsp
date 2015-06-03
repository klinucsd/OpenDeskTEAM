<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title id='title'>DeskTEAM</title>
 
        <!-- ** CSS ** -->
        <!-- base library -->
        <link rel="stylesheet" type="text/css" href="ext-3.4.1/resources/css/ext-all.css" />
 
        <!-- overrides to base library -->
        <link rel="stylesheet" type="text/css" href="css/team.css" />
        <!-- link rel="stylesheet" type="text/css" href="css/examples.css" / -->
        <link rel="stylesheet" type="text/css" href="css/data-view.css" />

	<link rel="stylesheet" type="text/css" href="ext-3.4.1/examples/ux/css/RowEditor.css" />

        <!-- ** Javascript ** -->
        <!-- ExtJS library: base/adapter -->
        <script type="text/javascript" src="ext-3.4.1/adapter/ext/ext-base.js"></script>
        <!-- ExtJS library: all widgets -->
        <script type="text/javascript" src="ext-3.4.1/ext-all-debug.js"></script>
 
        <!-- overrides to library -->
 
        <!-- extensions -->
 
        <!-- page specific -->
        <script type="text/javascript" src="javascripts/datetime.js"></script> 
        <script type="text/javascript" src="javascripts/data-panel.js"></script>
        <script type="text/javascript" src="javascripts/jquery-1.2.3.js"></script>
        <script type="text/javascript" src="javascripts/DataView-more.js"></script>
	<script type="text/javascript" src="javascripts/jquery-set-offset.js"></script>
        <script type="text/javascript" src="javascripts/RowEditor.js"></script> 
        <script type="text/javascript" src="javascripts/openDeskTEAM-setup.js"></script>
        <script type="text/javascript" src="javascripts//Ext.ux.util.js"></script>
        <script type="text/javascript" src="javascripts/Ext.ux.form.LovCombo.js"></script>

	<script>
           var action = <%= request.getParameter("action") %>;
      	   var pid = <%= request.getParameter("pid") %>;				   
           var homepath = '<%= System.getProperty("user.home").replace("\\", "/")+"/Desktop" %>';	   
	</script>

    </head>
    <body>
      <div style="-moz-background-clip:border; -moz-background-inline-policy:continuous; -moz-background-origin:padding; background:#1E4176  repeat-x scroll 0 0; border:0 none; padding-left:8px; padding-top:5px; padding-bottom:5px; color:white; font-family:tahoma,arial,sans-serif; font-size:16px;"><b>Open DeskTEAM <%= org.team.sdsc.datamodel.util.DeskTEAMProperties.getProperty("deskTEAM.version") %></b></div>
      <div id="deskTEAMDiv"></div>
    </body>
</html>
