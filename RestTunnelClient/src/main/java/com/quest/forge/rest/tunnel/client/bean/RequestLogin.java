package com.quest.forge.rest.tunnel.client.bean;

public class RequestLogin {

	private String customCode;
	private String accessKey;
	private String authToken;
	private String foglightUrl;
	
	public RequestLogin(String customCode, String accessKey, String authToken, String foglightUrl) {
		this.customCode = customCode;
		this.accessKey = accessKey;
		this.authToken = authToken;
		this.foglightUrl = foglightUrl;
	}
	
	public RequestLogin(String accessKey, String authToken) {
		this.accessKey = accessKey;
		this.authToken = authToken;
	}
	
	public RequestLogin() {
		this(null, null, null, null);
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getFoglightUrl() {
		return foglightUrl;
	}

	public void setFoglightUrl(String foglightUrl) {
		this.foglightUrl = foglightUrl;
	}
}
