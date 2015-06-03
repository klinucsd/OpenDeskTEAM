
Ext.grid.PagedRowNumberer = function(config){
    Ext.apply(this, config);
    if(this.rowspan){
        this.renderer = this.renderer.createDelegate(this);
    }
};


Ext.grid.PagedRowNumberer.prototype = {
    header: "",
    width: 35,
    sortable: false,
    fixed:false,
    hideable: false,
    dataIndex: '',
    id: 'numberer',
    rowspan: undefined,
    renderer : function(v, p, record, rowIndex, colIndex, store){
        if(this.rowspan){
            p.cellAttr = 'rowspan="'+this.rowspan+'"';
        }
        var i = store.lastOptions.params.start;
        if (isNaN(i)) {
            i = 0;
        }
        i = i + rowIndex + 1;
        i = Number(i).toLocaleString(); //May not work in all browsers.
        return i;
    }
}; 


function createCameraTrappingExcelGridPane1(siteval, protocolVal, id, filename, errNum, trapName, event) {

   var photoDataLoaded = false;
   var damagedTrapLoaded = false;

   var tabs = new Ext.TabPanel({
        activeTab: 0,
        width:940,
        height:477,
        plain:true,
        //tabPosition: 'bottom',
	id: 'exceltab',
        defaults:{autoScroll: true},

        listeners :  {
	   tabchange: function(p, t) {
	       if (t.title == 'Camera Trap Data') {

	       }
	   }
	}

    });


    var yesnoStore = new Ext.data.SimpleStore({
                                   fields: ['name'],
                                   data : [
                                              [true],
                                              [false]
                                          ]
                             });





    var ctCameraSerialStore = new Ext.data.JsonStore({
	    autoLoad: true,
	    pruneModifiedRecords: true,
	    url : "jsp/camera-serial-number.jsp?siteId="+siteId,
	    root : "results",
	    fields: ['name']
	});



    // setting person store
    var ctSetPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					

    // picking person store
    var ctPickPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });


    
   var trapDataCM = new Ext.grid.ColumnModel([
		new Ext.grid.PagedRowNumberer(), 
	        {
                   id:'common',
                   header: "Site",
                   dataIndex: 'Site',
		   hidden: true,
                   width: 100
                 },{
                   header: "Sampling Event",
                   dataIndex: 'Sampling_Period',
                   width: 100
                },{
                   header: "Camera Trap Name",
                   dataIndex: 'Camera_Trap_Point_ID',
                   width: 120
                },{
                   header: "Camera Serial Number",
                   dataIndex: 'Camera_Serial_Number',
                   width: 120,
		   editor: new Ext.form.ComboBox({
			   store: ctCameraSerialStore,
			   fieldLabel: 'Camera Serial Number',
			   name: 'cameraSerial',
			   allowBlank: false,    
			   displayField:'name',
			   typeAhead: true,
			   mode: 'local',
			   triggerAction: 'all',
			   emptyText:'Select a camera serial number',
			   selectOnFocus:true,
			   width: 130,
			   editable : true
		       })
                },{
                   header: "Memory Card Serial Number",
                   dataIndex: 'Memory_Card_Serial_Number',
                   width: 150,
                   editor: new Ext.form.TextField({
                      allowBlank: true
                   })
		},{
		   header: "Start Date",
                   dataIndex: 'Start_Date',
                   width: 80,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
		},{
		   header: "Start Time",
                   dataIndex: 'Start_Time',
                   width: 80
                },{
		   header: "End Date",
                   dataIndex: 'End_Date',
                   width: 80,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
		},{
		   header: "End Time",
                   dataIndex: 'End_Time',
                   width: 80
                },{
                   header: "Setup Person",
                   dataIndex: 'Set_Person',
                   width: 120,
		   editor: new Ext.form.ComboBox({
			   store: ctSetPersonStore,
			   id: 'setter',
			   displayField:'name',
			   typeAhead: true,
			   mode: 'local',
			   triggerAction: 'all',
			   emptyText:'Select a person',
			   selectOnFocus:true,
			   width: 130,	
			   allowBlank: false,
			   fieldLabel: 'Person Setting up the Camera',
			   editable : false,
			   listeners: {
			       select: function(obj, record, ind){
			       }
			   }
		       })
                },{
                   header: "Pickup Person",
                   dataIndex: 'Pick_Person',
                   width: 120,					
		   editor: new Ext.form.ComboBox({
			   store: ctPickPersonStore,
			   id: 'picker',
			   displayField:'name',
			   typeAhead: true,
			   mode: 'local',
			   triggerAction: 'all',
			   emptyText:'Select a person',
			   selectOnFocus:true,
			   width: 130,	
			   allowBlank: false,
			   fieldLabel: 'Person Picking up the Camera',
			   editable : false,
			   listeners: {
			       select: function(obj, record, ind){
			       }
			   }
		       })
                },{
                   header: "Camera Working?",
                   dataIndex: 'Camera_Working_',
                   width: 120,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
                },{
                   header: "Camera Trap Missing?",
                   dataIndex: 'Camera_Trap_Missing_',
                   width: 120,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
                },{
                   header: "Case Damage?",
                   dataIndex: 'Case_Damage_',
                   width: 120,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
                },{
                   header: "Camera Damage?",
                   dataIndex: 'Camera_Damage_',
	           width: 120,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
                },{
                   header: "Card Damage?",
                   dataIndex: 'Card_Damage_',
                   width: 120,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
                },{
                   header: "Notes",
                   dataIndex: 'Notes',
                   width: 450,
                   editor: new Ext.form.TextField({
                      allowBlank: true
                   })
	        }

              ]);


    var trapDataStore = new Ext.data.JsonStore({

            root: 'result',
            totalProperty: 'totalCount',
            remoteSort: true,
            fields: [
	        'Site',
		'Sampling_Period',
                'Camera_Trap_Point_ID',
                'Camera_Trap_Number',
                'Camera_Serial_Number',
                'Memory_Card_Serial_Number',
                {name: 'Start_Date', type: 'date', dateFormat: 'Y-m-d' },
                'Start_Time', 
                {name: 'End_Date', type: 'date', dateFormat: 'Y-m-d' },
                'End_Time',
		'Set_Person',
		'Pick_Person',
                'Camera_Working_',
                'Camera_Trap_Missing_',
                'Case_Damage_',
                'Camera_Damage_',
                'Card_Damage_',
                'Notes'
             ],
	
             proxy: new Ext.data.HttpProxy({
		 url: 'jsp/camera-trap-data.jsp?trapName='+trapName+'&event='+event 
             })
         });

         trapDataStore.setDefaultSort('Sid', 'DESC');

        // create the editor grid
        var trapDataPane = new Ext.grid.EditorGridPanel({
	    id: 'trapdatagrid',
            store: trapDataStore,
            cm: trapDataCM,
            width:940,
            height:470,
            frame:false,
	    hidden: false,
	    title: 'Camera Trap Data',
	    sm : new Ext.grid.CheckboxSelectionModel(),
            viewConfig: {
                forceFit:false
            },
	    loadMask: true,
            bbar: new Ext.PagingToolbar({
	       id: 'pager',
               pageSize: 100,
               store: trapDataStore,
               displayInfo: true,
               displayMsg: 'Displaying records {0} - {1} of {2}   ',
               emptyMsg: "No records to display"
            }),


	   listeners :  {

                validateedit: function(obj) {

		    var value = obj.value;
		    var field = obj.field;

		},

		afteredit: function(obj) {

		    var value = obj.value;
		    var field = obj.field;

		    if (field == 'Pick_Person') {
			var picker = Ext.getCmp('picker');
			value = picker.store.getAt(picker.selectedIndex).get('id');
		    } else if (field == 'Set_Person') {
			var setter = Ext.getCmp('setter');
                        value = setter.store.getAt(setter.selectedIndex).get('id');
                    } 

		    // get sampling event
		    var event = obj.record.get("Sampling_Period");

		    // get camera trap id
		    var trapName = obj.record.get("Camera_Trap_Point_ID");

		    $.getJSON("jsp/edit-camera-trap-data.jsp",
	                      { 
				 trapName: trapName,
				 event: event, 
				 field: field,
				 value: value
			      },
                              function(j) {
				  if (j.success) {
				      //msg("Status", "The data has been updated.");	      
				  } else {
				      msg("Status", j.message);
				  }
			      });
                 }

	   }


        });

	var pager = Ext.getCmp('pager');
	pager.store.sortInfo = { field: 'Id', direction: 'DESC' };

        // trigger the data store load
        trapDataStore.load.defer(100, trapDataStore,[ {params:{start:0, limit:100}}]);


   var photoDataCM = new Ext.grid.ColumnModel([
		new Ext.grid.PagedRowNumberer(), 
	        {
                   header: "Sampling Event",
                   dataIndex: 'Sampling_Period',
                   width: 100,
		   renderer: function(value) {
                        var tmp = value + "";
                        var index = tmp.indexOf(".");
                        if (index != -1) {
                           var fst = tmp.substring(0, index);
                           var snd = tmp.substring(index+1);

                           if (snd.match(/^0[1-4]$/g) != null && fst.match(/^\d\d\d\d$/g) != null) {
                               if (parseInt(fst) < 2003 || parseInt(fst) > (new Date()).getFullYear()) {
                                  return '<span style="color:red;">'+value+'</span>';
                               } else {
                                  return value;
                               }
                           } else {
                               return '<span style="color:red;">'+value+'</span>';
                           }
                        } else {
                           return '<span style="color:red;">'+value+'</span>';
                        }
                   }
                 },{
                   header: "Camera Trap",
                   dataIndex: 'Camera_Trap_Point_ID',
                   width: 100
                 },{
                   header: "Date",
                   dataIndex: 'Date',
                   width: 80,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
                },{
                   header: "Time",
                   dataIndex: 'Time',
                   width: 60,
		   renderer: function (value) {
                       var res = value;
                       if (value != null) {
                           try {
                               res = value.dateFormat('H:i:s')
                           } catch (e) {}
                       }
                       return res;
                   }
                },{
                   header: "Raw Filename",
                   dataIndex: 'Raw_Filename',
                   width: 100
                },{
                   header: "Team Filename",
                   dataIndex: 'Team_Filename',
                   width: 240,
		   hidden: true
                },{
                   header: "Photo Type",
                   dataIndex: 'Type',
                   width: 80
                },{
                   header: "Genus",
                   dataIndex: 'Genus',
                   width: 80
                },{
                   header: "Species",
                   dataIndex: 'Species',
                   width: 80
                },{
                   header: "Binomial",
                   dataIndex: 'Binomial',
                   width: 100,
		   hidden: true
                },{
                   header: "Number of Animals",
                   dataIndex: 'Number_of_Animals',
                   width: 110,
                   renderer: function(value) {
                       if (value < 1 || value > 100) {
                            return '<span style="color:red;">'+value+'</span>';
                       } else {
                            return value;
                       }
                   }
                },{
                   header: "Identified By",
                   dataIndex: 'Identified_By',
                   width: 150	   
                },{
                   header: "Notes",
                   dataIndex: 'Notes',
                   width: 300
		   /*
		   ,
                   editor: new Ext.form.TextField({
                      allowBlank: true
                   })
		   */
	        }

              ]);


    var photoDataStore = new Ext.data.JsonStore({

            root: 'result',
            totalProperty: 'totalCount',
            remoteSort: true,
            fields: [
	        'Sampling_Period',
		'Camera_Trap_Point_ID',
                {name: 'Date', type: 'date', dateFormat: 'Y-m-d' },
                {name: 'Time', type: 'date', dateFormat: 'H:i:s' },
		'Raw_Filename',
		'Team_Filename',
		'Type',
		'Genus',
		'Species',
		'Binomial',
		'Number_of_Animals',
		'Identified_By',
		'Notes',
		'trg',
		'flash',
		'exposure',
		'takentime',
		'temperature'
             ],
	
             proxy: new Ext.data.HttpProxy({
		     url: 'jsp/photo-data.jsp?trapName='+trapName+'&event='+event 
             })
         });

         photoDataStore.setDefaultSort('Sid', 'DESC');

        // create the editor grid
        var photoDataPane = new Ext.grid.EditorGridPanel({
	    id: 'photodatagrid',
            store: photoDataStore,
            cm: photoDataCM,
            width:940,
            height:470,
            frame:false,
	    hidden: false,
	    title: 'Photo Data',
	    sm : new Ext.grid.CheckboxSelectionModel(),
            viewConfig: {
                forceFit:false
            },
	    loadMask: true,
            bbar: new Ext.PagingToolbar({
	       id: 'pager1',
               pageSize: 100,
               store: photoDataStore,
               displayInfo: true,
               displayMsg: 'Displaying records {0} - {1} of {2}   ',
               emptyMsg: "No records to display"
            }),


	   listeners :  {

                validateedit: function(obj) {
		    var value = obj.value;
		    var field = obj.field;
		},

		activate: function() {
		    if (!photoDataLoaded) { 
		       photoDataStore.load.defer(500, photoDataStore,[ {params:{start:0, limit:100}}]);
		       photoDataLoaded = true;
	            } else {
		    } 
		},

		afteredit: function(obj) {

                    //alert(obj.field+"  "+obj.value+"  "+obj.originalValue+"  "+obj.row+"   "+obj.column+"  "+obj.record.get("Id"));
		    var value = obj.value;
		    var field = obj.field;

		    var proxy = new Ext.data.HttpProxy({
                          url: 'jsp/update-excel.jsp?'+
			       'site='+siteval+
			       '&columnname='+field+	
			       '&value='+escape(value)+
			       '&oldvalue='+escape(obj.originalValue)+
			       '&id='+id+
			       '&filename='+filename+
			       '&row='+obj.row+
			       '&column='+obj.column+
			       '&protocol='+escape("Camera Trapping")+
			       '&sheet='+this.store.reader.jsonData.sheet
                       });
                   
                    proxy.load(null, null, function() { } );

                 },

		 cellclick :  function(grid, rowIndex, columnIndex, e) {
		    
		    selectedRow = rowIndex;

		    var record = grid.getStore().getAt(rowIndex);  // Get the Record

		    // get event name
                    var eventName = grid.getColumnModel().getDataIndex(1); 
                    var event = record.get(eventName);

		    // get trap name
                    var trapName = grid.getColumnModel().getDataIndex(2); 
                    var trap = record.get(trapName);

		    var arrays = trap.split("-");

		    // get raw filename
		    var fileName = grid.getColumnModel().getDataIndex(5); 
                    var file = record.get(fileName);

		    // get photo type
		    var photoType = record.get('Type');

		    var path = "/"+siteName+"/"+event+"/Array"+arrays[2]+"/"+trap+"/"+file;

                    var iPane  = Ext.getCmp('ImageViewer');
		    iPane.setText(path.replace(/\//g, " : "));

		    var img = document.getElementById("imageview");
		    img.src="/deskTEAM/image-repository"+escape(path)
		    zoomreset();

		    // refresh species UI
		    var sPane  = Ext.getCmp('speciesPanel');
		    sPane.setVisible(true);

		    $('#imgName').html(file);
		    $('#Trg').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+record.get('trg')+"</span>");
		    $('#Flash').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+record.get('flash')+"</span>");
                    $('#TakenTime').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+record.get('takentime')+"</span>");
		    //$('#Exposure').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+record.get('exposure')+"</span>");
		    $('#Tmp').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+record.get('temperature')+"</span>");

		    if (photoType == '' || photoType == 'Animal') {
		        var ugenus = Ext.getCmp('ugenus');
		        if (ugenus == null) {
			    insertSpeciesUI();
		        } 

			if (photoType == 'Animal') {
			    Ext.getCmp('typeCombo').setValue('Animal');
			    Ext.getCmp('ugenus').setValue(record.get('Genus'));
			    Ext.getCmp('uspecies').setValue(record.get('Species'));
			    Ext.getCmp('unumber').setValue(record.get('Number_of_Animals'));
			    Ext.getCmp('idperson').setValue(record.get('Identified_By'));
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
                        Ext.getCmp('idperson').setValue(record.get('Identified_By'));
			Ext.getCmp('idperson').clearInvalid();
			// remove species panel   
			removeSpeciesUI();
		    }
				
	            sTrap = trap;	
	            sEvent = event;
	            sRaw = file;
		    sArray = "Array"+arrays[2];

		    // reload gallery panel
		    if (!inGallery(sEvent, sTrap, sRaw)) {
		        var galleryStore = Ext.getCmp("gallery").store;
		        galleryStore.load.defer(100, 
					        galleryStore,
					        [{
					           params:{  
							 trapName:sTrap,
						 	 event:sEvent,
							 raw:sRaw,
							 path:escape(path)
						      }
					        }]);
		    }

		    // highlight selected tree node
		    highlightTreeNode(sEvent, sArray, sTrap, sRaw);

                 }
		 
	   }


        });

	pager = Ext.getCmp('pager1');
	pager.store.sortInfo = { field: 'id', direction: 'ASC' };

        tabs.add(trapDataPane);
        tabs.add(photoDataPane);

        return tabs;

}



