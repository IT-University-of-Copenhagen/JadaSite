<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page errorPage="/global/jerror.jsp" %>
<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html:html>
<head>
<title>JadaSite e-commerce solutions - <c:out value='${pageTitle}'/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- 
http://developer.yahoo.com/yui/articles/hosting/?button&calendar&connectioncore&container&datatable&dom&editor&element&menu&tabview&treeview&MIN
 -->

<link rel="stylesheet" type="text/css" href="/${adminBean.contextPath}/admin/web/yui_combine.css"> 
<link rel="stylesheet" type="text/css" href="/${adminBean.contextPath}/admin/web/styles.css">

<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/yui_combine.js"></script>
<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/javacontent.js"></script> 
<script type="text/javascript" src="/${adminBean.contextPath}/public/browser/JsImageUploader.js"></script>

<style type="text/css">
a.yui_image_browser_a:active,
a.yui_image_browser_a:link,
a.yui_image_browser_a:visited
{
    font-size: 1em;
  color: #333333;
  text-decoration: none;
}
a.yui_image_browser_a:hover {
    font-size: 1em;
    color: #000000;
  text-decoration: underline;  
}
.yui_image_browser_text {
  font-size: 1em;
  color: #333333;
  text-decoration: normal;
}

</style>

<script type="text/javascript">
    YAHOO.widget.MenuItem.prototype.IMG_ROOT = "/${adminBean.contextPath}/admin/web/yui/images/";  
    YAHOO.example.onMenuBarReady = function(p_oEvent) {
        // Instantiate and render the menu bar
        var oMenuBar = new YAHOO.widget.MenuBar("jc_menu", { autosubmenudisplay:true, hidedelay:750, lazyload:false });
        oMenuBar.render();
        // A hack to make sure it render in IE. Possibly will be fixed in the later version of YUI library.
        oMenuBar.hide();
        oMenuBar.show();
    }
    // Initialize and render the menu bar when it is available in the DOM
    YAHOO.util.Event.onContentReady("done", YAHOO.example.onMenuBarReady, "");
