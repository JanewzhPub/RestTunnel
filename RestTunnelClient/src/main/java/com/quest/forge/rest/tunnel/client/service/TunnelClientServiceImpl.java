package com.quest.forge.rest.tunnel.client.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quest.forge.rest.tunnel.client.TunnelClient;
import com.quest.forge.rest.tunnel.client.TunnelWebsocketWatcher;
import com.quest.forge.rest.tunnel.client.config.RestTunnelClientConfig;

@Service("TunnelClientService")
public class TunnelClientServiceImpl implements TunnelClientService {
	
	@Autowired
	private RestTunnelClientConfig clientConfig;
	
	public static final int CONNECT_WAITING_TIMEOUT_CHECKCOUNT = 10;
	public static final int CONNECT_WAITING_TIMEOUT = 5 * 1000;

	private static TunnelClient client;
	
	@Override
	public boolean startClient(String customCode, String connectionToken, String foglightUrl) {
		try {
			if (client != null && client.isConnected()) {
				if (!shutdownClient()) {
					//TODO handle shutdown exception
					return false;
				}
			}
			ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
			client = new TunnelClient(
					clientConfig.getWebsocketProtocol() + "://" + 
					clientConfig.getServerUrl() + clientConfig.getServerTunnelPath(),
					clientConfig.getFoglightConnectionProtocol() + "://" + 
					foglightUrl + clientConfig.getFoglightConnectionPath(), 
					customCode,
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
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean shutdownClient() {
		if (client != null && client.isConnected()) {
			try {
				client.stop();
				for (int i = 0; i < CONNECT_WAITING_TIMEOUT_CHECKCOUNT; i++) {
					Thread.sleep(CONNECT_WAITING_TIMEOUT);
					if (!client.isConnected()) {
						client = null;
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		return false;
	}
}
