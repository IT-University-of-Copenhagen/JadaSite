function isJsonResponseValid(responseText) {
  try {
    if (responseText == undefined) {
      return false;
    }
    var jsonObject = eval('(' + responseText + ')');
    return true;
  }
  catch (e) {
    return false;
  }
  return true;
}

function getJsonObject(responseText) {
    var jsonObject = eval('(' + responseText + ')');
	return jsonObject;
}

function setPreviewImage(divId, imageURL) {
	document.jc_image_preview.src = imageURL;
	return false;
}

function jc_item_compare_add(contextPath, prefixPath, itemId) {
  jQuery.ajax( {
      timeout: 30000,
      url: '/' + contextPath + '/content/content/update.do?process=itemCompareAdd&prefix=' + prefixPath + '&itemId=' + itemId,
      type: 'GET',
      success: function(response) {
	  	jc_item_compare_paint(contextPath, prefixPath);
      }
    }
  );
}

function jc_item_compare_remove(contextPath, prefixPath, itemId) {
  jQuery.ajax( {
      timeout: 30000,
      url: '/' + contextPath + '/content/content/update.do?process=itemCompareRemove&prefix=' + prefixPath + '&itemId=' + itemId,
      type: 'GET',
      success: function(response) {
	  jc_item_compare_paint(contextPath, prefixPath);
      }
    }
  );
}

function jc_item_compare_paint(contextPath, prefixPath) {
  jQuery.ajax( {
      timeout: 30000,
      dataType: 'html',
      url: '/' + contextPath + '/content/content/update.do?process=itemCompareList&prefix=' + prefixPath,
      type: 'GET',
      success: function(response) {
	  	$('#item-compare-select-container').html(response);
      }
    }
  );
}

function jc_script_escape(input) {
	document.write(input.replace(/\'/, "\\'"));
}

function jc_contactUs_submit(form) {
	var success = true;
	$(form).find('input').each(function(index) {
	    if ($(this).hasClass('jc_validation_noempty')) {
	    	var id = $(this).attr("id");
	    	if ($('#' + id + '_message')) {
		    	if (this.value.length == 0) {
		    		$('#' + id + '_message').css('display', 'block');
		    		success = false;
		    	}
		    	else {
		    		$('#' + id + '_message').css('display', 'none');
		    	}
		    }
	    }
	});
	return success;
}