</script>
</head>
<jsp:useBean id="adminBean" class="com.jada.admin.AdminBean" scope="session"/>
<body bgcolor="#FFFFFF" text="#000000" class="yui-skin-sam">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding-bottom: 5px">
        <tr>
          <td width="100%">
            <img src="/${adminBean.contextPath}/admin/images/jadasite-logo.gif">
          </td>
          <td nowrap valign="bottom">
            <div align="right">
              <span class="jc_logo">Administration panel</span><br>
              <span class="jc_logo">Current site - ${adminBean.siteId}</span>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td>
      <div id="jc_menu" class="yuimenubar yuimenubarnav" style="font-size: 1em; line-height: 2em">
        <div class="bd"> 
          <ul class="first-of-type">
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="/${adminBean.contextPath}/admin/home/home.do?process=list">Home</a>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="/${adminBean.contextPath}/admin/content/contentListing.do?process=start">Content</a>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Item catalog</a>
              <div id="item" class="yuimenu"> 
                <div class="bd"> 
                  <ul>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/customAttribute/customAttributeListing.do?process=start" class="yuimenuitemlabel">Custom attribute</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/customAttribute/customAttributeGroupListing.do?process=start" class="yuimenuitemlabel">Custom attribute group</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/item/itemListing.do?process=start" class="yuimenuitemlabel">Item</a></li>
                  </ul>
                </div>
              </div>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="/${adminBean.contextPath}/admin/inventory/orderListing.do?process=start">Order</a>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Store</a>
              <div id="store" class="yuimenu"> 
                <div class="bd"> 
                  <ul>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/customer/customerListing.do?process=start" class="yuimenuitemlabel">Customers</a></li>
                    <li class="yuimenuitem">
                      <a href="#tax" class="yuimenuitemlabel">Taxes</a>
                      <div id="tax" class="yuimenu"> 
                        <div class="bd"> 
                          <ul>
                            <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/tax/taxListing.do?process=start" class="yuimenuitemlabel">Tax Rates</a></li>
                            <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/tax/taxRegionListing.do?process=start" class="yuimenuitemlabel">Tax Region</a></li>
                          </ul>
                        </div>
                      </div>
                    </li>
                    <li class="yuimenuitem">
                      <a href="#shipping" class="yuimenuitemlabel">Shipping</a>
                      <div id="shipping" class="yuimenu"> 
                        <div class="bd"> 
                          <ul>
                            <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/shipping/shippingTypeListing.do?process=start" class="yuimenuitemlabel">Shipping Type</a></li>
                            <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/shipping/shippingRegionListing.do?process=start" class="yuimenuitemlabel">Shipping Region</a></li>
                            <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/shipping/shippingMethodListing.do?process=start" class="yuimenuitemlabel">Shipping Method / Rate</a></li>
                          </ul>
                        </div>
                      </div>
                    </li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/coupon/couponListing.do?process=start" class="yuimenuitemlabel">Coupons</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/paymentGateway/paymentGatewayListing.do?process=start" class="yuimenuitemlabel">Payment gateway</a></li>
                  </ul>
                </div>
              </div>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Public Site</a>
              <div id="public" class="yuimenu"> 
                <div class="bd">
                  <ul>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/template/templateListing.do?process=start" class="yuimenuitemlabel">Template</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/homePage/homePageListing.do?process=start" class="yuimenuitemlabel">Home Page</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/moderation/moderationMaint.do?process=start" class="yuimenuitemlabel">Comments moderation</a></li>
                  </ul>
                </div>
              </div>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Module</a>
              <div id="module" class="yuimenu"> 
                <div class="bd"> 
                  <ul>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/syndication/syndicationMaint.do?process=start" class="yuimenuitemlabel">Syndication</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/poll/pollListing.do?process=start" class="yuimenuitemlabel">Poll</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/contactus/contactUsListing.do?process=start" class="yuimenuitemlabel">Contact Us</a></li>
                  </ul>
                </div>
              </div>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Import/Export beta</a>
              <div id="ie" class="yuimenu"> 
                <div class="bd"> 
                  <ul>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/ie/ieProfileListing.do?process=start" class="yuimenuitemlabel">Profile</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/ie/import.do?process=start" class="yuimenuitemlabel">Import</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/ie/export.do?process=start" class="yuimenuitemlabel">Export</a></li>
                  </ul>
                </div>
              </div>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Reporting beta</a>
              <div id="reporting" class="yuimenu"> 
                <div class="bd"> 
                  <ul>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/report/reportListing.do?process=start" class="yuimenuitemlabel">Reports maintenance</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/report/reportGeneratorListing.do?process=start" class="yuimenuitemlabel">Reports generation</a></li>
                  </ul>
                </div>
              </div>
            </li>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Setup</a>
              <div id="setup" class="yuimenu"> 
                <div class="bd"> 
                  <ul>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/menu/menuListing.do?process=start" class="yuimenuitemlabel">Menus</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/category/categoryMaint.do?process=start" class="yuimenuitemlabel">Categories</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/country/countryListing.do?process=start" class="yuimenuitemlabel">Country</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/productClass/productClassListing.do?process=start" class="yuimenuitemlabel">Product class</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/customerClass/customerClassListing.do?process=start" class="yuimenuitemlabel">Customer class</a></li>
                  </ul>
                </div>
              </div>
            </li>
            <c:if test="${adminBean.userType != 'R'}">
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="javascript:void(null);">Administration</a>
              <div id="administration" class="yuimenu"> 
                <div class="bd"> 
                  <ul>
                    <c:if test="${adminBean.userType == 'S'}">
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/language/languageListing.do?process=start" class="yuimenuitemlabel">Language</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/site/siteListing.do?process=start" class="yuimenuitemlabel">Sites</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/site/siteProfileClassListing.do?process=start" class="yuimenuitemlabel">Site profile class</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/site/siteCurrencyClassListing.do?process=start" class="yuimenuitemlabel">Site currency class</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/indexing/indexingMaint.do?process=start" class="yuimenuitemlabel">Site index</a></li>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/system/systemMaint.do?process=start" class="yuimenuitemlabel">Reset installation</a></li>
                    </c:if>
                    <li class="yuimenuitem"><a href="/${adminBean.contextPath}/admin/user/userListing.do?process=start" class="yuimenuitemlabel">Users</a></li>
                  </ul>
                </div>
              </div>
            </li>
            </c:if>
            <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="/${adminBean.contextPath}/admin/logout.do">Sign Out</a>
            </li>
          </ul>
        </div>
      </div>
    </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
</table>
<br>
<div class="yui-skin-sam">
<jsp:include page='<%= adminBean.getContentPage() %>'/>
</div>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td> 
      <div align="center"><font size="1" face="Helvetica, sans-serif" color="#666666"><br>
        Copyright by JadaSite, 2008 - 2011</font></div>
    </td>
  </tr>
</table>
<div id="done"></div>
</body>
</html:html>

