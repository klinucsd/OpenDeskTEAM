// Path to the blank image should point to a valid location on your server
Ext.BLANK_IMAGE_URL = 'ext-3.0.0/resources/images/default/s.gif';

Ext.Ajax.timeout = 90000;

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


var docHeight;
var version = "0.1";
var newProject = false;
var newProjectIndex;
var currentProjectId;

var currentArrayId = null;

var newEvent = false;
var newEventIndex;

var newCamera = false;
var newCameraIndex;

var newPerson = false;
var newPersonIndex;
var projects = {};

var currentProjectRow;
var currentInstitutionRow;

Ext.onReady(function(){

    Ext.QuickTips.init();
    docHeight = getDocHeight();

    var baitStore = new Ext.data.SimpleStore({
        fields: ['name'],
        data : [
                  ['Yes'],
                  ['No']
               ]
        });


    ////////////////////////////////////////////////////                          
    //
    // create projects panel                                                   
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   

    var projectCM = new Ext.grid.ColumnModel([
                       {
			    header: "id",
			    dataIndex: 'id',
			    hidden: true,
			    width: 50
		       },{
			    header: "Name",
			    dataIndex: 'name',
			    hidden: false,
			    width: 140,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Abbreviation",
			    dataIndex: 'abbrev',
			    hidden: false,
			    width: 80,
			    renderer: function(value) {
			       return value.toUpperCase();
			    },
			    editor: new Ext.form.TextField({
				    allowBlank: false,
				    fieldClass: 'projectAbbv'
				})
			},{
			    header: "Latitude",
			    dataIndex: 'lat',
			    hidden: false,
			    width: 60,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180,
				    decimalPrecision : 5
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return "";
			       } else {
				   return value;
			       }
			   } 
			},{
			    header: "Longitude",
			    dataIndex: 'lon',
			    hidden: false,
			    width: 60,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180,
				    decimalPrecision : 5
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return "";
			       } else {
				   return value;
			       }
			   }
			},{
			    header: "Bait?",
			    dataIndex: 'useBait',
			    hidden: false,
			    width: 50,
			    editor: new Ext.form.ComboBox({ 
				       allowBlank: false,
				       store: new Ext.data.SimpleStore({
					       fields: ['name'],
					       data : [
						       ['Yes'],
						       ['No']
						       ]
					   }),
				       displayField:'name',
				       typeAhead: true,
				       mode: 'local',
				       triggerAction: 'all',
				       editable : false,
				       lazyRender:true,
				       listClass: 'x-combo-list-small'
				}),
			    renderer: function(value) {
			       if (value == "Yes") {
				   return "Yes";
			       } else {
				   return "No";
			       }
			   }
			},{
			    header: "Bait",
			    dataIndex: 'bait',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.TextField({
				    allowBlank: true
				}),
			    renderer: function(value) {
			       if (value == null) {
				   return "";
			       } else {
				   return value;
			       }
			   }
		        },{
			   header: "Objective",
			   dataIndex: 'objective',
			   hidden: false,
			   width: 100,
			   editor: new Ext.form.TextArea({
				   height:100,
				   allowBlank: true
			   })
		       },{
			    header: "Country",
			    dataIndex: 'country_name',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.ComboBox({
				store: new Ext.data.JsonStore({
					autoLoad: true,
					pruneModifiedRecords: true,
					url : "jsp/open-deskTEAM/country.jsp",
					root : "results",
					fields: ['id', 'name']
				    }),
				displayField:'name',
				valueField: 'id',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				selectOnFocus:true,
				width: 130,	
				allowBlank: true,
				editable : false,
				listeners: {
				    select: function(obj, record, ind){
					    obj.setValue(record.get('name'));
					    var rec =projectStore.getAt(currentProjectRow);
					    rec.set("country_id", record.get("id"));
					    rec.set("country_name", record.get("name"));
				    }
				}
				})

			    /*
			    editor: new Ext.form.TextField({
				    allowBlank: true
				})
			    */
		       },{
			    header: "Organization",
			    dataIndex: 'org',
			    hidden: true,
			    width: 150
		       },{
			   header: "Action",
			   dataIndex: 'action',
			   width: 60,
			   renderer: function (value, metaData, record, rowIndex, colIndex, store) {
			       var text = "";
			       text += "<a href='javascript:deleteProject("+record.get('id')+",\""+record.get('name')+"\")'><img src='images/icons/bin.gif' width=14 height=13 align=absbottom title='Delete'></a>&nbsp;";
			       return text;
			   }
		       }
                     ]);
					     
    var projectStore = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'totalCount',
                fields: [
                         {name: 'id', type: 'int'},
                         {name: 'name', type: 'string'},
                         {name: 'abbrev', type: 'string'},
                         {name: 'lat', type: 'float'},
                         {name: 'lon', type: 'float'},
                         {name: 'country_id', type: 'string'},
                         {name: 'country_name', type: 'string'},
                         {name: 'objective', type: 'string'},
                         {name: 'useBait', type: 'string'},
                         {name: 'bait', type: 'string'}
                         ],
                proxy: new Ext.data.HttpProxy({
                        methods: 'GET',
			url: "jsp/open-deskTEAM/projects.jsp"
                    }),
                listeners :  {
                    exception: function(ex) {
                        alert("Fail to load project data: "+ex);
                    },
                    load: function(grid, records, options) {

                    }
                }
            });

    var editor = new Ext.ux.grid.RowEditor({
	saveText: 'Save',
	errorSummary: false,
	clicksToEdit: 2,
	listeners: {
		canceledit: function(roweditor, rowIndex) {
		    if (newProject) {
			projectStore.removeAt(newProjectIndex);
			projectStore.commitChanges();
			projectPanel.getView().refresh();
			newProject = false;
		    }
		},
		validateedit: function(roweditor, changes, record, rowIndex){

		    var valid = true;
		    if (getProjectNameCount(changes.name, projectStore, rowIndex)>0) {
			msg("Error", "Duplicate project name: '"+changes.name+"'");
			valid = false;
		    }

		    if (changes.abbrev && changes.abbrev.length != 3) {
			msg("Error", "Abbreviation must use three letters: '"+changes.abbrev+"'");
			valid = false;
		    }

		    if (getProjectAbbrevCount(changes.abbrev, projectStore, rowIndex)>0) {
			msg("Error", "Duplicate project abbrev: '"+changes.abbrev+"'");
			valid = false;
		    }

		    if (!valid) {
			if (newProject) {
			    projectStore.removeAt(rowIndex);
			    projectStore.commitChanges();
			    projectPanel.getView().refresh();
			    newProject = false;
			}
		        return false;
		    }

		},
		afteredit: function(roweditor, changes, record, rowIndex){
		    if (newProject) {
			// create a new project
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/create-project.jsp",
				method: 'GET',
				params: { 
				    id: record.get('id'), 
				    name: changes.name, 
				    abbrev: changes.abbrev, 
				    lat: changes.lat, 
				    lon: changes.lon,
				    country: changes.country
				},
				success: function(response, opts) {
				    var rec = projectStore.getAt(rowIndex);
				    rec.set('name', changes.name);
				    rec.set('abbrev', changes.abbrev);
				    rec.set('lat', changes.lat);
				    rec.set('lon', changes.lon);
				    rec.set('country', changes.country);
				    projectStore.commitChanges();
				    projectPanel.getView().refresh();

				    // change the tree node
				    addProjectIntoTree(record.get('id'), changes.name);

				    // unset flag
				    newProject = false;
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    newProject = false;
				    return false;
				}
			    });
		    } else {
			// update the project
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/update-project.jsp",
				method: 'GET',
				params: { 
				    id: record.get('id'), 
				    name: record.get('name'), 
				    abbrev: record.get('abbrev'), 
				    lat: record.get('lat'), 
				    lon: record.get('lon'),
				    country: record.get('country')
				},
				success: function(response, opts) {
				    projectStore.commitChanges();
				    projectPanel.getView().refresh();

				    addProjectIntoTree(null, null);
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });
		    }

		}
		
	}
	    
    });

    var projectPanel = new Ext.grid.EditorGridPanel({
	    id: 'projectPanel',
	    store: projectStore,
	    cm: projectCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 7,
	    autoExpandMin: 100,
	    title: 'Projects',                                                                                       
	    viewConfig: {
		forceFit:false
	    },
	    //plugins: [editor],
	    loadMask: true,
	    bbar: [{
		    text: 'New Project',
		    icon: 'images/icons/new_project.png',
		    handler: function(){

			var countryStore  = new Ext.data.JsonStore({
				autoLoad: true,
				pruneModifiedRecords: true,
				url : "jsp/open-deskTEAM/country.jsp",
				root : "results",
				fields: ['id', 'name']
			    });
			
                        var countryCombo = new Ext.form.ComboBox({
				store: countryStore,
				id: 'projectCountry',
				name: 'projectCountry',
				displayField:'name',
				valueField: 'id',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'Select a country',
				selectOnFocus:true,
				width: 130,	
				allowBlank: true,
				fieldLabel: 'Country',
				editable : false,
				listeners: {
				    select: function(obj, record, ind){
				    }
				}
			    });

                       var form = new Ext.form.FormPanel({
                             labelWidth: 80,
                             bodyStyle:'padding:15px',
                             width: 300,
                             labelPad: 10,
			     frame       : true,
                             defaultType: 'textfield',
                             defaults: {
                                 // applied to each contained item
                                 width: 220,
                                 msgTarget: 'side'
                             },
                             layoutConfig: {
                                 // layout-specific configs go here
                                 labelSeparator: ''
                             },
                             items:[{
                                       fieldLabel: 'Name',
                                       name: 'projectName',
                                       id: 'projectName',
                                       allowBlank: false
				   },{
                                       fieldLabel: 'Abbreviation',
                                       name: 'projectAbbrev',
                                       id: 'projectAbbrev',
                                       allowBlank: false,
				       fieldClass: 'projectAbbv'
				   },{
                                       fieldLabel: 'Objective',
				       xtype: 'textarea',
                                       name: 'objective',
                                       id: 'objective',
                                       allowBlank: true
				   },{
                                       fieldLabel: 'Latitude',
				       xtype: 'numberfield',
                                       name: 'projectLatitude',
                                       id: 'projectLatitude',
                                       allowBlank: true,
				       maxValue: 180,
				       minValue: -180,
				       decimalPrecision : 5
				   },{
                                       fieldLabel: 'Longitude',
				       xtype: 'numberfield',
                                       name: 'projectLogitude',
                                       id: 'projectLongitude',
                                       allowBlank: true,
				       maxValue: 180,
				       minValue: -180,
				       decimalPrecision : 5
				   },
				       countryCombo
				 /*
		                   {
                                       fieldLabel: 'Country',
                                       name: 'projectCountry',
                                       id: 'projectCountry',
                                       allowBlank: true
				   }
				 */
				   ,{
                                       fieldLabel: 'Bait Used?',
				       xtype: 'combo',
                                       name: 'useBait',
                                       id: 'useBait',
				       allowBlank: false,
				       store: new Ext.data.SimpleStore({
					       fields: ['name'],
					       data : [
						       ['Yes'],
						       ['No']
						       ]
					   }),
				       displayField:'name',
				       typeAhead: true,
				       mode: 'local',
				       triggerAction: 'all',
				       editable : false,
				       value: 'No',
				       lazyRender:true,
				       listClass: 'x-combo-list-small',
				       listeners: {
                                           select: function(obj, record, ind){
					       var value = Ext.getCmp("useBait").value;
					       var bait = Ext.getCmp("bait");
					       if (value == 'Yes') {
						   bait.setVisible(true);
					       } else {
						   bait.setVisible(false);
					       }
					   }
				       }
				   },{
                                       fieldLabel: 'Bait',
                                       name: 'bait',
                                       id: 'bait',
				       hidden: true,
                                       allowBlank: true
				   }]
                        });

			var win = new Ext.Window({
                              id          : 'newProjectWindow',
                              title       : 'Create New Project',
                              layout      : 'fit',
                              width       : 380,
                              height      : 370,
                              closeAction : 'destroy',
                              plain       : true,
                              modal       : true,
			      frame       : true,
                              items       : [ form ],
			      buttons     : [{
                                               text     : 'Create',
			                       id       : 'createProjectButton',
                                               handler  : function(){
					           if (form.getForm().isValid()) {
						       var projName = Ext.getCmp("projectName").getValue();
						       var projAbbrev = Ext.getCmp("projectAbbrev").getValue().toUpperCase();
						       var projLatitude = Ext.getCmp("projectLatitude").getValue();
						       var projLongitude = Ext.getCmp("projectLongitude").getValue();
						       var projCountry = Ext.getCmp("projectCountry").getValue();
						       var projObjective = Ext.getCmp("objective").getValue();
						       var projUseBait = Ext.getCmp("useBait").getValue();
						       var projBait = Ext.getCmp("bait").getValue();
						       
						       var record = Ext.getCmp("projectCountry").getStore().getById(projCountry);
						       var countryName = record != null? record.get('name') : null;

						       // validate project name and abbreviation
						       var valid = true;
						       if (projName.length > 25) {
							   msg("Error", "Project name has more than 25 characters: '"+projName+"'");
							   valid = false;
						       } else if (projAbbrev.length != 3) {
							   msg("Error", "Abbreviation must use three letters: '"+projAbbrev+"'");
							   valid = false;
						       } else if (getProjectAbbrevCount(projAbbrev, projectStore, -1)>0) {
							   msg("Error", "Duplicate project abbreviation: '"+projAbbrev+"'");
							   valid = false;
						       }

						       if (!valid) {
							   return false;
						       }

						       var projId = getProjectId();
						       // create a new project
						       Ext.Ajax.request({
							           url: "jsp/open-deskTEAM/create-project.jsp",
								   method: 'GET',
								   params: { 
								       id: projId, 
								       name: projName, 
								       abbrev: projAbbrev, 
								       lat: projLatitude,  
								       lon: projLongitude, 
								       country: projCountry,
								       objective: projObjective,
								       useBait: projUseBait,
								       bait: projBait
								   },
								   success: function(response, opts) {
								       var project = 
									   new projectStore.recordType({
										   id: projId,
										   name: projName,
										   abbrev: projAbbrev,
										   lat: projLatitude,  
										   lon: projLongitude, 
										   country_id: projCountry,
										   country_name: countryName,
										   objective: projObjective,
										   useBait: projUseBait,
										   bait: projBait
									       });
								       projectStore.add(project);
								       projectPanel.getView().refresh();
								       newProjectIndex = projectStore.getCount()-1;

				                                       // change the tree node
				                                       addProjectIntoTree(projId, projName);

				                                       // unset flag
				                                       newProject = false;
								       win.destroy();

								       updateProjectMenu();

							           },
								   failure: function(response, opts) {
								       //msg('Error', 'server-side failure with status code ' + response.status);
								        msg('Error', 'Encounter a system error: ' + response.status);
									newProject = false;
									return false;
							       }
							   });





						   }
					       }
				             },{
		                               text     : 'Close',
		                               id       : 'closeProjectButton',
					       handler  : function(){
					            win.hide();
						    win.destroy();
		                               }
				            }]	 
                        });
                        win.show();



			/*
			newProject = true;
			var projectId = getProjectId();
			var projectName = getNewProjectName();
                        var projectAbbrev = getNewProjectAbbrev();
			var project = 
			    new projectStore.recordType({
				    id: projectId
				    //name: projectName,
				    //abbrev: projectAbbrev
			    });
			projectStore.add(project);
			projectPanel.getView().refresh();
			newProjectIndex = projectStore.getCount()-1;
			//projectPanel.getSelectionModel().selectRow(newProjectIndex);
			editor.startEditing(newProjectIndex);
			*/

		    }
		}],
	    listeners: {
		beforeedit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;
		    currentProjectRow = obj.row;
		    if (field == 'bait' && record.get('useBait') != 'Yes') {
			obj.cancel = true;
		    }
		},
		validateedit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;
		    if (field == 'name') {
			if (newValue.length > 25) {
			    projectEditErrorConfirm("Project name has more than 25 characters: '"+newValue+"'", rowIndex, columnIndex);
			    obj.cancel = true;
			}

			if (getProjectNameCount(newValue, projectStore, rowIndex)>0) {
			    projectEditErrorConfirm("Duplicate project name: '"+newValue+"'", rowIndex, columnIndex);
			    obj.cancel = true;
			}
		    } else if (field == 'abbrev') {
			if (newValue.length != 3) {
			    projectEditErrorConfirm("Project abbreviation is not in the format of three letters: '"+newValue+"'", rowIndex, columnIndex);
			    obj.cancel = true;
			} else if (getProjectAbbrevCount(newValue, projectStore, rowIndex)>0) {
			    projectEditErrorConfirm("Duplicate project abbrev: '"+newValue+"'", rowIndex, columnIndex);
			    obj.cancel = true;
			}
		    } 
		},
		afteredit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;

		    // update the project
		    Ext.Ajax.request({
			        url: "jsp/open-deskTEAM/update-project.jsp",
				method: 'GET',
				params: { 
				    id: record.get('id'), 
				    name: record.get('name'), 
				    abbrev: record.get('abbrev').toUpperCase(), 
				    lat: record.get('lat'), 
				    lon: record.get('lon'),
				    country_id: record.get('country_id'),
				    objective: record.get('objective'),
				    useBait: record.get('useBait'),
				    bait: record.get('bait')
				},
				success: function(response, opts) {

				    if (record.get('useBait') == 'No') {
					record.set('bait', null);
				    }
				
				    projectStore.commitChanges();
				    projectPanel.getView().refresh();

				    addProjectIntoTree(null, null);
				    updateProjectMenu();

				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });



		}

	    }
	});

    projectStore.load();


    ////////////////////////////////////////////////////                          
    //
    // create arrays panel                                                   
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   

    var arrayCM = new Ext.grid.ColumnModel([
                       {
			    header: "id",
			    dataIndex: 'id',
			    hidden: true,
			    width: 50
		       },{
			    header: "Name",
			    dataIndex: 'name',
			    hidden: false,
			    width: 250
			},{
			    header: "Latitude",
			    dataIndex: 'lat',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180,
				    decimalPrecision : 5
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return "";
			       } else {
				   return value;
			       }
			   } 
			},{
			    header: "Longitude",
			    dataIndex: 'lon',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180,
				    decimalPrecision : 5
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return "";
			       } else {
				   return value;
			       }
			   }
		       },{
			   header: "Action",
			   dataIndex: 'action',
			   width: 100,
			   renderer: function (value, metaData, record, rowIndex, colIndex, store) {
			       var text = "";
			       text += "<a href='javascript:deleteArray("+record.get('id')+",\""+record.get("name")+"\","+currentProjectId+")'><img src='images/icons/bin.gif' width=14 height=13 align=absbottom title='Delete'></a>&nbsp;";
			       return text;
			   }
		       }
                     ]);
					     
    var arrayStore = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'totalCount',
                fields: [
                         {name: 'id', type: 'int'},
                         {name: 'name', type: 'string'},
                         {name: 'lat', type: 'float'},
                         {name: 'lon', type: 'float'}
                         ],
                proxy: new Ext.data.HttpProxy({
                        methods: 'GET',
			url: "jsp/open-deskTEAM/arrays.jsp"
                    }),
                listeners :  {
                    exception: function(ex) {
                        alert("Fail to load project data: "+ex);
                    },
                    load: function(grid, records, options) {

                    }
                }
            });

    var arrayPanel = new Ext.grid.EditorGridPanel({
	    id: 'arrayPanel',
	    store: arrayStore,
	    cm: arrayCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 1,
	    autoExpandMin: 200,
	    title: 'Camera Trap Arrays',                                                                                
	    viewConfig: {
		forceFit:false
	    },
	    loadMask: true,
	    bbar: [{
		    text: 'New Array',
		    icon: 'images/icons/new_project.png',
		    handler: function(){
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/create-array.jsp",
				method: 'GET',
				params: { 
				    project_id: currentProjectId
				},
				success: function(response, opts) {
				    // reload arrayStore
				    arrayStore.reload();
				    
				    // change the tree node
				    addArrayIntoTree(currentProjectId);
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });

		    }
		},{
		    text: 'Set Camera Traps by Excel',
		    icon: 'images/icons/new_project.png',
		    handler: function(){
			openCameraTrapExcelWindow();
		    }
		}],
	    listeners: {
		afteredit: function(obj){

		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;

		    var lat = record.get('lat');
		    var lon = record.get('lon');

		    // update the array
		    Ext.Ajax.request({
			    url: "jsp/open-deskTEAM/update-array.jsp",
			    method: 'GET',
			    params: { 
				array_id: record.get('id'), 
				lat: lat != 0 ? lat: null, 
				lon: lon != 0 ? lon : null
			    },
			    success: function(response, opts) {
				arrayStore.commitChanges();
				arrayPanel.getView().refresh();
			    },
			    failure: function(response, opts) {
				//msg('Error', 'server-side failure with status code ' + response.status);
				msg('Error', 'Encounter a system error: ' + response.status);
				return false;
			    }
			});
		}
	    }

	});


    ////////////////////////////////////////////////////                          
    //
    // create traps panel                                                   
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   

    var trapCM = new Ext.grid.ColumnModel([
                       {
			    header: "id",
			    dataIndex: 'id',
			    hidden: true,
			    width: 50
		       },{
			    header: "Name",
			    dataIndex: 'name',
			    hidden: false,
			    width: 250,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Latitude",
			    dataIndex: 'lat',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180,
				    decimalPrecision : 5
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return "";
			       } else {
				   return value;
			       }
			   } 
			},{
			    header: "Longitude",
			    dataIndex: 'lon',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180,
				    decimalPrecision : 5
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return "";
			       } else {
				   return value;
			       }
			   }
		       },{
			   header: "Action",
			   dataIndex: 'action',
			   width: 100,
			   renderer: function (value, metaData, record, rowIndex, colIndex, store) {
			       var text = "";
			       text += "<a href='javascript:deleteTrap("+record.get('id')+",\""+record.get("name")+"\""+","+currentArrayId+","+currentProjectId+")'><img src='images/icons/bin.gif' width=14 height=13 align=absbottom title='Delete'></a>&nbsp;";
			       return text;
			   }
		       }
                     ]);
					     
    var trapStore = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'totalCount',
                fields: [
                         {name: 'id', type: 'int'},
                         {name: 'name', type: 'string'},
                         {name: 'lat', type: 'float'},
                         {name: 'lon', type: 'float'}
                         ],
                proxy: new Ext.data.HttpProxy({
                        methods: 'GET',
			url: "jsp/open-deskTEAM/traps.jsp"
                    }),
                listeners :  {
                    exception: function(ex) {
                        alert("Fail to load project data: "+ex);
                    },
                    load: function(grid, records, options) {

                    }
                }
            });

    var trapPanel = new Ext.grid.EditorGridPanel({
	    id: 'trapPanel',
	    store: trapStore,
	    cm: trapCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 1,
	    autoExpandMin: 200,
	    title: 'Camera Traps',                                                                                
	    viewConfig: {
		forceFit:false
	    },
	    loadMask: true,
	    bbar: [{
		    text: 'New Trap',
		    icon: 'images/icons/new_project.png',
		    handler: function(){
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/create-trap.jsp",
				method: 'GET',
				params: {
				    project_id: currentProjectId,
				    array_id: currentArrayId
				},
				success: function(response, opts) {
				    // reload trapStore
				    trapStore.reload();
				    
				    // change the tree node
				    addTrapIntoTree(currentProjectId, currentArrayId);
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });

		    }
		},{
		    text: 'Set Camera Traps by Excel',
		    icon: 'images/icons/new_project.png',
		    handler: function(){
			openCameraTrapExcelWindow();
		    }
		}],
	    listeners: {
		validateedit: function(obj){
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;
		    if (field == 'name') {
			if (newValue.length == 0) {
			    trapEditErrorConfirm("Camera trap name is blank: '"+newValue+"'", rowIndex, columnIndex);
			    obj.cancel = true;
			}
			if (getTrapNameCount(newValue, trapStore, rowIndex)>0) {
			    trapEditErrorConfirm("Duplicate camera trap name: '"+newValue+"'", rowIndex, columnIndex);
			    obj.cancel = true;
			}	
		    }
		},
		afteredit: function(obj){

		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;

		    var lat = record.get('lat');
		    var lon = record.get('lon');
		    var trapName = record.get('name');

		    // update the trap
		    Ext.Ajax.request({
			    url: "jsp/open-deskTEAM/update-trap.jsp",
			    method: 'GET',
			    params: { 
				trap_id: record.get('id'),
				trap_name: trapName,     
				lat: lat != 0 ? lat: null, 
				lon: lon != 0 ? lon : null
			    },
			    success: function(response, opts) {
				var obj1 = Ext.decode(response.responseText);
				if (obj1.success == false) {
				    msg('Error', 'Duplicate camera trap name: ' + newValue+' in '+obj1.array+'.');
				    record.set('name', oldValue);
				    return;
				}

				trapStore.commitChanges();
				trapPanel.getView().refresh();
				
				if (field == "name") {
				    // reload the node of this camera trap
				    //var returnObj = Ext.decode(response.responseText);
				    addTrapIntoTree(currentProjectId, currentArrayId);
				}
			    },
			    failure: function(response, opts) {
				//msg('Error', 'server-side failure with status code ' + response.status);
				msg('Error', 'Encounter a system error: ' + response.status);
				return false;
			    }
			});
		}
	    }

	});


    ////////////////////////////////////////////////////                          
    //
    // create events panel                                                   
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   

    var eventCM = new Ext.grid.ColumnModel([
                       {
			    header: "id",
			    dataIndex: 'id',
			    hidden: true,
			    width: 50
		       },{
			    header: "Name",
			    dataIndex: 'name',
			    hidden: false,
			    width: 250,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
		       },{
			   header: "Action",
			   dataIndex: 'action',
			   width: 100,
			   renderer: function (value, metaData, record, rowIndex, colIndex, store) {
			       var text = "";
			       text += "<a href='javascript:deleteEvent("+record.get('id')+",\""+record.get("name")+"\")'><img src='images/icons/bin.gif' width=14 height=13 align=absbottom title='Delete'></a>&nbsp;";
			       return text;
			   }
		       }
                     ]);
					     
    var eventStore = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'totalCount',
                fields: [
                         {name: 'id', type: 'int'},
                         {name: 'name', type: 'string'}
                         ],
                proxy: new Ext.data.HttpProxy({
                        methods: 'GET',
			url: "jsp/open-deskTEAM/events.jsp"
                    }),
                listeners :  {
                    exception: function(ex) {
                        alert("Fail to load event data: "+ex);
                    },
                    load: function(grid, records, options) {

                    }
                }
            });

    var eventEditor = new Ext.ux.grid.RowEditor({
	saveText: 'Save',
	errorSummary: false,
	clicksToEdit: 2,
	listeners: {
		canceledit: function(roweditor, rowIndex) {
		    if (newEvent) {
			eventStore.removeAt(newEventIndex);
			eventStore.commitChanges();
			eventPanel.getView().refresh();
			newEvent = false;
		    }
		},
		validateedit: function(roweditor, changes, record, rowIndex){

		    var valid = true;
		    if (getEventNameCount(changes.name, eventStore, rowIndex)>0) {
			msg("Error", "Duplicate event name: '"+changes.name+"'");
			valid = false;
		    }

		    if (!valid) {
			if (newEvent) {
			    eventStore.removeAt(rowIndex);
			    eventStore.commitChanges();
			    eventPanel.getView().refresh();
			    newEvent = false;
			}
		        return false;
		    }

		},
		afteredit: function(roweditor, changes, record, rowIndex){
		    if (newEvent) {
			// create a new event
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/create-event.jsp",
				method: 'GET',
				params: { 
				    event_id: record.get('id'), 
				    project_id: currentProjectId,
				    name: changes.name
				},
				success: function(response, opts) {
				    var rec = eventStore.getAt(rowIndex);
				    var obj = Ext.decode(response.responseText);
				    rec.set('id', obj.event_id);
				    rec.set('name', changes.name);
				    eventStore.commitChanges();
				    eventPanel.getView().refresh();

				    // change the tree node
				    //addEventIntoTree();

				    // unset flag
				    newEvent = false;
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    newProject = false;
				    return false;
				}
			    });
		    } else {
			// update the event
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/update-event.jsp",
				method: 'GET',
				params: { 
				    event_id: record.get('id'), 
				    name: record.get('name')
				},
				success: function(response, opts) {
				    eventStore.commitChanges();
				    eventPanel.getView().refresh();
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });
		    }

		}
		
	    }
	});

    var eventPanel = new Ext.grid.EditorGridPanel({
	    id: 'eventPanel',
	    store: eventStore,
	    cm: eventCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 1,
	    autoExpandMin: 200,
	    title: 'Events',   
	    //plugins: [eventEditor],                                                                             
	    viewConfig: {
		forceFit:false
	    },
	    loadMask: true,
	    bbar: [{
		    text: 'New Event',
		    icon: 'images/icons/new_project.png',
		    handler: function(){

                       var form = new Ext.form.FormPanel({
                             labelWidth: 80,
                             bodyStyle:'padding:15px',
                             width: 300,
                             labelPad: 10,
			     frame       : true,
                             defaultType: 'textfield',
                             defaults: {
                                 // applied to each contained item
                                 width: 220,
                                 msgTarget: 'side'
                             },
                             layoutConfig: {
                                 // layout-specific configs go here
                                 labelSeparator: ''
                             },
                             items: [{
                                       fieldLabel: 'Event Name',
                                       name: 'eventName',
                                       id: 'eventName',
                                       allowBlank: false
                                     }]
                        });

			var win = new Ext.Window({
                              id          : 'newEventWindow',
                              title       : 'Create New Event',
                              layout      : 'fit',
                              width       : 380,
                              height      : 150,
                              closeAction : 'destroy',
                              plain       : true,
                              modal       : true,
			      frame       : true,
                              items       : [ form ],
			      buttons     : [{
                                               text     : 'Create',
			                       id       : 'createEventButton',
                                               handler  : function(){
					           if (form.getForm().isValid()) {
						       var eventName = Ext.getCmp("eventName").getValue();

						       // validate project name and abbreviation
						       var valid = true;
						       if (getEventNameCount(eventName, eventStore, -1)>0) {
							   msg("Error", "Duplicate event name: '"+eventName+"'");
							   valid = false;
						       }

						       if (!valid) {
							   return false;
						       }
						       
						       // create a new event
						       Ext.Ajax.request({
							       url: "jsp/open-deskTEAM/create-event.jsp",
							       method: 'GET',
							       params: { 
								   project_id: currentProjectId,
								   name: eventName
							       },
							       success: function(response, opts) {

								   eventStore.reload();
								   // change the tree node
								   //addEventIntoTree();
								   
								   // unset flag
								   newEvent = false;
								   win.destroy();
							       },
							       failure: function(response, opts) {
								   //msg('Error', 'server-side failure with status code ' + response.status);
								   msg('Error', 'Encounter a system error: ' + response.status);
								   newEvent = false;
								   return false;
							       }
							   });




						   }
					       }
				             },{
		                               text     : 'Close',
		                               id       : 'closeProjectButton',
					       handler  : function(){
					            win.hide();
						    win.destroy();
		                               }
				            }]	 
                        });
                        win.show();




			/*
			newEvent = true;
			var event = 
			    new eventStore.recordType({
			    });
			eventStore.add(event);
			eventPanel.getView().refresh();
			newEventIndex = eventStore.getCount()-1;
			//projectPanel.getSelectionModel().selectRow(newProjectIndex);
			eventEditor.startEditing(newEventIndex);
			*/
		    }
		}],
	    listeners: {
		validateedit: function(obj){
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;
		    if (field == 'name') {
			if (getEventNameCount(newValue, eventStore, rowIndex)>0) {
			    msg("Error", "Duplicate event name: '"+newValue+"'");
			    obj.cancel = true;
			    return false;
			}
		    }
		},
		afteredit: function(obj){
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;

		    // update the event
		    Ext.Ajax.request({
			        url: "jsp/open-deskTEAM/update-event.jsp",
				method: 'GET',
				params: { 
				    event_id: record.get('id'), 
				    name: record.get('name')
				},
				success: function(response, opts) {
				    eventStore.commitChanges();
				    eventPanel.getView().refresh();
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });

		}

	    }

	});



    ////////////////////////////////////////////////////                          
    //
    // create cameras panel                                                   
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   

    var manufacturerStore1  = new Ext.data.JsonStore({
	    autoLoad: true,
	    pruneModifiedRecords: true,
	    url : "jsp/open-deskTEAM/manufacturer.jsp",
	    root : "results",
	    fields: ['name']
	});
			
    var manufacturerCombo1 = new Ext.form.ComboBox({
	    store: manufacturerStore1,
	    displayField:'name',
	    valueField: 'name',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'Select a manufacturer',
	    selectOnFocus:true,
	    width: 130,	
	    allowBlank: false,
	    editable : true,
	    listeners: {
		select: function(obj, record, ind){
		    // reset the model store
		    var cmodel = Ext.getCmp('cameraModel1');
		    cmodel.reset();
		    cmodel.getStore().load(
                          { params: 
	                      {'manufacturer': record.get('name') }
                          });
		}
	    }
	});

    var modelStore1  = new Ext.data.JsonStore({
	    autoLoad: true,
	    pruneModifiedRecords: true,
	    url : "jsp/open-deskTEAM/model.jsp",
	    root : "results",
	    fields: ['name']
	});
			
    var modelCombo1 = new Ext.form.ComboBox({
	    store: modelStore1,
	    id: 'cameraModel1',
	    displayField:'name',
	    valueField: 'name',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'Select a model',
	    selectOnFocus:true,
	    width: 130,	
	    allowBlank: false,
	    editable : true,
	    listeners: {
		focus: function(obj){
		    // reset the model store
		    var cmodel = Ext.getCmp('cameraModel1');
		    cmodel.reset();


		    /*
		    cmodel.getStore().load(
                          { params: 
	                      {'manufacturer': record.get('name') }
                          });
		    */
		}
	    }
	});


    var cameraCM = new Ext.grid.ColumnModel([
                       {
			    header: "id",
			    dataIndex: 'id',
			    hidden: true,
			    width: 50
		       },{
			    header: "Type",
			    dataIndex: 'type',
			    hidden: true, 
			    width: 100,
			    editor: new Ext.form.TextField({
			       allowBlank: true
			    })
			},{
			    header: "Manufacturer",
			    dataIndex: 'manufacturer',
			    hidden: false,
			    width: 130,
			    editor: manufacturerCombo1
			    /*
			    editor: new Ext.form.TextField({
			    	    allowBlank: true
			    })
			    */
			},{
			    header: "Model",
			    dataIndex: 'model',
			    hidden: false,
			    width: 130,
			    editor: modelCombo1
			    //,
			    //editor: new Ext.form.TextField({
			    //	    allowBlank: false
			    //	}) 
			},{
			    header: "Serial Number",
			    dataIndex: 'serial',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
		       },{
			   header: "Action",
			   dataIndex: 'action',
			   width: 60,
			   renderer: function (value, metaData, record, rowIndex, colIndex, store) {
			       var text = "";
			       text += "<a href='javascript:deleteCamera("+record.get('id')+")'><img src='images/icons/bin.gif' width=14 height=13 align=absbottom title='Delete'></a>&nbsp;";
			       return text;
			   }
		       }
                     ]);
					     
    var cameraStore = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'totalCount',
                fields: [
                         {name: 'id', type: 'int'},
                         {name: 'type', type: 'string'},
                         {name: 'manufacturer', type: 'string'},
                         {name: 'model', type: 'string'},
                         {name: 'serial', type: 'string'}
                         ],
                proxy: new Ext.data.HttpProxy({
                        methods: 'GET',
			url: "jsp/open-deskTEAM/cameras.jsp"
                    }),
                listeners :  {
                    exception: function(ex) {
                        alert("Fail to load camera data: "+ex);
                    },
                    load: function(grid, records, options) {

                    }
                }
            });

    var cameraEditor = new Ext.ux.grid.RowEditor({
	saveText: 'Save',
	errorSummary: false,
	clicksToEdit: 2,
	listeners: {
		canceledit: function(roweditor, rowIndex) {
		    if (newCamera) {
			cameraStore.removeAt(newCameraIndex);
			cameraStore.commitChanges();
			cameraPanel.getView().refresh();
			newCamera = false;
		    }
		},
		validateedit: function(roweditor, changes, record, rowIndex){

		    var valid = true;
		    //alert("check: "+changes.serial+"===="+record.get('serila')+"===="+JSON.stringify(changes));
		    if (getCameraNameCount(changes.serial, cameraStore, rowIndex)>0) {
			msg("Error", "Duplicate camera serial: '"+changes.serial+"'");
			valid = false;
		    }

		    if (!valid) {
			if (newCamera) {
			    cameraStore.removeAt(rowIndex);
			    cameraStore.commitChanges();
			    cameraPanel.getView().refresh();
			    newCamera = false;
			}
		        return false;
		    }

		},
		afteredit: function(roweditor, changes, record, rowIndex){
		    if (newCamera) {	
		    } else {
			// update the camera
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/update-camera.jsp",
				method: 'GET',
				params: { 
				    camera_id: record.get('id'), 
				    camera_type: record.get('type'), 
				    manufacturer: record.get('manufacturer'), 
				    model: record.get('model'), 
				    serial: record.get('serial')
				},
				success: function(response, opts) {
				    cameraStore.commitChanges();
				    cameraPanel.getView().refresh();
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });
		    }

		}
		
	}
	    
    });

    var cameraPanel = new Ext.grid.EditorGridPanel({
	    id: 'cameraPanel',
	    store: cameraStore,
	    cm: cameraCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 5,
	    autoExpandMin: 200,
	    title: 'Cameras',                                                                                       
	    viewConfig: {
		forceFit:false
	    },
	    //plugins: [cameraEditor],
	    loadMask: true,
	    bbar: [{
		    text: 'New Camera',
		    icon: 'images/icons/new_project.png',
		    handler: function(){

			var manufacturerStore  = new Ext.data.JsonStore({
				autoLoad: true,
				pruneModifiedRecords: true,
				url : "jsp/open-deskTEAM/manufacturer.jsp",
				root : "results",
				fields: ['name']
			    });
			
                        var manufacturerCombo = new Ext.form.ComboBox({
				store: manufacturerStore,
				id: 'cameraManufacturer',
				name: 'cameraManufacturer',
				displayField:'name',
				valueField: 'name',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'Select a manufacturer',
				selectOnFocus:true,
				width: 130,	
				allowBlank: false,
				fieldLabel: 'Manufacturer',
				editable : true,
				listeners: {
				    select: function(obj, record, ind){
					// reset the model store
					var cmodel = Ext.getCmp('cameraModel');
					cmodel.reset();
					cmodel.getStore().load(
			                   { params: 
					       {'manufacturer': record.get('name') }
			                   });
				    }
				}
			    });

			var modelStore  = new Ext.data.JsonStore({
				autoLoad: true,
				pruneModifiedRecords: true,
				url : "jsp/open-deskTEAM/model.jsp",
				root : "results",
				fields: ['name']
			    });
			
                        var modelCombo = new Ext.form.ComboBox({
				store: modelStore,
				id: 'cameraModel',
				name: 'cameraModel',
				displayField:'name',
				valueField: 'name',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'Select a model',
				selectOnFocus:true,
				width: 130,	
				allowBlank: false,
				fieldLabel: 'Model',
				editable : true,
				listeners: {
				    select: function(obj, record, ind){
				    }
				}
			    });

                       var form = new Ext.form.FormPanel({
                             labelWidth: 90,
                             bodyStyle:'padding:15px',
                             width: 300,
                             labelPad: 10,
			     frame       : true,
                             defaultType: 'textfield',
                             defaults: {
                                 // applied to each contained item
                                 width: 220,
                                 msgTarget: 'side'
                             },
                             layoutConfig: {
                                 // layout-specific configs go here
                                 labelSeparator: ''
                             },

                             items: 
			           [{
                                       fieldLabel: 'Type',
                                       name: 'cameraType',
                                       id: 'cameraType',
				       hidden: true,
				       value: 'Camera',
				       editable: false,
                                       allowBlank: false
				   },
                                   manufacturerCombo,
                                   modelCombo,

				       /*
		                   {
                                       fieldLabel: 'Manufacturer',
                                       name: 'cameraManufacturer',
                                       id: 'cameraManufacturer',
                                       allowBlank: false
				   },
		                   {
                                       fieldLabel: 'Model',
                                       name: 'cameraModel',
                                       id: 'cameraModel',
                                       allowBlank: false
				   },
				       */

				   {
                                       fieldLabel: 'Serial Number',
                                       name: 'cameraSerial',
                                       id: 'cameraSerial',
                                       allowBlank: false
				   }]
                        });

			var win = new Ext.Window({
                              id          : 'newCameraWindow',
                              title       : 'Create New Camera',
                              layout      : 'fit',
                              width       : 380,
                              height      : 220,
                              closeAction : 'destroy',
                              plain       : true,
                              modal       : true,
			      frame       : true,
                              items       : [ form ],
			      buttons     : [{
                                               text     : 'Create',
			                       id       : 'createCameraButton',
                                               handler  : function(){
					           if (form.getForm().isValid()) {
						       var cameraType = Ext.getCmp("cameraType").getValue();
						       var cameraManufacturer = Ext.getCmp("cameraManufacturer").getValue();
						       var cameraModel = Ext.getCmp("cameraModel").getValue();
						       var cameraSerial = Ext.getCmp("cameraSerial").getValue();

						       // validate project name and abbreviation
						       var valid = true;
						       if (getCameraNameCount(cameraSerial, cameraStore, -1)>0) {
							   msg("Error", "Duplicate camera serial number '"+cameraSerial+"'");
							   valid = false;
						       }

						       if (!valid) {
							   return false;
						       }

						       // create a new camera
						       Ext.Ajax.request({
							       url: "jsp/open-deskTEAM/create-camera.jsp",
							       method: 'GET',
							       params: { 
								   project_id: currentProjectId,
								   camera_type: cameraType, 
								   manufacturer: cameraManufacturer, 
								   model: cameraModel, 
								   serial: cameraSerial
							       },
							       success: function(response, opts) {
								   cameraStore.reload();

								   // change the tree node
								   //addCameraIntoTree(currentProjectId, changes.serial);

								   // unset flag
								   win.destroy();
								   newCamera = false;
							       },
							       failure: function(response, opts) {
								   //msg('Error', 'server-side failure with status code ' + response.status);
								   msg('Error', 'Encounter a system error: ' + response.status);
								   newCamera = false;
								   return false;
							       }
							   });


						   }
					       }
				             },{
		                               text     : 'Close',
		                               id       : 'closeProjectButton',
					       handler  : function(){
					            win.hide();
						    win.destroy();
		                               }
				            }]	 
                        });
                        win.show();

			/*
			newCamera = true;
			var camera = 
			    new cameraStore.recordType({
			    });
			cameraStore.add(camera);
			cameraPanel.getView().refresh();
			newCameraIndex = cameraStore.getCount()-1;
			//cameraPanel.getSelectionModel().selectRow(newCameraIndex);
			cameraEditor.startEditing(newCameraIndex);
			*/
		    }
		},{
		    text: 'Set Camera by Excel',
		    icon: 'images/icons/new_project.png',
		    handler: function(){
			openCameraExcelWindow();
		    }
		}],
	    listeners: {
		cellclick: function(grid1, rowIndex, columnIndex) {
		    var record = cameraStore.getAt(rowIndex);  // Get the Record
		    var fieldName = cameraPanel.getColumnModel().getDataIndex(columnIndex); // Get field name
		    if (fieldName == "model") {
		        var model = record.get(fieldName);
			var manufacturer = record.get("manufacturer");

		        var cmodel = Ext.getCmp('cameraModel1');
		        cmodel.reset();
		        cmodel.getStore().load(
                          { params: 
	                      {'manufacturer': manufacturer }
                          });

		    }
		},

		validateedit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;
		    if (field == 'serial') {
			if (getCameraNameCount(newValue, cameraStore, -1)>0) {
			    cameraEditErrorConfirm("Duplicate camera serial number: '"+newValue+"'", rowIndex, columnIndex);
			    obj.cancel = true;
			}
		    }
		},
		afteredit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;

		    Ext.Ajax.request({
			        url: "jsp/open-deskTEAM/update-camera.jsp",
			        method: 'GET',
				params: { 
				    camera_id: record.get('id'), 
				    camera_type: record.get('type'), 
				    manufacturer: record.get('manufacturer'), 
				    model: record.get('model'), 
				    serial: record.get('serial')
				},
				success: function(response, opts) {
				    cameraStore.commitChanges();
				    cameraPanel.getView().refresh();
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
			        }
			});

		}
	    }
	});


    ////////////////////////////////////////////////////                          
    //
    // create persons panel                                                   
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   

    var personCM = new Ext.grid.ColumnModel([
                       {
			    header: "id",
			    dataIndex: 'id',
			    hidden: true,
			    width: 50
		       },{
			    header: "First Name",
			    dataIndex: 'first_name',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Last Name",
			    dataIndex: 'last_name',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Email",
			    dataIndex: 'email',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				}) 
			},{
			    header: "Roles",
			    dataIndex: 'roles',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.ComboBox({
				store: new Ext.data.SimpleStore({
					fields: ['name'],
					data : [
						['Data Collector'],
						['Data Reviewer'],
						['Principal Investigator'],
						['Project Manager'],
						['Spatial Data Expert'],
						['Taxonomic Expert']
						]
				    }),
				displayField:'name',
				valueField: 'name',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'Select a role',
				selectOnFocus:true,
				width: 130,	
				allowBlank: true,
				fieldLabel: 'Role',
				editable : false,
				listeners: {
				    select: function(obj, record, ind){
				    }
				}
			    })
		       },{
			   header: "Action",
			   dataIndex: 'action',
			   width: 60,
			   renderer: function (value, metaData, record, rowIndex, colIndex, store) {
			       var text = "";
			       text += "<a href='javascript:deletePerson("+record.get('id')+","+currentProjectId+")'><img src='images/icons/bin.gif' width=14 height=13 align=absbottom title='Delete'></a>&nbsp;";
			       return text;
			   }
		       }
                     ]);
					     
    var personStore = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'totalCount',
                fields: [
                         {name: 'id', type: 'int'},
                         {name: 'first_name', type: 'string'},
                         {name: 'last_name', type: 'string'},
                         {name: 'roles', type: 'string'},
                         {name: 'email', type: 'string'}
                         ],
                proxy: new Ext.data.HttpProxy({
                        methods: 'GET',
			url: "jsp/open-deskTEAM/persons.jsp"
                    }),
                listeners :  {
                    exception: function(ex) {
                        alert("Fail to load person data: "+ex);
                    },
                    load: function(grid, records, options) {

                    }
                }
            });

    var personEditor = new Ext.ux.grid.RowEditor({
	saveText: 'Save',
	errorSummary: false,
	clicksToEdit: 2,
	listeners: {
		canceledit: function(roweditor, rowIndex) {
		    if (newPerson) {
			personStore.removeAt(newPersonIndex);
			personStore.commitChanges();
			personPanel.getView().refresh();
			newPerson = false;
		    }
		},
		validateedit: function(roweditor, changes, record, rowIndex){
		    var valid = true;
		    if (getPersonNameCount(changes.email, personStore, rowIndex)>0) {
			msg("Error", "Duplicate person email: '"+changes.email+"'");
			valid = false;
		    }

		    if (!valid) {
			if (newPerson) {
			    personStore.removeAt(rowIndex);
			    personStore.commitChanges();
			    personPanel.getView().refresh();
			    newPerson = false;
			}
		        return false;
		    }

		},
		afteredit: function(roweditor, changes, record, rowIndex){
		    if (newPerson) {
			// create a new person
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/create-person.jsp",
				method: 'GET',
				params: { 
				    first_name: changes.first_name, 
				    last_name: changes.last_name, 
				    email: changes.email,
				    project_id: currentProjectId
				},
				success: function(response, opts) {
				    var rec = personStore.getAt(rowIndex);
				    var obj = Ext.decode(response.responseText);
				    rec.set('id', obj.person_id);
				    rec.set('first_name', changes.first_name);
				    rec.set('last_name', changes.last_name);
				    rec.set('email', changes.email);
				    personStore.commitChanges();
				    personPanel.getView().refresh();

				    // change the tree node
				    //addPersonIntoTree(currentProjectId, changes.serial);

				    // unset flag
				    newPerson = false;
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    newPerson = false;
				    return false;
				}
			    });
		    } else {
			// update the person
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/update-person.jsp",
				method: 'GET',
				params: { 
				    person_id: record.get('id'), 
				    first_name: record.get('first_name'),
				    last_name: record.get('last_name'), 
				    email: record.get('email')
				},
				success: function(response, opts) {
				    personStore.commitChanges();
				    personPanel.getView().refresh();
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			    });
		    }

		}
		
	}
	    
    });

    var personPanel = new Ext.grid.EditorGridPanel({
	    id: 'personPanel',
	    store: personStore,
	    cm: personCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 4,
	    autoExpandMin: 200,
	    title: 'Persons',                                                                                       
	    viewConfig: {
		forceFit:false
	    },
	    //plugins: [personEditor],
	    loadMask: true,
	    bbar: [{
		    text: 'New Person',
		    icon: 'images/icons/new_project.png',
		    handler: function(){
			
                        var roleCombo = new Ext.form.ComboBox({
				store: new Ext.data.SimpleStore({
					fields: ['name'],
					data : [
						['Data Collector'],
						['Data Reviewer'],
						['Principal Investigator'],
						['Project Manager'],
						['Spatial Data Expert'],
						['Taxonomic Expert']
						]
				    }),
				id: 'roles',
				name: 'roles',
				displayField:'name',
				valueField: 'name',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'Select a role',
				selectOnFocus:true,
				width: 130,	
				allowBlank: true,
				fieldLabel: 'Role',
				editable : false,
				listeners: {
				    select: function(obj, record, ind){
				    }
				}
			    });



                       var form = new Ext.form.FormPanel({
                             labelWidth: 80,
                             bodyStyle:'padding:15px',
                             width: 300,
                             labelPad: 10,
			     frame       : true,
                             defaultType: 'textfield',
                             defaults: {
                                 // applied to each contained item
                                 width: 220,
                                 msgTarget: 'side'
                             },
                             layoutConfig: {
                                 // layout-specific configs go here
                                 labelSeparator: ''
                             },
                             items: [{
                                       fieldLabel: 'First Name',
                                       name: 'firstName',
                                       id: 'firstName',
                                       allowBlank: false
				   },{
                                       fieldLabel: 'Last Name',
                                       name: 'lastName',
                                       id: 'lastName',
                                       allowBlank: false
				   },{
                                       fieldLabel: 'Email',
                                       name: 'email',
                                       id: 'email',
                                       allowBlank: false
				   }, roleCombo]
                        });

			var win = new Ext.Window({
                              id          : 'newPersonWindow',
                              title       : 'Create New Person',
                              layout      : 'fit',
                              width       : 380,
                              height      : 230,
                              closeAction : 'destroy',
                              plain       : true,
                              modal       : true,
			      frame       : true,
                              items       : [ form ],
			      buttons     : [{
                                               text     : 'Create',
			                       id       : 'createPersonButton',
                                               handler  : function(){
					           if (form.getForm().isValid()) {
						       var firstName = Ext.getCmp("firstName").getValue();
						       var lastName = Ext.getCmp("lastName").getValue();
						       var email = Ext.getCmp("email").getValue();
						       var roles = Ext.getCmp("roles").getValue();

						       // validate person name and abbreviation
						       var valid = true;
						       if (getPersonNameCount(email, personStore, -1)>0) {
							   msg("Error", "Duplicate person email: '"+email+"'");
							   valid = false;
						       }

						       if (!valid) {
							   return false;
						       }

						       // create a new person
						       Ext.Ajax.request({
							           url: "jsp/open-deskTEAM/create-person.jsp",
								   method: 'GET',
								   params: { 
								       first_name: firstName, 
								       last_name: lastName, 
								       email: email,
								       roles: roles,
								       project_id: currentProjectId
								   },
								   success: function(response, opts) {
								       personStore.reload();
				                                       // unset flag
				                                       newPerson = false;
								       win.destroy();
							           },
								   failure: function(response, opts) {
								        //msg('Error', 'server-side failure with status code ' + response.status);
								        msg('Error', 'Encounter a system error: ' + response.status);
									newPerson = false;
									return false;
							       }
							   });





						   }
					       }
				             },{
		                               text     : 'Close',
		                               id       : 'closeProjectButton',
					       handler  : function(){
					            win.hide();
						    win.destroy();
		                               }
				            }]	 
                        });
                        win.show();

			/*
			newPerson = true;
			var person = 
			    new personStore.recordType({
			    });
			personStore.add(person);
			personPanel.getView().refresh();
			newPersonIndex = personStore.getCount()-1;
			//personPanel.getSelectionModel().selectRow(newPersonIndex);
			personEditor.startEditing(newPersonIndex);
			*/
		    }
		}],
	    listeners: {
		validateedit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;
		    if (field == 'email') {
			var valid = true;
			if (getPersonNameCount(newValue, personStore, -1)>0) {
			    msg("Error", "Duplicate person email: '"+newValue+"'");
			    obj.cancel = true;
			    return false;
			}
		    }
		},
		afteredit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;

		    // update the person
		    Ext.Ajax.request({
				url: "jsp/open-deskTEAM/update-person.jsp",
				method: 'GET',
				params: { 
				    person_id: record.get('id'), 
				    first_name: record.get('first_name'),
				    last_name: record.get('last_name'), 
				    email: record.get('email'),
				    roles: record.get('roles')
				},
				success: function(response, opts) {
				    personStore.commitChanges();
				    personPanel.getView().refresh();
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			});

		}



	    }
	});



    ////////////////////////////////////////////////////                          
    //
    // create institutions panel                                                   
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   

    var institutionCM = new Ext.grid.ColumnModel([
                       {
			    header: "id",
			    dataIndex: 'id',
			    hidden: true,
			    width: 50
		       },{
			    header: "Name",
			    dataIndex: 'name',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Address",
			    dataIndex: 'address',
			    hidden: false,
			    width: 200,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "City",
			    dataIndex: 'city',
			    hidden: false,
			    width: 80,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "State",
			    dataIndex: 'state',
			    hidden: false,
			    width: 80,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
		       },{
			    header: "Country",
			    dataIndex: 'country_name',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.ComboBox({
				store: new Ext.data.JsonStore({
					autoLoad: true,
					pruneModifiedRecords: true,
					url : "jsp/open-deskTEAM/country.jsp",
					root : "results",
					fields: ['id', 'name']
				    }),
				displayField:'name',
				valueField: 'id',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				selectOnFocus:true,
				width: 130,	
				allowBlank: true,
				editable : false,
				listeners: {
				    select: function(obj, record, ind){
					    obj.setValue(record.get('name'));
					    var rec =institutionStore.getAt(currentInstitutionRow);
					    rec.set("country_id", record.get("id"));
					    rec.set("country_name", record.get("name"));
				    }
				}
				})
			},{
			    header: "Postal Code",
			    dataIndex: 'zipcode',
			    hidden: false,
			    width: 80,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Phone",
			    dataIndex: 'phone',
			    hidden: false,
			    width: 80,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Email",
			    dataIndex: 'email',
			    hidden: false,
			    width: 120,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				}) 
		        },{
			   header: "Action",
			   dataIndex: 'action',
			   width: 60,
			   renderer: function (value, metaData, record, rowIndex, colIndex, store) {
			       var text = "";
			       text += "<a href='javascript:deleteInstitution("+record.get('id')+","+currentProjectId+")'><img src='images/icons/bin.gif' width=14 height=13 align=absbottom title='Delete'></a>&nbsp;";
			       return text;
			   }
		       }
                     ]);
					     
    var institutionStore = new Ext.data.JsonStore({
                root: 'result',
                totalProperty: 'totalCount',
                fields: [
                         {name: 'id', type: 'int'},
                         {name: 'name', type: 'string'},
                         {name: 'address', type: 'string'},
                         {name: 'city', type: 'string'},
                         {name: 'state', type: 'string'},
                         {name: 'country_id', type: 'string'},
                         {name: 'country_name', type: 'string'},
                         {name: 'zipcode', type: 'string'},
                         {name: 'phone', type: 'string'},
                         {name: 'email', type: 'string'}
                         ],
                proxy: new Ext.data.HttpProxy({
                        methods: 'GET',
			url: "jsp/open-deskTEAM/institutions.jsp"
                    }),
                listeners :  {
                    exception: function(ex) {
                        alert("Fail to load institution data: "+ex);
                    },
                    load: function(grid, records, options) {

                    }
                }
            });

    var institutionPanel = new Ext.grid.EditorGridPanel({
	    id: 'institutionPanel',
	    store: institutionStore,
	    cm: institutionCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 2,
	    autoExpandMin: 200,
	    title: 'Institutions',                                                                                       
	    viewConfig: {
		forceFit:false
	    },
	    //plugins: [institutionEditor],
	    loadMask: true,
	    bbar: [{
		    text: 'New Institution',
		    icon: 'images/icons/new_project.png',
		    handler: function(){

			var countryStore  = new Ext.data.JsonStore({
				autoLoad: true,
				pruneModifiedRecords: true,
				url : "jsp/open-deskTEAM/country.jsp",
				root : "results",
				fields: ['id', 'name']
			    });
			
                        var countryCombo = new Ext.form.ComboBox({
				store: countryStore,
				id: 'insCountry',
				name: 'insCountry',
				displayField:'name',
				valueField: 'id',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'Select a country',
				selectOnFocus:true,
				width: 130,	
				allowBlank: true,
				fieldLabel: 'Country',
				editable : false,
				listeners: {
				    select: function(obj, record, ind){
				    }
				}
			    });

			var form = new Ext.form.FormPanel({
                             labelWidth: 80,
                             bodyStyle:'padding:15px',
                             width: 300,
                             labelPad: 10,
			     frame       : true,
                             defaultType: 'textfield',
                             defaults: {
                                 // applied to each contained item
                                 width: 220,
                                 msgTarget: 'side'
                             },
                             layoutConfig: {
                                 // layout-specific configs go here
                                 labelSeparator: ''
                             },
                             items: [{
                                       fieldLabel: 'Name',
                                       name: 'insName',
                                       id: 'insName',
                                       allowBlank: false
				   },{
                                       fieldLabel: 'Address',
                                       name: 'insAddress',
                                       id: 'insAddress',
                                       allowBlank: true
				   },{
                                       fieldLabel: 'City',
                                       name: 'insCity',
                                       id: 'insCity',
                                       allowBlank: true
				   },{
                                       fieldLabel: 'State/Province',
                                       name: 'insState',
                                       id: 'insState',
                                       allowBlank: true
				    },countryCombo,{
                                       fieldLabel: 'Postal Code',
                                       name: 'insZip',
                                       id: 'insZip',
                                       allowBlank: true
				   },{
                                       fieldLabel: 'Phone',
                                       name: 'insPhone',
                                       id: 'insPhone',
                                       allowBlank: true
				   },{
                                       fieldLabel: 'Email',
                                       name: 'insEmail',
                                       id: 'insEmail',
                                       allowBlank: true
				   }]
                        });

			var win = new Ext.Window({
                              id          : 'newInstitutionWindow',
                              title       : 'Create New Institution',
                              layout      : 'fit',
                              width       : 380,
                              height      : 330,
                              closeAction : 'destroy',
                              plain       : true,
                              modal       : true,
			      frame       : true,
                              items       : [ form ],
			      buttons     : [{
                                               text     : 'Create',
			                       id       : 'createInstitutionButton',
                                               handler  : function(){
					           if (form.getForm().isValid()) {
						       var insName = Ext.getCmp("insName").getValue();
						       var insAddress = Ext.getCmp("insAddress").getValue();
						       var insCity = Ext.getCmp("insCity").getValue();
						       var insState = Ext.getCmp("insState").getValue();
						       var insCountry = Ext.getCmp("insCountry").getValue();
						       var insZip = Ext.getCmp("insZip").getValue();
						       var insPhone = Ext.getCmp("insPhone").getValue();
						       var insEmail = Ext.getCmp("insEmail").getValue();

						       // validate institution name and abbreviation
						       var valid = true;
						       if (getInstitutionNameCount(insName, institutionStore, -1)>0) {
							   msg("Error", "Duplicate institution: '"+insName+"'");
							   valid = false;
						       }

						       if (!valid) {
							   return false;
						       }

						       // create a new institution
						       Ext.Ajax.request({
							           url: "jsp/open-deskTEAM/create-institution.jsp",
								   method: 'GET',
								   params: { 
								       name: insName, 
								       address: insAddress, 
								       city: insCity,
								       state: insState,
								       country: insCountry,
								       zip: insZip,
								       phone: insPhone,
								       email: insEmail,
								       project_id: currentProjectId
								   },
								   success: function(response, opts) {
								       institutionStore.reload();
				                                       // unset flag
				                                       newInstitution = false;
								       win.destroy();
							           },
								   failure: function(response, opts) {
								        //msg('Error', 'server-side failure with status code ' + response.status);
								        msg('Error', 'Encounter a system error: ' + response.status);
									newInstitution = false;
									return false;
							       }
							   });





						   }
					       }
				             },{
		                               text     : 'Close',
		                               id       : 'closeProjectButton',
					       handler  : function(){
					            win.hide();
						    win.destroy();
		                               }
				            }]	 
                        });
                        win.show();
		    }
		}],
	    listeners: {
		validateedit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;
		    currentInstitutionRow = obj.row;

		    if (field == 'name') {
			var valid = true;
			if (getInstitutionNameCount(newValue, institutionStore, rowIndex)>0) {
			    msg("Error", "Duplicate institution email: '"+newValue+"'");
			    obj.cancel = true;
			    return false;
			}
		    }
		},
		afteredit: function(obj) {
		    var record = obj.record;
		    var field = obj.field;
		    var newValue = obj.value;
		    var oldValue = obj.originalValue;
		    var rowIndex = obj.row;
		    var columnIndex = obj.column;

		    // update the institution
		    Ext.Ajax.request({
				url: "jsp/open-deskTEAM/update-institution.jsp",
				method: 'GET',
				params: { 
				    institution_id: record.get('id'), 
				    name: record.get('name'),
				    address: record.get('address'),
				    city: record.get('city'), 
				    state: record.get('state'),
				    country_id: record.get('country_id'),  
				    zip: record.get('zipcode'),
				    phone: record.get('phone'),    
				    email: record.get('email')
				},
				success: function(response, opts) {
				    institutionStore.commitChanges();
				    institutionPanel.getView().refresh();
				},
				failure: function(response, opts) {
				    //msg('Error', 'server-side failure with status code ' + response.status);
				    msg('Error', 'Encounter a system error: ' + response.status);
				    return false;
				}
			});

		}



	    }
	});




    ////////////////////////////////////////////////////                          
    //
    // create tree panel for the navigation                                                  
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   
    
    var navRoot = new Ext.tree.AsyncTreeNode({
	    id:   1,
	    path: '/',
	    text: 'Home',
	    icon: 'images/icons/home_icon.png'
	});

    var navLoader =
	new Ext.tree.TreeLoader({
		requestMethod: 'GET',
		url: "jsp/open-deskTEAM/nav.jsp"
	    });

    navLoader.on('beforeload',
		 function(treeLoader,node){
		     this.baseParams.text=node.attributes.text;
		     this.baseParams.path=node.attributes.path;
		 },
		 navLoader);

    var navPanel =
	new Ext.tree.TreePanel({
		id: 'navPanel',
		title: 'Setup',
		region:'west',
		collapsible: false,
		margins: '5 0 0 5',
		cmargins: '5 0 0 0',
		cls: 'padding 5',
		width: 240,
		height: Math.max(546, docHeight-140),
		minSize: 240,
		maxSize: 240,
		root: navRoot,
		rootVisible: false,
		loader: navLoader,
		autoScroll : true,
		fitToFrame:true,
		border: false,
		listeners :  {
		    click: function(node, event) {
			if (node.attributes.path == '/Projects') {
			    Ext.getCmp('rightPanel').getLayout().setActiveItem(0);
			    projectStore.reload();
			} else {
			    var elements = node.attributes.path.split("/");
			    if (elements.length == 4) {
				var projectId = elements[2];
				if (elements[3] == 'Arrays') {
				    Ext.getCmp('rightPanel').getLayout().setActiveItem(1);
			            arrayPanel.setTitle("Camera Trap Arrays for "+node.parentNode.attributes.text);
				    arrayStore.load.defer(100,
							  arrayStore,
							  [ {params:{ id: elements[2] }}]);
				    currentProjectId = elements[2];
				    currentArrayId = null;

				} else if (elements[3] == 'Events') {
				    Ext.getCmp('rightPanel').getLayout().setActiveItem(3);
				    eventPanel.setTitle("Events for "+node.parentNode.attributes.text);
				    eventStore.load.defer(100,
							  eventStore,
							  [ {params:{ project_id: elements[2] }}]);
				    currentProjectId = elements[2];
				} else if (elements[3] == 'Cameras') {
				    Ext.getCmp('rightPanel').getLayout().setActiveItem(4);
				    cameraStore.load.defer(100,
							   cameraStore,
							   [ {params:{ project_id: elements[2] }}]);
				    currentProjectId = elements[2];
				} else if (elements[3] == 'People') {
				    Ext.getCmp('rightPanel').getLayout().setActiveItem(5);
				    personStore.load.defer(100,
							   personStore,
							   [ {params:{ project_id: elements[2] }}]);
				    currentProjectId = elements[2];
				} else if (elements[3] == 'Institutions') {
				    Ext.getCmp('rightPanel').getLayout().setActiveItem(6);
				    institutionStore.load.defer(100,
							institutionStore,
							   [ {params:{ project_id: elements[2] }}]);
				    currentProjectId = elements[2];
				}
			    } else if (elements.length == 5 && node.attributes.text.indexOf("Trap") != 0) {
				var projectId = elements[2];
				var arrayId = elements[3];
				var trapId = elements[4];
				Ext.getCmp('rightPanel').getLayout().setActiveItem(2);
				trapPanel.setTitle("Camera Traps for "+node.attributes.text);
				trapStore.load.defer(100,
						     trapStore,
						     [ {params:{ array_id: elements[4] }}]);
				currentProjectId = elements[2];
				currentArrayId = elements[4];				

			    }
			}
		    }
		}
	    });

    navRoot.expand(false,
		   true,
		   function() {
                      var projectNode = navRoot.findChild("text", "Projects");
		      projectNode.expand(false,
					 true,
					 function() {
					     if (pid != null) {
						 var tmpNode = projectNode.findChild("path", "/Projects/"+pid);
						 if (tmpNode != null) {
						     tmpNode.expand(false, true, function() {
							     var arrayNode = tmpNode.findChild("text", "Camera Trap Arrays");
							     if (arrayNode != null) {
								 arrayNode.expand(false, true, function() {});
							     }

							     // load array panel
							     Ext.getCmp('rightPanel').getLayout().setActiveItem(1);
							     arrayPanel.setTitle("Camera Trap Arrays for "+arrayNode.parentNode.attributes.text);
							     var arrayStore = Ext.getCmp("arrayPanel").store;
							     arrayStore.load.defer(100,
										   arrayStore,
										   [ {params:{ id: pid }}]);
							     currentProjectId = pid;
						 


						     });
						 }

					     }
					 });   

		   });



    ////////////////////////////////////////////////////                          
    //
    // create main panel
    //                                                                                                                  
    ////////////////////////////////////////////////////                                                   
   
    var mainPanel = new Ext.Panel({
	    id:       'mainPanel',
	    layout:   'border',
	    width:    '100%',
	    height:   docHeight-31,
	    defaults: {
		collapsible: false,
		split: true,
		bodyStyle: 'padding:0px'
	    },
	    renderTo: 'deskTEAMDiv',
	    items: [{
		    region: 'center',
		    layout: 'card',
		    frame:  false,
		    border: false,
		    activeItem: 0,
		    id: 'rightPanel',
		    width:  '100%',
		    items: [
			    projectPanel,
			    arrayPanel,
			    trapPanel,
			    eventPanel,
			    cameraPanel,
			    personPanel,
			    institutionPanel
		     ]
		},{
		    id: 'leftPanel',
		    region:'west',
		    layout:'fit',
		    //activeItem: 0,
		    frame:false,
		    border:true,
                    width:245,                                                                      
		    width:225,
		    margins: '2 0 2 2',
		    cmargins: '5 0 0 0',
		    cls: 'padding 5',
		    minSize: 240,
		    maxSize: 240,
		    split:true,
		    collapsible:false,
		    items: [
                            navPanel
			   ]
		}],
	    tbar: [ 
		   /*
                    {
			text: '<b>Process Data</b>',
			//icon: 'images/icons/new_project.png',
			handler: function(){
			    document.location.href="index.jsp";
			}
		    },
		   */
		    new Ext.Toolbar.SplitButton({
            		    text: '<b>Project</b>',
			    id: 'projectMenu',
			    menu: {
			        items:[{ 
                                         text: '<b>New Project</b>',
                                         iconCls: 'icon_project_add',
                                         handler: function() {
					     
					 }
				       }, '-'
				    ,{
					text: 'test',
					iconCls: 'icon_project_add'
				    }		
				   ] 
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

		 ]
	});

        updateProjectMenu();

        mainPanel.show();

        Ext.EventManager.onWindowResize(function() {
                Ext.getCmp('mainPanel').setHeight(Ext.getBody().getViewSize().height-31);
                Ext.getCmp('mainPanel').doLayout();
            });

	if (action != null) {
	    createNewProject();
	} 

        //setStatus();                                                                                                                                 
 }); //end onReady                                                         

/////////////////////////////////////////////////////////////////////////////////////

function getDocHeight() {
    var D = document;
    return Math.max(
		    Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
		    Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
		    Math.max(D.body.clientHeight, D.documentElement.clientHeight)
		    );
}


function updateProjectMenu() {

    Ext.Ajax.request({
	    url: "jsp/open-deskTEAM/projects.jsp",
	    method: 'GET',
	    success: function(response, opts) {
		var projectMenu = Ext.getCmp("projectMenu").menu;
		projectMenu.removeAll();
		projectMenu.add({ 
			text: '<b>New Project</b>',
			iconCls: 'icon_project_add',
			handler: function() {
			    document.location.href="init-setup.jsp?action=1";
			}
		    });
		projectMenu.add('-');
		
		projects = {};

		var object = Ext.decode(response.responseText);
		for (var i=0; i<object.result.length; i++) {
		    //alert("add: "+object.result[i].name+"====="+object.result[i].id);
		    projects[object.result[i].name]= object.result[i].id;
		    projectMenu.add({
			        text: object.result[i].name,
				iconCls: 'icon_project',
				handler: function(b, c) {
				   document.location.href="index.jsp?siteId="+projects[b.text];
			        }	 
			});
		}
	    }

	});

}


function getNewProjectName() {
    var count = 1;
    var projectName = "Project "+count;
    var projectStore = Ext.getCmp("projectPanel").getStore();
    while (getProjectNameCount(projectName, projectStore) > 0) {
	count++;
	projectName = "Project "+count;
    }
    return projectName;
}


function getNewProjectAbbrev() {
    var count = 1;
    var projectAbbrev = count < 10 ? "P0"+count : "P"+count;
    var projectStore = Ext.getCmp("projectPanel").getStore();
    while (getProjectAbbrevCount(projectAbbrev, projectStore) > 0) {
	count++;
	projectAbbrev = count < 10 ? "P0"+count : "P"+count;
    }
    return projectAbbrev;
}


function getProjectId() {
    var id = -1;
    var projectStore = Ext.getCmp("projectPanel").getStore();
    while (getProjectIdCount(id, projectStore) > 0) {
	id--;
    }
    return id;
}


function getProjectNameCount(projectName, projectStore, rowIndex) {
    var count = 0;
    for (var i=0; i<projectStore.getCount(); i++) {
	if (projectName == projectStore.getAt(i).get("name") && i != rowIndex) {
	    count++;
	}
    }
    return count;
}



function getTrapNameCount(trapName, trapStore, rowIndex) {
    var count = 0;
    for (var i=0; i<trapStore.getCount(); i++) {
	if (trapName == trapStore.getAt(i).get("name") && i != rowIndex) {
	    count++;
	}
    }
    return count;
}



function getProjectAbbrevCount(projectAbbrev, projectStore, rowIndex) {
    var count=0;
    for (var i=0; i<projectStore.getCount(); i++) {
	if (projectAbbrev == projectStore.getAt(i).get("abbrev") && i != rowIndex) {
	    count++;
	}
    }
    return count;
}


function getProjectIdCount(projectId, projectStore) {
    var count=0;
    for (var i=0; i<projectStore.getCount(); i++) {
	if (projectId == projectStore.getAt(i).get("id")) {
	    count++;
	}
    }
    return count;
}


function getEventNameCount(eventName, eventStore, rowIndex) {
    var count = 0;
    for (var i=0; i<eventStore.getCount(); i++) {
	if (eventName == eventStore.getAt(i).get("name") && i != rowIndex) {
	    count++;
	}
    }
    return count;
}



function getCameraNameCount(cameraName, cameraStore, rowIndex) {
    var count = 0;
    for (var i=0; i<cameraStore.getCount(); i++) {
	if (cameraName == cameraStore.getAt(i).get("serial") && i != rowIndex) {
	    count++;
	}
    }
    return count;
}



function getPersonNameCount(personName, personStore, rowIndex) {
    var count = 0;
    for (var i=0; i<personStore.getCount(); i++) {
	if (personName == personStore.getAt(i).get("email") && i != rowIndex) {
	    count++;
	}
    }
    return count;
}


function getInstitutionNameCount(insName, insStore, rowIndex) {
    var count = 0;
    for (var i=0; i<insStore.getCount(); i++) {
	if (insName == insStore.getAt(i).get("name") && i != rowIndex) {
	    count++;
	}
    }
    return count;
}



function projectEditErrorConfirm(msg, rowIndex, columnIndex) {

    var projectPanel = Ext.getCmp("projectPanel");
    var projectStore = projectPanel.getStore();
    projectPanel.stopEditing(rowIndex, columnIndex);
    Ext.Msg.confirm('Confirm',
                    msg+". Continue editing?",
                    function(btn, text){
                        if (btn == 'yes'){
                            projectPanel.startEditing(rowIndex, columnIndex);
                        } 
                    });

}



function trapEditErrorConfirm(msg, rowIndex, columnIndex) {

    var trapPanel = Ext.getCmp("trapPanel");
    var trapStore = trapPanel.getStore();
    trapPanel.stopEditing(rowIndex, columnIndex);
    Ext.Msg.confirm('Confirm',
                    msg+". Continue editing?",
                    function(btn, text){
                        if (btn == 'yes'){
                            trapPanel.startEditing(rowIndex, columnIndex);
                        } 
                    });

}


function cameraEditErrorConfirm(msg, rowIndex, columnIndex) {

    var cameraPanel = Ext.getCmp("cameraPanel");
    var cameraStore = cameraPanel.getStore();
    cameraPanel.stopEditing(rowIndex, columnIndex);
    Ext.Msg.confirm('Confirm',
                    msg+". Continue editing?",
                    function(btn, text){
                        if (btn == 'yes'){
                            cameraPanel.startEditing(rowIndex, columnIndex);
                        } 
                    });

}


function addArrayIntoTree(projectId) {

    var root = Ext.getCmp("navPanel").getRootNode();
    var projectsNode = root.findChild("text", "Projects");
    var projectNode  = projectsNode.findChild("path", "/Projects/"+projectId);
    if (projectNode != null) {
	var arraysNode  = projectNode.findChild("path", "/Projects/"+projectId+"/Arrays");
	arraysNode.reload(function() {
		arraysNode.expand(false, true,  function() {});    
	    });
    }
}


function addTrapIntoTree(projectId, arrayId) {

    var root = Ext.getCmp("navPanel").getRootNode();
    var projectsNode = root.findChild("text", "Projects");
    var projectNode  = projectsNode.findChild("path", "/Projects/"+projectId);
    var arraysNode  = projectNode.findChild("path", "/Projects/"+projectId+"/Arrays");
    var arrayNode = arraysNode.findChild("path", "/Projects/"+projectId+"/Arrays/"+arrayId);
    if (arrayNode != null) {
	arrayNode.reload(function() {
		arrayNode.expand(false, true,  function() {});    
	    });
    }
}


function addProjectIntoTree(projectId, projectName) {
    var root = Ext.getCmp("navPanel").getRootNode();
    var node = root.findChild("text", "Projects");
    node.reload(function() {
	    node.expand(false, true,  function() {});    
	});
}


function deleteProject(id, name) {

    var msg = "Are you sure you want to remove the project '"+name+"' and all its associated data?";

    Ext.Msg.confirm('Confirm',
                    msg,
                    function(btn, text){
                        if (btn == 'yes'){
                            var wait = Ext.MessageBox.wait("Please wait for deleting the project...", "Status");
			    Ext.Ajax.request({
				    url: "jsp/open-deskTEAM/delete-project.jsp",
				    method: 'GET',
				    params: { id: id },
				    success: function(response, opts) {
					Ext.getCmp("projectPanel").getStore().reload();
					addProjectIntoTree();
					updateProjectMenu();
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        //msg('Error', 'server-side failure with status code ' + response.status);
					msg('Error', 'Encounter a system error: ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}


function deleteArray(arrayId, arrayName, projectId) {

    var msgg = "Are you sure you want to remove the array '"+arrayName+"' and all its associated data?";

    Ext.Msg.confirm('Confirm',
                    msgg,
                    function(btn, text){
                        if (btn == 'yes'){
                            var wait = Ext.MessageBox.wait("Please wait for deleting the array...", "Status");
			    Ext.Ajax.request({
				    url: "jsp/open-deskTEAM/delete-array.jsp",
				    method: 'GET',
					params: { array_id: arrayId, project_id: projectId },
				    success: function(response, opts) {
					Ext.getCmp("arrayPanel").getStore().reload();
					addArrayIntoTree(projectId);
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        //msg('Error', 'server-side failure with status code ' + response.status);
					msg('Error', 'Encounter a system error: ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}


function deleteTrap(trapId, trapName, arrayId, projectId) {

    var msgg = "Are you sure you want to remove the trap '"+trapName+"' and all its associated data?";

    Ext.Msg.confirm('Confirm',
                    msgg,
                    function(btn, text){
                        if (btn == 'yes'){
                            var wait = Ext.MessageBox.wait("Please wait for deleting the trap...", "Status");
			    Ext.Ajax.request({
				    url: "jsp/open-deskTEAM/delete-trap.jsp",
				    method: 'GET',
					params: { trap_id: trapId },
				    success: function(response, opts) {
					Ext.getCmp("trapPanel").getStore().reload();
					addTrapIntoTree(projectId, arrayId);
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        //msg('Error', 'server-side failure with status code ' + response.status);
					msg('Error', 'Encounter a system error: ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}


function deleteEvent(eventId, eventName) {

    var msgg = "Are you sure you want to remove the event '"+eventName+"' and all its associated data?";

    Ext.Msg.confirm('Confirm',
                    msgg,
                    function(btn, text){
                        if (btn == 'yes'){
                            var wait = Ext.MessageBox.wait("Please wait for deleting the event...", "Status");
			    Ext.Ajax.request({
				    url: "jsp/open-deskTEAM/delete-event.jsp",
				    method: 'GET',
					params: { event_id: eventId },
				    success: function(response, opts) {
					Ext.getCmp("eventPanel").getStore().reload();
					//addEventIntoTree(eventId);
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        //msg('Error', 'server-side failure with status code ' + response.status);
					msg('Error', 'Encounter a system error: ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}


function deleteCamera(cameraId) {

    var msgg = "Are you sure you want to remove the selected camera and all its associated data?";

    Ext.Msg.confirm('Confirm',
                    msgg,
                    function(btn, text){
                        if (btn == 'yes'){
                            var wait = Ext.MessageBox.wait("Please wait for deleting the camera...", "Status");
			    Ext.Ajax.request({
				    url: "jsp/open-deskTEAM/delete-camera.jsp",
				    method: 'GET',
					params: { camera_id: cameraId },
				    success: function(response, opts) {
					Ext.getCmp("cameraPanel").getStore().reload();
					//addEventIntoTree(eventId);
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        //msg('Error', 'server-side failure with status code ' + response.status);
					msg('Error', 'Encounter a system error: ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}



function deletePerson(personId, projectId) {

    var msgg = "Are you sure you want to remove the selected person from this project?";

    Ext.Msg.confirm('Confirm',
                    msgg,
                    function(btn, text){
                        if (btn == 'yes'){
                            var wait = Ext.MessageBox.wait("Please wait for deleting the person...", "Status");
			    Ext.Ajax.request({
				    url: "jsp/open-deskTEAM/delete-person.jsp",
				    method: 'GET',
					params: { person_id: personId, project_id: projectId },
				    success: function(response, opts) {
					Ext.getCmp("personPanel").getStore().reload();
					//addEventIntoTree(eventId);
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        //msg('Error', 'server-side failure with status code ' + response.status);
					msg('Error', 'Encounter a system error: ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}


function deleteInstitution(institutionId, projectId) {

    var msgg = "Are you sure you want to remove the selected institution from this project?";

    Ext.Msg.confirm('Confirm',
                    msgg,
                    function(btn, text){
                        if (btn == 'yes'){
                            var wait = Ext.MessageBox.wait("Please wait for deleting the institution...", "Status");
			    Ext.Ajax.request({
				    url: "jsp/open-deskTEAM/delete-institution.jsp",
				    method: 'GET',
					params: { institution_id: institutionId, project_id: projectId },
				    success: function(response, opts) {
					Ext.getCmp("institutionPanel").getStore().reload();
					//addEventIntoTree(eventId);
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        //msg('Error', 'server-side failure with status code ' + response.status);
					msg('Error', 'Encounter a system error: ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}




function createNewProject() {


			var countryStore  = new Ext.data.JsonStore({
				autoLoad: true,
				pruneModifiedRecords: true,
				url : "jsp/open-deskTEAM/country.jsp",
				root : "results",
				fields: ['id', 'name']
			    });
			
                        var countryCombo = new Ext.form.ComboBox({
				store: countryStore,
				id: 'projectCountry',
				name: 'projectCountry',
				displayField:'name',
				valueField: 'id',
				typeAhead: true,
				mode: 'local',
				triggerAction: 'all',
				emptyText:'Select a country',
				selectOnFocus:true,
				width: 130,	
				allowBlank: true,
				fieldLabel: 'Country',
				editable : false,
				listeners: {
				    select: function(obj, record, ind){
				    }
				}
			    });



                       var form = new Ext.form.FormPanel({
                             labelWidth: 80,
                             bodyStyle:'padding:15px',
                             width: 300,
                             labelPad: 10,
			     frame       : true,
                             defaultType: 'textfield',
                             defaults: {
                                 // applied to each contained item
                                 width: 220,
                                 msgTarget: 'side'
                             },
                             layoutConfig: {
                                 // layout-specific configs go here
                                 labelSeparator: ''
                             },
                             items:[{
                                       fieldLabel: 'Name',
                                       name: 'projectName',
                                       id: 'projectName',
                                       allowBlank: false
				   },{
                                       fieldLabel: 'Abbreviation',
                                       name: 'projectAbbrev',
                                       id: 'projectAbbrev',
                                       allowBlank: false
				   },{
                                       fieldLabel: 'Objective',
				       xtype: 'textarea',
                                       name: 'objective',
                                       id: 'objective',
                                       allowBlank: true
				   },{
                                       fieldLabel: 'Latitude',
				       xtype: 'numberfield',
                                       name: 'projectLatitude',
                                       id: 'projectLatitude',
                                       allowBlank: true,
				       maxValue: 180,
				       minValue: -180,
				       decimalPrecision : 5
				   },{
                                       fieldLabel: 'Longitude',
				       xtype: 'numberfield',
                                       name: 'projectLogitude',
                                       id: 'projectLongitude',
                                       allowBlank: true,
				       maxValue: 180,
				       minValue: -180,
				       decimalPrecision : 5
				   }, countryCombo,{
                                       fieldLabel: 'Bait Used?',
				       xtype: 'combo',
                                       name: 'useBait',
                                       id: 'useBait',
				       allowBlank: false,
				       store: new Ext.data.SimpleStore({
					       fields: ['name'],
					       data : [
						       ['Yes'],
						       ['No']
						       ]
					   }),
				       displayField:'name',
				       typeAhead: true,
				       mode: 'local',
				       triggerAction: 'all',
				       editable : false,
				       value: 'No',
				       lazyRender:true,
				       listClass: 'x-combo-list-small',
				       listeners: {
                                           select: function(obj, record, ind){
					       var value = Ext.getCmp("useBait").value;
					       var bait = Ext.getCmp("bait");
					       if (value == 'Yes') {
						   bait.setVisible(true);
					       } else {
						   bait.setVisible(false);
					       }
					   }
				       }
				   },{
                                       fieldLabel: 'Bait',
                                       name: 'bait',
                                       id: 'bait',
				       hidden: true,
                                       allowBlank: true
				   }]
                        });

			var win = new Ext.Window({
                              id          : 'newProjectWindow',
                              title       : 'Create New Project',
                              layout      : 'fit',
                              width       : 380,
                              height      : 370,
                              closeAction : 'destroy',
                              plain       : true,
                              modal       : true,
			      frame       : true,
                              items       : [ form ],
			      buttons     : [{
                                               text     : 'Create',
			                       id       : 'createProjectButton',
                                               handler  : function(){
					           if (form.getForm().isValid()) {

						       var projName = Ext.getCmp("projectName").getValue();
						       var projAbbrev = Ext.getCmp("projectAbbrev").getValue();
						       var projLatitude = Ext.getCmp("projectLatitude").getValue();
						       var projLongitude = Ext.getCmp("projectLongitude").getValue();
						       var projCountry = Ext.getCmp("projectCountry").getValue();
						       var projObjective = Ext.getCmp("objective").getValue();
						       var projUseBait = Ext.getCmp("useBait").getValue();
						       var projBait = Ext.getCmp("bait").getValue();

						       var record = Ext.getCmp("projectCountry").getStore().getById(projCountry);

						       var countryName = record != null? record.get('name') : null;

						       var projectPanel = Ext.getCmp("projectPanel");
						       var projectStore = projectPanel.getStore(); 

						       // validate project name and abbreviation
						       var valid = true;
						       if (projName.length > 25) {
							   msg("Error", "Project name has more than 25 characters: '"+projName+"'");
							   valid = false;
						       } else if (projAbbrev.length != 3) {
							   msg("Error", "Abbreviation must use three letters: '"+projAbbrev+"'");
							   valid = false;
						       } else if (getProjectAbbrevCount(projAbbrev, projectStore, -1)>0) {
							   msg("Error", "Duplicate project abbreviation: '"+projAbbrev+"'");
							   valid = false;
						       }

						       if (!valid) {
							   return false;
						       }

						       var projId = getProjectId();
						       // create a new project
						       Ext.Ajax.request({
							           url: "jsp/open-deskTEAM/create-project.jsp",
								   method: 'GET',
								   params: { 
								       id: projId, 
								       name: projName, 
								       abbrev: projAbbrev, 
								       lat: projLatitude,  
								       lon: projLongitude, 
								       country: projCountry,
								       objective: projObjective,
								       useBait: projUseBait,
								       bait: projBait
								   },
								   success: function(response, opts) {
								       var project = 
									   new projectStore.recordType({
										   id: projId,
										   name: projName,
										   abbrev: projAbbrev,
										   lat: projLatitude,  
										   lon: projLongitude, 
										   country_id: projCountry,
										   country_name: countryName,
										   objective: projObjective,
										   useBait: projUseBait,
										   bait: projBait
									       });
								       projectStore.add(project);
								       projectPanel.getView().refresh();
								       newProjectIndex = projectStore.getCount()-1;

				                                       // change the tree node
				                                       addProjectIntoTree(projId, projName);

				                                       // unset flag
				                                       newProject = false;
								       win.destroy();

								       updateProjectMenu();

							           },
								   failure: function(response, opts) {
								        //msg('Error', 'server-side failure with status code ' + response.status);
								        msg('Error', 'Encounter a system error: ' + response.status);
									newProject = false;
									return false;
							       }
							   });

						   }
					       }
				             },{
		                               text     : 'Close',
		                               id       : 'closeProjectButton',
					       handler  : function(){
					            win.hide();
						    win.destroy();
		                               }
				            }]	 
                        });
                        win.show();

}



////////////////////////////////////////////////////////////////////


function openCameraTrapExcelWindow() {

    var packageWin = Ext.getCmp('excelWin');
    if (packageWin != null) { 
	packageWin.destroy();
	packageWin = null;
    }

    if (packageWin == null) { 

	var root = new Ext.tree.AsyncTreeNode({
		id:'0',
		//path: homepath,
		//text: homepath
		path: "/",
		text: ""
	    });

	// create tree panel
	var data = new Ext.tree.TreeLoader({
		url:'jsp/get-directory.jsp?'}); 

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

	var packageStore = new Ext.data.JsonStore({
            root: 'result',
            fields: [
	        'name'
	    ],

            proxy: new Ext.data.HttpProxy({
		 url: 'jsp//open-deskTEAM/get-excel.jsp?' 
            })
         });


        var packageCM = new Ext.grid.ColumnModel([
	        {
                   id:'common',
                   header: "File Name",
                   dataIndex: 'name',
                   width: 290
                }]);

	var packageGridPanel = new Ext.grid.GridPanel({
		id: 'excelid',
		store: packageStore,
		cm: packageCM,
		//width:270,
		width:300,
		height:290,
		frame:false,
		hidden:false,
		viewConfig: {
		    forceFit:false
		}
	    });

	packageWin = new Ext.Window({
		id          : 'excelWin',
		title       : 'Select An Excel File',
		layout      : 'table',
		layoutConfig: {
		    columns: 2
		},
		width       : 570,
		height      : 350,
		closeAction :'hide',
		plain       : true,
		modal       : true,
		items       : [ 
			       fileTreePanel,
			       packageGridPanel
			      ],
		bbar        : [
			       '->',
	                      {
				  xtype: 'button',
				  text:  'Load',
				  pressed: true,
				  handler: function() {
				      var node = Ext.getCmp("filetree").getRootNode().getOwnerTree().getSelectionModel().getSelectedNode();
				      var record = Ext.getCmp('excelid').getSelectionModel().getSelected();
				      if (node == null || record == null) {
					  msg("Error", "No Excel file is selected.");
				      } else {
					  packageWin.hide();
					  var waitBox = Ext.MessageBox.wait("Please wait. We are loading the excel file.", 
									    "Status");

					  Ext.Ajax.request({
				                url: "/deskTEAM/jsp/open-deskTEAM/load-excel.jsp",
				                method: 'GET',
				                params: { 
							 projectId: currentProjectId,
							 arrayId: currentArrayId,
							 path: node.getPath('text')+"/"+record.get('name')
						       },
						success: function(response, opts) {
						       waitBox.hide();
						       var j = Ext.decode(response.responseText);
						       if (j.success) {
							     // reload the node camera trap array
							     // show list of arrays in the panel
							     Ext.getCmp("arrayPanel").getStore().reload();
							     Ext.getCmp("trapPanel").getStore().reload();
					                     addArrayIntoTree(currentProjectId);

							} else {
							     msg("Error", j.message);
						        }
						 },
				                 failure: function(response, opts) {
						 }
			                  });

					  /*
					  $.getJSON( "/deskTEAM/jsp/open-deskTEAM/load-excel.jsp",
	                                             { 
							 projectId: currentProjectId,
							 arrayId: currentArrayId,
							 path: node.getPath('text')+"/"+record.get('name')
						     },
                                                     function(j) {


							 waitBox.hide();
							 if (j.success) {
							     // reload the node camera trap array
							     // show list of arrays in the panel
							     Ext.getCmp("arrayPanel").getStore().reload();
							     Ext.getCmp("trapPanel").getStore().reload();
					                     addArrayIntoTree(currentProjectId);

							 } else {
							     msg("Error", j.message);
							 }

						     });
					  */
				      }
				  }
			      },{
				   text: '&nbsp;'
			       },{
				   xtype: 'button',
				   text:  'Cancel',
				   pressed: true,
				   handler: function() {
				       packageWin.hide();
				   }
			       },{
				   text: '&nbsp;'
			       }
			       ]
	    });

    }

    packageWin.show();
        
    var fileRoot = Ext.getCmp("filetree").getRootNode();
    var selectionModel = fileRoot.getOwnerTree().getSelectionModel();
    selectionModel.on("selectionchange", 
		      function(m, node) {
                          packageStore.load.defer(100, packageStore,[ {params:{path: node.getPath('text')}}]);
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


}




////////////////////////////////////////////////////////////////////


function openCameraExcelWindow() {

    var packageWin = Ext.getCmp('excelWin');
    if (packageWin != null) { 
	packageWin.destroy();
	packageWin = null;
    }

    if (packageWin == null) { 

	var root = new Ext.tree.AsyncTreeNode({
		id:'0',
		//path: homepath,
		//text: homepath
		path: "/",
		text: ""
	    });

	// create tree panel
	var data = new Ext.tree.TreeLoader({
		url:'jsp/get-directory.jsp?'}); 

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

	var packageStore = new Ext.data.JsonStore({
            root: 'result',
            fields: [
	        'name'
	    ],

            proxy: new Ext.data.HttpProxy({
		 url: 'jsp//open-deskTEAM/get-excel.jsp?' 
            })
         });


        var packageCM = new Ext.grid.ColumnModel([
	        {
                   id:'common',
                   header: "File Name",
                   dataIndex: 'name',
                   width: 290
                }]);

	var packageGridPanel = new Ext.grid.GridPanel({
		id: 'excelid',
		store: packageStore,
		cm: packageCM,
		//width:270,
		width:300,
		height:290,
		frame:false,
		hidden:false,
		viewConfig: {
		    forceFit:false
		}
	    });

	packageWin = new Ext.Window({
		id          : 'excelWin',
		title       : 'Select An Excel File',
		layout      : 'table',
		layoutConfig: {
		    columns: 2
		},
		width       : 570,
		height      : 350,
		closeAction :'hide',
		plain       : true,
		modal       : true,
		items       : [ 
			       fileTreePanel,
			       packageGridPanel
			      ],
		bbar        : [
			       '->',
	                      {
				  xtype: 'button',
				  text:  'Load',
				  pressed: true,
				  handler: function() {
				      var node = Ext.getCmp("filetree").getRootNode().getOwnerTree().getSelectionModel().getSelectedNode();
				      var record = Ext.getCmp('excelid').getSelectionModel().getSelected();
				      if (node == null || record == null) {
					  msg("Error", "No Excel file is selected.");
				      } else {
					  packageWin.hide();
					  var waitBox = Ext.MessageBox.wait("Please wait. We are loading the excel file.", 
									    "Status");
					  $.getJSON( "/deskTEAM/jsp/open-deskTEAM/load-camera-excel.jsp",
	                                             { 
							 projectId: currentProjectId,
							 path: node.getPath('text')+"/"+record.get('name')
						     },
                                                     function(j) {
							 waitBox.hide();
							 if (j.success) {
							     // reload the node camera trap array
							     // show list of arrays in the panel
							     Ext.getCmp("cameraPanel").getStore().reload();
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
				   text:  'Cancel',
				   pressed: true,
				   handler: function() {
				       packageWin.hide();
				   }
			       },{
				   text: '&nbsp;'
			       }
			       ]
	    });

    }

    packageWin.show();
        
    var fileRoot = Ext.getCmp("filetree").getRootNode();
    var selectionModel = fileRoot.getOwnerTree().getSelectionModel();
    selectionModel.on("selectionchange", 
		      function(m, node) {
                          packageStore.load.defer(100, packageStore,[ {params:{path: node.getPath('text')}}]);
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


}

