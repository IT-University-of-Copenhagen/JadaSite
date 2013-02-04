function Carousel(el, cfg) {
    var container = document.getElementById(el);
    var innerContainer;
    var config = cfg;
    
    var isVertical = false;
    var itemWidth = 0;
    var itemHeight = 0;
    var numVisible = 3;
    var margin = 1;
    var itemCount = 0;
    var currentCount = 0;
    
    var get = function(name) {
        return eval('config.' + name);
    };
    
    var init = function() {
        var value = '';
        value = get('isVertical');
        if (value != undefined) {
            isVertical = value;
        }
        
        value = get('itemWidth');
        if (value != undefined) {
            itemWidth = value;
        }
        
        value = get('itemHeight');
        if (value != undefined) {
        	itemHeight = value;
        }
        
        value = get('numVisible');
        if (value != undefined) {
            numVisible = value;
        }
        
        value = get('margin');
        if (value != undefined) {
            margin = value;
        }
        if (!isVertical) {
            var containerWidth = itemWidth * numVisible + (numVisible - 1) * margin * 2 + 2 * margin;
            container.style.width = containerWidth + 'px';
        }
        else {
            var containerHeight = itemHeight * numVisible + (numVisible - 1) * margin * 2 + 2 * margin;
            container.style.height = containerHeight + 'px';
        }
        
        var children = YAHOO.util.Dom.getChildrenBy(container, function(el) {
            if (el.tagName == 'OL'); {
                return true;
            }
            return false;
        });
        innerContainer = children[0];
        var list = innerContainer.getElementsByTagName("li"); 
        itemCount = list.length;
        currentCount = 1;
        innerContainer.style.display = 'block';
    }();
    
    this.next = function() {
        if ((itemCount - currentCount) < numVisible) {
            return;
        }
        currentCount++;
        var attributes;
        if (isVertical) {
            var offset = -1 * (currentCount - 1) * (itemHeight + margin * 2);
            attributes = {
                top: { to: offset}
            }
        }
        else {
            var offset = -1 * (currentCount - 1) * (itemWidth + margin * 2);
            attributes = {
                left: { to: offset}
            }
        }
        var anim = new YAHOO.util.Anim(innerContainer, attributes, 1, YAHOO.util.Easing.easeOut);
        anim.animate(); 
    }
    
    this.previous = function() {
        if (currentCount <= 1) {
            return;
        }
        currentCount--;
        if (isVertical) {
            var offset = -1 * (currentCount - 1) * (itemHeight + margin * 2);
            attributes = {
                top: { to: offset}
            }
        }
        else {
            var offset = -1 * (currentCount - 1) * (itemWidth + margin * 2);
            attributes = {
                left: { to: offset}
            }
        }
        var anim = new YAHOO.util.Anim(innerContainer, attributes, 1, YAHOO.util.Easing.easeOut);
        anim.animate(); 
    }

}