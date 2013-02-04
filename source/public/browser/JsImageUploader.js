function JsImageUploader() {

context = null;

JsImageUploader.ACTION_LISTCURRENT = 'listCurrent';
JsImageUploader.ACTION_LISTPREVIOUS = 'listPrevious';
JsImageUploader.ACTION_LISTNEXT = 'listNext';
JsImageUploader.ACTION_UPLOADFILE = 'uploadFile';
JsImageUploader.ACTION_REMOVEFILE = 'removeFile';
JsImageUploader.ACTION_CREATEFOLDER = 'createFolder';

instanceName = null;

/* Main panel for image browing */
mainPanel = null;
mainPanelBody = null;
mainPanelId = null;

/* Panel for uploading new files */
uploadPanel = null;
uploadPanelId = null;
uploadForm = null;

/* Create folder panel */
createPanel = null;
createPanelId = null;
createInput = null;
createMessage = null;

var currentFolder = '/';
dom = YAHOO.util.Dom;
editorId = null;
config = null;



this.listFolder = function(action, current, targetFolder) {
	this.currentFolder = current;
    var url = this.context + '/yuiImageBrowser';
    url += '?action=' + action;
    url += '&currentFolder=' + this.currentFolder;
    url += '&targetFolder=' + targetFolder;
    this.mainPanelBody.innerHTML = '';
    var master = this;
	var listCallBack = {
	    success: function(o) {
	        var jsonObject = eval('(' + o.responseText + ')');
	        master.currentFolder = jsonObject.folder;
	        var table = document.createElement('table');
	        table.setAttribute('width', '100%');
	        master.mainPanelBody.appendChild(table);
	        tableBody = document.createElement("tbody");
	        table.appendChild(tableBody);
			var row = document.createElement('tr');
	        tableBody.appendChild(row);
	        var breadcrumCol = document.createElement('td');
	        row.appendChild(breadcrumCol);
	        var insertCol = document.createElement('td');
	        insertCol.setAttribute('width', '100%');
	        row.appendChild(insertCol);
	        var insertDiv = document.createElement('div');
	        insertDiv.setAttribute('align', 'right');
	        insertCol.appendChild(insertDiv);

	        /*
	         *  Create button
	         */
	        var buttonMenu = [
	        	{ text: "Upload new file", value: master.editorId, onclick: { fn: master.uploadFileDialog } }, 
	        	{ text: "Create folder", value: master.editorId, onclick: { fn: master.createFolderDialog } },
	        	{ text: "Remove selected file", value: master.editorId, onclick: { fn: master.removeFile } }
	        ];
			var menu = new YAHOO.widget.Button({ 
                            type: "menu", 
                            label: "Options", 
                            name: "menu",
                            menu: buttonMenu, 
                            container: insertDiv });           
	        
	        /*
	         *  Create breadcrumb
	         */
	        var anchor;
	        if (jsonObject.folder != '/') {
		        anchor = document.createElement('a');
		        anchor.setAttribute('class', master.config.linkStyle);
	        	anchor.setAttribute('href', 'javascript:void(0);');
	    		anchor.setAttribute('onclick', master.instanceName + '.listFolder(\'' + JsImageUploader.ACTION_LISTCURRENT + '\', \'/\', \'' + 
	    									  '' + 
	    									  '\')');
	    		anchor.appendChild(document.createTextNode('home'));
		        breadcrumCol.appendChild(anchor);
	        }
	        else {
	        	breadcrumCol.appendChild(document.createTextNode('home'));
	        }
	        for (var i = 0; i < jsonObject.breadcrumb.length; i++) {
	        	if (i != jsonObject.breadcrumb.length - 1) {
		        	breadcrumCol.appendChild(document.createTextNode('/'));
		        	anchor = document.createElement('a');
		        	anchor.setAttribute('class', master.config.linkStyle);
	        		anchor.setAttribute('href', 'javascript:void(0);');
	    			anchor.setAttribute('onclick', master.instanceName + '.listFolder(\'' + JsImageUploader.ACTION_LISTCURRENT + '\', \'' + 
		    								  	  jsonObject.breadcrumb[i].folder + '\', \'' + 
	    										  '' + 
	    										  '\')');
	    			anchor.appendChild(document.createTextNode(jsonObject.breadcrumb[i].name));
		        	breadcrumCol.appendChild(anchor);
	        	}
	        	else {
	        		breadcrumCol.appendChild(document.createTextNode('/' + jsonObject.breadcrumb[i].name));
	        	}
	        }
	         
	        table = document.createElement('table');
//	        table.setAttribute('id', 'tableId');
	        table.setAttribute('width', '100%');
	        master.mainPanelBody.appendChild(table);
	        tableBody = document.createElement("tbody");
	        table.appendChild(tableBody);
	        
	        /*
	         *  Create .. entry for navigation to the parent level.
	         */
	        if (jsonObject.folder != '/') {
	        	var row = document.createElement('tr');
	        	tableBody.appendChild(row);
	        	var col = null;
	        	col = document.createElement('td');
	        	col.setAttribute('width', '20px');
	        	row.appendChild(col);
	        	col = document.createElement('td');
	        	col.setAttribute('width', '100%');
	        	anchor = document.createElement('a');
		        anchor.setAttribute('class', master.config.linkStyle);
	    		anchor.setAttribute('href', 'javascript:void(0);');
	    		anchor.setAttribute('onclick', master.instanceName + '.listFolder(\'' + JsImageUploader.ACTION_LISTPREVIOUS + '\', \'' + 
	    								  jsonObject.folder + '\', \'' + 
	    								  '' + 
	    								  '\')');
	    		anchor.appendChild(document.createTextNode('..'));
	    		col.appendChild(anchor);
	        	row.appendChild(col);
	        	col = document.createElement('td');
	        	col.setAttribute('class', master.config.textStyle);
	        	col.setAttribute('width', '50');
	        	col.appendChild(document.createTextNode('<dir>'));
	        	row.appendChild(col);
	        	col = document.createElement('td');
	        	col.setAttribute('class', master.config.textStyle);
	        	col.setAttribute('width', '10%');
	        	row.appendChild(col);
	        	col = document.createElement('td');
	        	col.setAttribute('class', master.config.textStyle);
	        	col.setAttribute('width', '10%');
	        	row.appendChild(col);
        	}
        	

			/*
			 *  Create directory entry.
			 */
	        for (var i = 0; i < jsonObject.files.length; i++) {
	        	var row = document.createElement('tr');
	        	tableBody.appendChild(row);
	        	var col = null;
	        	col = document.createElement('td');
	        	col.setAttribute('width', '20px');
	        	row.appendChild(col);

	        	var checkbox = document.createElement('input');
	        	checkbox.setAttribute('type', 'checkbox');
	        	checkbox.setAttribute('name', 'checkbox');
	        	checkbox.setAttribute('value', jsonObject.files[i].name);
	        	YAHOO.util.Dom.addClass(checkbox, 'jsiu_checkbox'); 
	        	col.appendChild(checkbox);

	        	col = document.createElement('td');
	        	col.setAttribute('width', '100%');
	        	row.appendChild(col);
	        	if (jsonObject.files[i].isDirectory) {
	        		var a = document.createElement('a');
		        	a.setAttribute('class', master.config.linkStyle);
	       			a.setAttribute('href', 'javascript:void(0);');
	       			a.setAttribute('onclick', master.instanceName + '.listFolder(\'' + JsImageUploader.ACTION_LISTNEXT + '\', \'' + 
	       									  jsonObject.folder + '\', \'' + 
	       									  jsonObject.files[i].name + 
	       									  '\')');
	       			a.appendChild(document.createTextNode(jsonObject.files[i].name));
	       			col.appendChild(a);
	        	}
	        	else {
	        		var item = document.createElement('a');
		        	item.setAttribute('class', master.config.linkStyle);
	        		item.setAttribute('href', 'javascript:void(0);');
	        		item.appendChild(document.createTextNode(jsonObject.files[i].name));
	        		col.appendChild(item);
	        		YAHOO.util.Event.on(item, 'click', function(o) {
	        			JsImageUploader.select(master, this.innerHTML);
	        		} );
	        	}
	        	
	        	col = document.createElement('td');
	        	col.setAttribute('class', master.config.textStyle);
	        	col.setAttribute('width', '50');
	        	row.appendChild(col);
	        	if (jsonObject.files[i].isDirectory) {
	        		col.appendChild(document.createTextNode('<dir>'));
	        	}
	        	if (jsonObject.files[i].isImage) {
	        		var image = new Image();
	        		var imageName = '';
	        		if (jsonObject.folder != '/') {
	        			imageName += jsonObject.folder;
	        		}
	        		imageName += '/' + jsonObject.files[i].name;
	        		imageScale(image, imageName, 50);
	        		col.appendChild(image);
	        	}
	        	
	        	col = document.createElement('td');
	        	col.setAttribute('class', master.config.textStyle);
	        	col.setAttribute('width', '10%');
	        	col.noWrap = true;
	        	row.appendChild(col);
	        	col.appendChild(document.createTextNode(jsonObject.files[i].lastUpdateOn));
	        	            	
	        	col = document.createElement('td');
	        	col.setAttribute('class', master.config.textStyle);
	        	col.setAttribute('width', '10%');
	        	row.appendChild(col);
	        	if (!jsonObject.files[i].isDirectory) {
	         	var div = document.createElement('div');
	         	div.setAttribute('align', 'right');
	         	col.appendChild(div);
	         	div.appendChild(document.createTextNode(jsonObject.files[i].size));
	        	}
	       }
	       
	    },
	    failure: function(o) {
	        alert('failure to call backend');
	    }
	}

	var request = YAHOO.util.Connect.asyncRequest('GET', url, listCallBack, null);
}

JsImageUploader.prototype.init = function(contextPath, editor, configuration) {
	this.context = contextPath;
	this.editorId = editor.get('id');
	this.instanceName = 'jsiu_' + this.editorId;
	this.config = configuration;
	this.dom = YAHOO.util.Dom;
	
	editor.invalidHTML = { 
	};
	
    editor.addListener('toolbarLoaded', function(master) {
        var panel = null;
        var patched = false;
        var editorId = this.editorId;
		/*
		 *  Setup plain text switcher
		 */
		var textSwitcherConfig = {
			type: 'push',
			label: 'View HTML',
			value: 'toggleText',
            menu: function() {
			    var htmlPanelId = 'jsiu_htmlPanel_' + editorId;
				var htmlPanel = new YAHOO.widget.Panel(htmlPanelId, 
                        			{ width: '600px',
									  height: '400px',
									  fixedcenter : true,
                          			  visible : false,
                          			  constraintoviewport : true,
                          			  modal : true
                          			});
				htmlPanel.setHeader('HTML text view');
				var table = document.createElement('table');
				table.cellspacing = '0';
				table.cellpadding = '0';
				table.style.width = '100%';
				table.style.height = '100%';
				tableBody = document.createElement("tbody");
				table.appendChild(tableBody);
				var row = document.createElement('tr');
				tableBody.appendChild(row);
				var col = document.createElement('td');
				var div = document.createElement('div');
				div.setAttribute('align', 'right');
				col.appendChild(div);
				row.appendChild(col);
				var button = new YAHOO.widget.Button({label: 'Save',
													  id: 'button',
													  container: div });

				row = document.createElement('tr');
				tableBody.appendChild(row);
				col = document.createElement('td');
				col.style.width = '100%';
				col.style.height = '100%';
				row.appendChild(col);

				var textArea = document.createElement('textarea');
				col.appendChild(textArea);
				textArea.setAttribute('name', 'textarea');
				textArea.setAttribute('id', 'htmlTextAreaId');
				textArea.style.width = '98%';
				textArea.style.height = '300px';

				htmlPanel.beforeShowEvent.subscribe(function() {
					editor.saveHTML();
					textArea.value = editor.get('textarea').value;
                }); 
				
				button.on('click', function() {;
					editor.setEditorHTML(textArea.value);
					htmlPanel.hide();
				});
				htmlPanel.setBody(table);
				htmlPanel.render(document.body);
				return htmlPanel;
            }()
		};
		editor.toolbar.addButtonToGroup(textSwitcherConfig, 'insertitem');
	 
        /*
         *  Setup Image browser panel.
         */
    
        this.mainPanelId = 'jsiu_mainPanel_' + this.editorId;
        panel = document.createElement('div');
        document.body.appendChild(panel);
        panel.setAttribute('id', this.mainPanelId);
    
        var header = document.createElement('div');
        panel.appendChild(header);
        header.className = 'hd';
        header.innerHTML = 'Image Browser';

        this.mainPanelBody = document.createElement('div');
        panel.appendChild(this.mainPanelBody);
        this.mainPanelBody.className = 'bd';
        this.mainPanelBody.setAttribute('style', 'overflow : auto;');

        this.mainPanel = new YAHOO.widget.Panel(this.mainPanelId, 
                        			{ width : this.config.width,
                          			  height : this.config.height,
                          			  fixedcenter : true,
                          			  visible : false,
                          			  constraintoviewport : true,
                          			  modal : true
                          			});
        this.mainPanel.render();
    
        this.createPanelId = 'jsiu_createPanel_' + this.mainPanelId;
       	panel = document.createElement('div');
 	    panel.setAttribute('id', this.createPanelId);
	    document.body.appendChild(panel);
	    header = document.createElement('div');
	    header.setAttribute('class', 'hd');
	    header.innerHTML = 'Create folder';
		panel.appendChild(header);
	    var body = document.createElement('div');
	    body.setAttribute('class', 'bd');
	    body.setAttribute('style', 'overflow : auto;');
	    panel.appendChild(body);
	    body.appendChild(document.createTextNode('Create new folder'));
	    this.createInput = document.createElement('input');
	    this.createInput.setAttribute('type', 'text');
	    this.createInput.setAttribute('name', 'text');
	    body.appendChild(this.createInput);
		var button;
        button = document.createElement('input');
        button.setAttribute('type', 'submit');
        button.setAttribute('value', 'Create');
        body.appendChild(button);
        this.createMessage = document.createElement('div');
        body.appendChild(this.createMessage);
        YAHOO.util.Event.on(button, 'click', function(o) {
            YAHOO.util.Event.stopEvent(o);
        	this.createFolder(this);
        }, this, true );
    	this.createPanel = new YAHOO.widget.Panel(this.createPanelId, 
                                    { width : "20em",
                                      height : "10em",
                                      fixedcenter : true,
                                      visible : false, 
                                      constraintoviewport : true,
                                      modal : true
                                      });
		this.createPanel.render();
	    panel = document.createElement('div');
	    this.uploadPanelId = 'jsiu_uploadPanel_' + this.editorId;
 	    panel.setAttribute('id', this.uploadPanelId);
	    document.body.appendChild(panel);
	    header = document.createElement('div');
		panel.appendChild(header);
		header.className = 'hd';
	    header.innerHTML = 'Image Browser';
	    body = document.createElement('div');
	    panel.appendChild(body);
	    body.className = 'bd';
	    body.setAttribute('style', 'overflow : auto;');
	    this.uploadForm = document.createElement('form');
	    body.appendChild(this.uploadForm);
	    this.uploadForm.setAttribute('method', 'post');
	    this.uploadForm.setAttribute('enctype', 'multipart/form-data');
	    this.uploadForm.setAttribute('action', 'o');
	    this.uploadForm.appendChild(document.createTextNode('Upload new file'));
	    this.uploadForm.appendChild(document.createElement('br'));
	    var input = document.createElement('input');
	    input.setAttribute('type', 'file');
	    input.setAttribute('name', 'file1');
	    this.uploadForm.appendChild(input);
		var button;
        button = document.createElement('input');
        button.setAttribute('type', 'submit');
        button.setAttribute('value', 'Upload');
        this.uploadForm.appendChild(button);
        YAHOO.util.Event.on(button, 'click', function(o) {
            YAHOO.util.Event.stopEvent(o);
        	this.uploadFile(this);
        }, this, true );

    	this.uploadPanel = new YAHOO.widget.Panel(this.uploadPanelId, 
                                    { width : "20em",
                                      height : "10em",
                                      fixedcenter : true,
                                      visible : false, 
                                      constraintoviewport : true,
                                      modal : true
                                      });
		this.uploadPanel.render();

        editor.toolbar.addListener('insertimageClick', function(o, master) {
            if (!patched) {
                var object = dom.get(master.editorId + '_insertimage_url');
                object = master.dom.getAncestorByTagName(object, 'label');
                
                var label = document.createElement('label');
                var empty = document.createElement('strong');
                label.appendChild(empty);               
                var anchor = document.createElement('a');
                var anchorId = 'jsiu_' + master.editorId + '_link';
                label.appendChild(anchor);
                anchor.setAttribute('id', anchorId);
        		anchor.setAttribute('href', 'javascript:void(0);');
	    		anchor.appendChild(document.createTextNode('Insert image'));
	    		var button = new YAHOO.widget.Button(anchor);
                dom.insertAfter(label, object);
                patched = true;
                button.on("click", function(o1) {
	                YAHOO.util.Event.stopEvent(o1);
	                this.mainPanel.show();
	                this.listFolder(JsImageUploader.ACTION_LISTCURRENT, '/', '');
	            }, this, true );
            }
        }, this, true );
    }, this, true );
}

this.showHTML = function() {
		alert('here');
};

this.createFolderDialog = function() {
	eval('master = jsiu_' + this.value);
	master.createInput.value = '';
	master.createMessage.innerHTML = '';
	master.createPanel.show();
};

this.uploadFileDialog = function() {
	eval('master = jsiu_' + this.value);
	master.uploadPanel.show();
	master.uploadForm.file.value = '';
};

this.removeFile = function() {
	eval('master = jsiu_' + this.value);
	var url = master.context + '/yuiImageBrowser';
	url += '?action=' + JsImageUploader.ACTION_REMOVEFILE;
    var data = 'currentFolder=' + master.currentFolder;
    var elements = YAHOO.util.Dom.getElementsByClassName('jsiu_checkbox', master.mainPanelBody.id);
    for (var i = 0; i < elements.length; i++) {
    	var object = elements[i];
    	if (object.checked) {
    		data += '&filenames=' + object.value;
    	}
    }
    var callback = {
    	success: function(o) {
    		master.listFolder(JsImageUploader.ACTION_LISTCURRENT, master.currentFolder, '');
    	}
    }
    var response = YAHOO.util.Connect.asyncRequest('POST', url, callback, data);
};

this.createFolder = function() {
	var url = this.context + '/yuiImageBrowser';
	url += '?action=' + JsImageUploader.ACTION_CREATEFOLDER;
    url += '&currentFolder=' + this.currentFolder;
    url += '&folder=' + this.createInput.value;
    var master = this;
    var callback = {
    	success: function(o) {
	        var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'failed') {
	        	master.createMessage.innerHTML = jsonObject.message;
	        }
	        else {
    			master.createPanel.hide();
    			master.listFolder(JsImageUploader.ACTION_LISTCURRENT, currentFolder, '');
    		}
    	}
    }
    var response = YAHOO.util.Connect.asyncRequest('GET', url, callback);
}

