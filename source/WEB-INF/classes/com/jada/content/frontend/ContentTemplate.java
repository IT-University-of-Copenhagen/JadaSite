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

package com.jada.content.frontend;


public class ContentTemplate {
	/*
	String templateName = "";
	String templateLocation = "c:/work/ajk";
	String siteId;
	String emptyPlaceHolder = "-!-!-!EMPTYPLACEHOLDER!-!-!-";
	
	boolean empty = false;
	
	String horizontalMenuCode;
	String verticalMenuCode;
	String menuDivId;
	
	Long catId = null;
	Long itemId = null;
	Long contentId = null;
	int pageNum;
	// TODO pageSize should comes from config.
	int pageSize = 10;
	
	ShoppingCartSummaryInfo shoppingCartSummaryInfo = null;
	
	SiteInfo siteInfo = null;
	PageInfo pageInfo = null;
	
	ContentInfo contentInfo = null;
	ItemInfo itemInfo = null;
	CategoryInfo categoryInfo = null;
	
	ContentInfo categoryContentInfo = null;
	ItemInfo categoryItemInfo = null;
	
	ContentInfo homeFeatureContentInfo = null;
	ItemInfo homeFeatureItemInfo = null;
	Object homeDatas[];
	
	HttpServletRequest request = null;
	
	
	public ContentTemplate(HttpServletRequest request, String siteId, String templateName) throws Exception {
		this.request = request;
		this.siteId = siteId;
		this.templateName = templateName;
		
		String n = templateLocation;
		if (!templateLocation.endsWith("/") && !templateLocation.endsWith("\\")) {
			n += "/";
		}
		n += templateName + "/";
		Velocity.setProperty("file.resource.loader.path", n);
		Velocity.init();
		
		ContentApi api = new ContentApi(request);
		siteInfo = api.getSite();
		
		// TODO to find a better way to deal with title
		pageInfo = new PageInfo();
		pageInfo.setPageTitle("");
		
    	String id = null;
    	id = (String) request.getParameter("catId");
    	if (id != null) {
    		catId = Format.getLong(id);
    		String value = (String) request.getParameter("pageNum");
    		if (value == null) {
    			value = "1";
    		}
    		pageNum = Format.getInt(value);
    	}
    	id = (String) request.getParameter("itemId");
    	if (id != null) {
    		itemId = Format.getLong(id);
    	}
    	id = (String) request.getParameter("contentId");
    	if (id != null) {
    		contentId = Format.getLong(id);
    	}
	}
	
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public boolean isHome() {
		if (catId == null && itemId == null && contentId == null) {
			return true;
		}
		return false;
	}
	
	
	public boolean isCategory() {
		if (catId != null) {
			return true;
		}
		return false;
	}
	
	public boolean isItem() {
		if (itemId != null) {
			return true;
		}
		return false;
	}
	
	public boolean isContent() {
		if (contentId != null) {
			return true;
		}
		return false;
	}
	
	public Template getTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
		return Velocity.getTemplate(name);
	}

	public EmptyTemplateInfo getEmptyTemplateInfo() throws ResourceNotFoundException, ParseErrorException, Exception {
		Template template = getTemplate("template.lt");
		
		setEmpty(true);
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		String data = writer.toString();
		EmptyTemplateInfo emptyTemplateInfo = new EmptyTemplateInfo();
		emptyTemplateInfo.setTemplatePrefix("");
		emptyTemplateInfo.setTemplateSuffix("");
		int pos = data.indexOf(emptyPlaceHolder);
		if (pos > 0) {
			emptyTemplateInfo.setTemplatePrefix(data.substring(0, pos));
		}
		if (data.length() > pos + emptyPlaceHolder.length()) {
			emptyTemplateInfo.setTemplateSuffix(data.substring(pos + emptyPlaceHolder.length()));
		}
		return emptyTemplateInfo;
	}
	
	public String getContentData() throws ResourceNotFoundException, ParseErrorException, Exception {
		Template template = getTemplate("template.lt");
		
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		return writer.toString();
	}
	
	public String getHorizontalMenu(String menuSetName) throws ResourceNotFoundException, ParseErrorException, Exception {
		Template template = getTemplate("horizontalMenu.vm");
		
		VelocityContext context = new VelocityContext();
		String menuDivId = ContentUtility.getNextMenuDivId(menuSetName);
		boolean vertical = false;
		setHorizontalMenuCode(ContentUtility.generateMenu(siteId, menuSetName, menuDivId, vertical));
		setMenuDivId(menuDivId);
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getVerticalMenu(String menuSetName) throws ResourceNotFoundException, ParseErrorException, Exception  {
		Template template = getTemplate("verticalMenu.vm");
		
		VelocityContext context = new VelocityContext();
		String menuDivId = ContentUtility.getNextMenuDivId(menuSetName);
		boolean vertical = true;
		setVerticalMenuCode(ContentUtility.generateMenu(siteId, menuSetName, menuDivId, vertical));
		setMenuDivId(menuDivId);
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getShoppingCartSummary() throws ResourceNotFoundException, ParseErrorException, Exception  {
		ContentApi api = new ContentApi(request);
		shoppingCartSummaryInfo = api.getShoppingCartSummary(request);
		Template template = getTemplate("shoppingCartSummary.vm");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		return writer.toString();
	}
	
	public String getBody() throws Exception {
		if (isEmpty()) {
			return emptyPlaceHolder;
		}
		if (isItem()) {
			return getItem();
		}
		if (isContent()) {
			return getContent();
		}
		if (isCategory()) {
			return getCategory();
		}
		return getHome();
	}
	
	public String getCategory() throws Exception {
//		int start = pageSize * (pageNum - 1) + 1;
//		int end = start + pageSize - 1;
//		
		ContentApi api = new ContentApi(request);
//		categoryInfo = api.getCategory(catId, start, end);
		categoryInfo = api.getCategory(catId, pageSize, 5, pageNum);
		
		Template template = getTemplate("category.lt");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getCategoryHeader() throws Exception {
		Template template = getTemplate("categoryHeader.lt");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		return writer.toString();
	}
	
	public String getCategoryHeaderCategoryInfo() throws Exception {
		Template template = getTemplate("categoryHeaderCategoryInfo.vm");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		return writer.toString();
	}
	
	public String getItem() throws Exception {
		ContentApi api = new ContentApi(request);
		boolean updateStatistics = true;
		itemInfo = api.getItem(itemId, updateStatistics);
		
		Template template = getTemplate("item.vm");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getContent() throws Exception {
		ContentApi api = new ContentApi(request);
		boolean updateStatistics = true;
		contentInfo = api.getContent(contentId, updateStatistics);
		
		Template template = getTemplate("content.vm");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getHome() throws Exception {
		Template template = getTemplate("home.lt");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}
	
	public String getHomeBody() throws Exception {
		ContentApi api = new ContentApi(request);
		homeDatas = api.getHomePageData();
			
		Template template = getTemplate("homeBody.lt");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getHomeFeatureData() throws Exception {
		ContentApi api = new ContentApi(request);
		DataInfo dataInfo = api.getHomePageFeatureData();
		String templateName = "";
		VelocityContext context = new VelocityContext();
		if (dataInfo instanceof ContentInfo) {
			homeFeatureContentInfo = (ContentInfo) dataInfo;
			templateName = "homeFeatureContent.vm";
			context.put("contentInfo", homeFeatureContentInfo);
		}
		else {
			homeFeatureItemInfo = (ItemInfo) dataInfo;
			templateName = "homeFeatureItem.vm";
			context.put("itemInfo", homeFeatureItemInfo);
		}
		Template template = getTemplate(templateName);
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getHomeData(int index) throws Exception {
		Object object = homeDatas[index - 1];
		String templateName = "";
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		if (object instanceof ContentInfo) {
			templateName = "homeContent.vm";
			context.put("contentInfo", object);
		}
		else {
			templateName = "homeItem.vm";
			context.put("itemInfo", object);
		}
		Template template = getTemplate(templateName);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getCategoryFeatureData(int index) throws Exception {
		Object object = categoryInfo.getCategoryDatas()[index - 1];
		if (object instanceof ContentInfo) {
			return getCategoryContent(index, "categoryFeatureContent.vm");
		}
		else {
			return getCategoryItem(index, "categoryFeatureItem.vm");
		}
	}
	
	public String getCategoryHeaderCategoryList() throws Exception {
		Template template = getTemplate("categoryList.vm");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getCategoryHeaderFeatureList() throws Exception {
		Template template = getTemplate("categoryHeaderFeatureList.lt");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getCategoryBody() throws Exception {
		Template template = getTemplate("categoryBody.lt");
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getCategoryData(int index) throws Exception {
		Object object = categoryInfo.getCategoryDatas()[index - 1];
		if (object instanceof ContentInfo) {
			return getCategoryContent(index, "categoryContent.vm");
		}
		else {
			return getCategoryItem(index, "categoryItem.vm");
		}
	}
	
	private String getCategoryContent(int index, String templateName) throws Exception {
		this.categoryContentInfo = (ContentInfo) categoryInfo.getCategoryDatas()[index - 1];
		
		Template template = getTemplate(templateName);
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	private String getCategoryItem(int index, String templateName) throws Exception {
		this.categoryItemInfo = (ItemInfo) categoryInfo.getCategoryDatas()[index - 1];
		
		Template template = getTemplate(templateName);
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String getHorizontalMenuCode() {
		return horizontalMenuCode;
	}

	public void setHorizontalMenuCode(String horizontalMenuCode) {
		this.horizontalMenuCode = horizontalMenuCode;
	}

	public String getMenuDivId() {
		return menuDivId;
	}

	public void setMenuDivId(String menuDivId) {
		this.menuDivId = menuDivId;
	}

	public String getVerticalMenuCode() {
		return verticalMenuCode;
	}

	public void setVerticalMenuCode(String verticalMenuCode) {
		this.verticalMenuCode = verticalMenuCode;
	}

	public String getSiteId() {
		return siteId;
	}

	public SiteInfo getSiteInfo() {
		return siteInfo;
	}

	public void setSiteInfo(SiteInfo siteInfo) {
		this.siteInfo = siteInfo;
	}

	public ContentInfo getContentInfo() {
		return contentInfo;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	public CategoryInfo getCategoryInfo() {
		return categoryInfo;
	}

	public ContentInfo getCategoryContentInfo() {
		return categoryContentInfo;
	}

	public void setCategoryContentInfo(ContentInfo categoryContentInfo) {
		this.categoryContentInfo = categoryContentInfo;
	}

	public ItemInfo getCategoryItemInfo() {
		return categoryItemInfo;
	}

	public void setCategoryItemInfo(ItemInfo categoryItemInfo) {
		this.categoryItemInfo = categoryItemInfo;
	}

	public Object[] getHomeDatas() {
		return homeDatas;
	}

	public void setHomeDatas(Object[] homeDatas) {
		this.homeDatas = homeDatas;
	}
	
	public ShoppingCartSummaryInfo getShoppingCartSummaryInfo() {
		return shoppingCartSummaryInfo;
	}

	public void setShoppingCartSummaryInfo(
			ShoppingCartSummaryInfo shoppingCartSummaryInfo) {
		this.shoppingCartSummaryInfo = shoppingCartSummaryInfo;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	*/
}