function createCameraTrappingExcelGridPane2(siteval, protocolVal, id, filename, errNum) {

   var photoDataLoaded = false;
   var damagedTrapLoaded = false;

   var tabs = new Ext.TabPanel({
        activeTab: 0,
        width:740,
        height:477,
        plain:true,
        //tabPosition: 'bottom',
	id: 'exceltab',
        defaults:{autoScroll: true},

        listeners :  {
	   tabchange: function(p, t) {
	       if (t.title == 'Camera Trap Data') {

	       }
	   }
	}

    });


    var yesnoStore = new Ext.data.SimpleStore({
                                   fields: ['name'],
                                   data : [
                                              ['YES'],
                                              ['NO']
                                          ]
                             });


   var trapDataCM = new Ext.grid.ColumnModel([
		new Ext.grid.PagedRowNumberer(), 
	        {
                   id:'common',
                   header: "Site",
                   dataIndex: 'Site',
		   hidden: true,
                   width: 100
                 },{
                   header: "Sampling Event",
                   dataIndex: 'Sampling_Period',
                   width: 100
                },{
                   header: "Camera Trap Point ID",
                   dataIndex: 'Camera_Trap_Point_ID',
                   width: 120
                },{
		    /*
                   header: "Camera Trap Number",
                   dataIndex: 'Camera_Trap_Number',
                   width: 120,
		   renderer: function(value) {
                       if (value < 0 || value > 100) {
                            return '<span style="color:red;">'+value+'</span>';
                       } else {
                            return value;
                       }
                   }
                },{
		    */
                   header: "Camera Serial Number",
                   dataIndex: 'Camera_Serial_Number',
                   width: 120
                },{
                   header: "Memory Card Serial Number",
                   dataIndex: 'Memory_Card_Serial_Number',
                   width: 150,
		   renderer: function(value) {
                       if (value < 0 || value > 1000) {
                            return '<span style="color:red;">'+value+'</span>';
                       } else {
                            return value;
                       }
                   }
		},{
		   header: "Start Date",
                   dataIndex: 'Start_Date',
                   width: 80,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
		},{
		   header: "Start Time",
                   dataIndex: 'Start_Time',
                   width: 80
                },{
		   header: "End Date",
                   dataIndex: 'End_Date',
                   width: 80,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
		},{
		   header: "End Time",
                   dataIndex: 'End_Time',
                   width: 80
                },{
                   header: "Setup Person",
                   dataIndex: 'Set_Person',
                   width: 120
                },{
                   header: "Pickup Person",
                   dataIndex: 'Pick_Person',
                   width: 120
                },{
                   header: "Camera Working?",
                   dataIndex: 'Camera_Working_',
                   width: 120
                },{
                   header: "Camera Trap Missing?",
                   dataIndex: 'Camera_Trap_Missing_',
                   width: 120
                },{
                   header: "Case Damage?",
                   dataIndex: 'Case_Damage_',
                   width: 120
                },{
                   header: "Camera Damage?",
                   dataIndex: 'Camera_Damage_',
	           width: 120
                },{
                   header: "Card Damage?",
                   dataIndex: 'Card_Damage_',
                   width: 120
                },{
                   header: "Notes",
                   dataIndex: 'Notes',
                   width: 450
	        }

              ]);


    var trapDataStore = new Ext.data.JsonStore({

            root: 'result',
            totalProperty: 'totalCount',
            remoteSort: true,
            fields: [
	        'Site',
		'Sampling_Period',
                'Camera_Trap_Point_ID',
                'Camera_Trap_Number',
                'Camera_Serial_Number',
                'Memory_Card_Serial_Number',
                {name: 'Start_Date', type: 'date', dateFormat: 'Y-m-d' },
                'Start_Time',
                {name: 'End_Date', type: 'date', dateFormat: 'Y-m-d' },
                'End_Time', 
		'Set_Person',
		'Pick_Person',
                'Camera_Working_',
                'Camera_Trap_Missing_',
                'Case_Damage_',
                'Camera_Damage_',
                'Card_Damage_',
                'Notes'
             ],
	
             proxy: new Ext.data.HttpProxy({
                 url: 'jsp/excel-camera-trap-data.jsp?site='+siteval+'&protocol='+protocolVal+'&id='+id+'&filename='+filename 
             })
         });

         trapDataStore.setDefaultSort('Sid', 'DESC');

        // create the editor grid
        var trapDataPane = new Ext.grid.EditorGridPanel({
	    id: 'trapdatagrid',
            store: trapDataStore,
            cm: trapDataCM,
            width:940,
            height:470,
            frame:false,
	    hidden: false,
	    title: 'Camera Trap Data',
	    sm : new Ext.grid.CheckboxSelectionModel(),
            viewConfig: {
                forceFit:false
            },
	    loadMask: true,
            bbar: new Ext.PagingToolbar({
	       id: 'pager',
               pageSize: 100,
               store: trapDataStore,
               displayInfo: true,
               displayMsg: 'Displaying records {0} - {1} of {2}   ',
               emptyMsg: "No records to display"
            }),


	   listeners :  {

                validateedit: function(obj) {

		    var value = obj.value;
		    var field = obj.field;

		},

		afteredit: function(obj) {
 
                    //alert(obj.field+"  "+obj.value+"  "+obj.originalValue+"  "+obj.row+"   "+obj.column+"  "+obj.record.get("Id"));
		    var value = obj.value;
		    var field = obj.field;

		    var proxy = new Ext.data.HttpProxy({
                          url: 'jsp/update-excel.jsp?'+
			       'site='+siteval+
			       '&columnname='+field+	
			       '&value='+escape(value)+
			       '&oldvalue='+escape(obj.originalValue)+
			       '&id='+id+
			       '&filename='+filename+
			       '&row='+obj.row+
			       '&column='+obj.column+
			       '&protocol='+escape("Camera Trapping")+
			       '&sheet='+this.store.reader.jsonData.sheet	
                       });
                   
                    proxy.load(null, null, function() { } );
                 }

	   }


        });

	var pager = Ext.getCmp('pager');
	pager.store.sortInfo = { field: 'Id', direction: 'DESC' };

        // trigger the data store load
        trapDataStore.load.defer(100, trapDataStore,[ {params:{start:0, limit:100}}]);


   var photoDataCM = new Ext.grid.ColumnModel([
					       //new Ext.grid.PagedRowNumberer(), 
	        {
                   header: "Sampling Event",
                   dataIndex: 'Sampling_Period',
                   width: 100,
		   renderer: function(value) {
                        var tmp = value + "";
                        var index = tmp.indexOf(".");
                        if (index != -1) {
                           var fst = tmp.substring(0, index);
                           var snd = tmp.substring(index+1);

                           if (snd.match(/^0[1-4]$/g) != null && fst.match(/^\d\d\d\d$/g) != null) {
                               if (parseInt(fst) < 2003 || parseInt(fst) > (new Date()).getFullYear()) {
                                  return '<span style="color:red;">'+value+'</span>';
                               } else {
                                  return value;
                               }
                           } else {
                               return '<span style="color:red;">'+value+'</span>';
                           }
                        } else {
                           return '<span style="color:red;">'+value+'</span>';
                        }
                   }
                 },{
                   header: "Camera Trap",
                   dataIndex: 'Camera_Trap_Point_ID',
                   width: 80,
                   renderer: function(value) {
                       var start = "CT-"+siteAbbrev+"-";
                       if (value.indexOf(start) == 0) {
                          var tmp = value.substring(start.length);
                          var index = tmp.indexOf("-");
                          if (index != -1) {
                             var fst = tmp.substring(0, index);
                             var snd = tmp.substring(index+1);

                             if (fst.match(/^\d$/g) == null || snd.match(/^\d\d$/g) == null) {
                                  return '<span style="color:red;">'+value+'</span>';
                             } else {
                                  return value;
                             }
                          } else {
                              return '<span style="color:red;">'+value+'</span>';
                          }
                       } else {
                          return '<span style="color:red;">'+value+'</span>';
                       }
                   }
                 },{
                   header: "Date",
                   dataIndex: 'Date',
                   width: 80,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
                },{
                   header: "Time",
                   dataIndex: 'Time',
                   width: 60,
		   renderer: function (value) {
                       var res = value;
                       if (value != null) {
                           try {
                               res = value.dateFormat('H:i')
                           } catch (e) {}
                       }
                       return res;
                   }
                },{
                   header: "Raw Filename",
                   dataIndex: 'Raw_Filename',
                   width: 90
                },{
                   header: "Team Filename",
                   dataIndex: 'Team_Filename',
                   width: 240,
		   hidden: true
                },{
                   header: "Photo Type",
                   dataIndex: 'Type',
                   width: 80
                },{
                   header: "Genus",
                   dataIndex: 'Genus',
                   width: 80
                },{
                   header: "Species",
                   dataIndex: 'Species',
                   width: 80
                },{
                   header: "Binomial",
                   dataIndex: 'Binomial',
                   width: 100,
		   hidden: true
                },{
                   header: "Number of Animals",
                   dataIndex: 'Number_of_Animals',
                   width: 110,
                   renderer: function(value) {
                       if (value < 1 || value > 100) {
                            return '<span style="color:red;">'+value+'</span>';
                       } else {
                            return value;
                       }
                   }
                },{
                   header: "Identified By",
                   dataIndex: 'Identified_By',
                   width: 150		   
                },{
                   header: "Notes",
                   dataIndex: 'Notes',
                   width: 300
	        }

              ]);


    var photoDataStore = new Ext.data.JsonStore({

            root: 'result',
            totalProperty: 'totalCount',
            remoteSort: true,
            fields: [
	        'Sampling_Period',
		'Camera_Trap_Point_ID',
                {name: 'Date', type: 'date', dateFormat: 'Y-m-d' },
                {name: 'Time', type: 'date', dateFormat: 'H:i' },
		'Raw_Filename',
		'Team_Filename',
		'Genus',
		'Species',
		'Binomial',
		'Number_of_Animals',
		'Identified_By',
		'Notes'
             ],
	
             proxy: new Ext.data.HttpProxy({
                 url: 'jsp/excel-photo-data.jsp?site='+siteval+'&protocol='+protocolVal+'&id='+id+'&filename='+filename 
             })
         });

         photoDataStore.setDefaultSort('Sid', 'DESC');

        // create the editor grid
        var photoDataPane = new Ext.grid.EditorGridPanel({
	    id: 'photodatagrid',
            store: photoDataStore,
            cm: photoDataCM,
            width:940,
            height:470,
            frame:false,
	    hidden: false,
	    title: 'Photo Data',
	    sm : new Ext.grid.CheckboxSelectionModel(),
            viewConfig: {
                forceFit:false
            },
	    loadMask: true,
            bbar: new Ext.PagingToolbar({
	       id: 'pager1',
               pageSize: 100,
               store: photoDataStore,
               displayInfo: true,
               displayMsg: 'Displaying records {0} - {1} of {2}   ',
               emptyMsg: "No records to display"
            }),


	   listeners :  {

                validateedit: function(obj) {
		    var value = obj.value;
		    var field = obj.field;
		},

		activate: function() {
		    if (!photoDataLoaded) { 
		       photoDataStore.load.defer(500, photoDataStore,[ {params:{start:0, limit:100}}]);
		       photoDataLoaded = true;
	            } else {
		    } 
		},

		afteredit: function(obj) {

                    //alert(obj.field+"  "+obj.value+"  "+obj.originalValue+"  "+obj.row+"   "+obj.column+"  "+obj.record.get("Id"));
		    var value = obj.value;
		    var field = obj.field;

		    var proxy = new Ext.data.HttpProxy({
                          url: 'jsp/update-excel.jsp?'+
			       'site='+siteval+
			       '&columnname='+field+	
			       '&value='+escape(value)+
			       '&oldvalue='+escape(obj.originalValue)+
			       '&id='+id+
			       '&filename='+filename+
			       '&row='+obj.row+
			       '&column='+obj.column+
			       '&protocol='+escape("Camera Trapping")+
			       '&sheet='+this.store.reader.jsonData.sheet
                       });
                   
                    proxy.load(null, null, function() { } );

                 },

		 cellclick :  function(grid, rowIndex, columnIndex, e) {
                       
		    var record = grid.getStore().getAt(rowIndex);  // Get the Record

                    var eventName = grid.getColumnModel().getDataIndex(1); 
                    var event = record.get(eventName);

                    var trapName = grid.getColumnModel().getDataIndex(2); 
                    var trap = record.get(trapName);

		    var arrays = trap.split("-");

		    var fileName = grid.getColumnModel().getDataIndex(5); 
                    var file = record.get(fileName);

		    var path = "/"+siteVal+"/"+event+"/Array"+arrays[2]+"/"+trap+"/"+file;

                    var iPane  = Ext.getCmp('ImagePanel');
		    iPane.setTitle(path.replace(/\//g, " : "));
		    var img = document.getElementById("imageview");
		    img.src="/CorePortlet/image-repository"+path;

		    var sPane  = Ext.getCmp('speciesPane');
		    sPane.setVisible(true);

                 }
		 
	   }


        });

	pager = Ext.getCmp('pager1');
	pager.store.sortInfo = { field: 'Id', direction: 'DESC' };

        tabs.add(trapDataPane);
        tabs.add(photoDataPane);

        return tabs;

}


function loadFolder() {
			
    var iWin = Ext.getCmp("importWinF");
    if (iWin != null) {
	iWin.show();
        return;
    }					

    // sampling event combo
    var ctEventStore = new Ext.data.JsonStore({
	  autoLoad: true,
          pruneModifiedRecords: true,
          url : "jsp/camera-event.jsp?site="+siteName+"&siteId="+siteId,
          root : "results",
          fields: ['name']
    });

    var ctEventCombo = new Ext.form.ComboBox({
          store: ctEventStore,
          id: 'eventF',
          displayField:'name',
          typeAhead: true,
          mode: 'local',
          triggerAction: 'all',
          emptyText:'Select a sampling period',
          selectOnFocus:true,
          width: 130,	
          allowBlank: false,
	  fieldLabel: 'Sampling Event',
	  editable : false,
          listeners: {
	       select: function(obj, record, ind){
		    // reset trap
		    var trapBox  = Ext.getCmp('trapIdF');
		    trapBox.clearValue();
		    trapBox.clearInvalid();
		    trapBox.store.removeAll();
		    trapBox.store.load({ params: 
			                    {'event': record.get('name') }
			              });
		    trapBox.enable();
	       }
	 }
    });

    // camera trap id combo
    var ctTrapIdStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/camera-trap-id.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var ctTrapIdCombo = new Ext.form.ComboBox({
        store: ctTrapIdStore,
        id: 'trapIdF',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a camera trap',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Camera Trap ID',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });

    // camera serial combo
    var ctCameraSerialStore = new Ext.data.JsonStore({
	    autoLoad: true,
	    pruneModifiedRecords: true,
	    url : "jsp/camera-serial-number.jsp?siteId="+siteId,
	    root : "results",
	    fields: ['name']
	});

    var ctCameraSerialCombo = new Ext.form.ComboBox({
	    store: ctCameraSerialStore,
	    fieldLabel: 'Camera Serial Number',
            name: 'cameraSerialF',
            id: 'cameraSerialF',                                                                                   
            allowBlank: false,    
	    displayField:'name',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'Select a camera serial number',
	    selectOnFocus:true,
	    width: 130,
	    editable : true
	});


    // setting person store
    var ctSetPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var ctSetPersonCombo = new Ext.form.ComboBox({
        store: ctSetPersonStore,
        id: 'setpersonF',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a person',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Person Setting up the Camera',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });


    // picking person store
    var ctPickPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var ctPickPersonCombo = new Ext.form.ComboBox({
        store: ctPickPersonStore,
        id: 'pickpersonF',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a person',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Person Picking up the Camera',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });

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
	    id: 'filetreeF',
	    region:'south',
	    collapsible: true,
	    margins: '5 0 0 5',
	    cmargins: '5 0 0 0',
	    cls: 'padding 5',
	    width: 540,
	    height: 120,
	    //minSize: 440,
	    //maxSize: 240,
	    root: root,
	    loader: data,
	    autoScroll : true,
	    fitToFrame:true,
	    rootVisible: false,
            fieldLabel: 'Folder for Importing Images',
	    border: true
	});


    // form panel
    var form = new Ext.form.FormPanel({
	region: 'center',
        labelWidth: 170,
        bodyStyle:'padding:15px',
        width: 390,
        labelPad: 10,
        defaultType: 'textfield',
        defaults: {
            // applied to each contained item
            width: 230,
            msgTarget: 'side'
        },
        layoutConfig: {
            // layout-specific configs go here
            labelSeparator: ''
        },
        items: [
		 ctEventCombo, 
                 ctTrapIdCombo,
                 //ctMemoryFolderCombo,
		 {
		     xtype: 'hidden', 
		     id: 'memoryFolderF',
		     name: 'memoryFolderF',
		     hidden: true
                 },
                 fileTreePanel,
		 /*
		 {
                    fieldLabel: 'Folder for Importing Images',
                    id: 'memoryFolder',
		    name: 'memoryFolder',
                    allowBlank: false,		
	            editable: false,
		    disabled: true
                 },
		 */
		 ctCameraSerialCombo,
		 /*
                {
                   fieldLabel: 'Camera Serial Number',
                   name: 'cameraSerial',
                   id: 'cameraSerial',
                   allowBlank: false
                },
		 */
		{
                   fieldLabel: 'Memory Card Serial Number',
                   name: 'memoryCardSerialF',
                   id: 'memoryCardSerialF',
                   allowBlank: true
                },
		 ctSetPersonCombo,
                 ctPickPersonCombo,
                {
                    xtype: 'textarea',
                    id:"notesF",
                    name: 'notesF',
                    fieldLabel:"Notes",
                    height:100
		},{
                    xtype: 'progress',
                    id: 'progressF',
                    text: 'Copying Files',
                    width: 410,
                    hidden: true		           
		 }, 
           ]
     });


    win = new Ext.Window({
	id          : 'importWinF',
        title       : 'Import Images From Folder',
        layout      : 'fit', 
	//layout      : 'border',
        width       : 480,
        height      : 520,
        closeAction :'hide',
        plain       : true,
	modal       : true,
        items       : [ form ],	
        buttons     : [{
                           text     : 'Submit',
			   id       : 'submitButtonF',
                           handler  : function(){

			      if (form.getForm().isValid()) {

				  var root = Ext.getCmp("filetreeF").getRootNode();
				  var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
				  var memoryFolder = null;
				  if (selectedNode == null) {
				      Ext.MessageBox.show({
					          title: 'Message',
						  msg: "No folder has been selected.",
						  width:400,
						  modal: true,
						  icon: Ext.Msg.INFO,
						  buttons: Ext.Msg.OK
						  }); 

				      return;
				  } else {
				      memoryFolder = selectedNode.getPath('text');
				      //alert(exportDir);
				  }

				  var thisTrapIdCombo = Ext.getCmp('trapIdF');
				  var trapName = Ext.getCmp('trapIdF').getValue();
				  var trapId = thisTrapIdCombo.store.getAt(thisTrapIdCombo.selectedIndex).get('id');
				  
				  var event = Ext.getCmp('eventF').getValue();
                                              
				  var cameraSerial = Ext.getCmp('cameraSerialF').getValue();    
				  var memoryCardSerial = Ext.getCmp('memoryCardSerialF').getValue();    
				  //var start = Ext.getCmp('start').getValue();    
				  //var end = Ext.getCmp('end').getValue();    

				  var setPersonCombo = Ext.getCmp('setpersonF');
				  var setPersonId = setPersonCombo.store.getAt(setPersonCombo.selectedIndex).get('id');
				  var firstset = Ext.getCmp('setpersonF').getValue();

				  var pickPersonCombo = Ext.getCmp('pickpersonF');
				  var pickPersonId = pickPersonCombo.store.getAt(pickPersonCombo.selectedIndex).get('id');
				  var firstpick = Ext.getCmp('pickpersonF').getValue();
				     
				  var notes = Ext.getCmp('notesF').getValue();     

				  Ext.getCmp('submitButtonF').disable();
				  Ext.getCmp('closeButtonF').disable();

				  var pbar = Ext.getCmp('progressF');
				  pbar.show();

				  /*
				    pbar.wait( {interval: 100, //bar will move fast!
				                increment: 15,
						text: 'Copying...',
					       });
				  */		 

				  // Start a task to get the progress
				  var task = {
				      run: function(){
					  Ext.Ajax.request({
						  url: 'jsp/import-status.jsp?siteId='+siteId,
						  success: function(response, options) {
						      if (response) {
							  try {
							      var json = Ext.util.JSON.decode(response.responseText);
							  } catch (err) { return; }

							  if (json.failed) {
								         
							      pbar.wait({interval: 100, //bar will move fast!
									 increment: 15,
									 text: 'Copying...'
								  });	 
							      return;
							  }

							  if (!json.done) {
							      percentage =json.strPercent;
							      pbar.updateProgress(json.percentage,
										  json.strPercent);
							  } else { pbar.updateProgress(1, 'Done'); }
						      }
						  },
						  params: { 
						      site: siteName,
						      siteId: siteId,
						      trapName: trapName,
						      event: event,
						      memoryFolder: memoryFolder
						  }
					      });
				      },
				      interval: 300
				  };
				  var runner = new Ext.util.TaskRunner();
				  runner.start(task);

				  $.getJSON(
					    "jsp/tv-import-folder.jsp",
                                            { 
					        site: siteName, 
						siteId: siteId, 		 
						trapName: trapName,
						trapId: trapId,
						memoryFolder: memoryFolder, 
						event: event, 
						cameraSerial: cameraSerial, 
						memoryCardSerial: memoryCardSerial, 
						//start: start, 
						//end: end, 
						setperson: firstset, 
						pickperson: firstpick, 
						setpersonid: setPersonId, 
						pickpersonid: pickPersonId, 
						notes: notes 
					     },
					    function(j) {
						pbar.reset();
						pbar.hide();

						Ext.getCmp('submitButtonF').enable();
						Ext.getCmp('closeButtonF').enable();
						Ext.getCmp('importWinF').hide();

						//win.hide();

						runner.stop(task);

						if (j.success) {

						    // reset the form    
						    Ext.getCmp('cameraSerialF').reset();    
						    Ext.getCmp('memoryCardSerialF').reset();    
						    Ext.getCmp('notesF').reset();     

						    // reset trap
						    var trapBox  = Ext.getCmp('trapIdF');
						    trapBox.clearValue();
						    trapBox.clearInvalid();
						    trapBox.store.removeAll();
						    trapBox.store.load({ params: 
							    {'event': event }
							});
						    trapBox.enable();

						    var cameraSerial = Ext.getCmp('cameraSerial');
						    if (cameraSerial != null) {
							cameraSerial.reset();    
							Ext.getCmp('memoryCardSerial').reset();    
							Ext.getCmp('notes').reset();     

							// reset trap
							var trapBox  = Ext.getCmp('trapId');
							trapBox.clearValue();
							trapBox.clearInvalid();
							trapBox.store.removeAll();
							trapBox.store.load({ params: 
								{'event': Ext.getCmp('event').getValue() }
							    });
							trapBox.enable();
						    }



						    var error = "<span style='white-space:nowrap; width:350;'>Found the following problem:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>";
						    /*
						      if (j.camera_serial_confirm) {
								    error += "<br><br>The input camera serial number '"+j.input_camera_serial+
									"' is different to the camera serial number "+
									"'"+j.recorded_camera_serial+"' in the photos.";
								}
						    */

						    if (j.trap_confirm) {
							error += "<br><br>The input trap name '"+j.input_trap+
							    "' is different to the trap name "+
							    "'"+j.recorded_trap+"' in the photos.";
						    } 

						    error += "<br><br>Save the photos with the camera trap name "+j.input_trap+"?";
						    var keep = false;
						    //if (j.trap_confirm || j.camera_serial_confirm) {
						    if (j.trap_confirm) {
							Ext.MessageBox.confirm("Confirm",
									       error,
									       function (btn) {
										   if (btn == 'yes') {
										
										       var msgStr = "";
										       if (j.camera_serial_confirm) {
											   msgStr += "The input camera serial number '"+j.input_camera_serial+
											       "' is different to the camera serial number "+
											       "'"+j.recorded_camera_serial+"' in the photos. "+
											       "<br><br>Both are recorded in the database.";
										       }

										       if (msgStr != "") {
											   msgStr += "<br><br>";
										       }

										       msgStr += 'Totally '+j.counter+" images are saved.";

										       Ext.MessageBox.show({
											       title: 'Message',
												   msg: msgStr,
												   width:400,
												   modal: true,
												   icon: Ext.Msg.INFO,
												   buttons: Ext.Msg.OK
												   }); 

										       setStatus();

										       // reload tree
										       var root = Ext.getCmp("repository").getRootNode();
										       root.reload();							
										       root.expand(false, 
											     true,
											     function() {							 
												 var eventNode = root.findChild('text', event);
												 eventNode.expand(false,
														  true,
														  function() {
										                                      var arrayNode = eventNode.findChild('text', 
																			  //"Array"+trapName.split("-")[2]);
																			  "Array"+j.array_index);
														      arrayNode.expand(false,
																       true,
																       function() {
																	   var trapNode = arrayNode.findChild('text', trapName);
																	   Ext.getCmp('repository').selectPath(trapNode.getPath());
																	   trapNode.ensureVisible();
																       });
														  });
											     });

	
								                 // reload the data form
								                 var elements = trapName.split("-");
								                 //var tmp = "CT-"+ siteAbbrev+"-"+elements[2]+"-"+event;
										 var tmp = "CT-"+ siteAbbrev+"-"+j.array_index+"-"+event;
                                                                  
								                 var ctPane = createCameraTrappingExcelGridPane1(
														  siteName,
														  'Camera Trapping',
														  tmp,
														  tmp+".xls",
														  -1);
								                 ctPane.setTitle(null);
								                 var dataHolder = Ext.getCmp('dataholder');
								                 dataHolder.add(ctPane);
								                 dataHolder.doLayout();
								                 currentRepositoryPath = tmp;
									     										  
									      } else {
										  //var elements = trapName.split("-");
										  var dEvent = event;
										  //var dArray = 'Array'+elements[2];
										  var dArray = 'Array'+j.array_index;
										  var dTrap = trapName;

										  var waitBox = Ext.MessageBox.wait("Please wait. We are clearing the photos.", 
								       	   	     		                    "Status");
										  $.getJSON( "jsp/delete-photos.jsp",
                                                                                             { event: dEvent,  array: dArray, trap: dTrap},
                                                          			             function(j) {
											        waitBox.hide();
						             			                if (j.success) {
												    
								                                   // reset trap combobox
		                                                                                   var trapBox  = Ext.getCmp('trapId');
		                                                                                   trapBox.clearValue();
		                                                                                   trapBox.clearInvalid();
		                                                                                   trapBox.store.removeAll();
		                                                                                   trapBox.store.load({ params: 
			                                                                                                  {'event': event }
			                                                                                             });
		                                                                                   trapBox.enable();
												}
											     });
									      }
									});
								} else {  

                                                                   var msgStr = "";
								   if (j.camera_serial_confirm) {
								       msgStr += "The input camera serial number '"+j.input_camera_serial+
									         "' is different to the camera serial number "+
									         "'"+j.recorded_camera_serial+"' in the photos. "+
										 "<br><br>Both are recorded in the database.";
								   }

								   if (msgStr != "") {
									msgStr += "<br><br>";
								   }

								    msgStr += 'Totally '+j.counter+" images are saved.";
                                     	
								    Ext.MessageBox.show({
								        title: 'Message',
								        msg: msgStr,
								        width:400,
								        modal: true,
								        icon: Ext.Msg.INFO,
								        buttons: Ext.Msg.OK
                                                                    }); 

								    setStatus();

								    // reload tree
								    var root = Ext.getCmp("repository").getRootNode();
								    root.reload();							
								    root.expand(false, 
										true,
										function() {							 
										    var eventNode = root.findChild('text', event);
										    eventNode.expand(false,
												     true,
												     function() {
										                        var arrayNode = eventNode.findChild('text', 
																	    //"Array"+trapName.split("-")[2]);
																	    "Array"+j.array_index);
													arrayNode.expand(false,
															 true,
															 function() {
															    var trapNode = arrayNode.findChild('text', trapName);
															    Ext.getCmp('repository').selectPath(trapNode.getPath());
															    trapNode.ensureVisible();
															 });
												      });
								                });

								    // reload the data form
								    var elements = trapName.split("-");
								    //var tmp = "CT-"+ siteAbbrev+"-"+elements[2]+"-"+event;
								    var tmp = "CT-"+ siteAbbrev+"-"+j.array_index+"-"+event;
                                                                  
								    var ctPane = createCameraTrappingExcelGridPane1(
														  siteName,
														  'Camera Trapping',
														  tmp,
														  tmp+".xls",
														  -1);
								    ctPane.setTitle(null);
								    var dataHolder = Ext.getCmp('dataholder');
								    dataHolder.add(ctPane);
								    dataHolder.doLayout();

								    currentRepositoryPath = tmp;

								}
							    } else {
								Ext.MessageBox.show({
								    title: 'Error Message',
								    msg: j.text,
                                                                    width:350,
                                                                    modal: true,
                                                                    icon: Ext.Msg.INFO,
                                                                    buttons: Ext.Msg.OK
								}); 
							     }						  
							 });
							 
					 }
		    }


		},{
		    text     : 'Close',
		    id       : 'closeButtonF',
		    handler  : function(){
                        var pbar = Ext.getCmp('progressF');
                        pbar.reset();
                        pbar.hide();
			Ext.getCmp('importWinF').hide();
			//win.hide();  
		    }
		}]
	});

	win.show();

}


