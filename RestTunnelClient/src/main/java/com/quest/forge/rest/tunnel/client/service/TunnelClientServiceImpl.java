package com.quest.forge.rest.tunnel.client.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.stereotype.Service;

import com.quest.forge.rest.tunnel.client.TunnelClient;
import com.quest.forge.rest.tunnel.client.TunnelWebsocketWatcher;

@Service("TunnelClientService")
public class TunnelClientServiceImpl implements TunnelClientService {

	public static final String WEBSOCKET_PROTOCOL = "ws";
	public static final String MFOGLIGHT_SERVER_URL = "mfoglight.azurewebsites.net";
	public static final String MFOGLIGHT_SERVER_TUNNEL_PATH = "/api/tunnel";

	public static final String FOGLIGHT_CONNECTION_PROTOCOL = "http";
	public static final String FOGLIGHT_CONNECTION_PATH = "/api/v1";
	
	public static final int CONNECT_WAITING_TIMEOUT_CHECKCOUNT = 10;
	public static final int CONNECT_WAITING_TIMEOUT = 5 * 1000;

	@Override
	public boolean startClient(String customCode, String connectionToken, String foglightUrl) {
		try {
			ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
			TunnelClient client = new TunnelClient(
					WEBSOCKET_PROTOCOL + "://" + MFOGLIGHT_SERVER_URL + MFOGLIGHT_SERVER_TUNNEL_PATH,
					FOGLIGHT_CONNECTION_PROTOCOL + "://" + foglightUrl + FOGLIGHT_CONNECTION_PATH, customCode,
					connectionToken);
			client.start();
			new TunnelWebsocketWatcher(executor).startWatch(client);
			/*
			 * TODO will be replaced
			 */
			for (int i = 0; i < CONNECT_WAITING_TIMEOUT_CHECKCOUNT; i++) {
				Thread.sleep(CONNECT_WAITING_TIMEOUT);
				if (client.isConnected()) {
					return true;
				}
			}
		} catch (Exception e) {
			//TODO handle exception.
		}
		return false;
	}
}
