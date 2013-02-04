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

public class DemoLoader {
/*
    Color[] colors = {
//          Color.black,
          Color.blue,
          Color.cyan,
//          Color.darkGray,
          Color.gray,
          Color.lightGray,
          Color.magenta,
          Color.orange,
          Color.pink,
          Color.red,
          Color.white,
          Color.yellow
    };

	public void start(String siteId) {
		EntityManager em = null;
		try {
			em = JpaConnection.getInstance().getCurrentEntityManager();
			em.getTransaction().begin();
			load(siteId, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			em.getTransaction().commit();
		}
	}
	
	public void load(String siteId, Long categoryParentId) {
    	EntityManager em = null;
		try {
			em = JpaConnection.getInstance().getCurrentEntityManager();
			String sql = "from category in class Category where siteId = :siteId ";
			if (categoryParentId != null) {
				sql += "and categoryParentId = :categoryParentId ";
			}
			else {
				sql += "and categoryParentId is null ";
			}
			Query query = em.createQuery(sql);
	    	query.setParameter("siteId", siteId);
	    	if (categoryParentId != null) {
	    		query.setParameter("categoryParentId", categoryParentId);
	    	}
	    	Iterator<?> iterator = query.getResultList().iterator();
	    	Category category = null;
	    	while (iterator.hasNext()) {
	    		category = (Category) iterator.next();
	    		System.out.println(category.getCatTitle());
	    		if (category.getCategoryParentId() != null) {
	    			if (isNews(siteId, category.getCatId())) {
	    				createContents(siteId, category);
	    			}
	    			else {
	    				createItems(siteId, category);
	    			}
	    		}
	    		load(siteId, category.getCatId());
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isNews(String siteId, Long catId) throws Exception {
		if (catId == null) {
			return false;
		}
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Category category = (Category) em.find(Category.class, catId);
		if (category.getCatTitle().equals("News")) {
			return true;
		}
		
		return isNews(siteId, category.getCategoryParentId());
	}
	
	public void createContents(String siteId, Category category) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2099, 11, 31);
		Date endDate = calendar.getTime();
		calendar.set(2007, 0, 1);
		Date startDate = calendar.getTime();
		for (int i = 0; i < 10; i++) {
			Content content = new Content();
			content.setSiteId(siteId);
			content.setContentTitle(makeLoremLine(10));
			content.setContentShortDesc(makeLoremLine(20));
			content.setContentDesc(makeLoremParagraph(2));
			content.setPageTitle("jada, news, world, local");
			content.setContentPublishOn(startDate);
			content.setContentExpireOn(endDate);
			content.setRecUpdateBy("demo");
			content.setRecUpdateDatetime(startDate);
			content.setPublished('Y');
			content.setContentHitCounter(new Integer(0));
			content.setRecCreateBy("demo");
			content.setRecCreateDatetime(startDate);
			content.setCategory(category);
			em.persist(content);
			
			for (int j = 0; j < 5; j++) {
				ContentImage contentImage = new ContentImage();
				String imageName = "demo";
				if (j > 0) {
					imageName += j;
				}
				contentImage.setImageName(imageName);
				contentImage.setContentType("image/jpeg");
				contentImage.setImageValue(makeRandomImage());
				contentImage.setImageHeight(480);
				contentImage.setImageWidth(640);
				contentImage.setRecUpdateBy("demo");
				contentImage.setRecUpdateDatetime(startDate);
				contentImage.setRecCreateBy("demo");
				contentImage.setRecCreateDatetime(startDate);
				em.persist(contentImage);
				if (j == 0) {
					content.setImage(contentImage);
				}
				else {
					content.getImages().add(contentImage);
				}
			}
		}
	}
	
	public void createItems(String siteId, Category category) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2099, 11, 31);
		Date endDate = calendar.getTime();
		calendar.set(2007, 0, 1);
		Date startDate = calendar.getTime();
        Random random = new Random(System.currentTimeMillis());
        
        Query query = em.createQuery("from ShippingType where siteId = :siteId and shippingTypeName = :shippingTypeName");
        query.setParameter("siteId", siteId);
        query.setParameter("shippingTypeName", "Small Packages");
        Iterator<?> iterator = query.getResultList().iterator();
        ShippingType shippingType = null;
        if (iterator.hasNext()) {
        	shippingType = (ShippingType) iterator.next();
        }
        
		for (int i = 0; i < 10; i++) {
			Item item = new Item();
			long itemNum = random.nextInt(99999);
			if (itemNum < 10000) {
				itemNum += 10000;
			}
			item.setSiteId(siteId);
			item.setItemNum(String.valueOf(itemNum));
			item.setItemUpcCd("");
			item.setSeqNum(new Integer(0));
			item.setItemShortDesc(makeLoremLine(8));
			item.setItemShortDesc1(makeLoremLine(4));
			item.setItemDesc(makeLoremParagraph(2));
			item.setPageTitle("jada, golf, sale, drivers, irons, putters");
			float singlePrice = (float) random.nextInt(20) + (float) 0.99;
			float multPrice = 0;
			item.setItemPrice(new Float(singlePrice));
			int chance = random.nextInt(10);

			item.setItemPublishOn(startDate);
			item.setItemExpireOn(endDate);
			item.setPublished('Y');
			item.setRecUpdateBy("demo");
			item.setRecUpdateDatetime(startDate);
			item.setItemHitCounter(new Integer(0));
			item.setItemRating(new Float(0));
			item.setItemRatingCount(new Integer(0));
			item.setItemQty(new Integer(1000));
			item.setItemBookedQty(new Integer(0));
			item.setRecCreateBy("demo");
			item.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			if (shippingType != null) {
				item.setShippingType(shippingType);
			}
			item.setCategory(category);
			em.persist(item);
			
			for (int j = 0; j < 5; j++) {
				ItemImage itemImage = new ItemImage();
				String imageName = "demo";
				if (j > 0) {
					imageName += j;
				}
				itemImage.setImageName(imageName);
				itemImage.setContentType("image/jpeg");
				itemImage.setImageValue(makeRandomImage());
				itemImage.setImageHeight(480);
				itemImage.setImageWidth(640);
				itemImage.setRecUpdateBy("demo");
				itemImage.setRecUpdateDatetime(startDate);
				itemImage.setRecCreateBy("demo");
				itemImage.setRecCreateDatetime(startDate);
				em.persist(itemImage);
				if (j == 0) {
					item.setImage(itemImage);
				}
				else {
					item.getImages().add(itemImage);
				}
			}
		}
	}
	
	public String makeLoremLine(int noWords) {
		LoremIpsum4J lorem = new LoremIpsum4J();
		String words[] = lorem.getWords(noWords);
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < words.length; i++) {
			buffer.append(words[i] + " ");
		}
		return buffer.toString();
	}
	
	public String makeLoremParagraph(int noParagraphs) {
		LoremIpsum4J lorem = new LoremIpsum4J();
		String paragraphs[] = lorem.getParagraphs(noParagraphs);
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < paragraphs.length; i++) {
			buffer.append("<p>" + paragraphs[i] + "</p>");
		}
		return buffer.toString();
	}
	
	public byte[] makeRandomImage() throws Exception {
		int height = 640;
		int width = 480;
		
        Random random = new Random(System.currentTimeMillis());
        BufferedImage image = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(colors[random.nextInt(colors.length)]);
        graphics.fillRect(0, 0, height, width);
        for (int i = 0; i < 750; i++) {
            graphics.setColor(colors[random.nextInt(colors.length)]);
            graphics.fillRect(random.nextInt(height), random.nextInt(width), random.nextInt(20), random.nextInt(20));
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JPEGImageEncoderImpl j = new JPEGImageEncoderImpl(stream);
        j.encode(image);
        stream.close();
        return stream.toByteArray();
	}

	public static void main(String[] args) {
		DemoLoader loader = new DemoLoader();
		loader.start("jada");
	}
*/
}