function loadMemoryCard() {
			
    var iWin = Ext.getCmp("importWin");
    if (iWin != null) {
        Ext.getCmp('memoryFolder').store.load();
	iWin.show();
        return;
    }					

    // sampling event combo
    var ctEventStore = new Ext.data.JsonStore({
	  autoLoad: true,
          pruneModifiedRecords: true,
          url : "jsp/camera-event.jsp?site="+siteName+"&siteId="+siteId,
          root : "results",
          fields: ['name']
    });

    var ctEventCombo = new Ext.form.ComboBox({
          store: ctEventStore,
          id: 'event',
          displayField:'name',
          typeAhead: true,
          mode: 'local',
          triggerAction: 'all',
          emptyText:'Select a sampling period',
          selectOnFocus:true,
          width: 130,	
          allowBlank: false,
	  fieldLabel: 'Sampling Event',
	  editable : false,
          listeners: {
	       select: function(obj, record, ind){
		    // reset trap
		    var trapBox  = Ext.getCmp('trapId');
		    trapBox.clearValue();
		    trapBox.clearInvalid();
		    trapBox.store.removeAll();
		    trapBox.store.load({ params: 
			                    {'event': record.get('name') }
			              });
		    trapBox.enable();
	       }
	 }
    });

    // camera trap id combo
    var ctTrapIdStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/camera-trap-id.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var ctTrapIdCombo = new Ext.form.ComboBox({
        store: ctTrapIdStore,
        id: 'trapId',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a camera trap',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Camera Trap ID',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });

    // memory card folder combo
    var ctMemoryFolderStore = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/camera-memory-folder.jsp",
         root : "results",
         fields: ['name']
    });
					
    var mTask = {
                   run: function(){
			   var store = Ext.getCmp('memoryFolder').store;
			   if (store.getCount() == 0) {
				store.load.defer(1000, store,[ {params:{}}]);
			   } else {
				mRunner.stop(mTask);
			   }
		    },
                    interval: 1000
                };
    var mRunner = new Ext.util.TaskRunner();


    var ctMemoryFolderCombo = new Ext.form.ComboBox({
        store: ctMemoryFolderStore,
        id: 'memoryFolder',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select the memory card folder',
        selectOnFocus:true,
        width: 130,
        allowBlank: false,	
	fieldLabel: 'Memory Card Folder',
	editable : false,
        listeners: {
	     focus: function(field){
		       if (this.store.getCount() == 0) {
			   alert("Found no memory card");
                           mRunner.start(mTask);
		       }
		},
	     blur: function(field){
                       mRunner.stop(mTask);
		} 
	}
    });

    // camera serial combo
    var ctCameraSerialStore = new Ext.data.JsonStore({
	    autoLoad: true,
	    pruneModifiedRecords: true,
	    url : "jsp/camera-serial-number.jsp?siteId="+siteId,
	    root : "results",
	    fields: ['name']
	});

    var ctCameraSerialCombo = new Ext.form.ComboBox({
	    store: ctCameraSerialStore,
	    fieldLabel: 'Camera Serial Number',
            name: 'cameraSerial',                                                                                             
            id: 'cameraSerial',                                                                                                 
            allowBlank: false,    
	    displayField:'name',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'Select a camera serial number',
	    selectOnFocus:true,
	    width: 130,
	    editable : true
	});


    // setting person store
    var ctSetPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var ctSetPersonCombo = new Ext.form.ComboBox({
        store: ctSetPersonStore,
        id: 'setperson',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a person',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Person Setting up the Camera',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });


    // picking person store
    var ctPickPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var ctPickPersonCombo = new Ext.form.ComboBox({
        store: ctPickPersonStore,
        id: 'pickperson',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a person',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Person Picking up the Camera',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });


    // form panel
    var form = new Ext.form.FormPanel({
        labelWidth: 170,
        bodyStyle:'padding:15px',
        width: 390,
        labelPad: 10,
        defaultType: 'textfield',
        defaults: {
            // applied to each contained item
            width: 230,
            msgTarget: 'side'
        },
        layoutConfig: {
            // layout-specific configs go here
            labelSeparator: ''
        },
        items: [
		 ctEventCombo, 
                 ctTrapIdCombo,
                 ctMemoryFolderCombo,
		 ctCameraSerialCombo,
		 /*
                {
                   fieldLabel: 'Camera Serial Number',
                   name: 'cameraSerial',
                   id: 'cameraSerial',
                   allowBlank: false
                },
		 */
		{
                   fieldLabel: 'Memory Card Serial Number',
                   name: 'memoryCardSerial',
                   id: 'memoryCardSerial',
                   allowBlank: true
                },
		 ctSetPersonCombo,
                 ctPickPersonCombo,
                {
                    xtype: 'textarea',
                    id:"notes",
                    name: 'notes',
                    fieldLabel:"Notes",
                    height:100
		},{
                    xtype: 'progress',
                    id: 'progress',
                    text: 'Copying Files',
                    width: 410,
                    hidden: true		           
		}

           ]
     });


    win = new Ext.Window({
	id          : 'importWin',
        title       : 'Import Images From Memory Card',
        layout      : 'fit', 
        width       : 480,
        height      : 420,
        closeAction :'hide',
        plain       : true,
	modal       : true,
        items       : [ form ],	
        buttons     : [{
                           text     : 'Submit',
			   id       : 'submitButton',
                           handler  : function(){

				 if (form.getForm().isValid()) {

					     var thisTrapIdCombo = Ext.getCmp('trapId');
					     var trapName = Ext.getCmp('trapId').getValue();

					     var p = trapName.indexOf(" in ");
					     if (p != -1) {
						 trapName = trapName.substring(0, p); 
					     }
					     
					     var trapId = thisTrapIdCombo.store.getAt(thisTrapIdCombo.selectedIndex).get('id');
					     var memoryFolder = Ext.getCmp('memoryFolder').getValue();
					     var event = Ext.getCmp('event').getValue();
                                              
					     var cameraSerial = Ext.getCmp('cameraSerial').getValue();    
					     var memoryCardSerial = Ext.getCmp('memoryCardSerial').getValue();    
					     //var start = Ext.getCmp('start').getValue();    
					     //var end = Ext.getCmp('end').getValue();    

					     var setPersonCombo = Ext.getCmp('setperson');
					     var setPersonId = setPersonCombo.store.getAt(setPersonCombo.selectedIndex).get('id');
					     var firstset = Ext.getCmp('setperson').getValue();

					     var pickPersonCombo = Ext.getCmp('pickperson');
					     var pickPersonId = pickPersonCombo.store.getAt(pickPersonCombo.selectedIndex).get('id');
					     var firstpick = Ext.getCmp('pickperson').getValue();

					     var notes = Ext.getCmp('notes').getValue();     

					     Ext.getCmp('submitButton').disable();
					     Ext.getCmp('closeButton').disable();

					     var pbar = Ext.getCmp('progress');
					     pbar.show();

					     /*
					     pbar.wait( {interval: 100, //bar will move fast!
							 increment: 15,
							 text: 'Copying...',
							 });
					     */		 

                                             // Start a task to get the progress
                                             var task = {
                                                  run: function(){
						     Ext.Ajax.request({
							     url: 'jsp/import-status.jsp?siteId='+siteId,
							     success: function(response, options) {
								 if (response) {
								     try {
									 var json = Ext.util.JSON.decode(response.responseText);
								     } catch (err) { return; }

								     if (json.failed) {
								         
					                                 pbar.wait({interval: 100, //bar will move fast!
							                             increment: 15,
							                             text: 'Copying...'
							                          });	 
									 return;
								     }

								     if (!json.done) {
									 percentage =json.strPercent;
									 pbar.updateProgress(json.percentage,
											     json.strPercent);
								     } else { pbar.updateProgress(1, 'Done'); }
								 }
							     },
							     params: { 
								 site: siteName,
								 siteId: siteId,
								 trapName: trapName,
								 event: event,
								 memoryFolder: memoryFolder
							     }
							 });
                                                  },
                                                  interval: 300
                                             };
                                             var runner = new Ext.util.TaskRunner();
                                             runner.start(task);

					     $.getJSON(
                                                         "jsp/tv-import.jsp",
                                                         { 
							   site: siteName, 
							   siteId: siteId, 		 
							   trapName: trapName,
							   trapId: trapId,
							   memoryFolder: memoryFolder, 
							   event: event, 
							   cameraSerial: cameraSerial, 
							   memoryCardSerial: memoryCardSerial, 
							   //start: start, 
							   //end: end, 
							   setperson: firstset, 
							   pickperson: firstpick, 
							   setpersonid: setPersonId, 
							   pickpersonid: pickPersonId, 
							   notes: notes 
							 },
                                                         function(j) {
                                                            pbar.reset();
                                                            pbar.hide();

							    Ext.getCmp('submitButton').enable();
					                    Ext.getCmp('closeButton').enable();
               			     			    Ext.getCmp('importWin').hide();

							    //win.hide();

							    runner.stop(task);

                                                            if (j.success) {

			  					// reset the form    
							        Ext.getCmp('cameraSerial').reset();    
							        Ext.getCmp('memoryCardSerial').reset();    
								Ext.getCmp('notes').reset();     

								// reset trap
		                                                var trapBox  = Ext.getCmp('trapId');
		                                                trapBox.clearValue();
		                                                trapBox.clearInvalid();
		                                                trapBox.store.removeAll();
		                                                trapBox.store.load({ params: 
			                                                              {'event': event }
			                                                          });
		                                                trapBox.enable();

								var cameraSerial = Ext.getCmp('cameraSerialF');
								if (cameraSerial != null) {
								    cameraSerial.reset();    
								    Ext.getCmp('memoryCardSerialF').reset();    
								    Ext.getCmp('notesF').reset();     

								    // reset trap
								    var trapBox  = Ext.getCmp('trapIdF');
								    trapBox.clearValue();
								    trapBox.clearInvalid();
								    trapBox.store.removeAll();
								    trapBox.store.load({ params: 
									    {'event': Ext.getCmp('eventF').getValue() }
									});
								    trapBox.enable();
								}


								var error = "<span style='white-space:nowrap; width:350;'>Found the following problem:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>";
								/*
								if (j.camera_serial_confirm) {
								    error += "<br><br>The input camera serial number '"+j.input_camera_serial+
									"' is different to the camera serial number "+
									"'"+j.recorded_camera_serial+"' in the photos.";
								}
								*/

								if (j.trap_confirm) {
								    error += "<br><br>The input trap name '"+j.input_trap+
									"' is different to the trap name "+
									"'"+j.recorded_trap+"' in the photos.";
								} 


								error += "<br><br>Save the photos with the camera trap name "+j.input_trap+"?";

								var keep = false;
								//if (j.trap_confirm || j.camera_serial_confirm) {
                                                                if (j.trap_confirm) {
								    Ext.MessageBox.confirm("Confirm",
									error,
									function (btn) {
									    if (btn == 'yes') {
										
										var msgStr = "";
										if (j.camera_serial_confirm) {
										    msgStr += "The input camera serial number '"+j.input_camera_serial+
									                      "' is different to the camera serial number "+
									                      "'"+j.recorded_camera_serial+"' in the photos. "+
											      "<br><br>Both are recorded in the database.";
										}

										if (msgStr != "") {
										    msgStr += "<br><br>";
										}

										msgStr += 'Totally '+j.counter+" images are saved.";

								                Ext.MessageBox.show({
								                     title: 'Message',
								                     msg: msgStr,
								                     width:400,
								                     modal: true,
								                     icon: Ext.Msg.INFO,
								                    buttons: Ext.Msg.OK
                                                                                 }); 

										 setStatus();

								                 // reload tree
										 var root = Ext.getCmp("repository").getRootNode();
										 root.reload();							
										 root.expand(false, 
											     true,
											     function() {							 
												 var eventNode = root.findChild('text', event);
												 eventNode.expand(false,
														  true,
														  function() {
										                                      var arrayNode = eventNode.findChild('text', 
																			  //"Array"+trapName.split("-")[2]);
																			  "Array"+j.array_index);
														      arrayNode.expand(false,
																       true,
																       function() {
							
																	   //var p = trapName.indexOf(" in ");
																	   //if (p != -1) trapName = trapName.substring(0, p);
																	   var trapNode = arrayNode.findChild('text', trapName);
																	   Ext.getCmp('repository').selectPath(trapNode.getPath());
																	   trapNode.ensureVisible();
																       });
														  });
											     });

	
								                 // reload the data form
								                 var elements = trapName.split("-");
								                 //var tmp = "CT-"+ siteAbbrev+"-"+elements[2]+"-"+event;
										 var tmp = "CT-"+ siteAbbrev+"-"+j.array_index+"-"+event;
                                                                  
								                 var ctPane = createCameraTrappingExcelGridPane1(
														  siteName,
														  'Camera Trapping',
														  tmp,
														  tmp+".xls",
														  -1);
								                 ctPane.setTitle(null);
								                 var dataHolder = Ext.getCmp('dataholder');
								                 dataHolder.add(ctPane);
								                 dataHolder.doLayout();
								                 currentRepositoryPath = tmp;
									     										  
									      } else {
										  //var elements = trapName.split("-");
										  var dEvent = event;
										  //var dArray = 'Array'+elements[2];
										  var dArray = 'Array'+j.array_index;
										  var dTrap = trapName;

										  var waitBox = Ext.MessageBox.wait("Please wait. We are clearing the photos.", 
								       	   	     		                    "Status");
										  $.getJSON( "jsp/delete-photos.jsp",
                                                                                             { event: dEvent,  array: dArray, trap: dTrap},
                                                          			             function(j) {
											        waitBox.hide();
						             			                if (j.success) {
												    
								                                   // reset trap combobox
		                                                                                   var trapBox  = Ext.getCmp('trapId');
		                                                                                   trapBox.clearValue();
		                                                                                   trapBox.clearInvalid();
		                                                                                   trapBox.store.removeAll();
		                                                                                   trapBox.store.load({ params: 
			                                                                                                  {'event': event }
			                                                                                             });
		                                                                                   trapBox.enable();
												}
											     });
									      }
									});
								} else {  

                                                                   var msgStr = "";
								   if (j.camera_serial_confirm) {
								       msgStr += "The input camera serial number '"+j.input_camera_serial+
									         "' is different to the camera serial number "+
									         "'"+j.recorded_camera_serial+"' in the photos. "+
										 "<br><br>Both are recorded in the database.";
								   }

								   if (msgStr != "") {
									msgStr += "<br><br>";
								   }

								    msgStr += 'Totally '+j.counter+" images are saved.";
                                     	
								    Ext.MessageBox.show({
								        title: 'Message',
								        msg: msgStr,
								        width:400,
								        modal: true,
								        icon: Ext.Msg.INFO,
								        buttons: Ext.Msg.OK
                                                                    }); 

								    setStatus();

								    // reload tree
								    var root = Ext.getCmp("repository").getRootNode();
								    root.reload();							
								    root.expand(false, 
										true,
										function() {							 
										    var eventNode = root.findChild('text', event);
										    eventNode.expand(false,
												     true,
												     function() {
										                        var arrayNode = eventNode.findChild('text', 
																	    //"Array"+trapName.split("-")[2]);
																	    "Array"+j.array_index);
													arrayNode.expand(false,
															 true,
															 function() {
															    var trapNode = arrayNode.findChild('text', trapName);
															    Ext.getCmp('repository').selectPath(trapNode.getPath());
															    trapNode.ensureVisible();
															 });
												      });
								                });

								    // reload the data form
								    var elements = trapName.split("-");
								    //var tmp = "CT-"+ siteAbbrev+"-"+elements[2]+"-"+event;
								    var tmp = "CT-"+ siteAbbrev+"-"+j.array_index+"-"+event;
                                                                  
								    var ctPane = createCameraTrappingExcelGridPane1(
														  siteName,
														  'Camera Trapping',
														  tmp,
														  tmp+".xls",
														  -1);
								    ctPane.setTitle(null);
								    var dataHolder = Ext.getCmp('dataholder');
								    dataHolder.add(ctPane);
								    dataHolder.doLayout();

								    currentRepositoryPath = tmp;

								}
							    } else {
								Ext.MessageBox.show({
								    title: 'Error Message',
								    msg: j.text,
                                                                    width:350,
                                                                    modal: true,
                                                                    icon: Ext.Msg.INFO,
                                                                    buttons: Ext.Msg.OK
								}); 
							     }						  
							 });
							 
					 }
		    }


		},{
		    text     : 'Close',
		    id       : 'closeButton',
		    handler  : function(){
                        var pbar = Ext.getCmp('progress');
                        pbar.reset();
                        pbar.hide();
			Ext.getCmp('importWin').hide();
			//win.hide();  
		    }
		}]
	});

	win.show();
}



