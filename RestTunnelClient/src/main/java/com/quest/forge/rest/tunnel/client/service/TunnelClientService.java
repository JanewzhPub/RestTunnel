package com.quest.forge.rest.tunnel.client.service;

public interface TunnelClientService {

	boolean shutdownClient();

	boolean startClient(String customCode, String connectionToken, String foglightUrl);
}
