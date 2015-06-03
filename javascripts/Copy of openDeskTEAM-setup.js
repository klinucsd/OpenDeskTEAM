
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
var newProjectIdex;

Ext.onReady(function(){

    Ext.QuickTips.init();
    docHeight = getDocHeight();


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
			    width: 200,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Abbreviation",
			    dataIndex: 'abbrev',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.TextField({
				    allowBlank: false
				})
			},{
			    header: "Latitude",
			    dataIndex: 'lat',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return null;
			       }
			    }
			},{
			    header: "Longitude",
			    dataIndex: 'long',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.NumberField({
				    allowBlank: true,
				    maxValue: 180,
				    minValue: -180
				}),
			    renderer: function(value) {
			       if (value == 0) {
				   return null;
			       }
			    }
			},{
			    header: "Country",
			    dataIndex: 'country',
			    hidden: false,
			    width: 100,
			    editor: new Ext.form.TextField({
				    allowBlank: true
				})
			},{
			    header: "Organization",
			    dataIndex: 'org',
			    hidden: false,
			    width: 150,
			    editor: new Ext.form.TextField({
				    allowBlank: true
				})
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
                         {name: 'long', type: 'float'},
                         {name: 'country', type: 'string'},
                         {name: 'org', type: 'string'}
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

    var projectPanel = new Ext.grid.EditorGridPanel({
	    id: 'projectPanel',
	    store: projectStore,
	    cm: projectCM,
	    width: '100%',
	    height: 1000,
	    frame:false,
	    hidden: false,
	    autoExpandColumn: 6,
	    autoExpandMin: 200,
	    title: 'Projects',                                                                                       
	    viewConfig: {
		forceFit:false
	    },
	    loadMask: true,
	    bbar: [{
		    text: 'New Project',
		    icon: 'images/icons/new_project.png',
		    handler: function(){
			newProject = true;
			var projectId = getProjectId();
			var projectName = getNewProjectName();
                        var projectAbbrev = getNewProjectAbbrev();
			Ext.Ajax.request({
				url: "jsp/open-deskTEAM/create-project.jsp",
				method: 'GET',
				    params: { id: projectId, name: projectName, abbrev: projectAbbrev },
				success: function(response, opts) {

				    var project = 
					new projectStore.recordType({
						id: projectId,
						name: projectName,
						abbrev: projectAbbrev
					    });
				    projectStore.add(project);
				    projectStore.commitChanges();
				    projectPanel.getView().refresh();
				    newProjectIndex = projectStore.getCount()-1;
				    projectPanel.startEditing(newProjectIndex, 1);

				    // change the tree node
				    addProjectIntoTree(projectId, projectName);

				},
				failure: function(response, opts) {
				    msg('Error', 'server-side failure with status code ' + response.status);
				    return false;
				}
			    });
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
		    if (field == 'name') {
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
		}

	    }
	});

    projectStore.load();


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
			} else if (node.attributes.path == '/People') {
			    Ext.getCmp('rightPanel').getLayout().setActiveItem(1);
			} else if (node.attributes.path == '/Cameras') {
			    Ext.getCmp('rightPanel').getLayout().setActiveItem(2);
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
					 function() {});   

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
			      projectPanel
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
		}]
	});

        mainPanel.show();

        Ext.EventManager.onWindowResize(function() {
                Ext.getCmp('mainPanel').setHeight(Ext.getBody().getViewSize().height-31);
                Ext.getCmp('mainPanel').doLayout();
            });

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
				        wait.hide();
                                    },
                                    failure: function(response, opts) {
                                        wait.hide();
                                        msg('Error', 'server-side failure with status code ' + response.status);
                                        return false;
                                    }
                                });
			}
		    });

}

