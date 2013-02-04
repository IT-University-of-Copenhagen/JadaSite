var menuWidth = "100%";
var ie = false;

function sm_getMenuId(objId) {
	var idlists = objId.split("_");
	return idlists[0];
}

function sm_getMenuLevel(objId) {
	var value = "";
	var idlists = objId.split("_");
	for (var i = 2; i < idlists.length; i++) {
		if (value.length > 0) {
			value += "_";
		}
		value += idlists[i];
	}
	return value;
}

function sm_getParentLevel(objId) {
	var value = "";
	var idlists = objId.split("_");
	for (var i = 2; i < idlists.length - 1; i++) {
		if (value.length > 0) {
			value += "_";
		}
		value += idlists[i];
	}
	return value;
}

function sm_getLevelCount(id) {
	var idlists = id.split("_");
	return idlists.length - 2;
}

function sm_menuDivOver(obj) {
	menuId = sm_getMenuId(obj.id);
	var parentObj = obj;
	while (parentObj.tagName != 'DIV') {
		parentObj = parentObj.parentNode;
	}
	eval(menuId + '_currMenuId=parentObj.id');
}

function sm_menuDivOut(obj) {
	menuId = sm_getMenuId(obj.id);
	eval(menuId + '_currMenuId=""');
	var parentObj = obj;
	while (parentObj.tagName != 'DIV') {
		parentObj = parentObj.parentNode;
	}
	menuObjId = parentObj.id;
	window.setTimeout('sm_popSubMenu(\'' + menuObjId + '\')', 100);
}

function sm_menuOver(obj) {
	menuId = sm_getMenuId(obj.id);
	eval(menuId + '_currItemId=obj.id');
//	currItemId = obj.id;
	menuLevel = sm_getMenuLevel(obj.id);
	menuObjId = menuId + '_menu_' + sm_getParentLevel(obj.id);
	parentItemId = menuId + '_item_' + sm_getParentLevel(obj.id);
	childObjId = menuId + '_menu_' + menuLevel;
	obj.className = 'sm_menuOver';
	parentObj = document.getElementById(parentItemId);
	if (parentObj != null) {
		parentObj.className = 'sm_menuOver';
	}
	
	eval(menuId + '_currMenuId=menuObjId');
//	currMenuId = menuObjId;
	childObj = document.getElementById(childObjId);
	if (childObj == null) {
		return;
	}

	topObj = document.getElementById(menuObjId);  // This is the div that contains the menu
	// Firefox has -ve relative position for the first item.  This is to compensate for that.
	firstItemId = menuId + '_item_' + sm_getParentLevel(obj.id) + '_1';
	firstItemObj = document.getElementById(firstItemId);
	var vertical = true;
	eval('vertical=' + menuId + '_vertical');
	
	// Calculate the table cell padding
	var padding = 0;
	parentObj = obj.parentNode; // this is the cell that contains the div
	// Only align to the bottom when position is veritcal and it is directly related to the 1st level.
	if (!vertical && sm_getLevelCount(childObjId) == 2) {

		padding = (topObj.offsetHeight - obj.offsetHeight) / 2;
		childObj.style.left = topObj.offsetLeft + obj.offsetLeft - padding;
		childObj.style.top = topObj.offsetTop + obj.offsetHeight + padding;
	}
	else {
		padding = (topObj.offsetWidth - obj.offsetWidth) / 2;
		childObj.style.left = topObj.offsetLeft + obj.offsetWidth + + padding;
		childObj.style.top = topObj.offsetTop + obj.offsetTop - firstItemObj.offsetTop;
	}

	childObj.style.visibility = 'visible';
	childObj.style.display = 'block';

}

function sm_menuOut(obj) {
	menuId = sm_getMenuId(obj.id);
	eval(menuId + '_currMenuId=""');
	eval(menuId + '_currItemId=""');
	
	menuObjId = menuId + '_menu_' + sm_getMenuLevel(obj.id);
	obj.className = 'sm_menu';
	window.setTimeout('sm_popSubMenu(\'' + menuObjId + '\')', 100);
}

