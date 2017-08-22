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

package com.quest.forge.rest.tunnel.client;

import static com.quest.forge.rest.tunnel.common.Constants.HTTP_HEADER_ACCESS_KEY;
import static com.quest.forge.rest.tunnel.common.Constants.HTTP_HEADER_CUSTOM_CODE;
import static com.quest.forge.rest.tunnel.common.ExchangeMessage.gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quest.forge.rest.tunnel.common.ExchangeMessage;
import com.quest.forge.rest.tunnel.common.GZipUtil;
import com.quest.forge.rest.tunnel.common.ResourceRequest;
import com.quest.forge.rest.tunnel.common.ResourceResponse;

/**
 * Tunnel client keeping websocket connection to tunnel server, and recive
 * server message to consume rest services through {@link RestClient}, at last
 * response to tunnel server about the rest response through websocket.
 * 
 * @author bliu
 *
 */
// TODO: have a better control of maxIdleTime with server together, it shouldn't
// be never expire
@WebSocket(maxIdleTime = -1, maxBinaryMessageSize = 2048 * 1024)
public class TunnelClient {

private final static Logger logger = LoggerFactory.getLogger(TunnelClient.class);

private Session session;

private WebSocketClient wsClient;

private final RestClient restClient;

private final String tunnelServerWebsocketUri;

private final String tunnelServerAccessKey;

private final String customCode;

public TunnelClient(String tunnelServerWebsocketUri, String restServerBaseUri, String customCode,
		String tunnelServerAccessKey) {
	restClient = new RestClient(restServerBaseUri);
	this.tunnelServerWebsocketUri = tunnelServerWebsocketUri;
	this.customCode = customCode;
	this.tunnelServerAccessKey = tunnelServerAccessKey;
}

public boolean isConnected() {
	return session != null && session.isOpen();
}

public void stop() throws Exception {
	if (session != null) {
		session.close();
	}
	if (wsClient != null) {
		wsClient.stop();
	}
}

public void start() throws Exception {
	restClient.testConnection();
	logger.info("Successfully verified rest service " + restClient.getBaseURL());

//	SslContextFactory sslContextFactory = new SslContextFactory();
//	sslContextFactory.setKeyStorePath("mfoglight.pfx");
//	sslContextFactory.setKeyStoreType("PKCS12");
//	sslContextFactory.setKeyManagerPassword("foglight");
//	sslContextFactory.setKeyStorePassword("foglight");
	
//	wsClient = new WebSocketClient(sslContextFactory);
	wsClient = new WebSocketClient();
	wsClient.start();
	URI tunnelServerUri = new URI(tunnelServerWebsocketUri);
	ClientUpgradeRequest request = new ClientUpgradeRequest();
	request.setHeader(HTTP_HEADER_ACCESS_KEY, tunnelServerAccessKey);
	request.setHeader(HTTP_HEADER_CUSTOM_CODE, customCode);
	wsClient.connect(this, tunnelServerUri, request);
}

@OnWebSocketClose
public void onClose(int statusCode, String reason) {
	logger.info("Connection closed: " + statusCode + " -" + reason);
	this.session = null;
}

@OnWebSocketConnect
public void onConnect(Session session) {
	logger.info("Successfully connected to " + tunnelServerWebsocketUri);
	this.session = session;
}

@OnWebSocketError
public void onError(final Throwable e) {
	logger.error(e.getMessage(), e);
}

@OnWebSocketMessage
// TODO: need more exception handling to make it more stable
// TODO: more audit is needed for security concern
public void onMessage(final byte[] content, final int offset, final int length) throws IOException {
	String msgStr = GZipUtil.decompress(new ByteArrayInputStream(content, offset, length));
	if (logger.isDebugEnabled()) {
		logger.debug("Received message: " + msgStr);
	}
	ExchangeMessage message = gson.fromJson(msgStr, ExchangeMessage.class);
	try {
		if (message.retrieveIsResourceRequest()) {
			ResourceResponse response = restClient.request((ResourceRequest) message.getData());
			sendResponse(response, message);
		}
		else {
			logger.warn("Invalid message: " + msgStr);
		}
	}
	catch (Exception e) {
		logger.error(e.getMessage(), e);
		try {
			sendResponse(new ResourceResponse(500, "application/json",
					"{status:0,code:\"tunnelClientException\"}"), message);
		}
		catch (IOException e1) {
			logger.error(e1.getMessage(), e1);
		}
	}
}

private void sendResponse(ResourceResponse response, ExchangeMessage request) throws IOException {
	ExchangeMessage message = new ExchangeMessage(request);
	message.setData(response);
	sendMessage(message);
}

private void sendMessage(ExchangeMessage message) throws IOException {
	String content = gson.toJson(message);
	/*
	 * use binary compressed content will reduce much payload in websocket
	 */
	session.getRemote().sendBytes(ByteBuffer.wrap(GZipUtil.compress(content)));
	if (logger.isDebugEnabled()) {
		logger.debug("sent message: " + content);
	}
}

}
