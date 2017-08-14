package com.quest.forge.rest.tunnel.server.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TTokenResponseData extends AbstractResponseData {
	
	private String customCode;
	private String ttoken;
	
	public TTokenResponseData(String customCode, String ttoken) {
		this.customCode = customCode;
		this.ttoken = ttoken;
	}

	public String getTtoken() {
		return ttoken;
	}

	public void setTtoken(String ttoken) {
		this.ttoken = ttoken;
	}

	@JsonProperty("custom-code")
	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
}
