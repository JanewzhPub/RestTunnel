package com.quest.forge.rest.tunnel.client.bean;

public class TunnelClientInfo {

	private String customCode;
	private String connectionToken;
	private String accessKey;
	private String authToken;
	private String foglightUrl;
	
	public TunnelClientInfo () {
		this (null, null, null, null, null);
	}
	
	public TunnelClientInfo (String customCode, String connectionToken, String accessKey, String foglightUrl, String authToken) {
		this.customCode = customCode;
		this.connectionToken = connectionToken;
		this.accessKey = accessKey;
		this.foglightUrl = foglightUrl;
		this.authToken = authToken;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getFoglightUrl() {
		return foglightUrl;
	}

	public void setFoglightUrl(String foglightUrl) {
		this.foglightUrl = foglightUrl;
	}

	public String getConnectionToken() {
		return connectionToken;
	}

	public void setConnectionToken(String connectionToken) {
		this.connectionToken = connectionToken;
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
}
