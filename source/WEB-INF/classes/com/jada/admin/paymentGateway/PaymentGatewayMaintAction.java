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

package com.jada.admin.paymentGateway;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.PaymentGatewayDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.Site;
import com.jada.order.payment.gateway.AuthorizeNetEngine;
import com.jada.order.payment.gateway.EWayCVNAustraliaEngine;
import com.jada.order.payment.gateway.FirstDataEngine;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.order.payment.gateway.PayPalPayFlowEngine;
import com.jada.order.payment.gateway.PayPalWebsitePaymentProEngine;
import com.jada.order.payment.gateway.PayPalWebsitePaymentProHostedEngine;
import com.jada.order.payment.gateway.PaymentExpressEngine;
import com.jada.order.payment.gateway.PsiGateEngine;
import com.jada.util.AESEncoder;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.paymentGateway.AuthorizeNet;
import com.jada.xml.paymentGateway.EWayCVNAustralia;
import com.jada.xml.paymentGateway.FirstData;
import com.jada.xml.paymentGateway.PSIGate;
import com.jada.xml.paymentGateway.PayPalExpressCheckOut;
import com.jada.xml.paymentGateway.PayPalPayFlow;
import com.jada.xml.paymentGateway.PayPalWebsitePaymentPro;
import com.jada.xml.paymentGateway.PayPalWebsitePaymentProHosted;
import com.jada.xml.paymentGateway.PaymentExpress;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class PaymentGatewayMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

    	Site site = getAdminBean(request).getSite();
		PaymentGatewayMaintActionForm form = (PaymentGatewayMaintActionForm) actionForm;
        form.setMode("C");
        initForm(form, site);
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {
    	Site site = getAdminBean(request).getSite();
        PaymentGatewayMaintActionForm form = (PaymentGatewayMaintActionForm) actionForm;
        PaymentGateway paymentGateway = new PaymentGateway();
        paymentGateway = PaymentGatewayDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(form.getPaymentGatewayId()));
        form.setMode("U");
        copyProperties(form, paymentGateway);
        initForm(form, site);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		PaymentGatewayMaintActionForm form = (PaymentGatewayMaintActionForm) actionForm;
	
		try {
			PaymentGateway paymentGateway = PaymentGatewayDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(form.getPaymentGatewayId()));
			em.remove(paymentGateway);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.paymentGateway.constraint"));
				saveMessages(request, errors);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		PaymentGatewayMaintActionForm form = (PaymentGatewayMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		String siteId = site.getSiteId();

		PaymentGateway paymentGateway = new PaymentGateway();
		if (!insertMode) {
			paymentGateway = PaymentGatewayDAO.load(siteId, Format.getLong(form.getPaymentGatewayId()));
		}

		ActionMessages errors = validate(form, insertMode);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			initForm(form, site);
			return mapping.findForward("error");
		}

		if (insertMode) {
			paymentGateway.setSite(site);
			paymentGateway.setRecCreateBy(adminBean.getUser().getUserId());
			paymentGateway.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		paymentGateway.setPaymentGatewayName(form.getPaymentGatewayName());
		String provider = form.getPaymentGatewayProvider();
		paymentGateway.setPaymentGatewayProvider(provider);
		String paymentGatewayData = paymentGateway.getPaymentGatewayData();
		if (provider.equals(AuthorizeNetEngine.class.getSimpleName())) {
			AuthorizeNet authorizeNet = new AuthorizeNet();
			if (!insertMode) {
				authorizeNet = (AuthorizeNet) Utility.joxUnMarshall(AuthorizeNet.class, paymentGatewayData);
			}
			authorizeNet.setLoginId(form.getAuthorizeNetLoginId());
			if (!Format.isNullOrEmpty(form.getAuthorizeNetTranKey())) {
				authorizeNet.setTranKey(form.getAuthorizeNetTranKey());
			}
			authorizeNet.setEnvironment(form.getAuthorizeNetEnvironment());
			paymentGatewayData = Utility.joxMarshall("AuthorizeNet", authorizeNet);
		}
		if (provider.equals(PsiGateEngine.class.getSimpleName())) {
			PSIGate psiGate = new PSIGate();
			if (!insertMode) {
				psiGate = (PSIGate) Utility.joxUnMarshall(PSIGate.class, paymentGatewayData);
			}
			psiGate.setStoreId(form.getPsiGateStoreId());
			if (!Format.isNullOrEmpty(form.getPsiGatePassPhrase())) {
				psiGate.setPassPhrase(form.getPsiGatePassPhrase());
			}
			psiGate.setEnvironment(form.getPsiGateEnvironment());
			paymentGatewayData = Utility.joxMarshall("PSIGate", psiGate);
		}
		if (provider.equals(PayPalEngine.class.getSimpleName())) {
			PayPalExpressCheckOut payPalExpressCheckOut = new PayPalExpressCheckOut();
			if (!insertMode) {
				payPalExpressCheckOut = (PayPalExpressCheckOut) Utility.joxUnMarshall(PayPalExpressCheckOut.class, paymentGatewayData);
			}
			payPalExpressCheckOut.setPaymentPaypalCustClassId(Format.getLong(form.getPaymentPaypalCustClassId()));
			payPalExpressCheckOut.setPaymentPaypalApiUsername(form.getPaymentPaypalApiUsername());
			payPalExpressCheckOut.setPaymentPaypalEnvironment(form.getPaymentPaypalEnvironment());
			if (!Format.isNullOrEmpty(form.getPaymentPaypalExtraAmount())) {
				payPalExpressCheckOut.setPaymentPaypalExtraAmount(Format.getDouble(form.getPaymentPaypalExtraAmount()));
			}
			if (!Format.isNullOrEmpty(form.getPaymentPaypalExtraPercentage())) {
				payPalExpressCheckOut.setPaymentPaypalExtraPercentage(Format.getDouble(form.getPaymentPaypalExtraPercentage()));
			}
			if (!Format.isNullOrEmpty(form.getPaymentPaypalApiPassword())) {
				payPalExpressCheckOut.setPaymentPaypalApiPassword(AESEncoder.getInstance().encode(form.getPaymentPaypalApiPassword()));
			}
			if (!Format.isNullOrEmpty(form.getPaymentPaypalSignature())) {
				payPalExpressCheckOut.setPaymentPaypalSignature(AESEncoder.getInstance().encode(form.getPaymentPaypalSignature()));
			}
			paymentGatewayData = Utility.joxMarshall("PayPalExpressCheckOut", payPalExpressCheckOut);
		}
		if (provider.equals(PayPalWebsitePaymentProEngine.class.getSimpleName())) {
			PayPalWebsitePaymentPro payPalWebsitePaymentPro = new PayPalWebsitePaymentPro();
			if (!insertMode) {
				payPalWebsitePaymentPro = (PayPalWebsitePaymentPro) Utility.joxUnMarshall(PayPalWebsitePaymentPro.class, paymentGatewayData);
			}
			payPalWebsitePaymentPro.setPaypalApiUsername(form.getPaypalWebsitePaymentProApiUsername());
			payPalWebsitePaymentPro.setPaypalEnvironment(form.getPaypalWebsitePaymentProEnvironment());
			if (!Format.isNullOrEmpty(form.getPaypalWebsitePaymentProApiPassword())) {
				payPalWebsitePaymentPro.setPaypalApiPassword(AESEncoder.getInstance().encode(form.getPaypalWebsitePaymentProApiPassword()));
			}
			if (!Format.isNullOrEmpty(form.getPaypalWebsitePaymentProSignature())) {
				payPalWebsitePaymentPro.setPaypalSignature(AESEncoder.getInstance().encode(form.getPaypalWebsitePaymentProSignature()));
			}
			paymentGatewayData = Utility.joxMarshall("PayPalWebsitePaymentProEngine", payPalWebsitePaymentPro);
		}
		if (provider.equals(PayPalWebsitePaymentProHostedEngine.class.getSimpleName())) {
			PayPalWebsitePaymentProHosted payPalWebsitePaymentProHosted = new PayPalWebsitePaymentProHosted();
			if (!insertMode) {
				payPalWebsitePaymentProHosted = (PayPalWebsitePaymentProHosted) Utility.joxUnMarshall(PayPalWebsitePaymentProHosted.class, paymentGatewayData);
			}
			payPalWebsitePaymentProHosted.setBodyBgColor(form.getPaypalWebsitePaymentProHostedBodyBgColor());
			payPalWebsitePaymentProHosted.setBodyBgImg(form.getPaypalWebsitePaymentProHostedBodyBgImg());
			payPalWebsitePaymentProHosted.setFooterTextColor(form.getPaypalWebsitePaymentProHostedFooterTextColor());
			payPalWebsitePaymentProHosted.setHeaderBgColor(form.getPaypalWebsitePaymentProHostedHeaderBgColor());
			payPalWebsitePaymentProHosted.setHeaderHeight(form.getPaypalWebsitePaymentProHostedHeaderHeight());
			payPalWebsitePaymentProHosted.setLogoFont(form.getPaypalWebsitePaymentProHostedLogoFont());
			payPalWebsitePaymentProHosted.setLogoFontColor(form.getPaypalWebsitePaymentProHostedLogoFontColor());
			payPalWebsitePaymentProHosted.setLogoFontSize(form.getPaypalWebsitePaymentProHostedLogoFontSize());
			payPalWebsitePaymentProHosted.setLogoImage(form.getPaypalWebsitePaymentProHostedLogoImage());
			payPalWebsitePaymentProHosted.setLogoImagePosition(form.getPaypalWebsitePaymentProHostedLogoImagePosition());
			payPalWebsitePaymentProHosted.setLogoImage(form.getPaypalWebsitePaymentProHostedLogoImage());
			payPalWebsitePaymentProHosted.setPaypalApiUsername(form.getPaypalWebsitePaymentProHostedApiUsername());
			payPalWebsitePaymentProHosted.setPaypalEnvironment(form.getPaypalWebsitePaymentProHostedEnvironment());
			if (!Format.isNullOrEmpty(form.getPaypalWebsitePaymentProHostedApiPassword())) {
				payPalWebsitePaymentProHosted.setPaypalApiPassword(AESEncoder.getInstance().encode(form.getPaypalWebsitePaymentProHostedApiPassword()));
			}
			if (!Format.isNullOrEmpty(form.getPaypalWebsitePaymentProHostedSignature())) {
				payPalWebsitePaymentProHosted.setPaypalSignature(AESEncoder.getInstance().encode(form.getPaypalWebsitePaymentProHostedSignature()));
			}
			paymentGatewayData = Utility.joxMarshall("PayPalWebsitePaymentProHostedEngine", payPalWebsitePaymentProHosted);
		}
		if (provider.equals(PaymentExpressEngine.class.getSimpleName())) {
			PaymentExpress paymentExpress = new PaymentExpress();
			if (!insertMode) {
				paymentExpress = (PaymentExpress) Utility.joxUnMarshall(PaymentExpress.class, paymentGatewayData);
			}
			paymentExpress.setPostUsername(form.getPaymentExpressPostUsername());
			if (!Format.isNullOrEmpty(form.getPaymentExpressPostPassword())) {
				paymentExpress.setPostPassword(form.getPaymentExpressPostPassword());
			}
			paymentExpress.setEnvironment(form.getPaymentExpressEnvironment());
			paymentGatewayData = Utility.joxMarshall("PaymentExpress", paymentExpress);
		}
		if (provider.equals(PayPalPayFlowEngine.class.getSimpleName())) {
			PayPalPayFlow paypalPayFlow = new PayPalPayFlow();
			if (!insertMode) {
				paypalPayFlow = (PayPalPayFlow) Utility.joxUnMarshall(PayPalPayFlow.class, paymentGatewayData);
			}
			paypalPayFlow.setUser(form.getPaypalPayFlowUser());
			paypalPayFlow.setVendor(form.getPaypalPayFlowVendor());
			paypalPayFlow.setPartner(form.getPaypalPayFlowPartner());
			if (!Format.isNullOrEmpty(form.getPaypalPayFlowPassword())) {
				paypalPayFlow.setPassword(AESEncoder.getInstance().encode(form.getPaypalPayFlowPassword()));
			}
			paypalPayFlow.setEnvironment(form.getPaypalPayFlowEnvironment());
			paymentGatewayData = Utility.joxMarshall("PayPalPayFlow", paypalPayFlow);
		}
		if (provider.equals(EWayCVNAustraliaEngine.class.getSimpleName())) {
			EWayCVNAustralia eWayCVNAustralia = new EWayCVNAustralia();
			if (!insertMode) {
				eWayCVNAustralia = (EWayCVNAustralia) Utility.joxUnMarshall(EWayCVNAustralia.class, paymentGatewayData);
			}
			eWayCVNAustralia.setCustomerId(form.getEWayCVNAustraliaCustomerId());
			eWayCVNAustralia.setEnvironment(form.getEWayCVNAustraliaEnvironment());
			paymentGatewayData = Utility.joxMarshall("EWayCVSAustralia", eWayCVNAustralia);
		}
		
		if (provider.equals(FirstDataEngine.class.getSimpleName())) {
			FirstData firstData = new FirstData();
			if (!insertMode) {
				firstData = (FirstData) Utility.joxUnMarshall(FirstData.class, paymentGatewayData);
			}
			firstData.setFirstDataPassword(form.getFirstDataPassword());
			firstData.setFirstDataStoreNum(form.getFirstDataStoreNum());
			firstData.setFirstDataKeyFile(form.getFirstDataKeyFile());
			firstData.setFirstDataHostName(form.getFirstDataHostName());
			firstData.setFirstDataHostPort(form.getFirstDataHostPort());
			paymentGatewayData = Utility.joxMarshall("FirstData", firstData);
		}
		
		paymentGateway.setPaymentGatewayData(paymentGatewayData);
		
		paymentGateway.setRecUpdateBy(adminBean.getUser().getUserId());
		paymentGateway.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(paymentGateway);
		}
		form.setMode("U");
        initForm(form, site);
		form.setPaymentGatewayId(paymentGateway.getPaymentGatewayId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void initForm(PaymentGatewayMaintActionForm form, Site site) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		LabelValueBean paymentGateways[] = {new LabelValueBean("", ""),
				new LabelValueBean("Authorize.Net", AuthorizeNetEngine.class.getSimpleName()),
				new LabelValueBean("eWAY Australia CVN", EWayCVNAustraliaEngine.class.getSimpleName()),
				new LabelValueBean("PsiGate", PsiGateEngine.class.getSimpleName()),
				new LabelValueBean("PayPal Express Checkout", PayPalEngine.class.getSimpleName()),
				new LabelValueBean("PayPal Payflow Pro", PayPalPayFlowEngine.class.getSimpleName()),
				new LabelValueBean("PayPal Website Payment Pro", PayPalWebsitePaymentProEngine.class.getSimpleName()),
				new LabelValueBean("PayPal Website Payment Pro Hosted", PayPalWebsitePaymentProHostedEngine.class.getSimpleName()),
				new LabelValueBean("Payment Express", PaymentExpressEngine.class.getSimpleName()),
				new LabelValueBean("First Data", FirstDataEngine.class.getSimpleName())
		};
		form.setPaymentGateways(paymentGateways);
		
        
        Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
        Query query = em.createQuery("from CustomerClass where siteId = :siteId order by custClassId");
        query.setParameter("siteId", site.getSiteId());
        Iterator<?> iterator = query.getResultList().iterator();
        vector.add(new LabelValueBean("", ""));
        while (iterator.hasNext()) {
        	CustomerClass customerClass = (CustomerClass) iterator.next();
        	LabelValueBean bean = new LabelValueBean();
        	bean.setLabel(customerClass.getCustClassName());
        	bean.setValue(customerClass.getCustClassId().toString());
        	vector.add(bean);
        }
        LabelValueBean customerClasses[] = new LabelValueBean[vector.size()];
        vector.copyInto(customerClasses);
        form.setCustomerClasses(customerClasses);
	}

	private void copyProperties(PaymentGatewayMaintActionForm form, PaymentGateway paymentGateway) throws Exception {
		form.setPaymentGatewayId(paymentGateway.getPaymentGatewayId().toString());
		form.setPaymentGatewayName(paymentGateway.getPaymentGatewayName());
		
		String provider = paymentGateway.getPaymentGatewayProvider();
		form.setPaymentGatewayProvider(provider);
		String data = paymentGateway.getPaymentGatewayData();
		if (!Format.isNullOrEmpty(data)) {
			if (provider.equals(AuthorizeNetEngine.class.getSimpleName())) {
				AuthorizeNet authorizeNet = (AuthorizeNet) Utility.joxUnMarshall(AuthorizeNet.class, data);
				form.setAuthorizeNetLoginId(authorizeNet.getLoginId());
//				form.setAuthorizeNetTranKey(authorizeNet.getTranKey());
				form.setAuthorizeNetEnvironment(authorizeNet.getEnvironment());
			}
			if (provider.equals(PsiGateEngine.class.getSimpleName())) {
				PSIGate psiGate = (PSIGate) Utility.joxUnMarshall(PSIGate.class, data);
				form.setPsiGateStoreId(psiGate.getStoreId());
//				form.setPsiGatePassPhrase(psiGate.getPassPhrase());
				form.setPsiGateEnvironment(psiGate.getEnvironment());
			}
			if (provider.equals(PayPalEngine.class.getSimpleName())) {
				PayPalExpressCheckOut payPalExpressCheckOut = (PayPalExpressCheckOut) Utility.joxUnMarshall(PayPalExpressCheckOut.class, data);
				if (payPalExpressCheckOut.getPaymentPaypalCustClassId() != null) {
					form.setPaymentPaypalCustClassId(payPalExpressCheckOut.getPaymentPaypalCustClassId().toString());
				}
				form.setPaymentPaypalApiUsername(payPalExpressCheckOut.getPaymentPaypalApiUsername());
//				form.setPaymentPaypalApiPassword(payPalExpressCheckOut.getPaymentPaypalApiPassword());
//				form.setPaymentPaypalSignature(payPalExpressCheckOut.getPaymentPaypalSignature());
				form.setPaymentPaypalEnvironment(payPalExpressCheckOut.getPaymentPaypalEnvironment());
				form.setPaymentPaypalExtraAmount(Format.getDouble(payPalExpressCheckOut.getPaymentPaypalExtraAmount()));
				form.setPaymentPaypalExtraPercentage(Format.getDouble(payPalExpressCheckOut.getPaymentPaypalExtraPercentage()));
			}
			if (provider.equals(PayPalWebsitePaymentProEngine.class.getSimpleName())) {
				PayPalWebsitePaymentPro payPalWebsitePaymentPro = (PayPalWebsitePaymentPro) Utility.joxUnMarshall(PayPalWebsitePaymentPro.class, data);
				form.setPaypalWebsitePaymentProApiUsername(payPalWebsitePaymentPro.getPaypalApiUsername());
				form.setPaypalWebsitePaymentProEnvironment(payPalWebsitePaymentPro.getPaypalEnvironment());
			}
			if (provider.equals(PayPalWebsitePaymentProHostedEngine.class.getSimpleName())) {
				PayPalWebsitePaymentProHosted payPalWebsitePaymentProHosted = (PayPalWebsitePaymentProHosted) Utility.joxUnMarshall(PayPalWebsitePaymentProHosted.class, data);
				form.setPaypalWebsitePaymentProHostedBodyBgColor(payPalWebsitePaymentProHosted.getBodyBgColor());
				form.setPaypalWebsitePaymentProHostedBodyBgImg(payPalWebsitePaymentProHosted.getBodyBgImg());
				form.setPaypalWebsitePaymentProHostedFooterTextColor(payPalWebsitePaymentProHosted.getFooterTextColor());
				form.setPaypalWebsitePaymentProHostedHeaderBgColor(payPalWebsitePaymentProHosted.getHeaderBgColor());
				form.setPaypalWebsitePaymentProHostedHeaderHeight(payPalWebsitePaymentProHosted.getHeaderHeight());
				form.setPaypalWebsitePaymentProHostedLogoFont(payPalWebsitePaymentProHosted.getLogoFont());
				form.setPaypalWebsitePaymentProHostedLogoFontColor(payPalWebsitePaymentProHosted.getLogoFontColor());
				form.setPaypalWebsitePaymentProHostedLogoFontSize(payPalWebsitePaymentProHosted.getLogoFontSize());
				form.setPaypalWebsitePaymentProHostedLogoImage(payPalWebsitePaymentProHosted.getLogoImage());
				form.setPaypalWebsitePaymentProHostedLogoImagePosition(payPalWebsitePaymentProHosted.getLogoImagePosition());
				form.setPaypalWebsitePaymentProHostedLogoImage(payPalWebsitePaymentProHosted.getLogoImage());
				form.setPaypalWebsitePaymentProHostedApiUsername(payPalWebsitePaymentProHosted.getPaypalApiUsername());
				form.setPaypalWebsitePaymentProHostedEnvironment(payPalWebsitePaymentProHosted.getPaypalEnvironment());
			}
			if (provider.equals(PaymentExpressEngine.class.getSimpleName())) {
				PaymentExpress paymentExpress = (PaymentExpress) Utility.joxUnMarshall(PaymentExpress.class, data);
				form.setPaymentExpressPostUsername(paymentExpress.getPostUsername());
				form.setPaymentExpressEnvironment(paymentExpress.getEnvironment());
			}
			if (provider.equals(PayPalPayFlowEngine.class.getSimpleName())) {
				PayPalPayFlow payPalPayFlow = (PayPalPayFlow) Utility.joxUnMarshall(PayPalPayFlow.class, data);
				form.setPaypalPayFlowUser(payPalPayFlow.getUser());
				form.setPaypalPayFlowVendor(payPalPayFlow.getVendor());
				form.setPaypalPayFlowPartner(payPalPayFlow.getPartner());
//				form.setPaypalPayFlowPassword(payPalPayFlow.getPassword());
				form.setPaypalPayFlowEnvironment(payPalPayFlow.getEnvironment());
			}
			if (provider.equals(EWayCVNAustraliaEngine.class.getSimpleName())) {
				EWayCVNAustralia eWayCVNAustralia = (EWayCVNAustralia) Utility.joxUnMarshall(EWayCVNAustralia.class, data);
				form.setEWayCVNAustraliaCustomerId(eWayCVNAustralia.getCustomerId());
				form.setEWayCVNAustraliaEnvironment(eWayCVNAustralia.getEnvironment());
			}
			if (provider.equals(FirstDataEngine.class.getSimpleName())) {
				FirstData firstData = (FirstData) Utility.joxUnMarshall(FirstData.class, data);
				form.setFirstDataStoreNum(firstData.getFirstDataStoreNum());
				form.setFirstDataPassword(firstData.getFirstDataPassword());
				form.setFirstDataKeyFile(firstData.getFirstDataKeyFile());
				form.setFirstDataHostName(firstData.getFirstDataHostName());
				form.setFirstDataHostPort(firstData.getFirstDataHostPort());
			}
		}
	}

    public ActionMessages validate(PaymentGatewayMaintActionForm form, boolean insertMode) { 
    	ActionMessages errors = new ActionMessages();
    	String provider = form.getPaymentGatewayProvider();
    	if (provider.equals(AuthorizeNetEngine.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getAuthorizeNetLoginId())) {
    			errors.add("authorizeNetLoginId", new ActionMessage("error.string.required"));
    		}
    		if (insertMode) {
	    		if (Format.isNullOrEmpty(form.getAuthorizeNetTranKey())) {
	    			errors.add("authorizeNetTranKey", new ActionMessage("error.string.required"));
	    		}
    		}
    	}
    	if (provider.equals(PsiGateEngine.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getPsiGateStoreId())) {
    			errors.add("psiGateStoreId", new ActionMessage("error.string.required"));
    		}
    		if (insertMode) {
	    		if (Format.isNullOrEmpty(form.getPsiGatePassPhrase())) {
	    			errors.add("psiGatePassPhrase", new ActionMessage("error.string.required"));
	    		}
    		}
    	}
    	if (provider.equals(PayPalEngine.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getPaymentPaypalApiUsername())) {
    			errors.add("paymentPaypalApiUsername", new ActionMessage("error.string.required"));
    		}
    		if (Format.isNullOrEmpty(form.getPaymentPaypalCustClassId())) {
    			errors.add("paymentPaypalCustClassId", new ActionMessage("error.string.required"));
    		}
    		if (insertMode) {
        		if (Format.isNullOrEmpty(form.getPaymentPaypalApiPassword())) {
        			errors.add("paymentPaypalApiPassword", new ActionMessage("error.string.required"));
        		}
        		if (Format.isNullOrEmpty(form.getPaymentPaypalSignature())) {
        			errors.add("paymentPaypalSignature", new ActionMessage("error.string.required"));
        		}
    		}
    		if (!Format.isNullOrEmpty(form.getPaymentPaypalExtraAmount())) {
    			if (!Format.isDouble(form.getPaymentPaypalExtraAmount())) {
    				errors.add("paymentPaypalExtraAmount", new ActionMessage("error.float.invalid"));
    			}
    		}
    		if (!Format.isNullOrEmpty(form.getPaymentPaypalExtraPercentage())) {
    			if (!Format.isDouble(form.getPaymentPaypalExtraPercentage())) {
    				errors.add("paymentPaypalExtraPercentage", new ActionMessage("error.float.invalid"));
    			}
    		}
    	}
    	if (provider.equals(PayPalWebsitePaymentProEngine.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getPaypalWebsitePaymentProApiUsername())) {
    			errors.add("paypalWebsitePaymentProApiUsername", new ActionMessage("error.string.required"));
    		}
    		if (insertMode) {
        		if (Format.isNullOrEmpty(form.getPaypalWebsitePaymentProApiPassword())) {
        			errors.add("paypalWebsitePaymentProApiPassword", new ActionMessage("error.string.required"));
        		}
        		if (Format.isNullOrEmpty(form.getPaypalWebsitePaymentProSignature())) {
        			errors.add("paypalWebsitePaymentProSignature", new ActionMessage("error.string.required"));
        		}
    		}
    	}
    	if (provider.equals(PayPalWebsitePaymentProHostedEngine.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getPaypalWebsitePaymentProHostedApiUsername())) {
    			errors.add("paypalWebsitePaymentProHostedApiUsername", new ActionMessage("error.string.required"));
    		}
    		if (insertMode) {
        		if (Format.isNullOrEmpty(form.getPaypalWebsitePaymentProHostedApiPassword())) {
        			errors.add("paypalWebsitePaymentProHostedApiPassword", new ActionMessage("error.string.required"));
        		}
        		if (Format.isNullOrEmpty(form.getPaypalWebsitePaymentProHostedSignature())) {
        			errors.add("paypalWebsitePaymentProHostedSignature", new ActionMessage("error.string.required"));
        		}
    		}
    	}
    	if (provider.equals(PayPalPayFlowEngine.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getPaypalPayFlowUser())) {
    			errors.add("paypalPayFlowUser", new ActionMessage("error.string.required"));
    		}
    		if (Format.isNullOrEmpty(form.getPaypalPayFlowVendor())) {
    			errors.add("paypalPayFlowVendor", new ActionMessage("error.string.required"));
    		}
    		if (Format.isNullOrEmpty(form.getPaypalPayFlowPartner())) {
    			errors.add("paypalPayFlowPartner", new ActionMessage("error.string.required"));
    		}
    		if (insertMode) {
        		if (Format.isNullOrEmpty(form.getPaypalPayFlowPassword())) {
        			errors.add("paypalPayFlowPassword", new ActionMessage("error.string.required"));
        		}
    		}
    	}
    	if (provider.equals(PaymentExpressEngine.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getPaymentExpressPostUsername())) {
    			errors.add("paymentExpressPostUsername", new ActionMessage("error.string.required"));
    		}
    		if (insertMode) {
	    		if (Format.isNullOrEmpty(form.getPaymentExpressPostPassword())) {
	    			errors.add("paymentExpressPostPassword", new ActionMessage("error.string.required"));
	    		}
    		}
    	}
    	if (provider.equals(EWayCVNAustralia.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getEWayCVNAustraliaCustomerId())) {
    			errors.add("eWayCVNAustraliaCustomerId", new ActionMessage("error.string.required"));
    		}
    	}
    	if (provider.equals(FirstData.class.getSimpleName())) {
    		if (Format.isNullOrEmpty(form.getFirstDataStoreNum())) {
    			errors.add("firstDataStoreNum", new ActionMessage("error.string.required"));
    		}
    		if (Format.isNullOrEmpty(form.getFirstDataKeyFile())) {
    			errors.add("firstDataKeyFile", new ActionMessage("error.string.required"));
    		}
    		if (Format.isNullOrEmpty(form.getFirstDataHostName())) {
    			errors.add("firstDataHostName", new ActionMessage("error.string.required"));
    		}
    		if (Format.isNullOrEmpty(form.getFirstDataHostPort())) {
    			errors.add("firstDataHostPort", new ActionMessage("error.string.required"));
    		}
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        return map;
    }
}
