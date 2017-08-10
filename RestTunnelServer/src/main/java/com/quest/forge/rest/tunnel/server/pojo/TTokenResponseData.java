package com.quest.forge.rest.tunnel.server.pojo;

public class TTokenResponseData extends AbstractResponseData {

	private String ttoken;
	
	public TTokenResponseData(String ttoken) {
		this.ttoken = ttoken;
	}

	public String getTtoken() {
		return ttoken;
	}

	public void setTtoken(String ttoken) {
		this.ttoken = ttoken;
	}
}
