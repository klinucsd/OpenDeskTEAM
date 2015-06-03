<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.hibernate.*" %>
<%@ page import="org.hibernate.cfg.*" %>
<%@ page import="org.hibernate.validator.*" %>
<%@ page import="org.team.sdsc.datamodel.*" %>
<%@ page import="org.team.sdsc.datamodel.util.*" %>

<%
    response.setHeader("Cache-Control","no-cache"); 
    response.setHeader("Pragma","no-cache"); 
    response.setDateHeader ("Expires", -1); 

    Network network = NetworkFactory.newInstance();
    List<Site> sites = network.getSites();

    if (!sites.isEmpty()) {
        response.sendRedirect("index.jsp");
    }

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title id='title'>DeskTEAM</title>
 
        <!-- ** CSS ** -->
        <!-- base library -->
        <link rel="stylesheet" type="text/css" href="ext-3.0.0/resources/css/ext-all.css" />
 
        <!-- overrides to base library -->
         <link rel="stylesheet" type="text/css" href="css/team.css" />
         <!-- link rel="stylesheet" type="text/css" href="css/examples.css" / -->
         <link rel="stylesheet" type="text/css" href="css/data-view.css" />

	 <style>
	 #gallery {
	    overflow-y : auto;
	 }

	 #applytogroup {
	    padding-top:4pt;
	 }

         .x-form-cb-label {
            top:0px;
         }

	 .annotated a span{
	    color: #006600;
	    // font-weight: bold;
	 }

	 .cellClass {
	    vertical-align:text-top;
	 }

	 .falseImg {
	     opacity : 1.0;
	     filter : "alpha(opacity=1.0)";
	 }

	 .trueImg {
	     opacity : 0.4;
	     filter : "alpha(opacity=0.4)";
	 }

	 .ckCls {
             font-family:arial,tahoma,helvetica,sans-serif;
             font-size:11px;
             font-size-adjust:none;
             font-style:normal;
             font-variant:normal;
             font-weight:normal;

	     padding-top: 5pt;
	     padding-left: 2pt;
	 }
	 </style>


        <!-- ** Javascript ** -->
        <!-- ExtJS library: base/adapter -->
        <script type="text/javascript" src="ext-3.0.0/adapter/ext/ext-base.js"></script>
        <!-- ExtJS library: all widgets -->
        <script type="text/javascript" src="ext-3.0.0/ext-all-debug.js"></script>
 
        <!-- overrides to library -->
 
        <!-- extensions -->
 
        <!-- page specific -->
        <script type="text/javascript" src="javascripts/datetime.js"></script> 
        <script type="text/javascript" src="javascripts/data-panel.js"></script>
        <script type="text/javascript" src="javascripts/jquery-1.2.3.js"></script>
        <script type="text/javascript" src="javascripts/DataView-more.js"></script>
        <!-- script type="text/javascript" src="ext-3.0.0/shared/examples.js"></script -->
	<script type="text/javascript" src="javascripts/jquery-set-offset.js"></script>


        <script type="text/javascript">
        // Path to the blank image should point to a valid location on your server
        Ext.BLANK_IMAGE_URL = 'ext-3.0.0/resources/images/default/s.gif';

	// Fix a browser bug
        Ext.override(Ext.Element, {
           contains: function() {
              var isXUL = Ext.isGecko ? function(node) {
                  return Object.prototype.toString.call(node) == '[object XULElement]';
              } : Ext.emptyFn;

             return function(el) {
                 return !this.dom.firstChild || // if this Element has no children, return false immediately
                        !el ||
                        isXUL(el) ? false : Ext.lib.Dom.isAncestor(this.dom, el.dom ? el.dom : el);
             };
          }()
        });

        var homepath = "<%= System.getProperty("user.home").replace("\\", "/") %>";

        var msg = function(title, msg){
            Ext.Msg.show({
                title: title,
                msg: msg,
                minWidth: 300,
                modal: true,
                icon: Ext.Msg.INFO,
                buttons: Ext.Msg.OK
            });
        };

        Ext.onReady(function(){

	    var confWin = Ext.getCmp('confWin');
	    if (confWin != null) {
	        confWin.destroy();
	        confWin = null;
            }

            if (confWin == null) { 

	        var root = new Ext.tree.AsyncTreeNode({
		    id:'0',
		    //path: homepath,
		    //text: homepath
		    path: "/",
		    text: ""
	        });

	        // create tree panel
	        var data = new Ext.tree.TreeLoader({
		    url:'jsp/get-directory.jsp'}); 

	        data.on('beforeload',
		        function(treeLoader,node){  
		           this.baseParams.text=node.attributes.text;  
		           //this.baseParams.path=node.attributes.path;
                           this.baseParams.path = node.getPath("text");	
		        },
		        data); 

	        var fileTreePanel = new Ext.tree.TreePanel({
		    id: 'filetree',
	            region:'west',
		    collapsible: true,
		    margins: '5 0 0 5',
		    cmargins: '5 0 0 0',
		    cls: 'padding 5',
		    //width: 230,
                    width: 260,	    
		    height: 290,
		    //minSize: 440,
		    //maxSize: 240,
		    root: root,
		    loader: data,
		    autoScroll : true,
		    fitToFrame:true,
		    rootVisible: false,
		    border: false
	        });

	        var confStore = new Ext.data.JsonStore({
                    root: 'result',
                    fields: [ 'name' ],
                    proxy: new Ext.data.HttpProxy({
		       url: 'jsp/get-configure.jsp' 
                    })
                });

                var confCM = new Ext.grid.ColumnModel([{
                    id:'common',
                    header: "File Name",
                    dataIndex: 'name',
                    width: 290
                }]);

	        var confGridPanel = new Ext.grid.GridPanel({
		    id: 'confgrid',
		    store: confStore,
		    cm: confCM,
		    //width:270,
                    width:300,	    
		    height:290,
		    frame:false,
		    hidden:false,
		    viewConfig: {
		        forceFit:false
		    }
	        });

	        confWin = new Ext.Window({
		    id          : 'confWin',
		    title       : 'Select a TEAM Configuration File (.tcf)',
		    layout      : 'table',
		    layoutConfig: { columns: 2 },
		    width       : 570,
		    height      : 380,
		    closeAction : 'hide',
		    plain       : true,
		    modal       : false,
		    items       : [ 
			            fileTreePanel,
			            confGridPanel,
				    {
				       xtype: 'checkbox',
				       boxLabel: 'Show selected directory as default',
				       ctCls: 'ckCls',
				       id: 'defaultFlag'
				    }
			          ],
		    bbar        : [
			            '->',
	                            {
				      xtype: 'button',
				      text:  'Setup',
				      pressed: true,
				      handler: function() {

				          var node = Ext.getCmp("filetree").getRootNode().getOwnerTree().getSelectionModel().getSelectedNode();
				          var record = Ext.getCmp('confgrid').getSelectionModel().getSelected();
					  var defaultFlag = Ext.getCmp('defaultFlag').getValue();
				          if (node == null || record == null) {
					      msg("Error", "No configuration file is selected.");
				          } else {
					      //confWin.hide();
					      var waitBox = Ext.MessageBox.wait("Please wait. We are validating the configuration file.", 
									        "Status");
					      $.getJSON( "jsp/load-configure.jsp",
	                                            { 
						        path: node.getPath('text')+"/"+record.get('name'),
							defaultFlag: defaultFlag 
						    },
                                                    function(j) {
						       waitBox.hide();
						       if (j.success) {
						           waitBox = Ext.MessageBox.wait("<span style='white-space:nowrap;'>Please wait. We are initializing the deskTEAM ... </span>", 
								       		         "Status");

						           $.getJSON("jsp/init-by-configure.jsp",
								 { path: j.path },
								 function(k) {
								     waitBox.hide();

								     if (k.success) {
	                                                                 document.location.href="index.jsp";
								     } else {
									 msg("Failed", j.message); 
								     }
								 });

						       } else {
						           msg("Error", j.message);
						       }
					      });
	                                   }
				       }
			          },{
				   text: '&nbsp;'
			          },{
				      xtype: 'button',
				      text:  'Refresh',
				      pressed: true,
				      handler: function() {
                                          var node = Ext.getCmp("filetree").getRootNode().getOwnerTree().getSelectionModel().getSelectedNode();
                                          if (node) confStore.load.defer(100, confStore,[ {params:{path: node.getPath('text')}}]);
				      }
				    },
				    '&nbsp;'

			   ]
	              });

                }

                confWin.show();
        
                var fileRoot = Ext.getCmp("filetree").getRootNode();
                var selectionModel = fileRoot.getOwnerTree().getSelectionModel();
                selectionModel.on("selectionchange", 
		      function(m, node) {
                          confStore.load.defer(100, confStore,[ {params:{path: node.getPath('text')}}]);

		      }); 

	        expand(fileRoot, homepath);
		/*
                fileRoot.expand(false, 
	                        true,
		    function() {
			var desktop = fileRoot.findChild('text', 'Desktop');
			desktop.select();
		    });
		*/


        });


        </script> 
    </head>
    <body>
        <div style="-moz-background-clip:border; -moz-background-inline-policy:continuous; -moz-background-origin:padding; background:#1E4176 url(hd-bg.gif) repeat-x scroll 0 0; border:0 none; padding-left:8px; padding-top:5px; padding-bottom:5px; color:white; font-family:tahoma,arial,sans-serif; font-size:16px;">DeskTEAM <%= DeskTEAMProperties.getProperty("deskTEAM.version") %>  - Setup</div>
        <div style="padding:20px; font-family:times,arial,sans-serif;">
           The DeskTEAM needs a TEAM configuration file to initialize the database.

       <br><P>You can download a TEAM configuration file from the myTEAM area after you log into the TEAM website if you don't have one; otherwise you can select it and click the "Setup" button.  
        </div>
    </body>
</html>