function updateViaInternet() {
			

    var waitBox = Ext.MessageBox.wait("<span style='white-space:nowrap;'>Please wait. We are updating the deskTEAM ... </span>", 
				      "Status");

    $.getJSON("jsp/update-via-internet.jsp",
	      { sitename: 'siteName' },
	      function(j) {
		  waitBox.hide();
		  if (j.success) {
		      if (j.update) {
			  Ext.MessageBox.confirm("Confirm",
						 "The deskTEAM has been updated to the latest version. Enabling the new version requires reloading the current page. Reload now?",
						 function (btn) {
						     if (btn == 'yes') {					
							 waitBox = Ext.MessageBox.wait("<span style='white-space:nowrap;'>We are reloading the deskTEAM ... </span>",
										       "Status");
							 setTimeout("document.location.href='index.jsp';", 5000);
						     }
						 });
		      } else {
		         msg("Status", "The deskTEAM has been updated using the latest data from TEAM Network.");
		      }
		  } else {
		      msg("Failed", j.message); 
		  }
	      });

}


function underConstruction() {

    msg("Information", "Under Construction");

}



function aboutDeskTEAM() {

    var win = Ext.getCmp('aboutWin');
    if (win == null) {
        win = new Ext.Window({
	   id          : 'aboutWin',
           title       : '',
           layout      : 'fit', 
           width       : 490,
           height      : 350,
           closeAction :'hide',
           plain       : true,
	   modal       : true,
           items       : [{ 
			xtype: 'panel',
			html: 
                         '<div style="padding:20pt; font-family: times,arial,sans-serif; ">'+
			 '<table>'+
	                 '<tr><td><span style="font-size:36; font-weight:bold;">Open DeskTEAM</span></td></tr>'+
	                 '<tr><td><span style="color:#150517;">version '+version+'</tr>'+
	                 '<tr><td> &copy Team Network, 2010. All rights reserved. Licensed under GPL version 3 (<a href="license.html" target="_blank">see license information</a>).</td></tr>'+
			 '<tr height=10><td></td></tr>'+
			 '<tr><td>Design and development team: Jorge Ahumada, Chaitan Baru, Sandeep Chandra, Kate Ericson, Eric Fegraus and Kai Lin. </td></tr>'+
			 '<tr height=10><td></td></tr>'+
                         '<tr><td>This application was developed as a collaboration between the San Diego Supercomputer Center and TEAM Network.</td></tr>'+
	                 '</table>'+
			 '</div>'
		    }],
           buttons     : [{
		         text     : 'Close',
		         handler  : function(){
			    win.hide();
		         }
		     }]
        });
    }

    win.show();

}



