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

package com.jada.myaccount.forgot;

import com.jada.content.frontend.FrontendBaseForm;

public class MyAccountForgotActionForm extends FrontendBaseForm {
	private static final long serialVersionUID = -1992667571314367436L;
	String custEmail;
	String captchaPublicKey;
	String captchaPrivateKey;
	String enableCaptcha;
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
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
	public String getEnableCaptcha() {
		return enableCaptcha;
	}
	public void setEnableCaptcha(String enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}
}
