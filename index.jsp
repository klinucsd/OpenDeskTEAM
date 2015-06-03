<%@ page language="java" session="true" %>
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

    String path = pageContext.getServletContext().getRealPath("WEB-INF");

    String homePath = System.getProperty("user.home").replace("\\", "/")+"/Desktop";
    String dataPath = homePath;
    File datahome = new File(path+"/datahome.txt");
    if (datahome.exists()) {
        BufferedReader br = new BufferedReader(new FileReader(datahome));
        dataPath = br.readLine();
        br.close();
    }

    // start database
    String DRIVER = "com.mysql.jdbc.Driver";
    String databaseDir = path+"/database";
    int port = 3336;
    String dbName = "deskTEAMDB";

    String CONN_STRING = "jdbc:mysql:mxj://localhost:" + port + "/" + dbName //                                               
        + "?" + "server.basedir=" + databaseDir //                                                                            
        + "&" + "createDatabaseIfNotExist=true"//                                                                            
        + "&" + "server.initialize-user=true" //                     
        + "&" + "useUnicode=true" //
	+ "&" + "characterEncoding=UTF-8"
        ;

    //System.out.println(CONN_STRING);

    String USERNAME = "alice";
    String PASSWORD = "q93uti0opwhkd";
    Class.forName(DRIVER).newInstance();
    Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * from sites_team");
    if (!rs.next()) {
    } 

    try {
        stmt.execute("ALTER TABLE tv_photo_animal ADD uncertainty varchar(30) DEFAULT 'Absolutely sure'");
    } catch (Exception ex) {
        //ex.printStackTrace();
    }

    conn.close();

    // start mysql session
    /*
    Configuration mConfigure = new AnnotationConfiguration();
    mConfigure.configure("deskTEAM.hibernate.cfg.xml");
    SessionFactory mFactory = mConfigure.buildSessionFactory();
    Session mSession = mFactory.openSession();
    mSession.close();
    */

    String siteId = request.getParameter("siteId");	

    Network network = NetworkFactory.newInstance();
    List<Site> sites = network.getSites();

    if (sites.isEmpty()) {
       //response.sendRedirect("init.jsp");
       response.sendRedirect("init-setup.jsp");
       return;       
    }
    
    Site site = null;
    if (siteId != null) {
        site = network.getSiteById(Integer.parseInt(siteId));
    } else {
        site = sites.get(0);
    }


    //System.out.println("\n\n\n==========================");
    //System.out.println(site.getName());

    Session hsession = network.getSession();
    Query query = hsession.createQuery("SELECT site.id, site.name "+
                                       "FROM Site as site "+
                                       "ORDER BY site.name");
    List list = query.list();


    network.close();


