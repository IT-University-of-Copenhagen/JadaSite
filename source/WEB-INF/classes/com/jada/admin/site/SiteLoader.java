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

package com.jada.admin.site;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.dao.CountryDAO;
import com.jada.dao.StateDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.CategoryLanguage;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.CreditCard;
import com.jada.jpa.entity.Currency;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeLanguage;
import com.jada.jpa.entity.HomePage;
import com.jada.jpa.entity.HomePageDetail;
import com.jada.jpa.entity.HomePageLanguage;
import com.jada.jpa.entity.IeProfileDetail;
import com.jada.jpa.entity.IeProfileHeader;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.MenuLanguage;
import com.jada.jpa.entity.Report;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.ShippingMethodLanguage;
import com.jada.jpa.entity.ShippingMethodRegion;
import com.jada.jpa.entity.ShippingMethodRegionType;
import com.jada.jpa.entity.ShippingRate;
import com.jada.jpa.entity.ShippingRegion;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.SiteProfile;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.Tax;
import com.jada.jpa.entity.Template;
import com.jada.util.Constants;

public class SiteLoader {
	/*
	 * Used to load up tables when a new site is initially created.
	 * Tables to be loaded.
	 * Country
	 * State
	 * Currency
	 * Menu
	 * Category
	 * Tax
	 * Template
	 * ShippingRegion
	 * CreditCard
	 */
	Site site = null;
	String userId = null;
	public SiteLoader(Site site, String userId) {
		this.site = site;
		this.userId = userId;
	}
	
	public void load() throws Exception {
		loadCountry();
		loadState();
		loadCurrency();
		loadTax();
		loadTemplate();
		loadShippingType();
		loadShippingRegion();
		loadShippingMethod();
		loadCreditCard();
	}
	
	public void remove() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = null;
    	Query query = null;
    	Iterator<?> iterator = null;

