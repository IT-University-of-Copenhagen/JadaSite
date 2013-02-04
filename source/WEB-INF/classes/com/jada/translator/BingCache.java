package com.jada.translator;

import java.io.Serializable;

public class BingCache implements Serializable {
	private static final long serialVersionUID = 1L;
	String accessToken;
	Long expireOn;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Long getExpireOn() {
		return expireOn;
	}
	public void setExpireOn(Long expireOn) {
		this.expireOn = expireOn;
	}
}