function createFileWindow(size) {

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
	    width: 340,
	    height: 235,
	    //minSize: 440,
	    //maxSize: 240,
	    root: root,
	    loader: data,
	    autoScroll : true,
	    fitToFrame:true,
	    rootVisible: false,
	    border: false
	});

    var fileWin = new Ext.Window({
	             id          : 'fileWin',
        	     title       : 'Select a directory to export data',
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
					 xtype: 'hidden',
					 id: 'exportEvent'
				     },{
					 xtype: 'hidden',
					 id: 'exportArray'
				     },{
					 xtype: 'hidden',
					 id: 'packageSize'
				     },{
					 xtype: 'hidden',
					 id: 'packageValid'
				     },{
					 xtype: 'label',
		                         html: "<div style='padding:10pt;font-size:8pt;'>The estimated size of the exported package is <span id='packageSize'>"+(size != null ? size +" MB" : "unknown" )+"</span>. Please make sure you have enough space on your storage system.</div>",
					 frame: true
		                     }
				   ],
		     bbar        : [
				    '->',
                                     {
		                       xtype: 'button',
                                       text:  'Export',
				       pressed: true,
				       handler: function() {
					     fileWin.hide();
					     var wait = Ext.MessageBox.progress("<span style='white-space:nowrap;'>Please wait. We are exporting the data ...</span>", "Status");
					     
					     var root = Ext.getCmp("filetree").getRootNode();
					     var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
					     var exportDir = selectedNode.getPath('text');
					     //alert(exportDir);

					     $.getJSON( "jsp/export-data.jsp",
                                                     { 
							 path: exportDir,
							 siteId: siteId,
							 event: Ext.getCmp('exportEvent').getValue(), 
							 array: Ext.getCmp('exportArray').getValue(),
							 valid: Ext.getCmp('packageValid').getValue()   
						     },
						     function(j) {
							 wait.hide();
							 runner.stop(task);
						     });


                                             // Start a task to get the progress
                                             var task = {
                                                  run: function(){
						     Ext.Ajax.request({
							     url: 'jsp/export-status.jsp',
							     success: function(response, options) {
								 if (response) {
								     try {
									 var json = Ext.util.JSON.decode(response.responseText);
								     } catch (err) { return; }

								     if (json.failed) {
					                                 wait.updateProgress(1, "Failed");
									 return;
								     }

								     if (!json.done) {
									 percentage =json.strPercent;
									 wait.updateProgress(json.percentage,
											     json.strPercent);
								     } else { 
									 wait.updateProgress(1, 'Done'); 
								     }
								 }
							     },
							     params: { 
							        path: exportDir,
								siteId: siteId, 
							        event: Ext.getCmp('exportEvent').getValue(), 
							        array: Ext.getCmp('exportArray').getValue(), 
								size: Ext.getCmp('packageSize').getValue()
							     }
							 });
                                                  },
                                                  interval: 300
                                             };
                                             var runner = new Ext.util.TaskRunner();
                                             runner.start(task);

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

    var fileRoot = Ext.getCmp("filetree").getRootNode();
    expand(fileRoot, homepath);

    return fileWin;
}



function createCSVFileWindow(size) {

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
	    id: 'csvtree',
	    region:'west',
	    collapsible: true,
	    margins: '5 0 0 5',
	    cmargins: '5 0 0 0',
	    cls: 'padding 5',
	    width: 340,
	    height: 275,
	    //minSize: 440,
	    //maxSize: 240,
	    root: root,
	    loader: data,
	    autoScroll : true,
	    fitToFrame:true,
	    rootVisible: false,
	    border: false
	});

    var csvWin = new Ext.Window({
	             id          : 'csvWin',
        	     title       : 'Select a directory to export data',
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
					 xtype: 'hidden',
					 id: 'exportEvent'
				     },{
					 xtype: 'hidden',
					 id: 'exportArray'
		                     }
				   ],
		     bbar        : [
				    '->',
                                     {
		                       xtype: 'button',
                                       text:  'Export',
				       pressed: true,
				       handler: function() {
					     csvWin.hide();
					     var wait = Ext.MessageBox.progress("<span style='white-space:nowrap;'>Please wait. We are exporting the data ...</span>", "Status");
					     
					     var root = Ext.getCmp("csvtree").getRootNode();
					     var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
					     var exportDir = selectedNode.getPath('text');
					     //alert(exportDir);

					     $.getJSON( "jsp/export-csv.jsp",
                                                     { 
							 path: exportDir,
							 siteId: siteId,
							 event: Ext.getCmp('exportEvent').getValue(), 
							 array: Ext.getCmp('exportArray').getValue()
						     },
						     function(j) {
							 wait.hide();
							 msg("Status", 
							     "The data was saved into the file '"+j.name+"'");
						     });
				       }
		                     },{
					text: '&nbsp;'
				     },{
		                       xtype: 'button',
                                       text:  'Cancel',
				       pressed: true,
				       handler: function() {
					    csvWin.hide();
				       }
		                     },{
					text: '&nbsp;'
				     }
				   ]
    	         });

    var csvRoot = Ext.getCmp("csvtree").getRootNode();
    expand(csvRoot, homepath);

    return csvWin;
}





function getExportErrorWindow(event, array, size) {

    var errorWin = Ext.getCmp("exportErrorWin");
    if (errorWin != null) {
	errorWin.destroy();
	errorWin = null;
    }
    if (errorWin == null) {

	var errorStore = new Ext.data.JsonStore({
            root: 'message',
            fields: [
	        'trap',
		'imported',
		'start',
		'end',
		'unprocessed'
	     ],

             proxy: new Ext.data.HttpProxy({
		 url: 'jsp/export-validate.jsp?array='+array+'&event='+event+'&siteId='+siteId 
             })
         });


        var errorCM = new Ext.grid.ColumnModel([
		new Ext.grid.PagedRowNumberer(), 
	        {
                   id:'common',
                   header: "Camera Trap",
                   dataIndex: 'trap',
                   width: 80
                 },{
                   header: "Imported?",
                   dataIndex: 'imported',
                   width: 60,
		   renderer: displayInRed
                },{
                   header: "Start Photo",
                   dataIndex: 'start',
                   width: 70,
		   renderer: displayInRed
                },{
                   header: "End Photo",
                   dataIndex: 'end',
                   width: 70,
		   renderer: displayInRed
                },{
                   header: "Unprocessed Photo",
                   dataIndex: 'unprocessed',
                   width: 110,
		   renderer: displayInRed
                }]);

	var errorGridPanel = new Ext.grid.GridPanel({
		id: 'errorgrid',
		store: errorStore,
		cm: errorCM,
		width:460,
		height:240,
		frame:true,
		hidden:false,
		viewConfig: {
		    forceFit:false
		}
	    });

        errorStore.load.defer(100, errorStore,[ {params:{start:0, limit:100}}]);

        errorWin = new Ext.Window({
	             id          : 'exportErrorWin',
        	     title       : 'Errors',
		     layout      : 'table',
		     layoutConfig: {
		                     columns: 1
	                           },
        	     width       : 480,
        	     height      : 390,
        	     closeAction :'hide',
        	     plain       : true,
		     modal       : true,
        	     items       : [
	                              errorGridPanel,
	                              {
					 xtype: 'label',
		                         html: "<div style='padding:10pt;font-size:8pt;'>If you want to export the data for sending to the TEAM Network Office, please click the Cancel button and fix all the errors. <br><br>If you want to export the data for internal communication, please click the Continue button. This invalid data will not be accepted by TEAM Network.</div>",
					 frame: true
		                     }
				   ],
		     bbar        : [
				    '->',
                                     {
		                       xtype: 'button',
                                       text:  'Continue',
				       pressed: true,
				       handler: function() {
					     errorWin.destroy();
					     showExportDirWindow(event, array, size, false);
					 }	  
				     },{
					text: '&nbsp;'
				     },{
		                       xtype: 'button',
                                       text:  'Cancel',
				       pressed: true,
				       handler: function() {
					    errorWin.destroy();
				       }
		                     },{
					text: '&nbsp;'
				     }
				    ],
		     listeners     : {
		         close : function() {
			    errorWin.destroy();
		         }
		     }
	    });	   
    }
    return errorWin;


}



function displayInRed(value) {
    if (value == false) {
        return '<span style="color:red;">'+value+'</span>';
    } else if (value == true) {
        return '<span style="color:green;">'+value+'</span>';
    } else if (value > 0) {
        return '<span style="color:red;">'+value+'</span>';
    }
}


