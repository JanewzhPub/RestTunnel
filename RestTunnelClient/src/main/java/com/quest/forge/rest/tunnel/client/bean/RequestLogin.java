package com.quest.forge.rest.tunnel.client.bean;

public class RequestLogin {

	private String customCode;
	private String accessKey;
	private String authToken;
	
	public RequestLogin(String customCode, String accessKey, String authToken) {
		this.customCode = customCode;
		this.accessKey = accessKey;
		this.authToken = authToken;
	}
	
	public RequestLogin() {
		this(null, null, null);
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
