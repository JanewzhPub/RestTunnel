package com.quest.forge.rest.tunnel.client.bean;

public class TunnelClientInfo {

	private String customCode;
	private String connectionToken;
	private String foglightUrl;
	
	public TunnelClientInfo () {
		this (null, null, null);
	}
	
	public TunnelClientInfo (String customCode, String connectionToken, String foglightUrl) {
		this.customCode = customCode;
		this.connectionToken = connectionToken;
		this.foglightUrl = foglightUrl;
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
}
