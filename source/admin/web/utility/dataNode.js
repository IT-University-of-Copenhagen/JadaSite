

YAHOO.widget.DataNode = function(oData, oKey, oParent, expanded) {
	YAHOO.widget.DataNode.superclass.constructor.call(this, oData, oParent, expanded);
    this.key = oKey;
};

YAHOO.extend(YAHOO.widget.DataNode, YAHOO.widget.TextNode, {
	key: 0
});