%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title id='title'>Open DeskTEAM</title>
 
        <!-- ** CSS ** -->
        <!-- base library -->
        <link rel="stylesheet" type="text/css" href="ext-3.0.0/resources/css/ext-all.css" />
 
        <!-- overrides to base library -->
         <link rel="stylesheet" type="text/css" href="css/team.css" />
         <!-- link rel="stylesheet" type="text/css" href="css/examples.css" / -->
         <link rel="stylesheet" type="text/css" href="css/data-view.css" />
         <link rel="stylesheet" type="text/css" href="superbox/superboxselect.css" />

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

	 .damaged a span{
	    //color: firebrick;
            color: SaddleBrown;    
	    // font-weight: bold;
	 }

	 .cellClass {
	    vertical-align:text-top;
	 }

	 .icon_new_data {  
            background-image: url(images/icons/database_add.png) !important;  
         } 

	 .icon_new_camera_data {  
            background-image: url(images/icons/camera_add.png) !important;  
         } 

	 .icon_camera_error {  
            background-image: url(images/icons/camera_error.png) !important;  
         } 

	 .icon_new_pda_data {  
            background-image: url(images/icons/phone_add.png) !important;  
         } 

	 .icon_package_add {  
            background-image: url(images/icons/package_add.png) !important;  
         } 

	 .icon_delete {  
            background-image: url(images/icons/delete.png) !important;  
         } 

	 .icon_search {  
            background-image: url(images/icons/find.png) !important;  
         } 

	 .icon_export_to_team {  
            background-image: url(images/icons/package_go.png) !important;  
         } 

	 .icon_export_to_file {  
            background-image: url(images/icons/package.png) !important;  
         } 

	 .icon_update_via_internet {  
            background-image: url(images/icons/link_edit.png) !important;  
         } 

	 .icon_update_via_file {  
            background-image: url(images/icons/disk_edit.png) !important;  
         } 

	 .icon_help {  
            background-image: url(images/icons/help.png) !important;  
         } 

	 .icon_about {  
            background-image: url(images/icons/flag_green.png) !important;  
         } 

	 .icon_pref {  
            background-image: url(images/icons/application_add.png) !important;  
         } 

	 .icon_project {  
            background-image: url(images/icons/cone.png) !important;  
         } 

	 .icon_project_add {  
    	    background-image: url(images/icons/application_add.png) !important;  
	 } 

         .icon_project_edit {  
            background-image: url(images/icons/application_edit.png) !important;  
         } 

	 .falseImg {
	     opacity : 1.0;
	     filter : "alpha(opacity=1.0)";
	 }

	 .trueImg {
	     opacity : 0.4;
	     filter : "alpha(opacity=0.4)";
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
        <script type="text/javascript" src="javascripts/data-panel.js?rand=<%= (new java.util.Random()).nextInt(256) %>"></script>
        <script type="text/javascript" src="javascripts/jquery-1.2.3.js"></script>
        <script type="text/javascript" src="javascripts/DataView-more.js"></script>
        <!-- script type="text/javascript" src="ext-3.0.0/shared/examples.js"></script -->
	<script type="text/javascript" src="javascripts/jquery-set-offset.js"></script>
        <script type="text/javascript" src="superbox/SuperBoxSelect.js"></script>


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


	Ext.ux.PanPanel = Ext.extend(Ext.Panel, {
	     constructor: function(config) {
            		     config.autoScroll = false;
            		     Ext.ux.PanPanel.superclass.constructor.apply(this, arguments);
     	     },

             onRender: function() {
             	           Ext.ux.PanPanel.superclass.onRender.apply(this, arguments);

                           var center = document.createElement('center');
			   this.body.appendChild(center);

                           center.appendChild(this.client);	
                           //this.body.appendChild(this.client);
                           this.client = Ext.get(this.client);
                           this.client.on('mousedown', this.onMouseDown, this);
                           this.client.setStyle('cursor', 'move');
             },

             onMouseDown: function(e) {
                             e.stopEvent();
                             this.mouseX = e.getPageX();
                             this.mouseY = e.getPageY();
            		     Ext.getBody().on('mousemove', this.onMouseMove, this);
            		     Ext.getDoc().on('mouseup', this.onMouseUp, this);
             },

             onMouseMove: function(e) {
             		     e.stopEvent();
            		     var x = e.getPageX();
            		     var y = e.getPageY();
            		     if (e.within(this.body)) {
	        	         var xDelta = x - this.mouseX;
				 var yDelta = y - this.mouseY;
	        		 this.body.dom.scrollLeft -= xDelta;
	        		 this.body.dom.scrollTop -= yDelta;
            	             }
            		     this.mouseX = x;
            		     this.mouseY = y;
              },

              onMouseUp: function(e) {
              		     Ext.getBody().un('mousemove', this.onMouseMove, this);
            		     Ext.getDoc().un('mouseup', this.onMouseUp, this);
              }

       	})


	var docHeight;

	var version = "<%= DeskTEAMProperties.getProperty("deskTEAM.version") %>";
        var siteName = "<%= site.getName() %>";
        var siteAbbrev = "<%= site.getAbbv() %>";
        var siteId = "<%= site.getId() %>";
        var homepath = "<%= System.getProperty("user.home").replace("\\", "/") %>";

        var datapath = "<%= dataPath %>";
					  
        var currentRepositoryPath = null;
	var sTrap = null;
	var sEvent = null;
	var sArray = null;
	var selectedRow = null;

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

	var searchSiteData  = [];
<%
	for (Site site1 : sites) {
	   out.println("searchSiteData.push(['"+site1.getAbbv()+"', '"+site1.getName()+"', 'The Heart of Dixie','ALClass']);\n");
	}
	

%>
        Ext.onReady(function(){

            Ext.QuickTips.init();
	    docHeight = getDocHeight();

            var root = new Ext.tree.AsyncTreeNode({
               id:'0',
	       path: '/'+siteName,
               text: siteName
            });

	    // create tree panel for the image repository
            var data = new Ext.tree.TreeLoader({
	       url:'jsp/image-repository-tree.jsp?site='+siteName}); 

            data.on('beforeload',
                   function(treeLoader,node){  
                        this.baseParams.text=node.attributes.text;  
                        this.baseParams.path=node.attributes.path;
                   },
	           data); 

            var repositoryPane = new Ext.tree.TreePanel({
                             id: 'repository',
                             region:'west',
                             collapsible: false,
                             margins: '5 0 0 5',
                             cmargins: '5 0 0 0',
			     cls: 'padding 5',
                             width: 240,
                             //width: 220,		     
			     //height: 600,
			     height: Math.max(546, docHeight-140),
			     //height: Math.max(550, docHeight-140),
                             minSize: 240,
                             maxSize: 240,
			     root: root,
			     loader: data,
			     autoScroll : true,
                             fitToFrame:true,
			     border: false,
                             listeners :  {
			         /*
			         afterrender: function() {
				     this.nav = 
				        new Ext.KeyNav(this.getEl(),
						       {
                                                          down: function() { 
                                                             syncGUI();
							  },
        					          up: function() { 
							     syncGUI();
							  },
						          scope: this
						       });		       
				 },
				 */
	                         click: function(node, event) {

				 	    // handle damaged camera trap
				 	    if (node.attributes.damaged) {

        					currentRepositoryPath = null;
						sTrap = null;
						sEvent = null;
						sArray = null;
						selectedRow = null;
		
					        // reset single viewer
                                                var iPane  = Ext.getCmp('ImageViewer');
						iPane.setText('Image Viewer');

					        var img = document.getElementById("imageview");
					        img.src="ext-3.0.0/resources/images/default/s.gif";
						zoomreset();
     
						// reset group viewer
						Ext.getCmp("gallery").store.removeAll();

						// reset species panel
						Ext.getCmp('speciesPanel').hide();

					        // reset datagrid
						Ext.getCmp('trapdatagrid').store.removeAll();

						var tmp = "";
						var ctPane = createCameraTrappingExcelGridPane3(siteName,
                                                                                                'Camera Trapping',
                                                                                                tmp,
                                                                                                tmp+".xls",
												-1,
                                                                                                node.attributes.text,
                                                                                                node.parentNode.parentNode.attributes.text);
						ctPane.setTitle(null);
                                                var dataHolder = Ext.getCmp('dataholder');
                                                dataHolder.removeAll(true);
                                                dataHolder.add(ctPane);
                                                dataHolder.doLayout();
					    }

               	       		       	    if (node.getPath('text').split("/").length == 5) {
           				         var elements = node.getPath('text').split("/");
           					 var dEvent = elements[2];
	   					 var dArray = elements.length > 3 ? elements[3] : '';
	   					 var dTrap = elements.length > 4 ? elements[4] : '';

					         var tmp = "CT-"+ siteAbbrev+"-"+elements[3].substring(5)+"-"+elements[2]+"-"+elements[4];
						 if (currentRepositoryPath == null || currentRepositoryPath != tmp)  {
						     
						     if (dTrap != null && dEvent != null) {
						            currentRepositoryPath = tmp;
					             }
						     var ctPane = createCameraTrappingExcelGridPane1(siteName, 
									                             'Camera Trapping', 
										              	     tmp,
										                     tmp+".xls", 
										                     -1,
												     dTrap,
												     dEvent);
						     ctPane.setTitle(null);
						     var dataHolder = Ext.getCmp('dataholder');
					             dataHolder.removeAll(true);
						     dataHolder.add(ctPane);
						     dataHolder.doLayout();
 
						     selectedRow = null;
						     node.expand(false, true);

						 }
					    }
					    
					    // handle working camera trap
				            if (node.attributes.leaf) {

					        var path = node.attributes.path.substring(1);
						path = path.replace(/\//g, " : ");

						// set the image title
                                                var iPane  = Ext.getCmp('ImageViewer');
						iPane.setText(path);

						// load image
					        var img = document.getElementById("imageview");
						img.src="image-repository"+escape(node.attributes.path);
						zoomreset();

						// highlight the photo in the group view
						highlightPhotoInGroup(node.attributes.text);

				                // show species panel
					        var sPane  = Ext.getCmp('speciesPanel');
						sPane.setVisible(true);
						    
						if (Ext.isSafari) {
                                                    $('#updateButton').offset({left:$('#applytogroup').offset().left+80,
									       top:$('#updateButton').offset().top});
						}

						// set species panel by the values in the database
				                $.getJSON(
                                                          "jsp/photo-species-set.jsp",
                                                          { site: siteName, path: node.attributes.path},
                                                          function(j) {

							     $('#imgName').html(node.attributes.text);
							     $('#Trg').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.trg+"</span>");
							     $('#Flash').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.flash+"</span>");
							     //$('#Exposure').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.exposure+"</span>");
                                                             $('#TakenTime').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.takentime+"</span>");
							     $('#Tmp').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.temperature+"</span>");

						             if (j.success) {
	      						        if (j.type == 'Animal' || j.type == 'unknown') {
								    var ugenus = Ext.getCmp('ugenus');
								    if (ugenus != null) {
								        // reset 
								    } else {
								        insertSpeciesUI();     
								    }

								    if (j.genus2 != null) {
								        var ugenus2 = Ext.getCmp('ugenus2');
								    	if (ugenus2 != null) {
								            // reset 
								        } else {
								            insertSpeciesUI2();     
								        }   
								    } else {
								        var ugenus2 = Ext.getCmp('ugenus2');
								    	if (ugenus2 != null) {
								            removeSpeciesUI2();     
								        } 
									Ext.getCmp('addSpeciesButton').setVisible(true);  
								    }
								    
								    if (j.type == 'Animal') {
								        Ext.getCmp('typeCombo').setValue(j.type);
		    						        Ext.getCmp('ugenus').setValue(j.genus);
                    						        Ext.getCmp('uspecies').setValue(j.species);
									Ext.getCmp('unumber').setValue(j.number);
									Ext.getCmp('uncertainty').setValue(j.uncertainty);

									if (j.genus2 != null) {
									   Ext.getCmp('ugenus2').setValue(j.genus2);
                    						           Ext.getCmp('uspecies2').setValue(j.species2);
									   Ext.getCmp('unumber2').setValue(j.number2);
									   Ext.getCmp('uncertainty2').setValue(j.uncertainty2);
									}
								    } else {
                                                                        Ext.getCmp('typeCombo').setValue('Animal');
								    }
		    						    
								    Ext.getCmp('ugenus').clearInvalid(); 
								    Ext.getCmp('uspecies').clearInvalid();
                    						    Ext.getCmp('unumber').clearInvalid();
								    Ext.getCmp('uncertainty').clearInvalid();

								} else {
								    Ext.getCmp('typeCombo').setValue(j.type);

								    // remove species panel   
								    removeSpeciesUI();
								    removeSpeciesUI2();
								}

								var idperson = Ext.getCmp('idperson');
		    						idperson.setValue(j.idperson);
                    						idperson.clearInvalid();

								var k = 0;
                                                                idPersonCombo.store.each( function(record) {
								       if (record.get('name') == j.idperson) {
								           idperson.selectedIndex = k;
								       }	
								       k++;
								    }
								);

							   } else {

							        // not annotated yet
                                                                Ext.getCmp('typeCombo').setValue('Animal');			     
							        var ugenus = Ext.getCmp('ugenus');

								if (ugenus == null || ugenus=='null') {
								    insertSpeciesUI(); 
								} else {
		    						    Ext.getCmp('ugenus').setValue(null);
                    						    Ext.getCmp('ugenus').clearInvalid();

		    						    Ext.getCmp('uspecies').setValue(null);
 					                            Ext.getCmp('uspecies').clearInvalid();
								}

		    						Ext.getCmp('unumber').setValue(null);
                    						Ext.getCmp('unumber').clearInvalid();

		    						Ext.getCmp('uncertainty').setValue('Absolutely sure');
                    						Ext.getCmp('uncertainty').clearInvalid();

		    						//Ext.getCmp('idperson').reset();
                    						//Ext.getCmp('idperson').clearInvalid();

							        var ugenus2 = Ext.getCmp('ugenus2');
								if (ugenus2 != null) {
								    removeSpeciesUI2();
								} 
							        Ext.getCmp('addSpeciesButton').setVisible(true);

							  }
						      }
						);

					        var elements = node.attributes.path.split("/");
					        if (elements.length > 3) {
						    if (elements.length > 5) {
					                sEvent = elements[2];
						        sArray = elements[3];
					                sTrap = elements[4];
						        sRaw = elements[5];
						    }
						   
						    // reload gallery panel
						    if (!inGallery(sEvent, sTrap, sRaw)) {
						        var galleryStore = Ext.getCmp("gallery").store;
						        galleryStore.load(
								          [{
									     params:{ trapName:sTrap,
						 			              event:sEvent,
										      raw:sRaw,
										      //array: sArray,
										      path:escape(node.attributes.path)}
                                                                          }]
                                                                        );
						    }

					            var tmp = "CT-"+ siteAbbrev+"-"+elements[3].substring(5)+"-"+elements[2]+"-"+elements[4];
						    if (currentRepositoryPath == null || currentRepositoryPath != tmp)  {
						     
						        if (sTrap != null && sEvent != null) {
						            currentRepositoryPath = tmp;
					                }
						        var ctPane = createCameraTrappingExcelGridPane1(siteName, 
									                                'Camera Trapping', 
										              		tmp,
										                        tmp+".xls", 
										                        -1,
												        sTrap,
												        sEvent);
						        ctPane.setTitle(null);
						        var dataHolder = Ext.getCmp('dataholder');
							dataHolder.removeAll(true);
						        dataHolder.add(ctPane);
						        dataHolder.doLayout();
 
							selectedRow = null;
						    } else { 

						        var photoDataGrid = Ext.getCmp('photodatagrid');
							if (photoDataGrid != null) {
							    var selectionModel = photoDataGrid.getSelectionModel();

							    var found = false;
							    for (var i=0; i<photoDataGrid.store.getTotalCount(); i++) {
							       var record = photoDataGrid.store.getAt(i);
							       if (typeof(record)=="undefined" || record == null) continue;

							       if (record.get('Sampling_Period') == sEvent &&
							           record.get('Camera_Trap_Point_ID') == sTrap &&
  								   record.get('Raw_Filename') == sRaw) {
								   selectionModel.selectRow(i);
								   photoDataGrid.getView().focusRow(i);

								   // adjust a bit
								   try {
								       if (selectedRow == null) {
								           if (i > 5) photoDataGrid.getView().focusRow(i+1);
								       } else if (selectedRow < i) {
								           photoDataGrid.getView().focusRow(i+1);
								       } else if (selectedRow > i) {
								           photoDataGrid.getView().focusRow(i-1);
								       }
								   } catch(e) {}

								   selectedRow = i;
								   found = true;
								   break;
							       }					   
							    }

							    if (!found && selectedRow != null) {
							        try {
							            selectionModel.deselectRow(selectedRow);
								} catch (e) {}
							    }

							    selectionModel.on('selectionchange',
									      function(selmodel) {
                                             				      	 var rec = selmodel.getSelected();
                                         					 if (rec) {
                                                				     sRaw = rec.get('Raw_Filename');
                                            					     sEvent = rec.get('Sampling_Period');
                                                   				     sTrap = rec.get('Camera_Trap_Point_ID');
                                                 				     var path = "/"+siteName+"/"+sEvent+"/"+sArray+"/"+sTrap+"/"+sRaw;
                                                            			     Ext.getCmp('repository').selectPath(path,
															 'text',
                                                                                         				 function(b, c){
                                                           								     if ($('#repository').offset().top < 85) {
                                                                   							          $('#repository').offset({left:4,top:85});
                                                         								     }
                                                         								     highlightTreeNode();
                                                     									 });
                                            				             //alert(sRaw+"---"+sEvent+"---"+sTrap+"---"+path);
                                         					 }
                                          				     });

							}
						    }
					         } 

					    }

				        }
			     }			     
                         });

            root.expand(false, true);

	    // create imagePanel
            var idPersonStore  = new Ext.data.JsonStore({
                autoLoad: true,
                pruneModifiedRecords: true,
                url : "jsp/person.jsp?siteId="+siteId,
                root : "results",
                fields: ['id', 'name']
            });
					
            var idPersonCombo = new Ext.form.ComboBox({
                store: idPersonStore,
                id: 'idperson',
                displayField:'name',
		valueField:'name',
                typeAhead: true,
                mode: 'local',
                triggerAction: 'all',
                emptyText:'Select a person',
                selectOnFocus:true,
                width: 130,	
                allowBlank: false,
	        fieldLabel: 'Identified By',
	        editable : false,
                listeners: {
	            select: function(obj, record, ind){
	                    }
	        }
            });

	    var genusValue = null;
	    var speciesValue = null;
	    var numberValue = null;

            var typeStore = new Ext.data.SimpleStore({
                                   fields: ['name'],
                                   data : [
                                             ['Animal'],
                                             ['Blank'],
					     ['Setup/Pickup'],
                                             ['Start'],
                                             ['End'],
					     ['Unidentifiable'],
					     ['Unknown']
                                          ]
                             });

            var typeCombo = new Ext.form.ComboBox({
	      id: 'typeCombo',
              store: typeStore,
              displayField: 'name',
              typeAhead: true,
              mode: 'local',
              forceSelection: true,
              triggerAction: 'all',
              emptyText: 'Select a type ...',
              selectOnFocus: true,
	      allowBlank: false,
	      value: 'Animal',
              editable: false,
	      name: 'type',
	      fieldLabel: 'Photo Type',
	      listeners: {

                  select: function(obj, record, ind){
	              var panel = Ext.getCmp('speciesPanel');
		      if (record.get('name') == 'Animal') {   
			 // show genus,	species and number
	                 var ugenus = Ext.getCmp('ugenus');
			 if (ugenus == null) {
			     panel.insert(2, getUncertaintyCombo()); 
			     panel.insert(2, { fieldLabel: 'Number',
				               xtype: 'numberfield',
                                               name: 'unumber',
                                               id: 'unumber',
                                               allowBlank: false 
                                              });
                             panel.insert(2, getSpeciesCombo());
			     panel.insert(2, getGenusCombo());

			     if (numberValue != null) {
			        Ext.getCmp('unumber').setValue(numberValue);
			     } 

			     if (speciesValue != null) {
			        Ext.getCmp('uspecies').setValue(speciesValue);
			     }		      

			     if (genusValue != null) {
			        Ext.getCmp('ugenus').setValue(genusValue);
			     }		      
			     panel.doLayout();		       

                             Ext.getCmp('uncertainty').clearInvalid();
                             Ext.getCmp('unumber').clearInvalid();
                             Ext.getCmp('ugenus').clearInvalid();
			     Ext.getCmp('uspecies').clearInvalid();	      

			 }

			 Ext.getCmp('addSpeciesButton').setVisible(true);

			 // check applytogroup
			 // Ext.getCmp('applytogroup').setValue(true);

		      } else {
		         // remove genus, species and number
	                 var ugenus = Ext.getCmp('ugenus');
                         var uspecies = Ext.getCmp('uspecies');
                         var unumber = Ext.getCmp('unumber');
                         var uncertainty = Ext.getCmp('uncertainty');
			 
			 if (ugenus != null) {
			     genusValue = ugenus.getValue();
			     speciesValue = uspecies.getValue();
			     numberValue = unumber.getValue();

			     panel.remove(ugenus);
			     panel.remove(uspecies);
			     panel.remove(unumber);
			     panel.remove(uncertainty);

			     ugenus.destroy();
			     uspecies.destroy();
			     unumber.destroy();
			     uncertainty.destroy();
			 }

			 var ugenus2 = Ext.getCmp('ugenus2');
			 if (ugenus2 != null) {
			    removeSpeciesUI2();
			 }

			 Ext.getCmp('addSpeciesButton').setVisible(false);

			 if (record.get('name') == 'Start' || record.get('name') == 'End' || record.get('name') == 'Blank') {   
			     // uncheck applytogroup
			     Ext.getCmp('applytogroup').setValue(false);
			 } else {
			     // check applytogroup
			     //Ext.getCmp('applytogroup').setValue(true);
			 }
	              }

	          }
		 
              }

	    });

            var speciesPanel =  new Ext.form.FormPanel({
	    		     	  id: 'speciesPanel',
	        	          labelWidth: 100,
                          	  //bodyStyle:'padding-top:20px',
                          	  width: 290,
                          	  labelPad: 4,
                          	  //defaultType: 'textfield',
			  	  hidden: true,
	                  	  border: false,
				  buttonAlign: 'center', 			  
			  	  id: 'speciesPanel',
                        	  //anchor:'-20',
                          	  //bodyStyle:'padding:10px 12px 4px;',
                                  bodyStyle:'padding:2px 12px 0px;',
                          	  defaults: {
                                      // applied to each contained item
                               	      width: 155,
                                      msgTarget: 'side'
                                  },
				  cls: 'speciesClass',
				  cellCls: 'cellClass',
                                  layoutConfig: {
                                      // layout-specific configs go here
                                      labelSeparator: ''
                                  },
                                  items: [ 
				     {
				         xtype: 'panel',
					 id: 'imgInfoPanel',
					 border: false,
					 width: 290,
					 html: '<table style="padding-top:5;">'+
					       '  <tr><td nowrap colspan=2 align=center><span class="x-form-item"><b>Image Details for  <span id="imgName" style="color:brown"></span></b></span></td></tr>'+
					       '  <tr><td nowrap><span class="x-form-item">Sequence Number:</span></td><td id="Trg" nowrap></td></tr>'+
					       '  <tr><td nowrap><span class="x-form-item">Flash:</span></td><td id="Flash" nowrap></td></tr>'+
					       //'  <tr><td nowrap><span class="x-form-item">Exposure Time:</span></td><td id="Exposure" nowrap></td></tr>'+
					       '  <tr><td nowrap><span class="x-form-item">Taken Time:</span></td><td id="TakenTime" nowrap></td></tr>'+
					       '  <tr><td nowrap><span class="x-form-item">Temperature:</span></td><td id="Tmp" nowrap></td></tr>'+
					       '  <tr><td nowrap colspan=2 height=1 style="background:lightblue url(hd-bg.gif)"></td></tr>'+
					       '</table>',
					       //'<hr noshadow size=1>',
                          	         bodyStyle:'padding:0px 0px 10px;'
				     },
				     typeCombo,
				     getGenusCombo(),
				     getSpeciesCombo(),
				     {
                                        fieldLabel: 'Number',
				        xtype: 'numberfield',
                                        name: 'unumber',
                                        id: 'unumber',
                                        allowBlank: false
				     },
				     getUncertaintyCombo(),
				     idPersonCombo
				     ,
				     {
				        xtype: 'checkbox',
				        checked: true,
                                        fieldLabel: '',
                                        labelSeparator: '',
                                        boxLabel: 'Apply to the Group',
                                        name: 'applytogroup',
					id: 'applytogroup'
				     }
                                ],

	                     buttons: [
			         new Ext.Button({
				        text: ' Add 2nd Species ',
					id: 'addSpeciesButton',
					hidden: false,
					handler : function() {
					     var btnText = Ext.getCmp('addSpeciesButton').getText();
					     if (btnText == ' Add 2nd Species ') {
					     	 speciesPanel.insert(6, getGenusCombo1());
					     	 speciesPanel.insert(7, getSpeciesCombo1());
					     	 speciesPanel.insert(8, {
                                             			   	   fieldLabel: 'Number 2',
				        			      	   xtype: 'numberfield',
                                        			      	   name: 'unumber2',
                                        			      	   id: 'unumber2',
                                        			      	   allowBlank: false
				    				      	 });
						 speciesPanel.insert(9, getUncertaintyCombo2());	
						 Ext.getCmp('addSpeciesButton').setText(' Delete 2nd Species ');
				             } else {
					         speciesPanel.remove(Ext.getCmp('ugenus2'));
					         speciesPanel.remove(Ext.getCmp('uspecies2'));
					         speciesPanel.remove(Ext.getCmp('unumber2'));
						 speciesPanel.remove(Ext.getCmp('uncertainty2'));
					         Ext.getCmp('addSpeciesButton').setText(' Add 2nd Species ');
					     }
					     speciesPanel.doLayout();	     
					}					
				     }),
                                 new Ext.Button({
				       text: ' Update ',
				       id: 'updateButton',
                                       cls: 'updateClass',
                                       handler : function() {
					   if (Ext.getCmp('speciesPanel').getForm().isValid()) {

						var wait = Ext.MessageBox.wait("Please wait. We are updating the data ...", "Status");
							
					        var ugenus = Ext.getCmp('ugenus') != null? Ext.getCmp('ugenus').getValue() : '';
					        var uspecies = Ext.getCmp('uspecies') != null ? Ext.getCmp('uspecies').getValue() : '';
						var unumber = Ext.getCmp('unumber') != null ? Ext.getCmp('unumber').getValue() : '';
						var uncertainty = Ext.getCmp('uncertainty') != null ? Ext.getCmp('uncertainty').getValue() : '';


					        var ugenus2 = Ext.getCmp('ugenus2') != null? Ext.getCmp('ugenus2').getValue() : '';
					        var uspecies2 = Ext.getCmp('uspecies2') != null ? Ext.getCmp('uspecies2').getValue() : '';
						var unumber2 = Ext.getCmp('unumber2') != null ? Ext.getCmp('unumber2').getValue() : '';
						var uncertainty2 = Ext.getCmp('uncertainty2') != null ? Ext.getCmp('uncertainty2').getValue() : '';

						var idPersonCombo = Ext.getCmp('idperson'); 
                                                var uperson = idPersonCombo.store.getAt(idPersonCombo.selectedIndex).get('id');	
                                                var upersonName = idPersonCombo.store.getAt(idPersonCombo.selectedIndex).get('name');	

						var applytogroup = Ext.getCmp('applytogroup').getValue();
						var typeVal = Ext.getCmp('typeCombo').getValue();

						// disable to set 'Start' and 'End' to a group
						if (applytogroup && (typeVal == 'Start'|| typeVal == 'End' )) {
						    msg("Error", "The type '"+typeVal+"' can not be applied to a group."); 
						    return;
						}

						if (applytogroup) {
                                                    var galleryStore = Ext.getCmp("gallery").store;
						    var annotatedNumber = 0;
						    var deletedImgs = "";
						    var systemDefault = null;
						    var galleryIndex = null;
						    for (var p=0; p<galleryStore.getTotalCount(); p++) {
                                                       var rec = galleryStore.getAt(p);
						       if (rec.get('annotated') && !rec.get('deleted')) {
						          annotatedNumber++;
							  if (rec.get('type') == 'Start' || rec.get('type') == 'End') {
							     systemDefault = rec.get('type');
							     galleryIndex = p;
							  }
						       }   

						       if (rec.get('deleted')) {
						          if (deletedImgs == "") {
							     deletedImgs = rec.get('name');
							  } else {
                                                             deletedImgs += ","+ rec.get('name');
							  }
						       }
						    }

						    if (annotatedNumber > 1) {
						        wait.hide();
                                                        if (confirm("Change the annotations of "+annotatedNumber+" images in the group?")) {
                                                             wait = Ext.MessageBox.wait("Please wait. We are updating the data ...", "Status");
							} else {
						             return;
							}
						    } else if (annotatedNumber == 1) {
						        wait.hide();
							if (systemDefault != null) {
							   if (confirm("Change the "+systemDefault+ " image setup by the system?")) {
                                                              galleryStore.getAt(p).set('type', '');
							      wait = Ext.MessageBox.wait("Please wait. We are updating the data ...", "Status");
							   } else {
							      return;
							   }
							} else if (confirm("Change the annotation of one image in the group?")) {
                                                             wait = Ext.MessageBox.wait("Please wait. We are updating the data ...", "Status");
							} else {
						             return;
							}
						    }

						} else {
						    				
						    wait.hide();
						    var galleryStore = Ext.getCmp("gallery").store;
						    var systemDefault = null;
						    var annotated = false;
						    var galleryIndex = null;
						    for (var p=0; p<galleryStore.getTotalCount(); p++) {
                                                       var rec = galleryStore.getAt(p);
						       if (rec.get('event') == sEvent && rec.get('trap') == sTrap && rec.get('name') == sRaw) {
						          if (rec.get('annotated')) annotated = true; 
							  if (rec.get('type') == 'Start' || rec.get('type') == 'End') {
							     systemDefault = rec.get('type');
							  }
							  galleryIndex = p;
							  break;
						       }   
						    }

						    if (annotated) {
						       if (systemDefault != null) {
						           if (confirm("Change the "+systemDefault+ " image setup by the system?")) {
							      galleryStore.getAt(p).set('type', '');
							      wait = Ext.MessageBox.wait("Please wait. We are updating the data ...", "Status");
							   } else {
							      return;
							   }
						       } else if (confirm("Change the annotation of "+sRaw+"?")) {
                                                             wait = Ext.MessageBox.wait("Please wait. We are updating the data ...", "Status");
						       } else {
						             return;
						       }
						          
						    }
						}

						$.getJSON(
                                                          "jsp/photo-species-update.jsp",
                                                          { 
							    site: siteName, 
							    trapId: sTrap, 
							    event: sEvent, 
							    raw: sRaw, 
							    'delete': deletedImgs,
							    type: typeVal,
							    genus: ugenus, 
							    species: uspecies, 
							    number: unumber,
							    uncertainty: uncertainty, 
							    genus2: ugenus2, 
							    species2: uspecies2, 
							    number2: unumber2, 
							    uncertainty2: uncertainty2, 
							    personid: uperson,
							    applytogroup: applytogroup
							  },
                                                          function(j) {
							     if (j.success) {
						   
								 // update the data grid and tree nodes
								 var cTab = Ext.getCmp('photodatagrid');
								 var cStore = cTab.getStore();

	                                                         var root = Ext.getCmp("repository").getRootNode();    
                                                                 var eventNode = root.findChild('text', sEvent);
                                                                 var arrayNode = eventNode.findChild('text', sArray);
                                                                 var trapNode = arrayNode.findChild('text', sTrap);

								 if (j.remove) {
								    // update the data grid
								    for (var p=0; p<cStore.getTotalCount(); p++) {
                                                                        var rec = cStore.getAt(p);
									if (rec == null) continue;
                                                                        if (rec.get('Sampling_Period') == j.remove.event &&
                                                                            rec.get('Camera_Trap_Point_ID') == j.remove.trap &&
                                                                            rec.get('Raw_Filename') == j.remove.raw) {
                                                                            rec.set('Identified_By', null);
                                                                            rec.set('Type', null);

                                                                            break;
                                                                        }
                                                                    }

								    // update the tree nodes
								    var rawNode = trapNode.findChild('text', j.remove.raw);
								    rawNode.getUI().removeClass("annotated");

								    // update the gallery
							            var galleryStore = Ext.getCmp("gallery").store;
						                    for (var p=0; p<galleryStore.getTotalCount(); p++) {
                                                                        var rec = galleryStore.getAt(p);
						                        if (rec.get('name') == j.remove.raw) {
						          		    rec.set('annotated', false);
									    break;
						                        }   
						                    }	    

								 }

								 for (var k=0; k<j.update.length; k++) {

								    // update the data grid
								    for (var p=0; p<cStore.getTotalCount(); p++) {
								        var rec = cStore.getAt(p);
									if (rec == null) continue;
								        if (rec.get('Sampling_Period') == j.update[k].event &&
									    rec.get('Camera_Trap_Point_ID') == j.update[k].trap &&
                                                                            rec.get('Raw_Filename') == j.update[k].raw) {
									    
								            rec.set('Genus', ugenus);
								            rec.set('Species', uspecies);
								            rec.set('Binomial', ugenus+" "+uspecies);
								            rec.set('Number_of_Animals', unumber );
								            rec.set('Identified_By', upersonName);
									    rec.set('Type', typeVal);

									    break;    
									}
								    }

								    // update the tree nodes
                                                                    var rawNode = trapNode.findChild('text', j.update[k].raw);
								    rawNode.getUI().addClass("annotated");
								  
								    // update the gallery
							            var galleryStore = Ext.getCmp("gallery").store;
						                    for (var p=0; p<galleryStore.getTotalCount(); p++) {
                                                                        var rec = galleryStore.getAt(p);
						                        if (rec.get('name') == j.update[k].raw) {
						          		    rec.set('annotated', true);
									    break;
						                        }   
						                    }	    
  
								 }

								 // set status
								 setStatus();

                                                                 wait.hide();
							    } else {
							         msg("Error", j.text);
							    }
							  }
					         );  // getJSON	
			                    } // if    				
				       } // handler
                                 })  // new Ext.Button
			    ] 
                     });

            
              var i = document.createElement('img');
              i.src = 'ext-3.0.0/resources/images/default/s.gif';
              i.width = 616;
              i.height = Math.max(315, docHeight-370);
              i.id = 'imageview';
    
	      var p = new Ext.ux.PanPanel({
                 frame: true,
                 border: false,
                 client: i,
                 height: Math.max(315, docHeight-370),
                 width: 616,
                 collapsible: false,
		 region: 'center',
		 id: 'panpanel',
                 //floating: true,
                 //x: 0, y: 0,
 	         listeners: {
                 }
            });	    


	    var imageControlPanel = new Ext.Panel({
                 width: 35,
		 height: 330,
		 border: true,
		 region: 'east',
		 frame: true,
		 html: "<a href='javascript:zoomin()'><img src='images/Zoom-In-32x32.png' width=26 height=26 style='padding-bottom:4pt;'></a><br><a href='javascript:zoomout()'><img src='images/Zoom-Out-32x32.png' width=26 height=26></a>"
            });

	    var q = new Ext.Panel({
	        layout:'border',
		border: false,
		items: [ p, imageControlPanel ]
	    });


    	    var xd = Ext.data;
    	    var store = new Ext.data.JsonStore({
               url: 'jsp/get-images.jsp',
               root: 'images',
               fields: ['name',
	                'event',
			'trap', 
	                'url', 
			{name:'size', type: 'float'}, 
			{name:'lastmod', type:'date', dateFormat:'timestamp'}, 
			'annotated',
			'type',
			'deleted'],
	       listeners: {
	            load: function() {
	                 var gal = Ext.getCmp("gallery");
			 if (gal != null) gal.select(0, false, false);
		    }
	       }
            });
            store.load();

            var tpl = new Ext.XTemplate(
    	    	         '<tpl for=".">',
            		 '<div class="thumb-wrap {deleted}Img" id="{name}">',
	    	      	 '<div class="thumb"><img src="{url}" title="{name}" width=100 height=100></div>',
		      	        '<span class="x-editable">{shortName}</span></div>',
        		 '</tpl>',
        		 '<div class="x-clear"></div>'
			 );

            var galleryPanel = new Ext.Panel({
                id:'images-view',
                frame:true,
		width:520,
		height:330,
                minWidth:520,
		minHeight:330,
                //autoHeight:true,
                collapsible:false,
                layout:'border',

                items: new Ext.DataView({
		   id: 'gallery',
                   store: store,
                   tpl: tpl,
		   region:'center',
		   height: 330,
                   autoScroll : true,
                   singleSelect: true,
                   //multiSelect: true,
                   overClass:'x-view-over',
                   itemSelector:'div.thumb-wrap',
                   emptyText: '<div style="padding:10px;">No images to display</div>',

		   /*
                   plugins: [
                      new Ext.DataView.DragSelector(),
                      new Ext.DataView.LabelEditor({dataIndex: 'name'})
                   ],
		   */

                   prepareData: function(data){
                      data.shortName = Ext.util.Format.ellipsis(data.name, 15);
                      data.sizeString = Ext.util.Format.fileSize(data.size);
                      data.dateString = data.lastmod.format("m/d/Y g:i a");
                      return data;
                   },
                   listeners: {
		       /*
		       afterrender: {
                          fn: function() {
                              new Ext.KeyMap(document, 
		   	     	        [{
					   key: [8,46],
                                           fn: function() {
					       // check weather group viewer is enabled
                                               var panel = Ext.getCmp("displayPanel");
                                               if (panel.layout.activeItem.id == 'images-view') {					  
					            // check weather there is a selected images
						    var selNodes = this.getSelectedNodes();
						    if (selNodes.length > 0) {
						        var selNode = this.getSelectedNodes()[0];
							mask(selNode);
						    } 
					       } 
                                           },
                                           scope: this
                                        },{
					   key: " rR",
					   stopEvent:true,
                                           fn: function() {
					       // check weather group viewer is enabled
                                               var panel = Ext.getCmp("displayPanel");
                                               if (panel.layout.activeItem.id == 'images-view') {
					           reverseMasks();
					       }  
					   },
					   scope: this
                                        },{
					   key: "aA",
					   stopEvent:true,
                                           fn: function() {
					       // check weather group viewer is enabled
                                               var panel = Ext.getCmp("displayPanel");
                                               if (panel.layout.activeItem.id == 'images-view') {
					           maskAnnotatedPhotos();
					       }  
					   },
					   scope: this
					}]);
			   }
		       },
		       */
		       contextmenu: {
		          fn: function(dataview, index, node, e) {
                              e.preventDefault();
                              var coords = e.getXY();
			      var deleted = getMask(node);

                              var galleryMenu = new Ext.menu.Menu();

			      if (!deleted) { 
                                  galleryMenu.add(
                                      new Ext.menu.Item({
						       text: 'Mask This Photo',
						       handler: function() {
						          mask(node);  
						       }
						    })
                                  );
			      } else {
                                  galleryMenu.add(
                                      new Ext.menu.Item({
						       text: 'Unmask This Photo',
						       handler: function() {
						          mask(node);  
						       }
						    })
                                  );
			      }

			      // add Reverse Menu Item
                              galleryMenu.add(
                                  new Ext.menu.Item({
						       text: 'Reverse All Masks',
					               handler: reverseMasks
						   })
			      );			   

			      // add Mask Annotated Photos Menu Item
                              galleryMenu.add(
                                  new Ext.menu.Item({
						       text: 'Mask Annotated Photos Only',
					               handler: maskAnnotatedPhotos
						   })
			      );			   

			      galleryMenu.showAt([coords[0], coords[1]]);
			  }
		       },
                       dblclick: {
		          fn: function() {
                              var selNode = this.getSelectedNodes()[0];

			      // load this image into the viewer
			      var path = "/"+siteName+"/"+sEvent+"/"+sArray+"/"+sTrap+"/"+selNode.id;
			      var img = document.getElementById("imageview");
		              img.src="image-repository"+escape(path);
			      zoomreset();

			      sRaw = selNode.id;  
			    
			      // switch to the single image viewer
			      var panel = Ext.getCmp("displayPanel");
			      panel.layout.setActiveItem(0);

		              Ext.getCmp('singleButton').setText('<b>Single</b>');
		              Ext.getCmp('groupButton').setText('Group');

			      // refresh the title
                              var iPane  = Ext.getCmp('ImageViewer');
			      iPane.setText(path.replace(/\//g, " : "));

	                      highlightTreeNode(sEvent, sArray, sTrap, sRaw);
                              highlightDatagridRecord();

			      // refresh species panel
		              $.getJSON(
                                        "jsp/photo-species-set.jsp",
                                        { site: siteName, path: path},
                                        function(j) {
					   $('#imgName').html(sRaw);
					   $('#Trg').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.trg+"</span>");
					   $('#Flash').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.flash+"</span>");
					   //$('#Exposure').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.exposure+"</span>");
                                           $('#TakenTime').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.takentime+"</span>");
					   $('#Tmp').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.temperature+"</span>");

				           if (j.success) {
	      					if (j.type == 'Animal' || j.type == 'unknown') {
						    var ugenus = Ext.getCmp('ugenus');
						    if (ugenus != null) {
						        // reset 
						    } else {
						        insertSpeciesUI();     
						    }

						    if (j.type == 'Animal') {
						        Ext.getCmp('typeCombo').setValue(j.type);
		    				        Ext.getCmp('ugenus').setValue(j.genus);
                    					Ext.getCmp('uspecies').setValue(j.species);
							Ext.getCmp('unumber').setValue(j.number);
						    } else {
                                                        Ext.getCmp('typeCombo').setValue('Animal');
						    }
		    						    
						    Ext.getCmp('ugenus').clearInvalid(); 
						    Ext.getCmp('uspecies').clearInvalid();
                    				    Ext.getCmp('unumber').clearInvalid();

						} else {
						    Ext.getCmp('typeCombo').setValue(j.type);

						    // remove species panel   
						    removeSpeciesUI();
						}

						var idperson = Ext.getCmp('idperson');
		    				idperson.setValue(j.idperson);
                    				idperson.clearInvalid();

						var k = 0;
                                                idPersonCombo.store.each( function(record) {
						   if (record.get('name') == j.idperson) {
						       idperson.selectedIndex = k;
						   }	
						   k++;
						});

					   } else {

					        // not annotated yet
                                                Ext.getCmp('typeCombo').setValue('Animal');			     
			                        var ugenus = Ext.getCmp('ugenus');
						if (ugenus == null) insertSpeciesUI();     

		    				Ext.getCmp('ugenus').reset();
                    				Ext.getCmp('ugenus').clearInvalid();

		    				Ext.getCmp('uspecies').reset();
 					        Ext.getCmp('uspecies').clearInvalid();

		    				Ext.getCmp('unumber').reset();
                    				Ext.getCmp('unumber').clearInvalid();

		    				//Ext.getCmp('idperson').reset();
                    				//Ext.getCmp('idperson').clearInvalid();
					   }
				      }
			        );	
			      
			  }
		       }
                  }
              })
            });


	    var displayPanel = new Ext.Panel({
	           id: 'displayPanel',
                   //title: 'Image',
                   collapsible: false,
                   region:'center',
                   margins: '2 5 0 0',
                   autoScroll : true, 
                   layout: 'card',
                   activeItem: 0,   
		   border: false,
		   //columnWidth: .65,
		   width: 700,
		   //height:330,
                   height: Math.max(330, docHeight-350),	   
                   items: [
		            q,
			    galleryPanel
                          ]
	    });

            var imagePanel = new Ext.Panel({
	           id: 'ImagePanel',
                   //title: 'Image',
                   collapsible: false,
                   region:'center',
                   margins: '2 5 0 0',
                   autoScroll : true, 
                   //layout: 'column',
		   layout: 'table',
		   layoutConfig: {
                      columns: 2
                   },
		   border: true,
		   extraCls: 'imgClass',
		   tbar: [{
		   	     xtype: 'tbtext',
			     text: 'Image Viewer',
			     id: 'ImageViewer'
		         },'->',{
			     xtype: 'tbbutton',
                             text: '<img src="ext-3.0.0/resources/images/default/grid/page-prev.gif">',
			     tooltip: 'previous image',
			     handler: function() {

	                              var root = Ext.getCmp("repository").getRootNode();
                                      var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
				      if (selectedNode != null) {
			     	          var elements = selectedNode.attributes.path.split("/");
				          if (elements.length > 5) {
					      var fEvent = elements[2];
					      var fArray = elements[3];
					      var fTrap = elements[4];
					      var fRaw = elements[5];
					      					      
				              $.getJSON(
                                                 "jsp/find-previous-image.jsp",
                                                 { event: fEvent, trap: fTrap, raw: fRaw},
                                                 function(j) {
				                     if (j.success) {
					                 sEvent = j.img_event;
						         sArray = j.img_array;
						         sTrap = j.img_trap;
						         sRaw = j.img_raw;

					                 path = "/"+siteName+"/"+j.img_event+"/"+j.img_array+"/"+j.img_trap+"/"+j.img_raw;
						         Ext.getCmp('repository').selectPath(path,
                                                                                             'text',
                                                                                             function(b, c){
	  								                        if ($('#repository').offset().top < 85) {
          								                            $('#repository').offset({left:4,top:85});
									                        }
									                        highlightTreeNode();
									                      });
									  
				                          // refresh single view
                                                          var iPane  = Ext.getCmp('ImageViewer');
		                                          iPane.setText(path.replace(/\//g, " : "));

		                                          var img = document.getElementById("imageview");
		                                          img.src="/deskTEAM/image-repository"+escape(path)
		                                          zoomreset();

					                  // refresh group view
						          refreshGroupViewer(path);
					      
					                  // refresh species panel
						          refreshSpeciesPanel(j.img_type,
								              j.img_genus,
								              j.img_species,
								              j.img_number,
								              j.img_identified_by,
								              j.trg,
								              j.flash,
								              j.exposure,
								              j.temperature,
									      j.takentime);

					                 // highlight the record on the datagrid view
                                                         highlightDatagridRecord();

					             }
					         })
				            }
			              }

			     }
                         },{
		             xtype: 'tbbutton',
			     text: '<b>Single</b>',
			     id: 'singleButton',
			     handler: function() {
			         var panel = Ext.getCmp("displayPanel");
			         panel.layout.setActiveItem(0);

				 Ext.getCmp('singleButton').setText('<b>Single</b>');
				 Ext.getCmp('groupButton').setText('Group');
			     }
			 },{
			     xtype: 'tbbutton',
                             text: '<img src="ext-3.0.0/resources/images/default/grid/page-next.gif">',
			     tooltip: 'next image',
			     handler: function() {

	                              var root = Ext.getCmp("repository").getRootNode();
                                      var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
				      if (selectedNode != null) {
			     	          var elements = selectedNode.attributes.path.split("/");
				          if (elements.length > 5) {
					      var fEvent = elements[2];
					      var fArray = elements[3];
					      var fTrap = elements[4];
					      var fRaw = elements[5];
					      					      
				              $.getJSON(
                                                 "jsp/find-next-image.jsp",
                                                 { event: fEvent, trap: fTrap, raw: fRaw},
                                                 function(j) {
				                     if (j.success) {
					                 sEvent = j.img_event;
						         sArray = j.img_array;
						         sTrap = j.img_trap;
						         sRaw = j.img_raw;

					                 path = "/"+siteName+"/"+j.img_event+"/"+j.img_array+"/"+j.img_trap+"/"+j.img_raw;
						         Ext.getCmp('repository').selectPath(path,
                                                                                             'text',
                                                                                             function(b, c){
	  								                        if ($('#repository').offset().top < 85) {
          								                            $('#repository').offset({left:4,top:85});
									                        }
									                        highlightTreeNode();
									                      });
									  
				                          // refresh single view
                                                          var iPane  = Ext.getCmp('ImageViewer');
		                                          iPane.setText(path.replace(/\//g, " : "));

		                                          var img = document.getElementById("imageview");
		                                          img.src="/deskTEAM/image-repository"+escape(path)
		                                          zoomreset();

					                  // refresh group view
						          refreshGroupViewer(path);
					      
					                  // refresh species panel
						          refreshSpeciesPanel(j.img_type,
								              j.img_genus,
								              j.img_species,
								              j.img_number,
								              j.img_identified_by,
								              j.trg,
								              j.flash,
								              j.exposure,
								              j.temperature,
									      j.takentime);

					                 // highlight the record on the datagrid view
                                                         highlightDatagridRecord();

					             }
					         })
				            }
			              }

			     }
			 },'-',{
			     xtype: 'tbbutton',
                             text: '<img src="ext-3.0.0/resources/images/default/grid/page-prev.gif">',
			     tooltip: 'previous<br>group',
			     handler: function() {

			          // jump to the previous group
                                  var galleryStore = Ext.getCmp("gallery").store;
				  var record = galleryStore.getAt(galleryStore.getTotalCount()-1);
				  if (record != null) {
				      var imgName = record.get('name');
				      var path = "/"+siteName+"/"+sEvent+"/"+sArray+"/"+sTrap+"/"+imgName;

				      $.getJSON(
                                          "jsp/find-previous-group.jsp",
                                          { event: sEvent, trap: sTrap, raw: imgName},
                                          function(j) {
				             if (j.success) {
					         sEvent = j.img_event;
						 sArray = j.img_array;
						 sTrap = j.img_trap;
						 sRaw = j.img_raw;

					         path = "/"+siteName+"/"+j.img_event+"/"+j.img_array+"/"+j.img_trap+"/"+j.img_raw;
						 Ext.getCmp('repository').selectPath(path,
                                                                          'text',
                                                                          function(b, c){
	  								      if ($('#repository').offset().top < 85) {
          								          $('#repository').offset({left:4,top:85});
									      }
									      highlightTreeNode();
									  });
									  

				                 // refresh single view
                                                 var iPane  = Ext.getCmp('ImageViewer');
		                                 iPane.setText(path.replace(/\//g, " : "));

		                                 var img = document.getElementById("imageview");
		                                 img.src="/deskTEAM/image-repository"+escape(path)
		                                 zoomreset();

					         // refresh group view
						 refreshGroupViewer(path);
					      
					         // refresh species panel
						 refreshSpeciesPanel(j.img_type,
								     j.img_genus,
								     j.img_species,
								     j.img_number,
								     j.img_identified_by,
								     j.trg,
								     j.flash,
								     j.exposure,
								     j.temperature,
							             j.takentime);

					         // highlight the record on the datagrid view
                                                 highlightDatagridRecord();
				             }
				          }
                                      );


				  }

			     }
			 },{ 
		             xtype: 'tbbutton',
			     text: 'Group',
			     id: 'groupButton',
                             handler: function() {
			         var panel = Ext.getCmp("displayPanel");
			         panel.layout.setActiveItem(1);

				 Ext.getCmp('singleButton').setText('Single');
				 Ext.getCmp('groupButton').setText('<b>Group</b>');
                             }
			 },{
			     xtype: 'tbbutton',
                             text: '<img src="ext-3.0.0/resources/images/default/grid/page-next.gif">',
			     handler: function() {
                              	  // jump to the next group
                                  var galleryStore = Ext.getCmp("gallery").store;
				  var record = galleryStore.getAt(galleryStore.getTotalCount()-1);
				  if (record != null) {
				      var imgName = record.get('name');
				      var path = "/"+siteName+"/"+sEvent+"/"+sArray+"/"+sTrap+"/"+imgName;

				      $.getJSON(
                                          "jsp/find-next-group.jsp",
                                          { event: sEvent, trap: sTrap, raw: imgName},
                                          function(j) {
				             if (j.success) {
					         sEvent = j.img_event;
						 sArray = j.img_array;
						 sTrap = j.img_trap;
						 sRaw = j.img_raw;

					         path = "/"+siteName+"/"+j.img_event+"/"+j.img_array+"/"+j.img_trap+"/"+j.img_raw;
						 Ext.getCmp('repository').selectPath(path,
                                                                          'text',
                                                                          function(b, c)
{	  								      if ($('#repository').offset().top < 85) {
          								          $('#repository').offset({left:4,top:85});
									      }
									      highlightTreeNode();
									  });

				                 // refresh single view
                                                 var iPane  = Ext.getCmp('ImageViewer');
		                                 iPane.setText(path.replace(/\//g, " : "));

		                                 var img = document.getElementById("imageview");
		                                 img.src="/deskTEAM/image-repository"+escape(path)
		                                 zoomreset();

					         // refresh group view
						 refreshGroupViewer(path);
					      
					         // refresh species panel
						 refreshSpeciesPanel(j.img_type,
								     j.img_genus,
								     j.img_species,
								     j.img_number,
								     j.img_identified_by,
								     j.trg,
								     j.flash,
								     j.exposure,
								     j.temperature,
							             j.takentime);

					         // highlight the record on the datagrid view
                                                 highlightDatagridRecord();
				             }
				          }
                                      );
				  }
                             }
			 }],
	           items: [ 
                            displayPanel,
		            speciesPanel
		          ]
	    });

	    // create data panel
            var dataPanel = new Ext.Panel({
                             //title: 'Data Forms',
			     collapsible: false,
                             region: 'south',
			     id: 'dataholder',
			     layout: 'fit',
                             height: 232,
                             minSize: 75,
                             maxSize: 250,
			     border: false,
			     frame: true,
			     items: [
                                       createCameraTrappingExcelGridPane2(siteName,
                                                                          'Camera Trapping',
                                                                          'std',
                                                                          "std.xls",
                                                                          -1)
			     ]
                             //margins: '0 5 5 5'
                         });


            var tvPanel = new Ext.Panel({
                title: 'Terrestrial Vertebrate',
		id: 'tvPanel',
		items: [ repositoryPane ] 
	    });                           

	    /*                                                                                                  
            var vgPanel = new Ext.Panel({
                title: 'Vegetation - Tree & Liana'
            });   
	    */

	    var vgPanel = new Ext.Panel({
                id: 'vgPanel',
                title: 'Search Result',
                hidden: true,
                listeners: {
                    expand : function(p) {
                        var searchTreePanel = Ext.getCmp('searchResult');
                        if (searchTreePanel != null) {
                           searchTreePanel.getSelectionModel().clearSelections();
                        }
                    }
                },
                bbar: [
                           {
                              xtype: 'button',
                              text:  'Back to <%= site.getName() %>',
                              pressed: true,
                              handler: function() {
                                 document.forms[0].submit();
                              }
                           }
                        ]
            });
     

            var panel = new Ext.Panel({
                 //title:    'DeskTEAM - '+siteName+' - Copyright 2009 Team Network',
                 id:       'deskTEAM',
                 layout:   'border',
                 width:    '100%',
                 //height:   650,
		 height: Math.max(docHeight-31, 650),
		 defaults: {
	                     collapsible: false,
                             split: true,
                             bodyStyle: 'padding:0px'
                          },
                 //renderTo: Ext.getBody(),
                 renderTo: 'deskTEAMDiv',
		 tbar: ['&nbsp;',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Project</b>',
			    id: 'projectMenu',
			    menu: {
			        items:[{ 
                                         text: '<b>New Project</b>',
                                         iconCls: 'icon_project_add',
                                         handler: function() {
					     document.location.href="init-setup.jsp?action=1";
					 }
					},{ 
                                         text: '<b>Edit Project</b>',
                                         iconCls: 'icon_project_edit',
                                         handler: function() {
					     document.location.href="init-setup.jsp?pid="+siteId;
					 }
				       }, '-'

<%
     for (int i=0; i<sites.size(); i++) {
         Site tmpSite = sites.get(i);
	 out.println(",{");
	 out.println("     text: '"+tmpSite.getName()+"',");
         out.println("     iconCls: 'icon_project',");
         out.println("     handler: function() {");
	 out.println("		document.location.href='index.jsp?siteId="+tmpSite.getId()+"';");
	 out.println("	   }");	 
	 out.println("}");
     }

%>
				       ] 
			    }
			}),
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Data Processing</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>Import from Memory Card</b>',
					 iconCls: 'icon_new_camera_data',
					 handler: loadMemoryCard
				       },{
                                         text: '<b>Import from Folder</b>',
                                         iconCls: 'icon_package_add',
					 handler: loadFolder
				       },{
                                         text: '<b>Import from TPK File</b>',
                                         iconCls: 'icon_package_add',
					 handler: loadTeamPackage 
				       },{
                                         text: '<b>Report Damage</b>',
					 iconCls: 'icon_camera_error',
					 handler: reportDamage 
				       },'-',{
                                         text: '<b>Delete</b>',
					 iconCls: 'icon_delete',
					 handler: function() {
					     	      var root = Ext.getCmp("repository").getRootNode();
                                                      var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();

						      if (selectedNode == null) {
						          msg("Error", "No photo collection is selected."); 
						      } else if (selectedNode.isLeaf()) {
						          msg("Error", "Can't delete a photo. Please select a photo collection.");
						      } else if (selectedNode.getPath('text') == root.getPath('text')) {
						          msg("Error", "Can't delete the entire repository.");
						      } else {

                                                          var elements = selectedNode.getPath('text').split("/");
					                  var dEvent = elements[2];
							  var dArray = elements.length > 3 ? elements[3] : '';
					                  var dTrap = elements.length > 4 ? elements[4] : '';

						          Ext.MessageBox.confirm("Confirm",
                                                               "<span style='white-space:nowrap;'>Are you sure to remove the collection</span>"+
							       " <span style='white-space:nowrap;'>"+selectedNode.getPath('text')+"?</span>",
                                                               function (btn) {
                                                                   if (btn == 'yes') {	         
								        var waitBox = Ext.MessageBox.wait("Please wait. We are deleteing the photos.", 
								       	   	     		          "Status");
   
									$.getJSON( "jsp/delete-photos.jsp",
                                                                                   { siteId: siteId,
										     event: dEvent,  
										     array: dArray, 
										     trap: dTrap},
                                                          			   function(j) {
											waitBox.hide();

						             			        if (j.success) {
        										    var currentRepositoryPath = null;
											    var sTrap = null;
											    var sEvent = null;
											    var sArray = null;
											    var selectedRow = null;
											    
											    // reset tree
											    //root.getOwnerTree().getLoader().load(root);
											    //root.getOwnerTree().getLoader().load(selectedNode.parentNode);

                                                                 			    var node = root.findChild('text', dEvent);
								 
											    if (dArray != null && dArray != '') {
                                                                    			        node = node.findChild('text', dArray);
								                            }

								 			    if (dTrap != null && dTrap != '') {
                                                                    			        node = node.findChild('text', dTrap);
								 			    }	

								 			    node.remove();
											    
											    // reset single viewer
                                                                                            var iPane  = Ext.getCmp('ImageViewer');
						                                            iPane.setText('Image Viewer');

					        					    var img = document.getElementById("imageview");
											    img.src="ext-3.0.0/resources/images/default/s.gif";
											    zoomreset();
     
											    // reset group viewer
											    Ext.getCmp("gallery").store.removeAll();

											    // reset species panel
											    Ext.getCmp('speciesPanel').hide();

											    // reset datagrid
											    Ext.getCmp('trapdatagrid').store.removeAll();

											    var photogrid = Ext.getCmp('photodatagrid');
											    if (photogrid != null) photogrid.store.removeAll();

											    // reset the trap combobox on the import panel
		                                                                            var trapBox  = Ext.getCmp('trapId');
											    if (trapBox != null) {
		                                                                                trapBox.clearValue();
		                                                                                trapBox.clearInvalid();
		                                                                                trapBox.store.removeAll();
		                                                                                trapBox.store.load({ params: 
			                                                                                                  {'event': Ext.getCmp('event').getValue() }
			                                                                                          });
		                                                                                trapBox.enable();
										            }

											    trapBox  = Ext.getCmp('trapIdF');							     
											    if (trapBox != null) {
		                                                                                trapBox.clearValue();
		                                                                                trapBox.clearInvalid();
		                                                                                trapBox.store.removeAll();
		                                                                                trapBox.store.load({ params: 
			                                                                                                  {'event': Ext.getCmp('eventF').getValue() }
			                                                                                          });
		                                                                                trapBox.enable();
										             }

											     // set status
											     setStatus();

											 } else {
											    msg("Error", j.message);
											 }
										    });


                              					    }
                          				       });
						      	    }
						       }  
				       }] 
			    }
			}),
			/*
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Edit Data</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>Delete</b>',
					 iconCls: 'icon_delete',
					 handler: function() {
					     	      var root = Ext.getCmp("repository").getRootNode();
                                                      var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();

						      if (selectedNode == null) {
						          msg("Error", "No photo collection is selected."); 
						      } else if (selectedNode.isLeaf()) {
						          msg("Error", "Can't delete a photo. Please select a photo collection.");
						      } else if (selectedNode.getPath('text') == root.getPath('text')) {
						          msg("Error", "Can't delete the entire repository.");
						      } else {

                                                          var elements = selectedNode.getPath('text').split("/");
					                  var dEvent = elements[2];
							  var dArray = elements.length > 3 ? elements[3] : '';
					                  var dTrap = elements.length > 4 ? elements[4] : '';

						          Ext.MessageBox.confirm("Confirm",
                                                               "<span style='white-space:nowrap;'>Are you sure to remove the collection</span>"+
							       " <span style='white-space:nowrap;'>"+selectedNode.getPath('text')+"?</span>",
                                                               function (btn) {
                                                                   if (btn == 'yes') {	         
								        var waitBox = Ext.MessageBox.wait("Please wait. We are deleteing the photos.", 
								       	   	     		          "Status");
   
									$.getJSON( "jsp/delete-photos.jsp",
                                                                                   { siteId: siteId,
										     event: dEvent,  
										     array: dArray, 
										     trap: dTrap},
                                                          			   function(j) {
											waitBox.hide();

						             			        if (j.success) {
        										    var currentRepositoryPath = null;
											    var sTrap = null;
											    var sEvent = null;
											    var sArray = null;
											    var selectedRow = null;
											    
											    // reset tree
											    //root.getOwnerTree().getLoader().load(root);
											    //root.getOwnerTree().getLoader().load(selectedNode.parentNode);

                                                                 			    var node = root.findChild('text', dEvent);
								 
											    if (dArray != null && dArray != '') {
                                                                    			        node = node.findChild('text', dArray);
								                            }

								 			    if (dTrap != null && dTrap != '') {
                                                                    			        node = node.findChild('text', dTrap);
								 			    }	

								 			    node.remove();
											    
											    // reset single viewer
                                                                                            var iPane  = Ext.getCmp('ImageViewer');
						                                            iPane.setText('Image Viewer');

					        					    var img = document.getElementById("imageview");
											    img.src="ext-3.0.0/resources/images/default/s.gif";
											    zoomreset();
     
											    // reset group viewer
											    Ext.getCmp("gallery").store.removeAll();

											    // reset species panel
											    Ext.getCmp('speciesPanel').hide();

											    // reset datagrid
											    Ext.getCmp('trapdatagrid').store.removeAll();

											    var photogrid = Ext.getCmp('photodatagrid');
											    if (photogrid != null) photogrid.store.removeAll();

											    // reset the trap combobox on the import panel
		                                                                            var trapBox  = Ext.getCmp('trapId');
											    if (trapBox != null) {
		                                                                                trapBox.clearValue();
		                                                                                trapBox.clearInvalid();
		                                                                                trapBox.store.removeAll();
		                                                                                trapBox.store.load({ params: 
			                                                                                                  {'event': event }
			                                                                                          });
		                                                                                trapBox.enable();
										             }

											     // set status
											     setStatus();

											 } else {
											    msg("Error", j.message);
											 }
										    });


                              					    }
                          				       });
						      }
					 }  
				       }, {
                                         text: '<b>Search</b>',
					 iconCls: 'icon_search',
					 handler: underConstruction
				       }, '-' , {
                                         text: '<b>Configure</b>',
                                         iconCls: 'icon_pref',
                                         handler: function() {
					     document.location.href="init-setup.jsp"
					 }
				       }, '-' , {
                                         text: '<b>Preferences</b>',
                                         iconCls: 'icon_pref',
                                         handler: editPreferences
                                       }] 
			    }
			}),
			*/
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Export Data</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>Export to TPK File</b>',
                                         iconCls: 'icon_export_to_file',
					 handler: exportToFile 
				       }, {
                                         text: '<b>Export to CSV File</b>',
                                         iconCls: 'icon_export_to_file',
					 handler: exportToCSV
				       }, '-', {
                                         text: '<b>Validate tpk File</b>',
                                         iconCls: 'icon_package_add',
					 handler: validateTeamPackage 
				       }] 
			    }
			}),
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Public Repository</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>Export</b>',
                                         iconCls: 'icon_export_to_team',
					 handler: exportToTeam  
				       }, {
                                         text: '<b>Delete</b>',
                                         iconCls: 'icon_export_to_team',
					 handler: deleteFromTeam  
				       }] 
			    }
			}),	
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Search</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>Search</b>',
                                         iconCls: 'icon_search',
					 handler: onSearch
				       }] 
			    }
			}),
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Setting</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>Preferences</b>',
                                         iconCls: 'icon_pref',
                                         handler: editPreferences
				       }] 
			    }
			}),
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Update DeskTEAM</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>via Internet</b>',
                                         iconCls: 'icon_update_via_internet',
					 handler: updateViaInternet
					 /*
					 handler: function() {
					     msg("Status", "No updates are available.");
					 }
					 */
				       }] 
			    }
			}),
			'-',
			new Ext.Toolbar.SplitButton({
            		    text: '<b>Help</b>',
			    menu: {
			        items:[{ 
                                         text: '<b>Help Contents</b>',
                                         iconCls: 'icon_help',
					 handler: underConstruction 
				       },'-',{
                                         text: '<b>About DeskTEAM</b>',
                                         iconCls: 'icon_about',
					 handler: aboutDeskTEAM 
				       }] 
			    }
			})
		 ],
		 bbar: [
		          '&nbsp;',
                           {
			      text: 'Status:'
			   }, 
                           {
			      text: '',
			      id: 'statusText'
			   }, 
			   '->',
                           {
                              text: '',
                              id: 'totalStatusText'
                           }

		 ],
                 items:    [{
                               region:'center',
                               layout:'border',
                               frame:false,
                               border:false,
			       items: [
			           imagePanel, dataPanel
			       ]
                            },{
			       id: 'leftPanel',
                               region:'west',
                               layout:'accordion',
                               frame:false,
                               border:true,
                               width:245,
                               //width:225,		       
                               margins: '2 0 2 2',
                               cmargins: '5 0 0 0',
                               cls: 'padding 5',
			       minSize: 240,
			       maxSize: 240,
                               split:true,
                               collapsible:false,
                               //collapseMode:'mini',
			       items: [
			         tvPanel, vgPanel
			       ]
                           }]
            });

	    panel.hide();
	    setTimeout("while ($('#repository').offset().top != 85) $('#repository').offset({left:4,top:85}); ", 2000);
            panel.show();
	
	    Ext.EventManager.onWindowResize(function() {
	        Ext.getCmp('deskTEAM').doLayout();
	    });

	    setStatus();
    
            var selectionModel = root.getOwnerTree().getSelectionModel()
	    selectionModel.on("selectionchange", 
	    		      function(m, n) {
			         syncGUI();
			      }); 

        }); //end onReady


	    
	function highlightTreeNode() {

          // highlight the correspondent node in the tree presentation
	  var root = Ext.getCmp("repository").getRootNode();
          var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();

          var eventNode = root.findChild('text', sEvent);
          var arrayNode = eventNode.findChild('text', sArray);
          var trapNode = arrayNode.findChild('text', sTrap);
          var rawNode = trapNode.findChild('text', sRaw);

          var oldTop = $('#repository').offset().top;
          var oldLeft = $('#repository').offset().left;

          var scrollDiv = $('#repository').children().children()[0];
          var oldScrollTop = scrollDiv.scrollTop;

          Ext.getCmp('repository').selectPath(rawNode.getPath());
          rawNode.ensureVisible();

          var newTop = $('#repository').offset().top;
          var newLeft = $('#repository').offset().left;
          var newScrollTop = scrollDiv.scrollTop;

          $('#repository').offset({left:oldLeft,top:oldTop});
          if (oldScrollTop > newScrollTop) {
              // move up
              scrollDiv.scrollTop = Math.max(0, scrollDiv.scrollTop-200);
          } else if (oldScrollTop < newScrollTop) {
              // move down
              scrollDiv.scrollTop += 400;
          }

        }


        function zoomin() {
	    var img = document.getElementById("imageview");
            img.width = Math.min(1200, img.width+200);
            img.height = Math.min(1000, img.height+300);
        }


        function zoomout() {
	    var img = document.getElementById("imageview");
            img.width = Math.max(616, img.width-200);
            img.height = Math.max(docHeight-370, Math.max(315, img.height-300));

	    Ext.getCmp('panpanel').body.dom.scrollLeft = 0;
	    Ext.getCmp('panpanel').body.dom.scrollTop = 0;
        }


	function zoomreset() {
	    var img = document.getElementById("imageview");
            img.width = 616;
            img.height = Math.max(315, docHeight-370);	 
	}


        function getDocHeight() {
            var D = document;
            return Math.max(
               Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
               Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
               Math.max(D.body.clientHeight, D.documentElement.clientHeight)
            );
        }


	function insertSpeciesUI() {
            // create UI for species
            var panel = Ext.getCmp("speciesPanel");
	    panel.insert(2, getUncertaintyCombo());
            panel.insert(2, { fieldLabel: 'Number',
                              xtype: 'numberfield',
                              name: 'unumber',
                              id: 'unumber',
                              allowBlank: false 
                            });

            panel.insert(2, getSpeciesCombo());
            panel.insert(2, getGenusCombo());
	    Ext.getCmp('addSpeciesButton').setVisible(true);
            Ext.getCmp('addSpeciesButton').setText(' Add 2nd Species ');

	    panel.doLayout();
	}


	function insertSpeciesUI2() {
            // create UI for species
            var panel = Ext.getCmp("speciesPanel");
	    panel.insert(6, getUncertaintyCombo2());	
            panel.insert(6, { fieldLabel: 'Number 2',
                              xtype: 'numberfield',
                              name: 'unumber2',
                              id: 'unumber2',
                              allowBlank: false 
                             });
            panel.insert(6, getSpeciesCombo1());
            panel.insert(6, getGenusCombo1());
	    Ext.getCmp('addSpeciesButton').setVisible(true);
	    Ext.getCmp('addSpeciesButton').setText(' Delete 2nd Species ');

	    panel.doLayout();

	}


	function removeSpeciesUI() {
            // remove UI for species
            var panel = Ext.getCmp("speciesPanel");

            var ugenus = Ext.getCmp('ugenus');
            var uspecies = Ext.getCmp('uspecies');
            var unumber = Ext.getCmp('unumber');
            var uncertainty = Ext.getCmp('uncertainty');


            if (ugenus != null) {
	        panel.remove(ugenus);
	        panel.remove(uspecies);
		panel.remove(unumber);
		panel.remove(uncertainty);

	        ugenus.destroy();
	        uspecies.destroy();
	        unumber.destroy();
		uncertainty.destroy();
	    }

            panel.doLayout();
	}


	function removeSpeciesUI2() {
            // remove UI for species
            var panel = Ext.getCmp("speciesPanel");

            var ugenus2 = Ext.getCmp('ugenus2');
            var uspecies2 = Ext.getCmp('uspecies2');
            var unumber2 = Ext.getCmp('unumber2');
            var uncertainty2 = Ext.getCmp('uncertainty2');

            if (ugenus2 != null) {
	        panel.remove(ugenus2);
	        panel.remove(uspecies2);
		panel.remove(unumber2);
		panel.remove(uncertainty2);

	        ugenus2.destroy();
	        uspecies2.destroy();
	        unumber2.destroy();
		uncertainty2.destroy();
	    }

	    Ext.getCmp('addSpeciesButton').setVisible(false);
            Ext.getCmp('addSpeciesButton').setText(' Add 2nd Species ');
            panel.doLayout();
	}


        function highlightDatagridRecord() {
             
            var photoDataGrid = Ext.getCmp('photodatagrid');
            if (photoDataGrid != null) {
                 var selectionModel = photoDataGrid.getSelectionModel();

                 var found = false;
                 for (var i=0; i<photoDataGrid.store.getTotalCount(); i++) {
	             var record = photoDataGrid.store.getAt(i);
	             if (typeof(record)=="undefined" || record == null) continue;

	             if (record.get('Sampling_Period') == sEvent &&
	                 record.get('Camera_Trap_Point_ID') == sTrap &&
               	         record.get('Raw_Filename') == sRaw) {
             	           selectionModel.selectRow(i);
             	           photoDataGrid.getView().focusRow(i);

             	           // adjust a bit
             	           try {
		             if (selectedRow == null) {
		                 if (i > 5) photoDataGrid.getView().focusRow(i+1);
		             } else if (selectedRow < i) {
		                 photoDataGrid.getView().focusRow(i+1);
		             } else if (selectedRow > i) {
		                 photoDataGrid.getView().focusRow(i-1);
		             }
             	          } catch(e) {}

             	          selectedRow = i;
             	          found = true;
             	          break;
	             }					   
                 }

                 if (!found && selectedRow != null) {
	              try {
	                  selectionModel.deselectRow(selectedRow);
             	      } catch (e) {}
                 }

             }
	}


	function refreshSpeciesPanel(photoType, 
		 		     genus, 
				     species, 
				     number, 
				     identified_by, 
				     trg, 
				     flash, 
				     exposure, 
				     temperature, 
				     takentime) {

	    var sPane  = Ext.getCmp('speciesPanel');
            sPane.setVisible(true);

	    $('#imgName').html(sRaw);
	    $('#Trg').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+trg+"</span>");
	    $('#Flash').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+flash+"</span>");
	    //$('#Exposure').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+exposure+"</span>");
            $('#TakenTime').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+takentime+"</span>");
	    $('#Tmp').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+temperature+"</span>");

	    if (photoType == '' || photoType == 'Animal') {
                 var ugenus = Ext.getCmp('ugenus');
                 if (ugenus == null) {
		     insertSpeciesUI();
                 } 
		 Ext.getCmp('addSpeciesButton').setVisible(true);
	         Ext.getCmp('addSpeciesButton').setText(' Add 2nd Species ');

                 if (photoType == 'Animal') {
                      Ext.getCmp('typeCombo').setValue('Animal');
                      Ext.getCmp('ugenus').setValue(genus);
                      Ext.getCmp('uspecies').setValue(species);
                      Ext.getCmp('unumber').setValue(number);
                      Ext.getCmp('idperson').setValue(identified_by);
                 } else {
                      Ext.getCmp('typeCombo').setValue('Animal');
                      Ext.getCmp('ugenus').reset();
                      Ext.getCmp('uspecies').reset();
                      Ext.getCmp('unumber').reset();
                      //Ext.getCmp('idperson').reset();
                 }

                 Ext.getCmp('ugenus').clearInvalid();
                 Ext.getCmp('uspecies').clearInvalid();
                 Ext.getCmp('unumber').clearInvalid();
                 Ext.getCmp('idperson').clearInvalid();
            } else {
                 Ext.getCmp('typeCombo').setValue(photoType);
                 Ext.getCmp('idperson').setValue(identified_by);
                 Ext.getCmp('idperson').clearInvalid();
                 // remove species panel   
                 removeSpeciesUI();
		 removeSpeciesUI2();
	    }
	}

	function refreshSpeciesPanel2(genus2, species2, number2) {
               var ugenus2 = Ext.getCmp('ugenus2');
               if (ugenus2 == null) {
		   insertSpeciesUI2();
               } 
               
	       Ext.getCmp('ugenus2').setValue(genus2);
               Ext.getCmp('uspecies2').setValue(species2);
               Ext.getCmp('unumber2').setValue(number2);
	       Ext.getCmp('addSpeciesButton').setVisible(true);
	       Ext.getCmp('addSpeciesButton').setText(' Delete 2nd Species ');
	} 


	function refreshGroupViewer(path) {

	    if (!inGallery(sEvent, sTrap, sRaw)) {
	        var galleryStore = Ext.getCmp("gallery").store;
                galleryStore.load.defer(100, 
			                galleryStore,
			                [{
					   params:{  
					       trapName:sTrap,
					       event:sEvent,
					       raw:sRaw,
					       //array:sArray,
					       path:escape(path)
					   }
			                }]);
            }

	}								 



	function getGenusCombo() {

            var genusStore = new Ext.data.JsonStore({
                  proxy: new Ext.data.HttpProxy({
	               url : "jsp/genus.jsp"
                  }),
                  root : "results",
                  fields: ['name']
            });

            var genusCombo = new Ext.form.ComboBox({
                store: genusStore,
                fieldLabel: 'Genus',
                name: 'ugenus',
		id: 'ugenus',
                displayField:'name',
                typeAhead: false,
                loadingText: 'Loading...',
                selectOnFocus:true,
                width: 180,
                hideTrigger:true,
                minChars: 1,
                allowBlank: false,
                editable : true
             });

	     return genusCombo;
	}


	function getGenusCombo1() {

            var genusStore = new Ext.data.JsonStore({
                  proxy: new Ext.data.HttpProxy({
	               url : "jsp/genus.jsp"
                  }),
                  root : "results",
                  fields: ['name']
            });

            var genusCombo = new Ext.form.ComboBox({
                store: genusStore,
                fieldLabel: 'Genus 2',
                name: 'ugenus2',
		id: 'ugenus2',
                displayField:'name',
                typeAhead: false,
                loadingText: 'Loading...',
                selectOnFocus:true,
                width: 180,
                hideTrigger:true,
                minChars: 1,
                allowBlank: false,
                editable : true
             });

	     return genusCombo;
	}


	function getSpeciesCombo() {

            var speciesStore = new Ext.data.JsonStore({
                  proxy: new Ext.data.HttpProxy({
                       url : "jsp/species.jsp"
                  }),
                  root : "results",
                  fields: ['name']
            });

             var speciesCombo = new Ext.form.ComboBox({
                store: speciesStore,
                fieldLabel: 'Species',
                name: 'uspecies',
                id: 'uspecies',
                displayField:'name',
                typeAhead: false,
                loadingText: 'Loading...',
                selectOnFocus:true,
                width: 180,
                hideTrigger:true,
                minChars: 1,
                allowBlank: false,
                editable : true,
                listeners: {
                   beforequery : function(x) {
                       x.combo.store.proxy.conn.url = "jsp/species.jsp?genus="+Ext.getCmp('ugenus').value;
                   }
                }
            });

	    return speciesCombo;
	    
	}


	function getUncertaintyCombo() {

            var uncertaintyStore = new Ext.data.SimpleStore({
                                   fields: ['name'],
                                   data : [
                                             ['Absolutely sure'],
                                             ['Pretty sure'],
					     ['Not sure'],
                                             ['Do not know']
                                          ]
                             });

            var uncertaintyCombo = new Ext.form.ComboBox({
	      id: 'uncertainty',
	      name: 'uncertainty',
              store: uncertaintyStore,
              displayField: 'name',
              typeAhead: true,
              mode: 'local',
              forceSelection: true,
              triggerAction: 'all',
              emptyText: 'Select a value ...',
              selectOnFocus: true,
	      allowBlank: false,
	      value: 'Absolutely sure',
              editable: false,
	      fieldLabel: 'Uncertainty'
            });

	    return uncertaintyCombo;

	}


	function getUncertaintyCombo2() {

            var uncertaintyStore = new Ext.data.SimpleStore({
                                   fields: ['name'],
                                   data : [
                                             ['Absolutely sure'],
                                             ['Pretty sure'],
					     ['Not sure'],
                                             ['Do not know']
                                          ]
                             });

            var uncertaintyCombo = new Ext.form.ComboBox({
	      id: 'uncertainty2',
	      name: 'uncertainty2',
              store: uncertaintyStore,
              displayField: 'name',
              typeAhead: true,
              mode: 'local',
              forceSelection: true,
              triggerAction: 'all',
              emptyText: 'Select a value ...',
              selectOnFocus: true,
	      allowBlank: false,
	      value: 'Absolutely sure',
              editable: false,
	      fieldLabel: 'Uncertainty2'
            });

	    return uncertaintyCombo;

	}



	function getSpeciesCombo1() {

            var speciesStore = new Ext.data.JsonStore({
                  proxy: new Ext.data.HttpProxy({
                       url : "jsp/species.jsp"
                  }),
                  root : "results",
                  fields: ['name']
            });

             var speciesCombo = new Ext.form.ComboBox({
                store: speciesStore,
                fieldLabel: 'Species 2',
                name: 'uspecies2',
                id: 'uspecies2',
                displayField:'name',
                typeAhead: false,
                loadingText: 'Loading...',
                selectOnFocus:true,
                width: 180,
                hideTrigger:true,
                minChars: 1,
                allowBlank: false,
                editable : true,
                listeners: {
                   beforequery : function(x) {
                       x.combo.store.proxy.conn.url = "jsp/species.jsp?genus="+Ext.getCmp('ugenus2').value;
                   }
                }
            });

	    return speciesCombo;
	    
	}


	function inGallery(event, trap, raw) {

	    try {
                var galleryStore = Ext.getCmp("gallery").store;
	        var found = false;
	        for (var p=0; p<galleryStore.getTotalCount(); p++) {
                    var rec = galleryStore.getAt(p);
	            if (rec.get('name') == raw && rec.get('event') == event && rec.get('trap') == trap) {
		       found = true
		       break;
		    }   
                }	    
	        return found;
	    } catch (e) {
	        return false;
	    }
	}


	function reverseMasks() {
             var galleryStore = Ext.getCmp("gallery").store;
             for (var p=0; p<galleryStore.getTotalCount(); p++) {
                 var rec = galleryStore.getAt(p);
                 rec.set('deleted', !rec.get('deleted'));
             }
        }


	function mask(selNode) {
             var galleryStore = Ext.getCmp("gallery").store;
	     for (var p=0; p<galleryStore.getTotalCount(); p++) {
                 var rec = galleryStore.getAt(p);
		 if (rec.get('name') == selNode.id) {
		     if (rec.get('deleted')) {
		         rec.set('deleted', false);
		     } else {
                         rec.set('deleted', true);
		     }
		     break;
	         }   
	      }
         }


	function getMask(selNode) {
             var galleryStore = Ext.getCmp("gallery").store;
	     for (var p=0; p<galleryStore.getTotalCount(); p++) {
                 var rec = galleryStore.getAt(p);
		 if (rec.get('name') == selNode.id) {
		     return rec.get('deleted');
		     break;
	         }   
	     }
	     return null;
        }


	function maskAnnotatedPhotos() {
             var galleryStore = Ext.getCmp("gallery").store;
             for (var p=0; p<galleryStore.getTotalCount(); p++) {
                 var rec = galleryStore.getAt(p);
		 if (rec.get('annotated')) {
                    rec.set('deleted', true);
		 } else {
                    rec.set('deleted', false);
		 }
             }
        }


	function syncGUI() {

           var root = Ext.getCmp("repository").getRootNode();
           var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
	   if (selectedNode == null) return;
           var elements = selectedNode.getPath('text').split("/");
					                  
           var fEvent = elements.length > 2 ? elements[2] : null;
           var fArray = elements.length > 3 ? elements[3] : null;
           var fTrap = elements.length > 4 ? elements[4] : null;
           var fRaw = elements.length > 5 ? elements[5] : null;
          
           $.getJSON(
               "jsp/find-image.jsp",
               { event: fEvent, trap: fTrap, raw: fRaw},
               function(j) {
                  if (j.success) {
                      sEvent = j.img_event;
		      sArray = j.img_array;
		      sTrap = j.img_trap;
		      sRaw = j.img_raw;

 		      path = "/"+siteName+"/"+j.img_event+"/"+j.img_array+"/"+j.img_trap+"/"+j.img_raw;
									  
                      // refresh single view
                      var iPane  = Ext.getCmp('ImageViewer');
                      iPane.setText(path.replace(/\//g, " : "));

                      var img = document.getElementById("imageview");
                      img.src="/deskTEAM/image-repository"+escape(path)
                      zoomreset();

                      // refresh group view
                      refreshGroupViewer(path);		    
                      highlightPhotoInGroup(sRaw)
  
                      // refresh species panel
                      refreshSpeciesPanel(j.img_type,
		                          j.img_genus,
		                          j.img_species,
		                          j.img_number,
		                          j.img_identified_by,
		                          j.trg,
		                          j.flash,
		                          j.exposure,
		                          j.temperature,
				          j.takentime);

		      if (j.img_genus2) {
		          refreshSpeciesPanel2(j.img_genus2,
					       j.img_species2,
		                               j.img_number2);
		      }	else {
		         removeSpeciesUI2();
			 if (j.img_type == '' || j.img_type == 'Animal') {
	    		     Ext.getCmp('addSpeciesButton').setVisible(true);
            		     Ext.getCmp('addSpeciesButton').setText(' Add 2nd Species ');
			 }
		      }	  

		      // highlight the record on the datagrid view
                      highlightDatagridRecord();
                      selectedNode.ui.getAnchor().focus();

                    }
               });

	}


	function setStatus() {
            $.getJSON(
                      "jsp/status.jsp?siteId="+siteId,
                      null,
                      function(j) {
     		          if (j.success) {
     	                      Ext.getCmp('statusText').setText("total "+j.total+" images, "+j.annotated+" processed");
			  }
		      });
	}


	function highlightPhotoInGroup(name) {

             var galleryStore = Ext.getCmp("gallery").store;
	     var galleryIndex = null;
	     for (var p=0; p<galleryStore.getTotalCount(); p++) {
                 var rec = galleryStore.getAt(p);
	         if (rec && rec.get('name') == name) {
		     galleryIndex = p;
		     break;
	         }   
	     }
	     if (galleryIndex != null) {
	         Ext.getCmp("gallery").select(galleryIndex, false, false);
	     }
 
       }


       function exportToFile() {

	   var root = Ext.getCmp("repository").getRootNode();
           var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
           if (selectedNode == null || selectedNode.isLeaf() || selectedNode.getPath('text') == root.getPath('text')) {
		msg("Error", "Please select a sampling event or an array to export data.");
	   } else {
                var elements = selectedNode.getPath('text').split("/");
		var dEvent = elements[2];
		var dArray = elements.length > 3 ? elements[3] : '';

                if (elements.length > 4) {
		    msg("Error", "Please select a sampling event or an array to export data.");
		} else {	       
		    // validate the selected photo collection
		    var waitBox = Ext.MessageBox.wait("Please wait. We are checking all export conditions are satisfied.", 
						      "Status");
		    $.getJSON( "jsp/export-validate.jsp",
                               { event: dEvent, array: dArray, siteId: siteId },
                               function(j) {
			           waitBox.hide();

				   if (j.valid) {
				       showExportDirWindow(dEvent, dArray, j.size, true);
				   } else {				       
				       var errorWin = getExportErrorWindow(dEvent, dArray, j.size);
				       errorWin.show();
				   }

				   /*
                                   var fileWin = Ext.getCmp("fileWin");
                                   if (fileWin == null) {
				       fileWin = createFileWindow();
				   }
                                   fileWin.show();
				   Ext.getCmp('exportEvent').setValue(dEvent);	
				   Ext.getCmp('exportArray').setValue(dArray);	
                                   Ext.getCmp('packageSize').setValue(j.size);	
	                           $('#packageSize').html(j.size+" Mb");			   

                                   var fileRoot = Ext.getCmp("filetree").getRootNode();			   				   
                                   fileRoot.expand(false, 
				   	           true,
						   function() {
				   		       var desktop = fileRoot.findChild('text', 'Desktop');
				                       desktop.select();
						   });
				    */		   
			       });
	        }	

	  }
   }


   function exportToCSV() {

	   var root = Ext.getCmp("repository").getRootNode();
           var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
           if (selectedNode == null || selectedNode.isLeaf() || selectedNode.getPath('text') == root.getPath('text')) {
		msg("Error", "Please select a sampling event or an array to export data.");
	   } else {
                var elements = selectedNode.getPath('text').split("/");
		var dEvent = elements[2];
		var dArray = elements.length > 3 ? elements[3] : '';

                if (elements.length > 4) {
		    msg("Error", "Please select a sampling event or an array to export data.");
		} else {
		    showExportCSVDirWindow(dEvent, dArray);
	        }	

	  }
   }


   
   //////////////////////////////////////////////////////
   //
   // handle the uploading action
   //
   //////////////////////////////////////////////////////

   var uploadingUsr = <%= session.getAttribute("username") != null ? "'"+session.getAttribute("username")+"'" : null %>; 
   var uploadingPwd = <%= session.getAttribute("password") != null ? "'"+session.getAttribute("password")+"'" : null %>; 

   function exportToTeam() {

       var root = Ext.getCmp("repository").getRootNode();
       var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
       if (selectedNode == null || selectedNode.isLeaf() || selectedNode.getPath('text') == root.getPath('text')) {
	   msg("Error", "Please select a camera trap to export data.");
       } else {
           var elements = selectedNode.getPath('text').split("/");
           var dEvent = elements[2];
	   var dArray = elements.length > 3 ? elements[3] : '';
	   var dTrap = elements.length > 4 ? elements[4] : '';

           if (elements.length != 5) {
		msg("Error", "Please select a camera trap to export data.");
           } else {	
	        if (uploadingUsr == null || uploadingPwd == null) {
		    authenticate(dTrap, dEvent);
		} else {
		    requestUploadJob(dTrap, dEvent);
		}
 
	   }
       }

   }


   function deleteFromTeam() {

       var root = Ext.getCmp("repository").getRootNode();
       var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
       if (selectedNode == null || selectedNode.isLeaf() || selectedNode.getPath('text') == root.getPath('text')) {
	   msg("Error", "Please select a camera trap to export data.");
       } else {
           var elements = selectedNode.getPath('text').split("/");
           var dEvent = elements[2];
	   var dArray = elements.length > 3 ? elements[3] : '';
	   var dTrap = elements.length > 4 ? elements[4] : '';

           if (elements.length != 5) {
		msg("Error", "Please select a camera trap to export data.");
           } else {	
	        if (uploadingUsr == null || uploadingPwd == null) {
		    authenticateForDelete(dTrap, dEvent);
		} else {
		    requestDeleteJob(dTrap, dEvent);
		}
 
	   }
       }

   }


   function authenticate(dTrap, dEvent) {

   	var loginForm = 
	    	 new Ext.FormPanel({
			labelWidth:80,
		        bodyStyle:'padding:15px',
			url:'jsp/login.jsp', 
        		frame:true, 
        		title:'Please Login to The Public Repository', 
        		defaultType:'textfield',
			monitorValid:true,
 			items:[{ 
     				 fieldLabel:'Username', 
                		 name:'loginUsername', 
				 id: 'loginUsername', 
                		 allowBlank:false 
            		       },{ 
				 fieldLabel:'Password', 
                		 name:'loginPassword',
				 id: 'loginPassword',
				 inputType:'password', 
                		 allowBlank:false 
            		      }],
			 buttons:[{ 
                	  	 text:'Login',
		                 formBind: true,	 
                                 handler:function(){ 
				     loginForm.getForm().submit({ 
					 method:'POST', 
					 waitTitle:'Connecting', 
                        		 waitMsg:'Sending data...',
					 success: function(){ 
						      uploadingUsr = Ext.getCmp('loginUsername').getValue();
						      uploadingPwd = Ext.getCmp('loginPassword').getValue();
					 	      Ext.getCmp('loginWin').destroy();
						      requestUploadJob(dTrap, dEvent);
                        			   },
					  failure: function(form, action) { 
					  	       if (action.failureType == undefined) {
                                                          Ext.Msg.alert('Login Failed!', 'Can not login'); 
						       } else {
                            			       	  if (action.failureType == 'server'){
                                			      Ext.Msg.alert('Warning!', 'Authentication server is unreachable'); 
                            			       	  } else {  
                                                              Ext.Msg.alert('Login Failed!', action.result.reason); 
                            			          }
						       } 
						    } 
				      });
				    }   		    
				 },{
				      text     : 'Close',
				      id       : 'closeLogin',
                    		      handler  : function(){
                         			     Ext.getCmp('loginWin').destroy();
			                         }
				 }]
		      });     		 
		
           var win = new Ext.Window({
    	                  id: 'loginWin', 
        	          layout:'fit',
			  width:300,
			  height:160,
			  closable: false,
			  resizable: false,
			  plain: true,
        		  border: false,
        		  items: [loginForm]										
                      });
           win.show();	

   }


   function requestUploadJob(dTrap, dEvent) {
   	   
	   $.getJSON(
                      "jsp/upload-request-job.jsp",
                      { trap: dTrap, event: dEvent, username: uploadingUsr, password: uploadingPwd},
		      function(j) {
		           if (j.success) {
			       if (j.type == 'old') {
			           Ext.Msg.confirm('Confirm', 'The previous uploading paused. Continue it?', 
				   	           function(btn, text){
      						       if (btn == 'yes'){
        					           runUploadJob(dTrap, dEvent);
      						       } else {
        					           // do nothing
      						       }
    						   });			    
			       } else if (j.type == 'finished') {
				   Ext.MessageBox.show({
						         title: 'Message',
							 msg: 'The data was already uploaded.',
						         width:300,
					                 modal: true,
						         icon: Ext.Msg.INFO,
						         buttons: Ext.Msg.OK
                                                      }); 
			       } else {
			           runUploadJob(dTrap, dEvent);
			       }
			   } else {
				Ext.MessageBox.show({
						       title: 'ERROR',
						       msg: j.reason,
						       width: 400,
					               modal: true,
						       icon: Ext.Msg.ERROR,
						       buttons: Ext.Msg.OK
                                                      }); 
			        //Ext.Msg.alert('Error', j.reason); 
			        if (j.right) {
				    uploadingUsr = null;
				    uploadingPwd = null;
				}
			   }
		      });

   }

   function runUploadJob(dTrap, dEvent) {

          var root = Ext.getCmp("repository").getRootNode();
          var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();

          /*
	  try {
	      selectedNode1.setIcon('/deskTEAM/images/icons/folder_go.png');
	  } catch (e) {}
	  */

	  var pNode = selectedNode.parentNode;
	  pNode.reload(function() {
                            var trapNode = pNode.findChild('text', dTrap);
			    //trapNode.expand(false, true);	    
	  	       });   	  

            // Start a task to get the progress
           var pwin = new Ext.Window({
    	                  id: 'uploadProgressWin', 
        	          layout: 'border',
		          modal: true,
			  width:400,
			  height:130,
			  margins: '10 10 10 10',
			  border: true,
			  closable: false,
			  resizable: false,
			  plain: true,
        		  border: false,
			  title: 'Upload Status',
        		  items: [{
			  	     xtype: 'label',
				     id: 'uploadingLabel',
			  	     html: '<b>Starting ...</b>',
				     frame: true,
			             margins: '10 10 10 10',
				     region: 'center'
			  	  },{
				     xtype: 'progress',
                       	   	     id: 'uploadProgress',
                    	   	     text: 'Uploading',
                    	   	     width: 300,  
			             margins: '10 10 10 10',
				     region: 'south',
                    	   	     hidden: false	
			  	 }],
           	           buttons: [{
                           	       text : 'Pause',
				       id: 'pwinPause',
                         	       handler  : function(){
				       		      var btnText = Ext.getCmp('pwinPause').getText();
						      if (btnText == 'Pause') {
	   			       		           $.getJSON(
							              "jsp/upload-pause-job.jsp",
                      						      { 
								         trap: dTrap, 
								         event: dEvent, 
								         username: uploadingUsr, 
								         password: uploadingPwd
								      },
		      						      function(j) {
		           					          if (j.success) {
								              Ext.getCmp('pwinPause').setText('Continue');
								          }
								      });
						       } else {
						           pwin.destroy();
						           runUploadJob(dTrap, dEvent);	   
						       }
                         			   }
			   	     },{
                           	       text : 'Close',
				       disabled: true,
				       id: 'pwinClose',
                         	       handler  : function(){
                            	       		      pwin.destroy();
						      runner.stop(task);
                         			  }
                     		     }]										
                      });
            pwin.show();	
	    var pbar = Ext.getCmp("uploadProgress");

            var task = {
                          run: function(){
			          Ext.Ajax.request({
					 url: 'jsp/upload-status.jsp',
					 success: function(response, options) {
						    if (response) {
							 try {
							      var json = Ext.util.JSON.decode(response.responseText);
							 } catch (err) { return; }

							 pbar = Ext.getCmp("uploadProgress");
							 if (pbar == null) runner.stop(task);

							 if (json.failed) {         
					                     pbar.wait({interval: 100, //bar will move fast!
							                increment: 15,
							                text: 'Uploading...'
							              });	 
							     return;
						         }

							 if (!json.done) {
								pbar.updateProgress(json.percentage,
										    json.strPercent);
								Ext.getCmp('uploadingLabel').setText('Uploading '+json.fileName+'');
							 } else { 
							        pbar.updateProgress(1, 'Done'); 
								var plabel = Ext.getCmp('uploadingLabel');
								if (plabel != null) plabel.setText('All images were uploaded.');
							 }
						     }
					  },
					  params: { 
						     trap: dTrap,
						     event: dEvent
						  }
				    });
                           },
                           interval: 300
                        };
            var runner = new Ext.util.TaskRunner();
            runner.start(task);

	   // upload images and data
	   $.getJSON(
                      "jsp/upload-run-job.jsp",
                      { trap: dTrap, event: dEvent, username: uploadingUsr, password: uploadingPwd},
		      function(j) {
		           if (j.success) {
			       if (j.done == 'paused') {
			           //pbar.updateProgress(1, 'Paused'); 
			           var plabel = Ext.getCmp('uploadingLabel');
			           if (plabel != null) plabel.setText('Uploading is paused.');
			       } else {
				   pbar = Ext.getCmp("uploadProgress");
				   if (pbar != null) {
			               pbar.updateProgress(1, 'Done'); 
				   }
			           var plabel = Ext.getCmp('uploadingLabel');
			           if (plabel != null) 
				      plabel.setText('All images and data for '+dTrap+' and event '+dEvent+' are uploaded.');
				   Ext.getCmp('pwinPause').setDisabled(true);

				   pNode.reload(function() {        
				                      var trapNode = pNode.findChild('text', dTrap);
			    			      trapNode.expand(false, true);	    
						});   	  

				   /*
				   try {
				       selectedNode1.setIcon('/deskTEAM/images/icons/database_save.png');
				   } catch (e) {}
				   */
			       }
                               runner.stop(task);
			       Ext.getCmp('pwinClose').setDisabled(false);
			   } else {
			       runner.stop(task);
			       pwin.destroy();
			       Ext.MessageBox.show({
						       title: 'ERROR',
						       msg: j.reason,
						       width: 400,
					               modal: true,
						       icon: Ext.Msg.ERROR,
						       buttons: Ext.Msg.OK
                                                   }); 
			   }
			   
		      });


   }


   function authenticateForDelete(dTrap, dEvent) {

   	var loginForm = 
	    	 new Ext.FormPanel({
			labelWidth:80,
		        bodyStyle:'padding:15px',
			url:'jsp/login.jsp', 
        		frame:true, 
        		title:'Please Login to The Public Repository', 
        		defaultType:'textfield',
			monitorValid:true,
 			items:[{ 
     				 fieldLabel:'Username', 
                		 name:'loginUsername', 
				 id: 'loginUsername', 
                		 allowBlank:false 
            		       },{ 
				 fieldLabel:'Password', 
                		 name:'loginPassword',
				 id: 'loginPassword',
				 inputType:'password', 
                		 allowBlank:false 
            		      }],
			 buttons:[{ 
                	  	 text:'Login',
		                 formBind: true,	 
                                 handler:function(){ 
				     loginForm.getForm().submit({ 
					 method:'POST', 
					 waitTitle:'Connecting', 
                        		 waitMsg:'Sending data...',
					 success: function(){ 
						      uploadingUsr = Ext.getCmp('loginUsername').getValue();
						      uploadingPwd = Ext.getCmp('loginPassword').getValue();
					 	      Ext.getCmp('loginWin').destroy();
						      requestDeleteJob(dTrap, dEvent);
                        			   },
					  failure: function(form, action) { 
					  	       if (action.failureType == undefined) {
                                                          Ext.Msg.alert('Login Failed!', 'Can not login'); 
						       } else {
                            			       	  if (action.failureType == 'server'){
                                			      Ext.Msg.alert('Warning!', 'Authentication server is unreachable'); 
                            			       	  } else {  
                                                              Ext.Msg.alert('Login Failed!', action.result.reason); 
                            			          }
						       } 
						    } 
				      });
				    }   		    
				 },{
				      text     : 'Close',
				      id       : 'closeLogin',
                    		      handler  : function(){
                         			     Ext.getCmp('loginWin').destroy();
			                         }
				 }]
		      });     		 
		
           var win = new Ext.Window({
    	                  id: 'loginWin', 
        	          layout:'fit',
			  width:300,
			  height:160,
			  closable: false,
			  resizable: false,
			  plain: true,
        		  border: false,
        		  items: [loginForm]										
                      });
           win.show();	

   }


   function requestDeleteJob(dTrap, dEvent) {
   	   
       var wait = Ext.MessageBox.wait("Please wait. We are deleting the data ...", "Status");

       $.getJSON(
                  "jsp/upload-delete-job.jsp",
                  { trap: dTrap, event: dEvent, username: uploadingUsr, password: uploadingPwd},
		    function(j) {
		       wait.hide();
		       if (j.success) {
		           if (j.action) {
			         Ext.MessageBox.show({
						title: 'Status',
						msg: "The data was removed from the public repository",
					        width: 400,
					        modal: true,
						icon: Ext.Msg.INFO,
						buttons: Ext.Msg.OK
                                              });

          		          var root = Ext.getCmp("repository").getRootNode();
          			  var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
	                          var pNode = selectedNode.parentNode;
	                          pNode.reload(function() {
                                      var trapNode = pNode.findChild('text', dTrap);
			              //trapNode.expand(false, true);	    
	  	                  });   	  

                           } else {
			         Ext.MessageBox.show({
						title: 'Status',
						msg: "Nothing needs to be removed.",
					        width: 400,
					        modal: true,
						icon: Ext.Msg.INFO,
						buttons: Ext.Msg.OK
                                              });
			   } 			        
		       } else {
			   Ext.MessageBox.show({
						title: 'ERROR',
						msg: j.reason,
					        width: 400,
					        modal: true,
						icon: Ext.Msg.ERROR,
						buttons: Ext.Msg.OK
                                              }); 
		       }
		});

   }



   //////////////////////////////////////////////////////
   //
   // end of handling the uploading action
   //
   //////////////////////////////////////////////////////


   function editPreferences(){

      var fileWin = Ext.getCmp("pfileWin");
      if (fileWin != null) {
         fileWin.destroy();
	 fileWin = null;
      }      

      if (fileWin == null) {
          var root = new Ext.tree.AsyncTreeNode({
	    id:'0',
	    path: homepath,
            text: homepath.substring(1)
	  });

          // create tree panel
          var data = new Ext.tree.TreeLoader({
	    url:'jsp/get-directory.jsp?'}); 

          data.on('beforeload',
            function(treeLoader,node){
		this.baseParams.text=node.attributes.text;  
		this.baseParams.path = node.getPath("text");
	    },
	    data); 

         var fileTreePanel = new Ext.tree.TreePanel({
	    id: 'pfiletree',
	    region:'west',
	    collapsible: true,
	    margins: '5 0 0 5',
	    cmargins: '5 0 0 0',
	    cls: 'padding 5',
	    width: 340,
	    height: 235,
	    //minSize: 440,
	    //maxSize: 240,
	    root: root,
	    loader: data,
	    autoScroll : true,
	    fitToFrame:true,
	    //rootVisible: false,
	    border: false
	});

        var fileWin = new Ext.Window({
	             id          : 'pfileWin',
        	     title       : 'Select a directory as the default working directory',
		     layout      : 'table',
		     layoutConfig: {
		                     columns: 1
	                           },
        	     width       : 360,
        	     height      : 350,
        	     closeAction :'hide',
        	     plain       : true,
		     modal       : true,
        	     items       : [ 
			             fileTreePanel,
				     {
					 xtype: 'label',
		                         html: "<div style='padding:10pt;font-size:8pt;'>The current default directory is "+datapath+"</div>",
					 frame: true
				     }
				   ],
		     bbar        : [
				    '->',
                                     {
		                       xtype: 'button',
                                       text:  'Setup',
				       pressed: true,
				       handler: function() {
					     fileWin.hide();					     
					     var root = Ext.getCmp("pfiletree").getRootNode();
					     var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
					     var exportDir = selectedNode.getPath('text');
					     //alert(exportDir);

					     $.getJSON( "jsp/set-default-dir.jsp",
                                                        { path: exportDir },
						        function(j) {
							   datapath = j.path;
							   msg('Status', 'The default working directory is set to '+datapath);
							} 
					      );
				       }
		                     },{
					text: '&nbsp;'
				     },{
		                       xtype: 'button',
                                       text:  'Cancel',
				       pressed: true,
				       handler: function() {
					    fileWin.hide();
				       }
		                     },{
					text: '&nbsp;'
				     }
				   ]
    	         });

             var fileRoot = Ext.getCmp("pfiletree").getRootNode();
             expand(fileRoot, datapath);


          }
          fileWin.show();

       }
        </script> 
    </head>
    <body>
        <form action="index.jsp" method="post" id="form">
        <div style="-moz-background-clip:border; -moz-background-inline-policy:continuous; -moz-background-origin:padding; background:#1E4176  repeat-x scroll 0 0; border:0 none; padding-left:8px; padding-top:5px; padding-bottom:5px; color:white; font-family:tahoma,arial,sans-serif; font-size:16px;">Open DeskTEAM <%= DeskTEAMProperties.getProperty("deskTEAM.version") %> - <span id='projectName'><%= site.getName() %></span></div>
        </form>
        <div id="deskTEAMDiv"></div>
        <div id="editor-grid"></div>
    </body>
</html>