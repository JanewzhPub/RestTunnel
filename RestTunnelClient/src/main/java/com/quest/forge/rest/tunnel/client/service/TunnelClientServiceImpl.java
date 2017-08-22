/*
  QUEST SOFTWARE PROPRIETARY INFORMATION
  
  This software is confidential.  Quest Software Inc., or one of its
  subsidiaries, has supplied this software to you under terms of a
  license agreement, nondisclosure agreement or both.
  
  You may not copy, disclose, or use this software except in accordance with
  those terms.
  
  
  Copyright 2017 Quest Software Inc.
  ALL RIGHTS RESERVED.
  
  QUEST SOFTWARE INC. MAKES NO REPRESENTATIONS OR
  WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE, OR
  NON-INFRINGEMENT.  QUEST SOFTWARE SHALL NOT BE
  LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
  AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
  THIS SOFTWARE OR ITS DERIVATIVES.
*/
package com.quest.forge.rest.tunnel.client.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quest.forge.rest.tunnel.client.TunnelClient;
import com.quest.forge.rest.tunnel.client.TunnelWebsocketWatcher;
import com.quest.forge.rest.tunnel.client.config.RestTunnelClientConfig;

/**
 * Manage connection between client and server using websocket
 * @author jwang7
 */
@Service("TunnelClientService")
public class TunnelClientServiceImpl implements TunnelClientService {
	
	private static final Logger logger = LoggerFactory.getLogger(TunnelClientServiceImpl.class);
	
	@Autowired
	private MessageBundleService message;
	@Autowired
	private RestTunnelClientConfig clientConfig;
	
	public static final int CONNECT_WAITING_TIMEOUT_CHECKCOUNT = 30;
	public static final int CONNECT_WAITING_TIMEOUT = 2 * 1000;

	private static TunnelClient client;
	
	@Override
	public boolean startClient(String customCode, String connectionToken, String foglightUrl) {
		try {
			if (client != null && client.isConnected()) {
				if (!shutdownClient()) {
					logger.error(message.getMessage("client.connection.shutdown.timeout.debug", 
							customCode, foglightUrl));
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
			logger.error(message.getMessage("client.start.timeout.debug"));
		} catch (Exception e) {
			logger.error(message.getMessage("client.start.failed.debug", 
					customCode, connectionToken, foglightUrl, e.getMessage()));
		}
		return false;
	}

	@Override
	public boolean shutdownClient() {
		if (client != null && client.isConnected()) {
			try {
				client.stop();
				/*
				 * TODO will be replaced
				 */
				for (int i = 0; i < CONNECT_WAITING_TIMEOUT_CHECKCOUNT; i++) {
					Thread.sleep(CONNECT_WAITING_TIMEOUT);
					if (!client.isConnected()) {
						client = null;
						return true;
					}
				}
				logger.error(message.getMessage("client.shutdown.timeout.debug"));
			} catch (Exception e) {
				logger.error(message.getMessage("client.shutdown.failed.debug"));
			}	
		}
		return false;
	}
}