this.uploadFile = function() {
	YAHOO.util.Connect.setForm(this.uploadForm, true, true);
	var url = this.context + '/yuiImageBrowser';
	url += '?action=' + JsImageUploader.ACTION_UPLOADFILE;
    url += '&currentFolder=' + this.currentFolder;
    var master = this;
    var callback = {
    	upload: function(o) {
    	    var value = o.responseText.replace(/<\/?pre>/ig, '').replace(/<pre.*>/ig, '');
	        var jsonObject = eval('(' + value + ')');
	        if (jsonObject.status == 'failed') {
	        }
	        else {
    			master.uploadPanel.hide();
    			master.listFolder(JsImageUploader.ACTION_LISTCURRENT, master.currentFolder, '');
    		}
    	}
    }
    var response = YAHOO.util.Connect.asyncRequest('GET', url, callback);
}

JsImageUploader.select = function(master, item) {
	var object = master.dom.get(master.editorId + '_insertimage_url');
	var value = master.config.urlPrefix;
	if (master.currentFolder != '/') {
		value += master.currentFolder;
	}
	value += '/' + item;
	object.value = value;
	master.close();
}



function imageScale(image, imageName, max) {
	image.style.display = 'none';
	image.src = imageName;
    YAHOO.util.Event.on(image, 'load', function(o) {
        YAHOO.util.Event.stopEvent(o);
		var height = image.height;
		var width = image.width;
		
		var ratio = 1;
		if (height > max) {
			ratio = max / height;
		}
		height *= ratio;
		width *= ratio;
		
		ratio = 1;
		if (width > max) {
			ratio = max / width;
		}
		height *= ratio;
		width *= ratio;
		
		image.height = height;
		image.width = width;
		image.style.display = 'block';
    } );
}

this.close = function() {
	this.mainPanel.hide();
};

}

function jsiu_getInstance(context, editor, config) {
	var objectName = 'jsiu_' + editor.get('id');
	var command = objectName + ' = new JsImageUploader()';
	eval(command);
	command = objectName + '.init("' + context + '", editor, config)';
	eval(command);
}