        sql = "delete from CreditCard where siteId = :siteId";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        query.executeUpdate();
        
		
		sql = "from Report report where report.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Report report = (Report) iterator.next();
		  	em.remove(report);
		}
		
		sql = "from IeProfileHeader ieProfileHeader where ieProfileHeader.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			IeProfileHeader ieProfileHeader = (IeProfileHeader) iterator.next();
			for (IeProfileDetail ieProfileDetail : ieProfileHeader.getIeProfileDetails()) {
				em.remove(ieProfileDetail);
			}
		  	em.remove(ieProfileHeader);
		}
        
        sql = "from Menu menu where menu.siteDomain in (select siteDomain from SiteDomain siteDomain where siteDomain.site.siteId = :siteId)";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Menu menu = (Menu) iterator.next();
        	menu.setMenuLanguage(null);
        	for (MenuLanguage menuLanguage : menu.getMenuLanguages()) {
        		em.remove(menuLanguage);
        	}
        	em.remove(menu);
        }
       
        sql = "from Category category where category.site.siteId = :siteId)";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Category category = (Category) iterator.next();
        	category.setCategoryLanguage(null);
        	for (CategoryLanguage categoryLanguage : category.getCategoryLanguages()) {
        		em.remove(categoryLanguage);
        	}
        	em.remove(category);
        }

        sql = "delete from ProductClass productClass where productClass.site.siteId = :siteId)";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        query.executeUpdate();

        sql = "delete from CustomerClass customerClass where customerClass.site.siteId = :siteId)";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        query.executeUpdate();

        sql = "from ShippingRate shippingRate " +
		  	  "where	shippingRate in ( " +
		  	  "select	shippingRate " +
		  	  "from		ShippingMethodRegionType shippingMethodRegionType " +
		  	  "where	shippingMethodRegionType.shippingMethod.site.siteId = :siteId " +
		  	  ")";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
		  	ShippingRate shippingRate = (ShippingRate) iterator.next();
		  	em.remove(shippingRate);
		}

		sql = "from ShippingMethod where siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
		 	ShippingMethod shippingMethod = (ShippingMethod) iterator.next();
		 	em.remove(shippingMethod.getShippingMethodLanguage());
		  	em.remove(shippingMethod);
		}

		sql = "from ShippingRegion where siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
		  	ShippingRegion shippingRegion = (ShippingRegion) iterator.next();
		  	em.remove(shippingRegion);
		}
  
		sql = "from ShippingType where siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
		  	ShippingType shippingType = (ShippingType) iterator.next();
		  	em.remove(shippingType);
		}
  
        sql = "from State where stateId in (select stateId from State state where state.country.site.siteId = :siteId)";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	State state = (State) iterator.next();
        	state.setCountry(null);
        	em.remove(state);
        }
        
        sql = "from Country where siteId = :siteId";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Country country = (Country) iterator.next();
        	em.remove(country);
        }
        site.setSiteCurrencyClassDefault(null);
        site.setSiteProfileClassDefault(null);

        for (SiteDomain siteDomain : site.getSiteDomains()) {
        	siteDomain.setSiteProfileDefault(null);
        	siteDomain.setSiteCurrencyDefault(null);
         	siteDomain.setBaseCurrency(null);
        	siteDomain.setSiteDomainLanguage(null);

        	HomePage homePage = siteDomain.getHomePage();
        	for (HomePageDetail homePageDetail : homePage.getHomePageDetails()) {
        		em.remove(homePageDetail);
        	}
        	for (HomePageLanguage homePageLanguage : homePage.getHomePageLanguages()) {
        		em.remove(homePageLanguage);
        	}
        	em.remove(homePage);
        	siteDomain.setHomePage(null);
        	
        	Template template = siteDomain.getTemplate();
        	em.remove(template);
        	siteDomain.setTemplate(null);
        }

		sql = "from SiteProfile siteProfile where siteProfile.siteDomain.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
		  	SiteProfile siteProfile = (SiteProfile) iterator.next();
		  	em.remove(siteProfile);
		}
		
		sql = "from SiteCurrency siteCurrency where siteCurrency.siteDomain.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteCurrency siteCurrency = (SiteCurrency) iterator.next();
		  	em.remove(siteCurrency);
		}
		
		sql = "from SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next();
		  	em.remove(siteProfileClass);
		}

		sql = "from SiteCurrencyClass siteCurrencyClass where siteCurrencyClass.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) iterator.next();
			siteCurrencyClass.setCurrency(null);
		  	em.remove(siteCurrencyClass);
		}
    
		sql = "from Currency currency where currency.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Currency currency = (Currency) iterator.next();
		  	em.remove(currency);
		}

		sql = "from CustomAttribute customAttribute where customAttribute.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			CustomAttribute customAttribute = (CustomAttribute) iterator.next();
		  	em.remove(customAttribute);
			CustomAttributeLanguage customAttributeLanguage = customAttribute.getCustomAttributeLanguage();
			customAttributeLanguage.setCustomAttribute(null);
		 	em.remove(customAttributeLanguage);
		}

		sql = "from SiteDomain siteDomain where siteDomain.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteDomain siteDomain = (SiteDomain) iterator.next();
			for (SiteDomainLanguage siteDomainLanguage : siteDomain.getSiteDomainLanguages()) {
				em.remove(siteDomainLanguage);
			}
		  	em.remove(siteDomain);
		}
	}
	
	public void loadCountry() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from Country country where siteId = :siteId order by countryCode";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Country master = (Country) iterator.next();
        	Country country = new Country();
        	PropertyUtils.copyProperties(country, master);
        	country.setSite(site);
        	country.setCountryId(null);
        	country.setRecUpdateBy(userId);
        	country.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	country.setRecCreateBy(userId);
        	country.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	country.setShippingRegion(null);
        	country.setStates(null);
        	country.setTaxes(null);
        	em.persist(country);
        }
	}
	
	public void loadState() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from State state where state.country.site.siteId = :siteId order by stateCode";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	State master = (State) iterator.next();
        	State state = new State();
        	PropertyUtils.copyProperties(state, master);
        	state.setStateId(null);
        	state.setRecUpdateBy(userId);
        	state.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	state.setRecCreateBy(userId);
        	state.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	state.setShippingRegion(null);
        	Country mc = master.getCountry();
        	Country country = null;
        	if (mc != null) {
        		country = CountryDAO.loadByCountryName(site.getSiteId(), mc.getCountryName());
        	}
        	state.setCountry(country);
        	state.setTaxes(null);
        	em.persist(state);
        }
	}
	
	public void loadCurrency() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from Currency where siteId = :siteId order by currencyCode";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Currency master = (Currency) iterator.next();
        	Currency currency = new Currency();
        	PropertyUtils.copyProperties(currency, master);
        	currency.setSite(site);
        	currency.setCurrencyId(null);
        	currency.setRecUpdateBy(userId);
        	currency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	currency.setRecCreateBy(userId);
        	currency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	em.persist(currency);
        }
	}

	public void loadMenu() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from Menu where siteId = :siteId order by menuSetName";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Menu master = (Menu) iterator.next();
        	Menu menu = new Menu();
        	PropertyUtils.copyProperties(menu, master);
