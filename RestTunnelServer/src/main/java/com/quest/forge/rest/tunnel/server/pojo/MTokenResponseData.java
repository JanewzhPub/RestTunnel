package com.quest.forge.rest.tunnel.server.pojo;

public class MTokenResponseData extends AbstractResponseData {

	private String mtoken;
	
	public MTokenResponseData(String mtoken) {
		this.mtoken = mtoken;
	}

	public String getMtoken() {
		return mtoken;
	}

	public void setMtoken(String mtoken) {
		this.mtoken = mtoken;
	}
}
