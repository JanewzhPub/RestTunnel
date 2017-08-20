package com.quest.forge.rest.tunnel.client.service;

public interface TunnelClientService {

	boolean startClient(String customCode, String connectionToken, String foglightUrl);
	
	String getFoglightUrl(String customCode);
}
