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

package com.jada.myaccount.register;

import com.jada.content.frontend.FrontendBaseForm;

public class MyAccountRegisterActionForm extends FrontendBaseForm {
	private static final long serialVersionUID = -7654275162594969929L;
	String url;
	String custEmail1;
	String custEmail2;
	String custPublicName;
	String custPassword;
	String custPassword1;
	String enableCaptcha;
	String captchaPublicKey;
	String captchaPrivateKey;
	String captchaChallengeField;
	String captchaResponseField;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCustPublicName() {
		return custPublicName;
	}
	public void setCustPublicName(String custPublicName) {
		this.custPublicName = custPublicName;
	}
	public String getCustPassword() {
		return custPassword;
	}
	public void setCustPassword(String custPassword) {
		this.custPassword = custPassword;
	}
	public String getCustPassword1() {
		return custPassword1;
	}
	public void setCustPassword1(String custPassword1) {
		this.custPassword1 = custPassword1;
	}
	public String getCustEmail1() {
		return custEmail1;
	}
	public void setCustEmail1(String custEmail1) {
		this.custEmail1 = custEmail1;
	}
	public String getCustEmail2() {
		return custEmail2;
	}
	public void setCustEmail2(String custEmail2) {
		this.custEmail2 = custEmail2;
	}
	public String getEnableCaptcha() {
		return enableCaptcha;
	}
	public void setEnableCaptcha(String enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}	
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
	public String getCaptchaChallengeField() {
		return captchaChallengeField;
	}
	public void setCaptchaChallengeField(String captchaChallengeField) {
		this.captchaChallengeField = captchaChallengeField;
	}
	public String getCaptchaResponseField() {
		return captchaResponseField;
	}
	public void setCaptchaResponseField(String captchaResponseField) {
		this.captchaResponseField = captchaResponseField;
	}
}