function sm_popSubMenu(menuObjId) {
	menuId = sm_getMenuId(menuObjId);
	eval('currMenuId=' + menuId + '_currMenuId');
	var currItemId = '';
	eval('currItemId=' + menuId + '_currItemId');
	// Do not remove any menu if moving deeper
//	var currMenuId = '';
	if (sm_getLevelCount(currMenuId) >= sm_getLevelCount(menuObjId)) {
		return;
	}
	// Do not remove the first menu
	if (sm_getLevelCount(menuObjId) == 1) {
		return;
	}
	parentItemId = menuId + '_item_' + sm_getMenuLevel(menuObjId);
	if (parentItemId != currItemId) {
		menuObj = document.getElementById(menuObjId);
		if (menuObj != null) {
			menuObj.style.visibility = 'hidden';
			// Reverse item entry that bring up the menu
			parentItemObj = document.getElementById(parentItemId);
			parentItemObj.className = 'sm_menu';
		}
	}
	parentMenuId = menuId + '_menu_' + sm_getParentLevel(menuObjId);
	sm_popSubMenu(parentMenuId);
}

function sm_menuClick(node) {
	var cnodes = node.childNodes;
	for (j = 0; j < cnodes.length; j++) {
		c = cnodes[j];
		if (c.tagName == 'A') {
			document.location.href = c.href;
		}
	}
}


function sm_generateMenu(menuId, style, childWidth) {
	if(navigator.appName.indexOf("Microsoft") > -1){
		ie = true;
	} 
	else {
		ie = false;
	}

	ul = document.getElementById(menuId);
	if (style == "horizontal") {
		eval(menuId + '_vertical=' + false);
	}
	else {
		eval(menuId + '_vertical=' + true);
	}
	if (childWidth > 0) {
		menuWidth = childWidth + "px";
	}

	menuDiv = sm_createMenu(menuId, menuId + '_menu_1', ul);

        menuDiv.className = "sm_topMenu";
	var container = ul.parentNode;
 	container.appendChild(menuDiv);
	container.removeChild(ul);
}

function sm_createMenu(menuId, menuLevelId, ul) {
	var parentDiv = document.createElement('div');
	parentDiv.onmouseover = function() { sm_menuDivOver(this) };
	parentDiv.onmouseout = function() { sm_menuDivOut(this) };
	parentDiv.setAttribute('id', menuLevelId);
	parentDiv.className = "sm_containerMenu";
	if (ie && sm_getLevelCount(menuLevelId) > 1) {
		parentDiv.style.display = 'none';
	}
	var table = document.createElement('table');
	table.className="sm_table";
	// Not able to set this in css. This is a hack.
	table.cellPadding = "0";
	var tableBody = document.createElement('tbody');

	var row;
	var vertical;
	eval('vertical=' + menuId + '_vertical');
	if (!vertical && sm_getLevelCount(menuLevelId) == 1) {
		row = document.createElement('tr');
		tableBody.appendChild(row);
	}
	
	var children = ul.childNodes;
	var count = 0;
	for (var i = 0; i < children.length; i++) {
		var li = children[i];
		if (!li.tagName) {
			continue;
		}

		if (vertical || sm_getLevelCount(menuLevelId) != 1) {
			row = document.createElement('tr');
			tableBody.appendChild(row);
		}

		var cell = document.createElement('td');
		cell.className="sm_tableCell";

		var childDiv = document.createElement('div');
		childDiv.setAttribute('id', menuId + '_' + 'item_' + sm_getMenuLevel(menuLevelId) + '_' + (count + 1));
		childDiv.className = 'sm_menu';
		// Do not set the height since it will throw off firefox.
		//childDiv.style.height = "100%";
		childDiv.onmouseover = function(){ sm_menuOver(this) };
		childDiv.onmouseout = function() { sm_menuOut(this) };
		childDiv.onclick = function() { sm_menuClick(this) };

		var cnodes = li.childNodes;
		var found = false;
		for (var j = 0; j < cnodes.length; j++) {
			c = cnodes[j];
			if (c.tagName == 'A') {
				childDiv.appendChild(c);
				c.className = "sm_anchor";
				found = true;
			}
			if (c.tagName == 'UL') {
				var childMenuId = menuId + '_menu_' + sm_getMenuLevel(menuLevelId) + '_' + (count + 1);
				var childMenu = sm_createMenu(menuId, childMenuId, c);
				document.body.appendChild(childMenu);
				found = true;
			}
		}
		if (!found) {
			childDiv.innerHTML = li.innerHTML;
		}

		// Do not set the width for the first level. Let it calculate automatically.
		if (sm_getLevelCount(childDiv.id) > 2) {
			childDiv.style.width = menuWidth;
		}
		else {
			childDiv.style.width = "100%";
		}
		cell.appendChild(childDiv);
		row.appendChild(cell);
		count++;
	}


	table.appendChild(tableBody);
	parentDiv.appendChild(table);
	return parentDiv;
}