function showExportDirWindow(event, array, size, valid) {

    var fileWin = Ext.getCmp("fileWin");
    if (fileWin == null) {
	fileWin = createFileWindow(size);
    }
    fileWin.show();
    Ext.getCmp('exportEvent').setValue(event);	
    Ext.getCmp('exportArray').setValue(array);	
    Ext.getCmp('packageSize').setValue(size);	
    Ext.getCmp('packageValid').setValue(valid);	
    $('#packageSize').html(size+" Mb");			   
    
    var fileRoot = Ext.getCmp("filetree").getRootNode();
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



function showExportCSVDirWindow(event, array) {

    var csvWin = Ext.getCmp("csvWin");
    if (csvWin == null) {
	csvWin = createCSVFileWindow();
    }
    csvWin.show();
    Ext.getCmp('exportEvent').setValue(event);	
    Ext.getCmp('exportArray').setValue(array);	
    
    var csvRoot = Ext.getCmp("csvtree").getRootNode();
    expand(csvRoot, homepath);
}






function loadTeamPackage() {

    var packageWin = Ext.getCmp('packageWin');
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
		 url: 'jsp/get-package.jsp?' 
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
		id: 'packagegrid',
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
		id          : 'packageWin',
		title       : 'Select TEAM Package',
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
				      var record = Ext.getCmp('packagegrid').getSelectionModel().getSelected();
				      if (node == null || record == null) {
					  msg("Error", "No TEAM package is selected.");
				      } else {
					  packageWin.hide();
					  var waitBox = Ext.MessageBox.wait("Please wait. We are loading the package.", 
									    "Status");
					  $.getJSON( "jsp/load-package.jsp",
	                                             { siteId: siteId,
						       path: node.getPath('text')+"/"+record.get('name')},
                                                     function(j) {
							 waitBox.hide();

							 if (j.success) {
							     // reload tree
							     var root = Ext.getCmp("repository").getRootNode();
							     root.reload(function(){

								 });							
							     root.expand(false, 
								         true,
								         function() {							 
									      var eventNode = root.findChild('text', j.event);
									      eventNode.expand(false,
											       true,
											       function() {
												   if (j.array) {
										                          var arrayNode = eventNode.findChild('text', 
																	      "Array"+j.array);
													  arrayNode.expand(false, true, null);
												   }
											       });
									 });

							     var currentRepositoryPath = null;
							     var sTrap = null;
							     var sEvent = null;
							     var sArray = null;
							     var selectedRow = null;

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
							     //Ext.getCmp('photodatagrid').store.removeAll();

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



function updateViaFile() {

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
            fields: [
	        'name'
	    ],

            proxy: new Ext.data.HttpProxy({
		 url: 'jsp/get-configure.jsp?' 
            })
         });


        var confCM = new Ext.grid.ColumnModel([
	        {
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
			       confGridPanel
			      ],
		bbar        : [
			       '->',
	                      {
				  xtype: 'button',
				  text:  'Update',
				  pressed: true,
				  handler: function() {

				      var node = Ext.getCmp("filetree").getRootNode().getOwnerTree().getSelectionModel().getSelectedNode();
				      var record = Ext.getCmp('confgrid').getSelectionModel().getSelected();
				      if (node == null || record == null) {
					  msg("Error", "No configuration file is selected.");
				      } else {
					  confWin.hide();
					  var waitBox = Ext.MessageBox.wait("Please wait. We are validating the configuration file.", 
									    "Status");
					  $.getJSON( "jsp/load-configure.jsp",
	                                      { path: node.getPath('text')+"/"+record.get('name')},
                                                function(j) {
						   waitBox.hide();
						   if (j.success) {
						       waitBox = Ext.MessageBox.wait("<span style='white-space:nowrap;'>Please wait. We are updating the deskTEAM ... </span>", 
										     "Status");
						       $.getJSON("jsp/update-by-configure.jsp",
								 { path: j.path },
								 function(k) {
								     waitBox.hide();

								     if (k.success) {
									 msg("Status", "The deskTEAM has been updated using the TEAM configuration file."); 
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
				   text:  'Cancel',
				   pressed: true,
				   handler: function() {
				       confWin.hide();
				   }
			      },{
				   text: '&nbsp;'
			      }
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

}


function expand(node, datapath) {
    //alert("expand = "+datapath+"   "+node.getPath('text'));
    var k = datapath.indexOf("/");
    if (k != -1) {
	var head = datapath.substring(0, k);
	var tail = datapath.substring(k+1);
	//alert("head="+head+", tail="+tail);
	node.expand(false, 
		    true,
		    function() {
			var tmp = node.findChild('text', head);
			//alert("got node for "+head);
			if (tmp != null) {
			    tmp.expand(false, true);
			    expand(tmp, tail);
			}
		    });
    } else {
	//alert("expand = "+datapath+"   "+node.getPath('text'));	
	node.expand(false,
		    true,
		    function() {
			var tmp = node.findChild('text', datapath);
			if (tmp != null) {
			    tmp.select();
			    tmp.expand(false, true);
			}
		    });	
    }
}




function reportDamage() {
	
    var iWin = Ext.getCmp("damageWin");
    if (iWin != null) {
	iWin.show();
        return;
    }					

    // sampling event combo
    var dctEventStore = new Ext.data.JsonStore({
	  autoLoad: true,
          pruneModifiedRecords: true,
          url : "jsp/camera-event.jsp?site="+siteName+"&siteId="+siteId,
          root : "results",
          fields: ['name']
    });

    var dctEventCombo = new Ext.form.ComboBox({
          store: dctEventStore,
          id: 'devent',
          displayField:'name',
          typeAhead: true,
          mode: 'local',
          triggerAction: 'all',
          emptyText:'Select a sampling period',
          selectOnFocus:true,
          width: 130,	
          allowBlank: false,
	  fieldLabel: 'Sampling Event',
	  editable : true,
          listeners: {
	       select: function(obj, record, ind){
		    // reset trap
		    var trapBox  = Ext.getCmp('dtrapId');
		    trapBox.clearValue();
		    trapBox.clearInvalid();
		    trapBox.store.removeAll();
		    trapBox.store.load({ params: 
			                    {'event': record.get('name') }
			              });
		    trapBox.enable();
	       }
	 }
    });

    // camera trap id combo
    var dctTrapIdStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/camera-trap-id.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var dctTrapIdCombo = new Ext.form.ComboBox({
        store: dctTrapIdStore,
        id: 'dtrapId',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a camera trap',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Camera Trap ID',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });

    // camera serial combo
    var dctCameraSerialStore = new Ext.data.JsonStore({
	    autoLoad: true,
	    pruneModifiedRecords: true,
	    url : "jsp/camera-serial-number.jsp?siteId="+siteId,
	    root : "results",
	    fields: ['name']
	});

    var dctCameraSerialCombo = new Ext.form.ComboBox({
	    store: dctCameraSerialStore,
	    fieldLabel: 'Camera Serial Number',
            name: 'cameraSerial',                                                                                             
            id: 'dcameraSerial',                                                                                                 
            allowBlank: false,    
	    displayField:'name',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'Select a camera serial number',
	    selectOnFocus:true,
	    width: 130,
	    editable : true
	});

    // setting person store
    var dctSetPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var dctSetPersonCombo = new Ext.form.ComboBox({
        store: dctSetPersonStore,
        id: 'dsetperson',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a person',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Person Setting up the Camera',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });

    // picking person store
    var dctPickPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					
    var dctPickPersonCombo = new Ext.form.ComboBox({
        store: dctPickPersonStore,
        id: 'dpickperson',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Select a person',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Person Finding the Damage',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){
	            }
	    }
    });

    // dctDamageDetailCombo
    var dctDamageDetailStore = new Ext.data.SimpleStore({
	    fields: ['name'],
	    data : [
		['Trap Missing'],
		['Case Damaged'],
		['Camera Damaged'],
		['Card Damaged']
            ]
     });

     var dctDamageDetailCombo = new Ext.form.ComboBox({
        store: dctDamageDetailStore,
        id: 'dDamageDetail',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        emptyText:'Please select one',
        selectOnFocus:true,
        width: 130,	
        allowBlank: false,
	fieldLabel: 'Damage Type',
	editable : false,
        listeners: {
	    select: function(obj, record, ind){

		     var pane = Ext.getCmp('damageFormPanel');

		     if (record.get('name') == 'Camera Damaged') {
			 // insert dctCameraDetailStore
			 var dctCameraDetailStore = new Ext.data.SimpleStore({
				 fields: ['name'],
				 data : [
					 ['Is Firing'],
					 ['Has Humidity Inside'],
					 ['Other']
				     ]
			     });

			 var dctCameraDetailCombo = new Ext.form.ComboBox({
				 store: dctCameraDetailStore,
				 id: 'dCameraDetail',
				 displayField:'name',
				 typeAhead: true,
				 mode: 'local',
				 triggerAction: 'all',
				 emptyText:'Please select one',
				 selectOnFocus:true,
				 width: 130,	
				 allowBlank: false,
				 fieldLabel: 'Damaged Camera Detail',
				 editable : false,
				 listeners: {
				     select: function(obj, record, ind){
				     }
				 }
			     });
			 pane.insert(9, dctCameraDetailCombo);

                         pane.insert(10, 
				     {
			                xtype: 'textarea',
				        id:"dfollowup",
					name: 'dfollowup',
					fieldLabel:"Follow up Steps",
					height:70
				     });
			 pane.doLayout();
		     } else {
			 // remove dctCameraDetailStore
			 var dctCameraDetailCombo = Ext.getCmp('dCameraDetail');
			 if (dctCameraDetailCombo) {
			     pane.remove(dctCameraDetailCombo);
			     dctCameraDetailCombo.destroy();
			 }

			 var dfollowup = Ext.getCmp('dfollowup');
			 if (dfollowup) {
			     pane.remove(dfollowup);
			     dfollowup.destroy();
			 }

			 pane.doLayout();
		     }
		 }
	    }
    });


    // form panel
    var form = new Ext.form.FormPanel({
	    id: 'damageFormPanel', 
	    labelWidth: 170,
	    bodyStyle:'padding:15px',
	    width: 390,
	    labelPad: 10,
	    defaultType: 'textfield',
	    defaults: {
		// applied to each contained item
		width: 230,
		msgTarget: 'side'
	    },
	    layoutConfig: {
		// layout-specific configs go here
		labelSeparator: ''
	    },
	    items: [
		    dctEventCombo, 
		    dctTrapIdCombo,
		    dctCameraSerialCombo,
                    {
			fieldLabel: 'Memory Card Serial Number',
			name: 'dmemoryCardSerial',
			id: 'dmemoryCardSerial',
			allowBlank: false
		    },{
			fieldLabel: 'Camera Deploy Time',
			name: 'start',
			id: 'start',
			xtype: 'xdatetime',
			timeFormat:'H:i:s',
			dateFormat:'Y-n-d',
			dateConfig: {
			    altFormats:'Y-m-d|Y-n-d',
			    allowBlank:false
			},
			timeConfig: {
			    value: '12:00',
			    allowBlank:false
			}
		    },{
			fieldLabel: 'Damage Found Time',
			name: 'end',
			id: 'end',
			xtype: 'xdatetime',
			timeFormat:'H:i:s',
			dateFormat:'Y-n-d',
			dateConfig: {
			    altFormats:'Y-m-d|Y-n-d',
			    allowBlank:false
			},
			timeConfig: {
			    value: '12:00',
			    allowBlank:false
			}
		    },
		    dctSetPersonCombo,
		    dctPickPersonCombo,
		    dctDamageDetailCombo,
                    {
			xtype: 'textarea',
			id:"dnotes",
			name: 'dnotes',
			fieldLabel:"Notes",
			height:70
		    }
		    ]
	});


    win = new Ext.Window({
	id          : 'damageWin',
        title       : 'Damage Report',
        layout      : 'fit', 
        width       : 480,
        height      : 500,
        closeAction :'hide',
        plain       : true,
	modal       : true,
        items       : [ form ],
        buttons     : [{
                           text     : 'Submit',
			   id       : 'dsubmitButton',
                           handler  : function(){

					 if (form.getForm().isValid()) {

					     var thisTrapIdCombo = Ext.getCmp('dtrapId');
					     var trapName = Ext.getCmp('dtrapId').getValue();
					     var trapId = thisTrapIdCombo.store.getAt(thisTrapIdCombo.selectedIndex).get('id');
					     var event = Ext.getCmp('devent').getValue();
					     var cameraSerial = Ext.getCmp('dcameraSerial').getValue();    
					     var memoryCardSerial = Ext.getCmp('dmemoryCardSerial').getValue();

					     var setPersonCombo = Ext.getCmp('dsetperson');
					     var setPersonId = setPersonCombo.store.getAt(setPersonCombo.selectedIndex).get('id');
					     var firstset = Ext.getCmp('dsetperson').getValue();

					     var pickPersonCombo = Ext.getCmp('dpickperson');
					     var pickPersonId = pickPersonCombo.store.getAt(pickPersonCombo.selectedIndex).get('id');
					     var firstpick = Ext.getCmp('dpickperson').getValue();

					     //var start = Ext.getCmp('start').getValue().format(dateFormatMy.masks.myDateTime);
					     //var end = Ext.getCmp('end').getValue().format(dateFormatMy.masks.myDateTime);

					     var start = Ext.getCmp('start').getValue().format('Y-m-d G:i:s');
					     var end = Ext.getCmp('end').getValue().format('Y-m-d G:i:s');
					     
					     var damageDetail = Ext.getCmp('dDamageDetail').getValue();
					     var cameraDetailCombo = Ext.getCmp('dCameraDetail');
					     var cameraDetail = cameraDetailCombo == null ? null : cameraDetailCombo.getValue();

					     var notes = Ext.getCmp('dnotes').getValue();     
					     var dfollowup = Ext.getCmp('dfollowup');
					     var followup = dfollowup == null ? null : dfollowup.getValue();

					     Ext.getCmp('dsubmitButton').disable();
					     Ext.getCmp('dcloseButton').disable();

					     $.getJSON(
                                                         "jsp/damage-report.jsp",
                                                         { 
							     site: siteName, 
							     siteId: siteId,
							     trapName: trapName,
							     trapId: trapId,
							     event: event, 
							     cameraSerial: cameraSerial, 
							     memoryCardSerial: memoryCardSerial, 
							     setperson: firstset, 
							     pickperson: firstpick, 
							     setpersonid: setPersonId, 
							     pickpersonid: pickPersonId, 
							     damageDetail: damageDetail,
							     cameraDetail: cameraDetail,
							     start: start,
							     end: end,
							     followup : followup,
							     notes: notes 
							 },
                                                         function(j) {
							     Ext.getCmp('dsubmitButton').enable();
							     Ext.getCmp('dcloseButton').enable();
							     Ext.getCmp('damageWin').hide();
							     //win.hide();
							     if (j.success) {

								 // reset the form    
								 Ext.getCmp('dcameraSerial').reset();    
								 Ext.getCmp('dmemoryCardSerial').reset();    
								 Ext.getCmp('dnotes').reset();     
								 Ext.getCmp('dDamageDetail').reset();
								 if (cameraDetailCombo) {
								     var pane = Ext.getCmp('damageFormPanel');
								     pane.remove(cameraDetailCombo);

								     var dfollowup = Ext.getCmp('dfollowup');
								     pane.remove(dfollowup);

								     pane.doLayout();
								     if (cameraDetailCombo) cameraDetailCombo.destroy();
								     dfollowup.destroy();
								 }
								     
								 // reset trap
								 var trapBox  = Ext.getCmp('dtrapId');
								 trapBox.clearValue();
								 trapBox.clearInvalid();
								 trapBox.store.removeAll();
								 trapBox.store.load({ params:  {'event': event } });
								 trapBox.enable();

								 // reset trap list in import memory card UI
								 var eventBox  = Ext.getCmp('event');
								 if (eventBox != null) {
		                                                     eventBox.clearValue();
								 }

								 // reload tree
								 var root = Ext.getCmp("repository").getRootNode();
								 root.reload();							
								 root.expand(false, 
									     true,
									     function() {							 
										 var eventNode = root.findChild('text', event);
										 eventNode.expand(false,
												  true,
												  function() {
												      var arrayNode = eventNode.findChild('text', 
																	  "Array"+trapName.split("-")[2]);
												      arrayNode.expand(false,
														       true,
														       function() {
															   var trapNode = arrayNode.findChild('text', trapName);
															   Ext.getCmp('repository').selectPath(trapNode.getPath());
															   trapNode.ensureVisible();
														       });
												  });
									     });


								 // reset
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
														 trapName,
														 event);
								 ctPane.setTitle(null);
								 var dataHolder = Ext.getCmp('dataholder');
								 dataHolder.removeAll(true);
								 dataHolder.add(ctPane);
								 dataHolder.doLayout();

							     } else {
								 Ext.MessageBox.show({
									 title: 'Error Message',
									 msg: j.text,
									 width:350,
									 modal: true,
									 icon: Ext.Msg.INFO,
									 buttons: Ext.Msg.OK
								  }); 
							     }						  
							 });
							 
					 }
		    }


		},{
		    text     : 'Close',
		    id       : 'dcloseButton',
		    handler  : function(){
			Ext.getCmp("damageWin").hide();
		    }
		}]
	});

	win.show();

   

}




function createCameraTrappingExcelGridPane3(siteval, protocolVal, id, filename, errNum, trapName, event) {

   var photoDataLoaded = false;
   var damagedTrapLoaded = false;

   var tabs = new Ext.TabPanel({
        activeTab: 0,
        width:940,
        height:477,
        plain:true,
        //tabPosition: 'bottom',
	id: 'exceltab',
        defaults:{autoScroll: true},

        listeners :  {
	   tabchange: function(p, t) {
	       if (t.title == 'Camera Trap Data') {

	       }
	   }
	}

    });


    var yesnoStore = new Ext.data.SimpleStore({
                                   fields: ['name'],
                                   data : [
                                              ['YES'],
                                              ['NO']
                                          ]
                             });


    var ctCameraSerialStore = new Ext.data.JsonStore({
	    autoLoad: true,
	    pruneModifiedRecords: true,
	    url : "jsp/camera-serial-number.jsp",
	    root : "results",
	    fields: ['name']
	});



    // setting person store
    var ctSetPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });
					

    // picking person store
    var ctPickPersonStore  = new Ext.data.JsonStore({
         autoLoad: true,
         pruneModifiedRecords: true,
         url : "jsp/person.jsp?siteId="+siteId,
         root : "results",
         fields: ['id', 'name']
    });


    
   var trapDataCM = new Ext.grid.ColumnModel([
		new Ext.grid.PagedRowNumberer(), 
	        {
                   id:'common',
                   header: "Site",
                   dataIndex: 'Site',
		   hidden: true,
                   width: 100
                 },{
                   header: "Sampling Event",
                   dataIndex: 'Sampling_Period',
                   width: 100
                },{
                   header: "Camera Trap Point ID",
                   dataIndex: 'Camera_Trap_Point_ID',
                   width: 120
                },{
                   header: "Camera Serial Number",
                   dataIndex: 'Camera_Serial_Number',
                   width: 140,
		   editor: new Ext.form.ComboBox({
			   store: ctCameraSerialStore,
			   fieldLabel: 'Camera Serial Number',
			   name: 'cameraSerial',
			   allowBlank: false,    
			   displayField:'name',
			   typeAhead: true,
			   mode: 'local',
			   triggerAction: 'all',
			   emptyText:'Select a camera serial number',
			   selectOnFocus:true,
			   width: 130,
			   editable : true
		       })
                },{
                   header: "Memory Card Serial Number",
                   dataIndex: 'Memory_Card_Serial_Number',
                   width: 160,
                   editor: new Ext.form.TextField({
                      allowBlank: false
                   })
		},{
		   header: "Camera Deploy Date",
                   dataIndex: 'Start_Date',
                   width: 120,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
		},{
		   header: "Camera Deploy Time",
                   dataIndex: 'Start_Time',
                   width: 120
                },{
		   header: "Damage Found Date",
                   dataIndex: 'End_Date',
                   width: 120,
		   renderer: function (value) {
                       return '<span style="color:black;">'+ (value ? value.dateFormat('Y-m-d') : '') + '</span>';
                   }
		},{
		   header: "Damage Found Time",
                   dataIndex: 'End_Time',
                   width: 120
                },{
                   header: "Person Setup Camera",
                   dataIndex: 'Set_Person',
                   width: 140,
		   editor: new Ext.form.ComboBox({
			   store: ctSetPersonStore,
			   id: 'setter',
			   displayField:'name',
			   typeAhead: true,
			   mode: 'local',
			   triggerAction: 'all',
			   emptyText:'Select a person',
			   selectOnFocus:true,
			   width: 130,	
			   allowBlank: false,
			   fieldLabel: 'Person Setting up the Camera',
			   editable : false,
			   listeners: {
			       select: function(obj, record, ind){
			       }
			   }
		       })
                },{
                   header: "Person Found Damage",
                   dataIndex: 'Pick_Person',
                   width: 140,					
		   editor: new Ext.form.ComboBox({
			   store: ctPickPersonStore,
			   id: 'picker',
			   displayField:'name',
			   typeAhead: true,
			   mode: 'local',
			   triggerAction: 'all',
			   emptyText:'Select a person',
			   selectOnFocus:true,
			   width: 130,	
			   allowBlank: false,
			   fieldLabel: 'Person Picking up the Camera',
			   editable : false,
			   listeners: {
			       select: function(obj, record, ind){
			       }
			   }
		       })
                },{
                   header: "Camera Working?",
                   dataIndex: 'Camera_Working_',
                   width: 120
		   /*
		   ,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
		   */
                },{
                   header: "Camera Trap Missing?",
                   dataIndex: 'Camera_Trap_Missing_',
                   width: 140
		   /*
		   ,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
		   */
                },{
                   header: "Case Damage?",
                   dataIndex: 'Case_Damage_',
                   width: 120
		   /*
		   ,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
		   */
                },{
                   header: "Camera Damage?",
                   dataIndex: 'Camera_Damage_',
	           width: 120
		   /*
		   ,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
		   */
                },{
                   header: "Card Damage?",
                   dataIndex: 'Card_Damage_',
                   width: 120
		   /*
		   ,
		   editor: new Ext.form.ComboBox({
                       allowBlank: true,
                       store: yesnoStore,
                       displayField:'name',
                       typeAhead: true,
                       mode: 'local',
                       triggerAction: 'all',
                       editable : false,
                       lazyRender:true,
                       width:50,
                       listClass: 'x-combo-list-small'
                   })
		   */

                },{
                   header: "Is Firing?",
                   dataIndex: 'Is_Firing',
                   width: 100
                },{
                   header: "Humidity inside Camera?",
                   dataIndex: 'Humidity_Inside_Camera',
                   width: 160
                },{
                   header: "Follow Up Steps",
                   dataIndex: 'Follow_Up',
                   width: 350,
                   editor: new Ext.form.TextField({
                      allowBlank: true
                   })	   
                },{
                   header: "Notes",
                   dataIndex: 'Notes',
                   width: 450,
                   editor: new Ext.form.TextField({
                      allowBlank: true
                   })
	        }

              ]);


    var trapDataStore = new Ext.data.JsonStore({

            root: 'result',
            totalProperty: 'totalCount',
            remoteSort: true,
            fields: [
	        'Site',
		'Sampling_Period',
                'Camera_Trap_Point_ID',
                'Camera_Trap_Number',
                'Camera_Serial_Number',
                'Memory_Card_Serial_Number',
                {name: 'Start_Date', type: 'date', dateFormat: 'Y-m-d' },
                'Start_Time', 
                {name: 'End_Date', type: 'date', dateFormat: 'Y-m-d' },
                'End_Time',
		'Set_Person',
		'Pick_Person',
                'Camera_Working_',
                'Camera_Trap_Missing_',
                'Case_Damage_',
                'Camera_Damage_',
                'Card_Damage_',
                'Notes',
		'Is_Firing',
		'Humidity_Inside_Camera',
		'Follow_Up',
             ],
	
             proxy: new Ext.data.HttpProxy({
		 url: 'jsp/camera-trap-data.jsp?trapName='+trapName+'&event='+event 
             })
         });

         trapDataStore.setDefaultSort('Sid', 'DESC');

        // create the editor grid
        var trapDataPane = new Ext.grid.EditorGridPanel({
	    id: 'trapdatagrid',
            store: trapDataStore,
            cm: trapDataCM,
            width:940,
            height:470,
            frame:false,
	    hidden: false,
	    title: 'Damage Camera Trap Data',
	    sm : new Ext.grid.CheckboxSelectionModel(),
            viewConfig: {
                forceFit:false
            },
	    loadMask: true,
            bbar: new Ext.PagingToolbar({
	       id: 'pager',
               pageSize: 100,
               store: trapDataStore,
               displayInfo: true,
               displayMsg: 'Displaying records {0} - {1} of {2}   ',
               emptyMsg: "No records to display"
            }),


	   listeners :  {

                validateedit: function(obj) {

		    var value = obj.value;
		    var field = obj.field;

		    if (field == 'Follow_Up') {
                        if (!obj.record.get("Camera_Damage_")) {
                            Ext.Msg.show({
                                    title: 'Error',
                                    msg :  'Follow up steps are only needed when the camera is damaged.',
				    buttons: Ext.Msg.OK,
				    minWidth: 400
                            });
                            obj.cancel = true;
                        }
                    }

		},

		afteredit: function(obj) {

		    var value = obj.value;
		    var field = obj.field;

		    if (field == 'Pick_Person') {
			var picker = Ext.getCmp('picker');
			value = picker.store.getAt(picker.selectedIndex).get('id');
		    } else if (field == 'Set_Person') {
			var setter = Ext.getCmp('setter');
                        value = setter.store.getAt(setter.selectedIndex).get('id');
                    } 

		    // get sampling event
		    var event = obj.record.get("Sampling_Period");

		    // get camera trap id
		    var trapName = obj.record.get("Camera_Trap_Point_ID");

		    $.getJSON("jsp/edit-camera-trap-data.jsp",
	                      { 
				 trapName: trapName,
				 event: event, 
				 field: field,
				 value: value
			      },
                              function(j) {
				  if (j.success) {
				      //msg("Status", "The data has been updated.");	      
				  } else {
				      msg("Status", j.message);
				  }
			      });
                 }

	   }


        });

	var pager = Ext.getCmp('pager');
	pager.store.sortInfo = { field: 'Id', direction: 'DESC' };

        // trigger the data store load
        trapDataStore.load.defer(100, trapDataStore,[ {params:{start:0, limit:100}}]);

        tabs.add(trapDataPane);
        return tabs;

}


function validateTeamPackage() {

    var packageWin = Ext.getCmp('packageWin');
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
		 url: 'jsp/get-package.jsp?' 
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
		id: 'packagegrid',
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
		id          : 'packageWin',
		title       : 'Select TEAM Package',
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
				  text:  'Validate',
				  pressed: true,
				  handler: function() {

				      var node = Ext.getCmp("filetree").getRootNode().getOwnerTree().getSelectionModel().getSelectedNode();
				      var record = Ext.getCmp('packagegrid').getSelectionModel().getSelected();
				      if (node == null || record == null) {
					  msg("Error", "No TEAM package is selected.");
				      } else {
					  packageWin.hide();
					  var waitBox = Ext.MessageBox.wait("Please wait. We are validating the package.", 
									    "Status");
					  $.getJSON( "jsp/validate-package.jsp",
	                                             { path: node.getPath('text')+"/"+record.get('name')},
                                                     function(j) {
							 waitBox.hide();

							 if (j.success) {
							     msg("Success", "The file contains data for "+j.cameratrap+" camera trap(s) and is a valid TEAM package.");
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



function onSearch() {

    var win = Ext.getCmp('searchWin');
    if (win == null) {

        var searchSiteStore = new Ext.data.SimpleStore({
            fields: ['abbr', 'state', 'nick', 'cls'],
            data: searchSiteData,
            sortInfo: {field: 'state', direction: 'ASC'}
        });

        var searchTypeStore = new Ext.data.SimpleStore({
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

         var searchTypeCombo = new Ext.form.ComboBox({
	      id: 'searchTypeCombo',
              store: searchTypeStore,
              displayField: 'name',
              typeAhead: true,
              mode: 'local',
              forceSelection: false,
              triggerAction: 'all',
              emptyText: 'Select a type ...',
              selectOnFocus: true,
	      allowBlank: true,
	      value: 'Animal',
              editable: true,
	      name: 'searchTypeCombo',
	      fieldLabel: 'Photo Type',
	      listeners: {
                  select: function(obj, record, ind){
		      var panel = Ext.getCmp('searchPanel');
		      if (record.get('name') == 'Animal') {   
			 // show genus,	species and number
	                 var searchGenus = Ext.getCmp('searchGenus');
			 if (searchGenus == null) { 
                             panel.insert(4, getSearchSpeciesCombo());
			     panel.insert(4, getSearchGenusCombo());
			     panel.doLayout();		       
			 }
		      } else {
		         // remove genus, species and number
	                 var searchGenus = Ext.getCmp('searchGenus');
                         var searchSpecies = Ext.getCmp('searchSpecies');
			 
			 if (searchGenus != null) {
			     genusValue = searchGenus.getValue();
			     speciesValue = searchSpecies.getValue();

			     panel.remove(searchGenus);
			     panel.remove(searchSpecies);

			     searchGenus.destroy();
			     searchSpecies.destroy();
			 }
	              }

	          }
		 
              }


	});

        var form = new Ext.form.FormPanel({
			 id: 'searchPanel',
                         labelWidth: 80,
        		 bodyStyle:'padding:15px',
        		 width: 420,
        		 labelPad: 10,
			 frame: true,
        		 defaultType: 'textfield',
        		 defaults: {
            			     // applied to each contained item
            			     width: 310,
            			     msgTarget: 'side'
        			   },
        		 layoutConfig: {
            			     // layout-specific configs go here
            			     labelSeparator: ':'
        			   },
        		 items: [

      				  new Ext.ux.form.SuperBoxSelect({
                			  allowBlank:true,
					  msgTarget: 'title',
                			  id:'searchSites',
                			  xtype:'superboxselect',
                			  fieldLabel: 'Projects',
                			  resizable: true,
					  hiddenName : 'statesHidden[]',
                			  width:310,
                			  store: searchSiteStore,
                			  mode: 'local',
                			  displayField: 'state',
                			  //displayFieldTpl: '{state} ({abbr})',
					  displayFieldTpl: '{abbr}',	
                			  valueField: 'abbr',
					  navigateItemsWithTab: false,
					  //value : ['AL', 'AZ']
                			  listeners: {
                                               additem: function(){
						   // reload searchGenusStore
                        			   var searchGenus = Ext.getCmp('searchGenus');
						   searchGenus.clearValue();
						   searchGenus.store.load({params:{ sites: Ext.getCmp('searchSites').getValue() }});

                        			   var searchSpecies = Ext.getCmp('searchSpecies');
						   searchSpecies.clearValue();		
					       },
					       removeitem: function() {
						   // reload searchGenusStore
                        			   var searchGenus = Ext.getCmp('searchGenus');
						   searchGenus.clearValue();
						   searchGenus.store.load({params:{ sites: Ext.getCmp('searchSites').getValue() }});

                        			   var searchSpecies = Ext.getCmp('searchSpecies');
						   searchSpecies.clearValue();		
					       }
                			  }
             			   }),{
					  fieldLabel: 'Start Time',
					  name: 'searchStart',
					  id: 'searchStart',
					  xtype: 'xdatetime',
					  timeFormat:'H:i:s',
					  dateFormat:'Y-n-d',
					  dateConfig: {
			    		      altFormats:'Y-m-d|Y-n-d',
			    		      allowBlank:true
				          },
					  timeConfig: {
			    		      //value: '12:00',
			    		      allowBlank:true
					  }
		    		   },{
					  fieldLabel: 'End Time',
					  name: 'searchEnd',
					  id: 'searchEnd',
					  xtype: 'xdatetime',
					  timeFormat:'H:i:s',
					  dateFormat:'Y-n-d',
					  dateConfig: {
			    		      altFormats:'Y-m-d|Y-n-d',
			    		      allowBlank:true
					  },
					  timeConfig: {
			    		      //value: '12:00',
			    		      allowBlank:true
					  }
			            },
				    searchTypeCombo,
				    getSearchGenusCombo(), 					
				    getSearchSpeciesCombo() 
           			 ],
           		buttons: [{
		                    text     : 'Search',
		                    handler  : function() {
			                            Ext.getCmp('searchWin').hide();
					            var sites = Ext.getCmp("searchSites").getValue();
						    var startTime = Ext.getCmp("searchStart").getValue() ;
						    var endTime = Ext.getCmp("searchEnd").getValue() ;
						    var photoType = Ext.getCmp("searchTypeCombo").getValue();

						    var genusCombo = Ext.getCmp('searchGenus'); 
						    var genus;
					            if (genusCombo == null) {
							genus = "";
						    } else {
						        genus = genusCombo.getValue();
						    }

						    var speciesCombo = Ext.getCmp('searchSpecies'); 
						    var species;
					            if (speciesCombo == null) {
							species = "";
						    } else {
						        species = speciesCombo.getValue();
						    }

						    tSearchSites = sites;
						    tSearchStartTime = startTime;
						    tSearchEndTime = endTime; 
						    tSearchPhotoType = photoType;
						    tSearchGenus = genus;
						    tSearchSpecies = species; 	

						    setSearchStatus();

						    Ext.getCmp('tvPanel').hide();

					            Ext.getCmp('leftPanel').layout.setActiveItem(1);
						    var vgPanel = Ext.getCmp('vgPanel');
				                    vgPanel.show();
						    vgPanel.removeAll(true);
						    vgPanel.expand(true);
						    var searchResultPanel = createSearchResultTreePanel(sites, 
												        startTime,
													endTime,
													photoType,
										                        genus,
													species);
						    vgPanel.add(searchResultPanel);
						    searchResultPanel.show();
						    vgPanel.doLayout();
		                               }
		                 },{
		                    text     : 'Reset',
		                    handler  : function() {
						    Ext.getCmp("searchSites").reset();
						    Ext.getCmp("searchStart").reset() ;
						    Ext.getCmp("searchEnd").reset() ;
						    Ext.getCmp("searchTypeCombo").reset();
						    Ext.getCmp('searchGenus').reset();
						    Ext.getCmp('searchSpecies').reset();
		                               }
		                 },{
		                    text     : 'Close',
		                    handler  : function() {
			                            Ext.getCmp('searchWin').hide();
		                               }
			         }]
     		});

         var win = new Ext.Window({
			id          : 'searchWin',
        		title       : 'Search',
        		layout      : 'fit', 
        		width       : 480,
        		height      : 320,
        		closeAction :'hide',
        		plain       : true,
			modal       : true,
        		items       : [ form ],
		});
    }

    win.show();

}




function getSearchGenusCombo() {

        var searchGenusStore = new Ext.data.JsonStore({
		  autoLoad: true,
                  pruneModifiedRecords: true,
         	  url : "jsp/search-genus.jsp",
         	  root : "results",
         	  fields: ['name']
            });

        var searchGenusCombo = new Ext.form.ComboBox({
                store: searchGenusStore,
                fieldLabel: 'Genus',
                name: 'searchGenus',
		id: 'searchGenus',
                displayField:'name',
                typeAhead: true,
                mode: 'local',
	        triggerAction: 'all',
	        emptyText:'Select a genus',
                loadingText: 'Loading...',
                selectOnFocus:true,
                width: 180,
                allowBlank: true,
                editable : false,
	        listeners :  {
		   change: function(field,  newValue, oldValue, options ) {
			// reload searchSpeciesStore
                        var searchSpecies = Ext.getCmp('searchSpecies');
			searchSpecies.clearValue();
			searchSpecies.store.load({params:{ genus:  newValue }});		
		   }
	        }		
             });
	
       return searchGenusCombo;
}


function getSearchSpeciesCombo() {

       var searchSpeciesStore = new Ext.data.JsonStore({
		  autoLoad: true,
                  pruneModifiedRecords: true,
         	  url : "jsp/search-species.jsp",
         	  root : "results",
         	  fields: ['name']
            });

       var searchSpeciesCombo = new Ext.form.ComboBox({
                store: searchSpeciesStore,
                fieldLabel: 'Species',
                name: 'searchSpecies',
                id: 'searchSpecies',
                displayField:'name',
                typeAhead: true,
                mode: 'local',
	        triggerAction: 'all',
	        emptyText:'Select a species',
                loadingText: 'Loading...',
                selectOnFocus:true,
                width: 180,
                minChars: 1,
                allowBlank: true,
                editable : false
            });

	return searchSpeciesCombo;
}


function setSearchStatus() {
       var startTime = tSearchStartTime;
       if (startTime != null && startTime != "") {
            startTime = startTime.format('Y-n-d H:i:s')
       }

       var endTime = tSearchEndTime;
       if (endTime != null && endTime != "") {
            endTime = endTime.format('Y-n-d H:i:s')
       }

       $.getJSON(
                      "jsp/search-status.jsp?sites="+escape(tSearchSites)+
                      "&startTime="+escape(startTime)+
                      "&endTime="+escape(endTime)+
                      "&photoType="+escape(tSearchPhotoType)+
                      "&genus="+escape(tSearchGenus)+
                      "&species="+escape(tSearchSpecies),
                      null,
                      function(j) {
                          if (j.success) {
                              var msg = "";
                              if (j.total != null) {
                                  msg += "Found "+j.total+" images.";

                              }
                              Ext.getCmp('statusText').setText(msg);

                              Ext.getCmp('totalStatusText').setText("Login as <%= loginName %>");

                              Ext.MessageBox.show({
                                                     title: 'Search Result',
                                                     msg: msg,
                                                     width:250,
                                                     modal: true,
                                                     icon: Ext.Msg.INFO,
                                                     buttons: Ext.Msg.OK
                                                 });

                          }
         });
}




function createSearchResultTreePanel(sites,
	                             startTime,
				     endTime,
				     photoType,
		      	      	     genus,
				     species) {

	if (startTime != null && startTime != "") {
	   startTime = startTime.format('Y-n-d H:i:s')
	}

	if (endTime != null && endTime != "") {
	   endTime = endTime.format('Y-n-d H:i:s')
	}

        var root = new Ext.tree.AsyncTreeNode({
               id:'0',
	       path: '/Sites',
               text: 'Projects'
        });

        var data = new Ext.tree.TreeLoader({
	    url:'jsp/search.jsp?sites='+escape(sites)+"&startTime="+escape(startTime)+"&endTime="+escape(endTime)+"&photoType="+escape(photoType)+"&genus="+escape(genus)+"&species="+escape(species)
        }); 

        data.on('beforeload',
                function(treeLoader,node){  
                        this.baseParams.text=node.attributes.text;  
                        this.baseParams.path=node.attributes.path;
                },
	        data); 

        var searchResultPanel = new Ext.tree.TreePanel({
                                  id: 'searchResult',
                                  region:'west',
                                  collapsible: false,
                                  margins: '5 0 0 5',
                                  cmargins: '5 0 0 0',
			     	  cls: 'padding 5',
                             	  width: 240,
			          height: Math.max(546, docHeight-140),
                                  minSize: 240,
                                  maxSize: 240,
			          root: root,
				  rootVisible: true,
			          loader: data,
			          autoScroll : true,
                                  fitToFrame:true,
			          border: false,
				  listeners :  {
				      click: function(node, event) {

					  if (node.attributes.leaf) {
						
						var path = node.attributes.path.substring(7);
						var elements = path.split("/");

					        var title = node.attributes.site;
					        title += " : "+elements[1];        // event
					        title += " : Array"+elements[2];   // array
						title += " : "+elements[3];        // trap
					        title += " : "+elements[4];        // image

					     	//path = path.replace(/\//g, " : ");

						// set the image title
                                                var iPane  = Ext.getCmp('ImageViewer');
						iPane.setText(title);

						// load image
					        var img = document.getElementById("imageview");
						img.src = "/deskTEAM/image-repository/"+escape(node.attributes.site)+"/"+elements[1]+"/Array"+elements[2]+"/"+elements[3]+"/"+elements[4];

						//alert(img.src);
						zoomreset();

						// reload gallery panel
						if (!inGallery(elements[1], elements[3], elements[4])) {
						     var galleryStore = Ext.getCmp("gallery").store;
						     galleryStore.load({
									     params:{ siteId:elements[0],
										      trapName:elements[3],
						 			              event:elements[1],
										      raw:elements[4],
										      path:escape("/"+node.attributes.site+
												  "/"+elements[1]+
											          "/Array"+elements[2]+
												  "/"+elements[3]+
												  "/"+elements[4])}
                                                                       });
						} 

						// highlight the photo in the group view
						highlightPhotoInGroup(node.attributes.text);
						
				                // show species panel
					        var sPane  = Ext.getCmp('speciesPanel');
						sPane.setVisible(true);
						    
						if (Ext.isSafari) {
                                                    $('#updateButton').offset({left:$('#applytogroup').offset().left+80,
									       top:$('#updateButton').offset().top});
						}

						// change the update target
					        siteName = node.attributes.site;
				 		siteId = elements[0];
					        sArray = "Array"+elements[2];
					        sTrap = elements[3]; 
						sEvent = elements[1];
						sRaw = elements[4];

						// set species panel by the values in the database
				                $.getJSON(
                                                          "jsp/photo-species-set.jsp",
                                                          { site: node.attributes.site, 
							    path: "/"+node.attributes.site+"/"+elements[1]+"/Array"+elements[2]+"/"+elements[3]+"/"+elements[4]},
                                                          function(j) {

							     $('#imgName').html(node.attributes.text);
							     $('#Trg').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.trg+"</span>");
							     $('#Flash').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.flash+"</span>");
							     //$('#Exposure').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.exposure+"</span>");
                                                             $('#TakenTime').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.takentime+"</span>");
							     $('#Tmp').html("<span class='x-form-item'>&nbsp;&nbsp;&nbsp;"+j.temperature+"</span>");

						             if (j.success) {

								if (j.editable == true) {
							            editable = j.editable;
								    Ext.getCmp("updateButton").enable();
								} else if (j.editable == false) {
							            editable = j.editable;
							            Ext.getCmp("updateButton").disable();
								} else {
								    editable = false;
							        }
							
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

								if (Ext.getCmp('unotes') != null) {
								    Ext.getCmp('unotes').setValue(j.notes);
								}

								var idperson = Ext.getCmp('idperson');
		    						idperson.setValue(j.idperson);
                    						idperson.clearInvalid();

								idperson.store.load({params:{ searchSiteId: siteId },
										     callback: function(rs, opt, succ) {
												   for (var i=0; i<rs.length; i++) {
												       if (rs[i].get('name') == j.idperson) {
													  idperson.selectedIndex = i;
													  break;
												       }
												   }
										               }	
										   });

								var k = 0;
								var idPersonCombo = Ext.getCmp('idperson');
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

								if (ugenus == null) {
								    insertSpeciesUI(); 
								} else {
		    						    Ext.getCmp('ugenus').setValue(null);
                    						    Ext.getCmp('ugenus').clearInvalid();

		    						    Ext.getCmp('uspecies').setValue(null);
 					                            Ext.getCmp('uspecies').clearInvalid();
								}

		    						Ext.getCmp('unumber').setValue(null);
                    						Ext.getCmp('unumber').clearInvalid();

		    						//Ext.getCmp('idperson').reset();
                    						//Ext.getCmp('idperson').clearInvalid();

								if (Ext.getCmp('unotes') != null) {
								    Ext.getCmp('unotes').setValue(j.notes);
								}

							  }
						      }
						);

					        var tmp = "CT-"+ sRaw.split("-")[1]+"-"+sArray+"-"+sEvent+"-"+sTrap;
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
					      }

						

					  }
				      }
				  }
	          		});	


      root.expand(false, true);

      var selectionModel = root.getOwnerTree().getSelectionModel()
      selectionModel.on("selectionchange", 
	    		      function(m, n) {
			          syncSearchGUI();
		       }); 

      return searchResultPanel;

}




function syncSearchGUI() {

           var isSearchResult = Ext.getCmp('vgPanel').isVisible();
           if (!isSearchResult) return;

           var root = Ext.getCmp("searchResult").getRootNode();
           var selectedNode = root.getOwnerTree().getSelectionModel().getSelectedNode();
           if (selectedNode == null) return;
           var elements = selectedNode.getPath('text').split("/");

           var fEvent = elements.length > 3 ? elements[3] : null;
           var fArray = elements.length > 4 ? elements[4] : null;
           var fTrap = elements.length > 5 ? elements[5] : null;
           var fRaw = elements.length > 6 ? elements[6] : null;

           $.getJSON(
               "jsp/find-image.jsp",
               { event: fEvent, trap: fTrap, raw: fRaw},
               function(j) {
                  if (j.success) {
                      sEvent = j.img_event;
                      sArray = j.img_array;
                      sTrap = j.img_trap;
                      sRaw = j.img_raw;

                      path = "/"+siteId+"/"+j.img_event+"/"+j.img_array+"/"+j.img_trap+"/"+j.img_raw;

                      // refresh single view
                      var iPane  = Ext.getCmp('ImageViewer');
                      iPane.setText(siteName+" : "+j.img_event+" : "+j.img_array+" : "+j.img_trap+" : "+j.img_raw);

                      var img = document.getElementById("imageview");
                      var pos = sTrap.lastIndexOf("-");
                      var arrayName = sTrap.substring(0, pos);
		      var arrayIndex = sTrap.substring(pos+1);
                      img.src = "/deskTEAM/image-repository/"+escape(siteName)+"/"+sEvent+"/Array"+arrayIndex+"/"+sTrap+"/"+sRaw;

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
                                           j.takentime,
                                           j.notes);

                      // highlight the record on the datagrid view
                      highlightDatagridRecord();
                      selectedNode.ui.getAnchor().focus();

                    }
               });

       }