//        	menu.setSite(site);
        	menu.setMenuId(null);
        	menu.setRecUpdateBy(userId);
        	menu.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	menu.setRecCreateBy(userId);
        	menu.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	menu.setCategory(null);
        	menu.setContent(null);
        	menu.setItem(null);
        	menu.setMenuLanguages(null);
        	em.persist(menu);
        }
	}
	
	public void loadCategory() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from Category where siteId = :siteId";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Category master = (Category) iterator.next();
        	Category category = new Category();
        	category.setCatNaturalKey(master.getCatNaturalKey());
        	category.setSeqNum(master.getSeqNum());
        	category.setPublished(master.getPublished());
        	category.setSite(site);
        	category.setCatId(null);
        	category.setRecUpdateBy(userId);
        	category.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	category.setRecCreateBy(userId);
        	category.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	category.setMenus(null);
        	category.setCategoryLanguages(null);
        	em.persist(category);
        }
	}
	
	public void loadTax() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from Tax where siteId = :siteId";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Tax master = (Tax) iterator.next();
        	Tax tax = new Tax();
        	Set<Country> countries = tax.getCountries();
        	Set<State> states = tax.getStates();
        	PropertyUtils.copyProperties(tax, master);
        	tax.setSite(site);
        	tax.setTaxId(null);
        	tax.setRecUpdateBy(userId);
        	tax.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	tax.setRecCreateBy(userId);
        	tax.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	tax.setTaxLanguages(null);
			tax.setCountries(countries);
			tax.setStates(states);
        	Iterator<?> it = null;
        	if (master.getCountries() != null) {
        		it = master.getCountries().iterator();
        		while (it.hasNext()) {
        			Country mc = (Country) it.next();
        			Country country = CountryDAO.loadByCountryName(site.getSiteId(), mc.getCountryName());
        			tax.getCountries().add(country);
        		}
        	}
        	if (master.getStates() != null) {
        		it = master.getStates().iterator();
        		while (it.hasNext()) {
        			State mc = (State) it.next();
        			State state = StateDAO.loadByStateName(site.getSiteId(), mc.getStateName());
        			tax.getStates().add(state);
        		}
        	}
        	em.persist(tax);
        }
	}
	
	public void loadTemplate() throws Exception {
/*
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from Template where siteId = :siteId order by templateName";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Template master = (Template) iterator.next();
        	Template template = new Template();
        	PropertyUtils.copyProperties(template, master);
        	template.setSite(site);
        	template.setTemplateId(null);
        	template.setRecUpdateBy(userId);
        	template.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	template.setRecCreateBy(userId);
        	template.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	em.persist(template);
        }
*/
	}
	
	Vector<ShippingType> shippingTypes = new Vector<ShippingType>();
	public void loadShippingType() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from ShippingType where siteId = :siteId order by shippingTypeId";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	ShippingType master = (ShippingType) iterator.next();
        	ShippingType shippingType = new ShippingType();
        	PropertyUtils.copyProperties(shippingType, master);
        	shippingType.setSite(site);
        	shippingType.setRecUpdateBy(userId);
        	shippingType.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	shippingType.setRecCreateBy(userId);
        	shippingType.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	shippingType.setShippingMethodRegionTypes(null);
            shippingTypes.add(shippingType);
        	em.persist(shippingType);
        }
	}

	Vector<ShippingRegion> shippingRegions = new Vector<ShippingRegion>();
	public void loadShippingRegion() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from ShippingRegion where siteId = :siteId order by shippingRegionId";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	ShippingRegion master = (ShippingRegion) iterator.next();
        	ShippingRegion shippingRegion = new ShippingRegion();
        	Set<Country> countries = shippingRegion.getCountries();
        	Set<State> states = shippingRegion.getStates();
        	PropertyUtils.copyProperties(shippingRegion, master);
        	shippingRegion.setSite(site);
        	shippingRegion.setShippingRegionId(null);
        	shippingRegion.setRecUpdateBy(userId);
        	shippingRegion.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	shippingRegion.setRecCreateBy(userId);
        	shippingRegion.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	shippingRegion.setCountries(countries);
			shippingRegion.setStates(states);
			shippingRegion.setZipCodes(null);
			shippingRegion.setShippingMethodRegions(null);
			shippingRegion.setShippingMethodRegionTypes(null);
        	Iterator<?> it = null;
        	if (master.getCountries() != null) {
        		it = master.getCountries().iterator();
        		while (it.hasNext()) {
        			Country mc = (Country) it.next();
        			Country country = CountryDAO.loadByCountryName(site.getSiteId(), mc.getCountryName());
        			shippingRegion.getCountries().add(country);
        		}
        	}
        	if (master.getStates() != null) {
        		it = master.getStates().iterator();
        		while (it.hasNext()) {
        			State mc = (State) it.next();
        			State state = StateDAO.loadByStateName(site.getSiteId(), mc.getStateName());
        			shippingRegion.getStates().add(state);
        		}
        	}
        	shippingRegions.add(shippingRegion);
        	em.persist(shippingRegion);
        }
	}

	public void loadShippingMethod() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from ShippingMethod where siteId = :siteId order by shippingMethodId";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	ShippingMethod master = (ShippingMethod) iterator.next();
        	ShippingMethod shippingMethod = new ShippingMethod();
        	shippingMethod.setSite(site);
        	shippingMethod.setSeqNum(master.getSeqNum());
        	shippingMethod.setPublished(master.getPublished());
        	shippingMethod.setRecUpdateBy(userId);
        	shippingMethod.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	shippingMethod.setRecCreateBy(userId);
        	shippingMethod.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	
        	for (ShippingMethodLanguage language : master.getShippingMethodLanguages()) {
        		ShippingMethodLanguage shippingMethodLanguage = new ShippingMethodLanguage();
        		shippingMethodLanguage.setShippingMethodName(language.getShippingMethodName());
        		shippingMethodLanguage.setRecUpdateBy(userId);
        		shippingMethodLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        		shippingMethodLanguage.setRecCreateBy(userId);
        		shippingMethodLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        		em.persist(shippingMethodLanguage);
        		if (language.getShippingMethodLangId().equals(master.getShippingMethodLanguage().getShippingMethodLangId())) {
        			shippingMethod.setShippingMethodLanguage(shippingMethodLanguage);
        		}
        		shippingMethod.getShippingMethodLanguages().add(shippingMethodLanguage);
        	}
        	
        	if (master.getShippingMethodRegions() != null) {
        		Iterator<?> it = master.getShippingMethodRegions().iterator();
        		while (it.hasNext()) {
        			ShippingMethodRegion m_shippingMethodRegion = (ShippingMethodRegion) it.next();
        			ShippingMethodRegion shippingMethodRegion = new ShippingMethodRegion();
        			shippingMethodRegion.setPublished(m_shippingMethodRegion.getPublished());
        			shippingMethodRegion.setRecUpdateBy(userId);
        			shippingMethodRegion.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        			shippingMethodRegion.setRecCreateBy(userId);
        			shippingMethodRegion.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        			shippingMethodRegion.setShippingMethod(shippingMethod);
        			ShippingRegion shippingRegion = getShippingRegion(m_shippingMethodRegion.getShippingRegion().getShippingRegionName());
        			shippingMethodRegion.setShippingRegion(shippingRegion);
        			
        			if (m_shippingMethodRegion.getShippingMethodRegionTypes() != null) {
        				Iterator<?> it1 = m_shippingMethodRegion.getShippingMethodRegionTypes().iterator();
        				while (it1.hasNext()) {
        					ShippingMethodRegionType m_shippingMethodRegionType = (ShippingMethodRegionType) it1.next();
        					ShippingMethodRegionType shippingMethodRegionType = new ShippingMethodRegionType();
                			shippingMethodRegionType.setPublished(m_shippingMethodRegionType.getPublished());
                			shippingMethodRegionType.setRecUpdateBy(userId);
                			shippingMethodRegionType.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
                			shippingMethodRegionType.setRecCreateBy(userId);
                			shippingMethodRegionType.setRecCreateDatetime(new Date(System.currentTimeMillis()));
                			shippingMethodRegionType.setShippingRegion(shippingRegion);
                			shippingMethodRegionType.setShippingMethod(shippingMethod);
                			shippingMethodRegionType.setShippingType(getShippingType(m_shippingMethodRegionType.getShippingType().getShippingTypeName()));
                			
                			ShippingRate m_shippingRate = m_shippingMethodRegionType.getShippingRate();
                			ShippingRate shippingRate = new ShippingRate();
                        	PropertyUtils.copyProperties(shippingRate, m_shippingRate);
                			shippingRate.setPublished(m_shippingRate.getPublished());
                			shippingRate.setRecUpdateBy(userId);
                			shippingRate.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
                			shippingRate.setRecCreateBy(userId);
                			shippingRate.setRecCreateDatetime(new Date(System.currentTimeMillis()));
                			shippingMethodRegionType.setShippingRate(shippingRate);
                			
                			em.persist(shippingRate);
                			em.persist(shippingMethodRegionType);
                			shippingMethodRegion.getShippingMethodRegionTypes().add(shippingMethodRegionType);
        				}
        			}
        			
        			shippingMethod.getShippingMethodRegions().add(shippingMethodRegion);
        		}
        	}
        	em.persist(shippingMethod);
        }
	}
	
	public ShippingRegion getShippingRegion(String shippingRegionName) {
		Iterator<?> iterator = shippingRegions.iterator();
		while (iterator.hasNext()) {
			ShippingRegion shippingRegion = (ShippingRegion) iterator.next();
			if (shippingRegion.getShippingRegionName().equals(shippingRegionName)) {
				return shippingRegion;
			}
		}
		return null;
	}
	
	public ShippingType getShippingType(String shippingTypeName) {
		Iterator<?> iterator = shippingTypes.iterator();
		while (iterator.hasNext()) {
			ShippingType shippingType = (ShippingType) iterator.next();
			if (shippingType.getShippingTypeName().equals(shippingTypeName)) {
				return shippingType;
			}
		}
		return null;
	}

	public void loadCreditCard() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from CreditCard where siteId = :siteId order by creditCardId";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", Constants.SITE_SYSTEM);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	CreditCard master = (CreditCard) iterator.next();
        	CreditCard creditCard = new CreditCard();
        	PropertyUtils.copyProperties(creditCard, master);
        	creditCard.setSite(site);
        	creditCard.setCreditCardId(null);
        	creditCard.setRecUpdateBy(userId);
        	creditCard.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	creditCard.setRecCreateBy(userId);
        	creditCard.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	em.persist(creditCard);
        }
	}
}
