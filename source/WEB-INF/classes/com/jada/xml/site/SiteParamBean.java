package com.jada.xml.site;

public class SiteParamBean {
	String enableCaptcha;
	String captchaPublicKey;
	String captchaPrivateKey;
	String storeCreditCard;
	String bingClientId;
	String bingClientSecert;
	public String getCaptchaPublicKey() {
		return captchaPublicKey;
	}
	public void setCaptchaPublicKey(String captchaPublicKey) {
		this.captchaPublicKey = captchaPublicKey;
	}
	public String getCaptchaPrivateKey() {
		return captchaPrivateKey;
	}
	public void setCaptchaPrivateKey(String captchaPrivateKey) {
		this.captchaPrivateKey = captchaPrivateKey;
	}
	public String getEnableCaptcha() {
		return enableCaptcha;
	}
	public void setEnableCaptcha(String enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}
	public String getStoreCreditCard() {
		return storeCreditCard;
	}
	public void setStoreCreditCard(String storeCreditCard) {
		this.storeCreditCard = storeCreditCard;
	}
	public String getBingClientId() {
		return bingClientId;
	}
	public void setBingClientId(String bingClientId) {
		this.bingClientId = bingClientId;
	}
	public String getBingClientSecert() {
		return bingClientSecert;
	}
	public void setBingClientSecert(String bingClientSecert) {
		this.bingClientSecert = bingClientSecert;
	}
}
