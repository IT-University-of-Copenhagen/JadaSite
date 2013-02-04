/*
 * Copyright 2007-2010 JadaSite.

 * This file is part of JadaSite.
 
 * JadaSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JadaSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JadaSite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jada.util;

public class Constants {
	public static final String SYSTEM_ENCODING = "UTF-8";
	public static final String MENU_HOME = "HOME";
	public static final String MENU_ITEM = "ITEM";
	public static final String MENU_CONTENT = "CONT";
	public static final String MENU_SECTION = "SECT";
	public static final String MENU_STATIC_URL = "SURL";
	public static final String MENU_SIGNIN = "SIGI";
	public static final String MENU_SIGNOUT = "SIGO";
	public static final String MENU_CONTACTUS = "CTUS";
	public static final String MENU_NOOPERATION = "NOOP";

	public static final String SESSION_COOKIE_USER = "user";
	public static final String SESSION_CONTENT_SESSION_KEYS = "com.jada.content.session.keys";
	public static final String SESSION_CONTENT_SESSION_CUSTOMER = "com.jada.content.session.custId";
	public static final String SESSION_CONTENT_SESSION_CONTENTBEAN = "contentBean";
	public static final String SESSION_CONTENT_SESSION_REQUESTID = "com.jada.content.session.requestId";
	public static final String SESSION_CONTENT_SESSION_ITEMCOMPARE = "com.jada.content.session.itemCompareList";
	
	public static final String SHIPPINGREGION_OTHERS = "Others (Default)";
	
	public static final String CREDITTYPE_VOID = "V";
	public static final String CREDITTYPE_OPEN = "O";
	public static final String CREDITTYPE_CLOSE = "C";
	
	public static final String ORDERSTATUS_OPEN = "O";
	public static final String ORDERSTATUS_PROCESSING = "P";
	public static final String ORDERSTATUS_COMPLETED = "C";
	public static final String ORDERSTATUS_CLOSED = "R";
	public static final String ORDERSTATUS_CANCELLED = "X";
	public static final String ORDERSTATUS_VOIDED = "V";
	public static final String ORDERSTATUS_ONHOLD = "H";
	
	public static final String ORDER_STEP_CART = "CT";
	public static final String ORDER_STEP_REVIEWADDRESS = "RA";
	public static final String ORDER_STEP_REVIEWPURCHASE = "RP";
	public static final String ORDER_STEP_CREDITCARD = "CC";
	public static final String ORDER_STEP_PAYPAL = "PP";
	public static final String ORDER_STEP_SHIPPINGQUOTE = "SQ";
	public static final String ORDER_STEP_QUOTE_CART = "Q_CT";
	public static final String ORDER_STEP_QUOTE_REVIEWPURCHASE = "Q_RP";
	public static final String ORDER_STEP_QUOTE_CREDITCARD = "Q_CC";
	
	public static final String ALERT_TYPE_AGREE = "A";
	public static final String ALERT_TYPE_DISAGREE = "D";
	public static final String ALERT_TYPE_MODERATOR = "M";
	
	public static final char PUBLISHED_YES = 'Y';
	public static final char PUBLISHED_NO = 'N';
	
	public static final char ACTIVE_YES = 'Y';
	public static final char ACTIVE_NO = 'N';
	
	public static final char VALUE_YES = 'Y';
	public static final char VALUE_NO = 'N';
	
	public static final String MODE_CREATE = "C";
	public static final String MODE_UPDATE = "U";
	
	public static final String USERNAME_CUSTOMER = "customer";
	public static final String USERNAME_SYSTEM = "system";
	public static final String USERNAME_IMPORT = "import";
	public static final String USERTYPE_SUPER = "S";
	public static final String USERTYPE_ADMIN = "A";
	public static final String USERTYPE_REGULAR = "R";
	
	public static final String CUSTOMER_ADDRESS_CUST = "C";
	public static final String CUSTOMER_ADDRESS_SHIPPING = "S";
	public static final String CUSTOMER_ADDRESS_BILLING = "B";
	public static final String CUSTOMER_SOURCE_SYSTEM = "system";
	public static final String CUSTOMER_SOURCE_PAYPAL = "paypal";
	public static final String CUSTOMER_SOURCE_REGISTER = "register";
	
	public static final String ACTIONFORM_ACTION_CREATE = "C";
	public static final String ACTIONFORM_ACTION_UPDATE = "U";
	public static final String ACTIONFORM_ACTION_START = "S'";
	
	public static final String SITE_DEFAULT = "default";
	public static final String SITE_SYSTEM = "_system";
	
	public static final String SITEPARAM_PREFIX = "siteparam.";
	public static final String SITEPARAM_CHECKOUT_SHOPPINGCART_MESSAGE = "siteparam.checkout.shoppingCart.message";
	public static final String SITEPARAM_CHECKOUT_SHOPPINGCART_MESSAGE_LANG = "siteparam.checkout.shoppingCart.message.lang";
	
	public static final String SITEPARAM_PAYMENT_PROCESS_TYPE = "siteparam.payment.process.type";
	public static final String SITEPARAM_PAYMENT_PAYPAL_APIUSERNAME = "siteparam.payment.paypal.apiUsername";
	public static final String SITEPARAM_PAYMENT_PAYPAL_APIPASSWORD = "siteparam.payment.paypal.apiPassword";
	public static final String SITEPARAM_PAYMENT_PAYPAL_SIGNATURE = "siteparam.payment.paypal.signature";
	public static final String SITEPARAM_PAYMENT_PAYPAL_ENVIRONMENT = "siteparam.payment.paypal.environment";
	public static final String SITEPARAM_PAYMENT_PAYPAL_ENVIRONMENT_PRODUCTION = "siteparam.payment.paypal.environment.production";
	public static final String SITEPARAM_PAYMENT_PAYPAL_ENVIRONMENT_SANDBOX = "siteparam.payment.paypal.enviornment.sandbox";
	public static final String SITEPARAM_PAYMENT_PAYPAL_EXTRAAMOUNT = "siteparam.payment.paypal.extraAmount";
	public static final String SITEPARAM_PAYMENT_PAYPAL_EXTRAPERCENTAGE = "siteparam.payment.paypal.extraPercentage";
	public static final String SITEPARAM_PAYMENT_PAYPAL_CUSTCLASSID = "siteparam.payment.paypal.custClassId";	
	
	public static final String SITEPARAM_PAYMENT_PSIGATE_NAME = "PSIGate";
	public static final String SITEPARAM_PAYMENT_PSIGATE_STOREID = "siteparam.payment.psigate.storeId";
	public static final String SITEPARAM_PAYMENT_PSIGATE_PASSPHRASE = "siteparam.payment.psigate.passphrase";
	public static final String SITEPARAM_PAYMENT_PSIGATE_ENVIRONMENT = "siteparam.payment.psigate.environment";
	
	public static final String SITEPARAM_PAYMENT_AUTHROIZENET_NAME = "Authorize.Net";
	public static final String SITEPARAM_PAYMENT_AUTHORIZENET_LOGINID = "siteparam.payment.authorizenet.loginId";
	public static final String SITEPARAM_PAYMENT_AUTHORIZENET_TRANKEY = "siteparam.payment.authorizenet.trankey";
	public static final String SITEPARAM_PAYMENT_AUTHORIZENET_ENVIRONMENT = "siteparam.payment.authorizenet.environment";

	public static final String SITEPARAM_PAYMENT_PAYMENTEXPRESS_NAME = "Payment Express";
	public static final String SITEPARAM_PAYMENT_PAYMENTEXPRESS_POSTUSERNAME = "siteparam.payment.paymentexpress.postusername";
	public static final String SITEPARAM_PAYMENT_PAYMENTEXPRESS_POSTPASSWORD = "siteparam.payment.paymentexpress.postpassword";
	public static final String SITEPARAM_PAYMENT_PAYMENTEXPRESS_ENVIRONMENT = "siteparam.payment.paymentexpress.environment";

	public static final String SITEPARAM_SHIPPING_SHIPPINGTYPE_DEFAULT = "siteparam.shipping.shippingType.default";
	
	public static final String SITEPARAM_GENERAL_CURRENCY_DEFAULT = "siteparam.general.currency.default";
	public static final String SITEPARAM_GENERAL_WORKING_DIRECTORY = "siteparam.general.working.directory";
	public static final String SITEPARAM_GENERAL_TEMPLATE_FOOTER = "siteparam.general.template.footer";
	public static final String SITEPARAM_GENERAL_TEMPLATE_FOOTER_LANG = "siteparam.general.template.footer.lang";
	public static final String SITEPARAM_GENERAL_TEMPLATE_DEFAULT = "siteparam.general.template.default";
	public static final String SITEPARAM_GENERAL_LISTING_PAGESIZE = "siteparam.general.listing.pagesize";
	public static final String SITEPARAM_GENERAL_SECTION_PAGESIZE = "siteparam.general.category.pagesize";
	public static final String SITEPARAM_GENERAL_MANAGE_INVENTORY = "siteparam.general.manage.inventory";
	
	public static final String SITEPARAM_BUSINESS_CONTACTNAME = "siteparam.business.contactName";
	public static final String SITEPARAM_BUSINESS_COMPANY = "siteparam.business.company";
	public static final String SITEPARAM_BUSINESS_ADDRESS1 = "siteparam.business.address1";
	public static final String SITEPARAM_BUSINESS_ADDRESS2 = "siteparam.business.address2";
	public static final String SITEPARAM_BUSINESS_CITY = "siteparam.business.city";
	public static final String SITEPARAM_BUSINESS_STATECODE = "siteparam.business.stateCode";
	public static final String SITEPARAM_BUSINESS_POSTALCODE = "siteparam.business.postalCode";
	public static final String SITEPARAM_BUSINESS_COUNTRYCODE = "siteparam.business.countryCode";
	public static final String SITEPARAM_BUSINESS_PHONE = "siteparam.business.phone";
	public static final String SITEPARAM_BUSINESS_FAX = "siteparam.business.fax";
	public static final String SITEPARAM_BUSINESS_EMAIL = "siteparam.business.email";
	
	public static final String SITEPARAM_MAIL_SMTP_HOST = "siteparam.mail.smtp.host";
	public static final String SITEPARAM_MAIL_SMTP_PORT = "siteparam.mail.smtp.port";
	public static final String SITEPARAM_MAIL_SMTP_ACCOUNT = "siteparam.mail.smtp.account";
	public static final String SITEPARAM_MAIL_SMTP_PASSWORD = "siteparam.mail.smtp.password";
	public static final String SITEPARAM_MAIL_FROM_PWDRESET = "siteparam.mail.from.pwdreset";
	public static final String SITEPARAM_SUBJECT_PWDRESET = "siteparam.subject.pwdreset";
	public static final String SITEPARAM_SUBJECT_PWDRESET_LANG = "siteparam.subject.pwdreset.lang";
	public static final String SITEPARAM_MAIL_FROM_CUSTSALES = "siteparam.mail.from.custsales";
	public static final String SITEPARAM_SUBJECT_CUSTSALES = "siteparam.subject.custsales";
	public static final String SITEPARAM_SUBJECT_CUSTSALES_LANG = "siteparam.subject.custsales.lang";
	public static final String SITEPARAM_CHECKOUT_NOTIFICATIION_EMAIL = "siteparam.checkout.notification.email";
	public static final String SITEPARAM_MAIL_FROM_NOTIFICATION = "siteparam.mail.from.notification";
	public static final String SITEPARAM_SUBJECT_NOTIFICATION = "siteparam.subject.notification";
	public static final String SITEPARAM_SUBJECT_NOTIFICATION_LANG = "siteparam.subject.notification.lang";	

	public static final String SITEPARAM_CONNECTION_SECURE_ENABLED = "siteparam.connection.secure.enabled";

	public static final String HOME_TITLE = "home.title";

	public static final String SITEMSG_PREFIX = "sitemsg.";
	public static final String SITEMSG_INDEXING_MSG = "sitemsg.indexing.msg";

	public static final String DATAINFO_FEATUREDATA = "dataInfo.featureData";
	
	public static final String IMAGEPROVIDER_ITEM = "I";
	public static final String IMAGEPROVIDER_CONTENT = "C";
	public static final String IMAGEPROVIDER_SITE = "S";
	public static final String IMAGEPROVIDER_TEMPLATE = "T";
	public static final String IMAGEPROVIDER_URL = "U";
	
	public static final String SESSION_PARAM_PREVIEW_SITE_ID = "com.jada.preview.siteId";
	public static final String SESSION_PARAM_PREVIEW_TEMPLATE_NAME = "com.jada.preview.templateName";
	public static final String SESSION_PARAM_ORDERMAINT_LINK_FROM = "com.jada.ordermaint.link.from";
	public static final String SESSION_PARAM_ORDERMAINT_LINK_KEY = "com.jada.ordermaint.link.key";
	public static final String TEMPLATE_BASIC = "basic";
	public static final int TEMPLATE_MODULE_DISPLAY_SIZE = 3;
	
	public static final String IMAGE_MIME[] = {"bmp", "gif", "jpeg", "jpg", "jpe", 
		                                "png", "tiff", "tif", "wbmp", "ras",
		                                "pnm", "pbm", "pgm", "ppm", "rgb",
		                                "xbm", "xpm", "xwd"};
//	public static int FILESIZE_LIMIT = 2097152;
	public static final int FILESIZE_LIMIT = 100000;
	
	public static final int MYACCOUNT_LISTING_PAGE_SIZE = 20;
	public static final int MYACCOUNT_LISTING_PAGE_COUNT = 5;
	
	public static final int DEFAULT_LISTING_PAGE_SIZE = 10;
	public static final int DEFAULT_LISTING_PAGE_COUNT = 10;
	
	public static final String ORDER_TRACKING_ORDER_OPENED = "ODO";
	public static final String ORDER_TRACKING_ORDER_CLOSED = "ODC";
	public static final String ORDER_TRACKING_ORDER_VOIDED = "ODV";
	public static final String ORDER_TRACKING_CREDIT_OPENED = "CRO";
	public static final String ORDER_TRACKING_CREDIT_CLOSED = "CRC";
	public static final String ORDER_TRACKING_CREDIT_VOIDED = "CRV";
	public static final String ORDER_TRACKING_MESSAGE = "MSG";
	
	public static final int CREDITCARD_VISA = 0;
	public static final int CREDITCARD_MASTERCARD = 1;
	public static final int CREDITCARD_AMEX = 2;
	
	public static final int PAGE_NAV_COUNT = 5;
	
	public static final String SESSION_IGNORETOKEN = "com.jada.ignoreToken";
	
	public static final String DATA_TYPE_CONTENT = "C";
	public static final String DATA_TYPE_ITEM = "I";
	
	public static final char SECTION_SORT_PRICE_DSC = '1';
	public static final char SECTION_SORT_PRICE_ASC = '2';
	public static final char SECTION_SORT_DESC_DSC = '3';
	public static final char SECTION_SORT_DESC_ASC = '4';
	
	public static final char SORT_TYPE_STRING = 'S';
	public static final char SORT_TYPE_FLOAT = 'F';
	public static final char SORT_ASC = 'A';
	public static final char SORT_DSC = 'D';
	
	public static final String MENUSET_MAIN = "MAIN";
	public static final String MENUSET_SECONDARY = "SECONDARY";
	
	public static final int ADMIN_SEARCH_MAXCOUNT = 100;
	
	public static final String FRONTEND_URL_ITEMCOMPARE = "itemCompare";
	public static final String FRONTEND_URL_SECTION = "category";
	public static final String FRONTEND_URL_ITEM = "item";
	public static final String FRONTEND_URL_ITEMCOMMENT = "itemComment";
	public static final String FRONTEND_URL_ITEMCOMMENTUPDATE = "itemCommentUpdate";
	public static final String FRONTEND_URL_CONTENT = "content";
	public static final String FRONTEND_URL_CONTENTCOMMENT = "contentComment";
	public static final String FRONTEND_URL_CONTENTCOMMENTUPDATE = "contentCommentUpdate";
	public static final String FRONTEND_URL_CONTACTUS = "contactus";
	public static final String FRONTEND_URL_SEARCH = "search";
	public static final String FRONTEND_URL_HOME = "home";
	public static final String FRONTEND_URL_STATIC = "static";
	public static final String FRONTEND_OPTION_DEFAULT = "default";
	
	public static final String FRONTEND_OPTION_SITEPROFILECLASSNAME = "spcn";
	public static final String FRONTEND_OPTION_SITECURRENCYCLASSNAME = "sccn";

	public static final String PORTNUM_PUBLIC = "80";
	public static final String PORTNUM_SECURE = "443";
	
	public static final String WEBSERVICE_STATUS_SUCCESS = "success";
	public static final String WEBSERVICE_STATUS_FAILED = "failed";
	public static final String WEBSERVICE_STATUS_SIGNIN = "signin";
	
	public static final String WEBSERVICE_REASON_INUSE = "inuse";
	
	public static final String FRONTEND_URL_PREFIX = "/web/fe";
	public static final String URL_PREFIX_PROXY = "/web/proxy";
	
	public static final int DB_PAYMENT_REFERENCE_SIZE = 30;
	
	public static final String CARD_TYPE_VISA = "VISA";
	public static final String CARD_TYPE_MASTERCARD = "MASTERCARD";
	public static final String CARD_TYPE_DINERS = "DINERS";
	public static final String CARD_TYPE_DISCOVERY = "DISCOVERY";
	
	public static final String CONFIG_PROPERTIES_FILENAME = "WEB-INF/jada.properties";
	public static final String CONFIG_PROPERTY_DRIVER = "connection.driver_class";
	public static final String CONFIG_PROPERTY_URL = "connection.url";
	public static final String CONFIG_PROPERTY_USER = "connection.username";
	public static final String CONFIG_PROPERTY_PASS = "connection.password";
	public static final String CONFIG_PROPERTY_WORKINGDIRECTORY = "working.directory";
	public static final String CONFIG_PROPERTY_LOGDIRECTORY = "log4j.directory";
	public static final String CONFIG_PROPERTY_ENCRYPTIONKEY = "encryption.key";
	public static final String CONFIG_PROPERTY_LOCALTESTING = "localTesting";	
	public static final String CONFIG_PROPERTY_REQUIREINSTALL = "requireInstall";	
	public static final String CONFIG_COMPLETED_FILENAME = "jada.complete.txt";
	public static final String CONFIG_COMPASS_STORE_TYPE = "compass.storage.type";
	public static final String CONFIG_TEMPLATE_ENGINE_CLASSNAME = "template.engine.classname";
	public static final String CONFIG_DATA_API_CLASSNAME = "data.api.classname";
	public static final String CONFIG_PRODUCT_CATALOG_SOURCE = "product.catalog.source";

	public static final String CONFIG_LOG4J_PROPERTY = "log4j.appender.R.File";
	public static final String CONFIG_LOG4J_LOGFILENAME = "jada.log";
	
	public static final char CACHE_TYPE_CODE_TRANSIENT = 'T';
	public static final char CACHE_TYPE_CODE_STATIC = 'S';
	public static final String CACHE_MENU = "cache.menu";
	public static final String CACHE_LANGUAGE = "cache.language";
	public static final String CACHE_ITEM_PRICE_FILTER = "cache.item.price.filter";
	public static final String CACHE_INDEXER_INFO = "cache.indexer.info";
	public static final String CACHE_BING_TRANSLATE = "cache.bing.translate";
	
	public static final char LANGUAGETRANSLATION_SOURCE_SYSTEM = 'S';
	public static final char LANGUAGETRANSLATION_SOURCE_TEMPLATE = 'T';
	public static final char LANGUAGETRANSLATION_SOURCE_USER = 'U';
	
	public static final String COUPONTYPE_DISCOUNT_PERCENT = "1";
	public static final String COUPONTYPE_DISCOUNT_AMOUNT = "2";
	public static final String COUPONTYPE_FREESHIPING = "3";
	public static final String COUPONTYPE_DISCOUNT_OVER_AMOUNT = "4";
	
	public static final String COUPONSCOPE_ORDER = "O";
	public static final String COUPONSCOPE_ITEM = "I";
	
	public static final int COUPON_ERROR_ALREADYAPPLIED = 1;
	public static final int COUPON_ERROR_NOTAPPLICABLE = 2;
	public static final int COUPON_ERROR_NOTFOUND = 3;
	public static final int COUPON_ERROR_USEEXCEEDED = 4;
	
	public static final String SEQUENCE_ORDER = "order";
	public static final String SEQUENCE_CREDIT = "credit";
	public static final String SEQUENCE_INVOICE = "invoice";
	public static final String SEQUENCE_SHIP = "ship";
	
	public static final String PAYMENT_PROCESS_TYPE_AUTHORIZE_ONLY = "A";
	public static final String PAYMENT_PROCESS_TYPE_AUTHORIZE_AND_CAPTURE = "C";
	
	public static final String CUSTOM_ATTRIBUTE_NAME_PRICE = "Price";
	public static final char CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN = '1';
	public static final char CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN = '2';
	public static final char CUSTOM_ATTRIBUTE_TYPE_USER_INPUT = '3';
	public static final char CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP = '4';
	public static final char CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT = '5';
	
	public static final char CUSTOM_ATTRIBUTE_DATA_TYPE_STRING = '1';
	public static final char CUSTOM_ATTRIBUTE_DATA_TYPE_INTEGER = '2';
	public static final char CUSTOM_ATTRIBUTE_DATA_TYPE_DECIMAL = '3';
	public static final char CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY = '4';
	
	public static final String ITEM_TYPE_REGULAR = "01";
	public static final String ITEM_TYPE_TEMPLATE = "02";
	public static final String ITEM_TYPE_SKU = "03";
	public static final String ITEM_TYPE_STATIC_BUNDLE = "04";
	public static final String ITEM_TYPE_RECOMMAND_BUNDLE = "05";
	
	public static final int WEB_FRONTEND_POS_PREFIX = 4;
	public static final int WEB_FRONTEND_POS_SITEPROFILECLASSNAME = 5;
	public static final int WEB_FRONTEND_POS_SITECURRENCYCLASSNAME = 6;
	
	public static final char ITEM_PRICE_TYPE_CODE_REGULAR = 'R';
	public static final char ITEM_PRICE_TYPE_CODE_SPECIAL = 'S';
	
	public static final int COMMENT_MAX_VALUE = 5;
	
	public static final String INDEXER_PENDING = "Pending";
	public static final String INDEXER_PROCESSING = "Processing";
	public static final String INDEXER_FINISHED = "Finshed";
	public static final String INDEXER_ERROR = "Error";
	
	public static final String CUST_ADDRESS_USE_CUST = "C";
	public static final String CUST_ADDRESS_USE_BILL = "B";
	public static final String CUST_ADDRESS_USE_OWN = "O";
	
	public static final String CUSTOMER_CLASS_REGULAR = "Regular";
	public static final String PRODUCT_CLASS_REGULAR = "Regular";
	public static final String SHIPPING_TYPE_REGULAR = "Regular";
	
	public static final String REPORT_OUTPUT_PDF = "P";
	public static final String REPORT_OUTPUT_HTML = "H";
	public static final String REPORT_BIRT_PARAM_TYPE_DATE = "date";
	public static final String REPORT_BIRT_PARAM_TYPE_STRING = "string";
	
	public static final String IE_EXPORT_LOCATION_LOCAL = "L";
	public static final String IE_EXPORT_LOCATION_SERVER = "S";
	public static final String IE_EXPORT_TYPE_CSV = "C";
	public static final String IE_EXPORT_TYPE_XML = "X";
	public static final char IE_PROFILE_TYPE_IMPORT = 'I';
	public static final char IE_PROFILE_TYPE_EXPORT = 'E';
	
	public static final int ID_SUSPEND_TIME = 180000;
	public static final int ID_SUSPEND_COUNT = 3;
	
	public static final String SHOPPING_CART_SHIPPING_PICKUP = "PU";
	
	public static final int CHECKOUT_STEP_NONE = 1;
	public static final int CHECKOUT_STEP_MYINFORMATION = 2;
	public static final int CHECKOUT_STEP_SHIPPING = 3;
	public static final int CHECKOUT_STEP_CREDITCARD = 4;
	public static final int CHECKOUT_STEP_PAYPALACTIVATE = 5;
	public static final int CHECKOUT_STEP_DONE = 6;
	
}
