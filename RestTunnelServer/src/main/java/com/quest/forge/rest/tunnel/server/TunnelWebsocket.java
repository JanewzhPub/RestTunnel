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

package com.quest.forge.rest.tunnel.server;

import static com.quest.forge.rest.tunnel.common.Constants.HTTP_HEADER_ACCESS_KEY;
import static com.quest.forge.rest.tunnel.common.Constants.HTTP_HEADER_CUSTOM_CODE;
import static com.quest.forge.rest.tunnel.common.ExchangeMessage.gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quest.forge.rest.tunnel.common.ExchangeMessage;
import com.quest.forge.rest.tunnel.common.GZipUtil;
import com.quest.forge.rest.tunnel.server.service.ServiceFactory;
import com.quest.forge.rest.tunnel.server.service.TunnelClientConnMaintainService;

/**
 * The tunnel server websocket server
 * 
 * @author bliu
 * 
 */
@ServerEndpoint(value = "/tunnel", configurator = RestServerEndpointConf.class)
public class TunnelWebsocket {

private Session wsSession;

private String accessKey;

private String customCode;

private Date estabishTime;

private final Map<String, ExchangeMessage> inQueueMessages = new ConcurrentHashMap<String, ExchangeMessage>();

private final static Log logger = LogFactory.getLog(TunnelWebsocket.class.getName());

private final static int WS_CLIENT_HANDLING_TIMEOUT = 30 * 1000;

private final static int MAX_CONCURRENT_REQUEST = 2000;

@OnOpen
public void onOpen(Session session, EndpointConfig config) {
	this.estabishTime = new Date();
	this.accessKey = (String) config.getUserProperties().get(HTTP_HEADER_ACCESS_KEY);
	this.customCode = (String) config.getUserProperties().get(HTTP_HEADER_CUSTOM_CODE);
	this.wsSession = session;
	getMaintainService().clientConnected(this);
	// TODO: currently never expire, a better setting is needed
	session.setMaxIdleTimeout(-1);
}

@OnClose
public void onClose(Session session) {
	getMaintainService().clientDisconnected(this);
}

public boolean isReachMaxConcurrentRequest() {
	return inQueueMessages.size() > MAX_CONCURRENT_REQUEST;
}

public ExchangeMessage sendAndWaitForResponse(ExchangeMessage msg) throws IOException {
	inQueueMessages.put(msg.getMessageId(), msg);
	try {
		String content = gson.toJson(msg);
		if (logger.isDebugEnabled()) {
			logger.debug("sending message to client <" + this + ">: " + content);
		}
		wsSession.getBasicRemote().sendBinary(ByteBuffer.wrap(GZipUtil.compress(content)));
		try {
			synchronized (msg) {
				msg.wait(WS_CLIENT_HANDLING_TIMEOUT);
			}
		}
		catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		ExchangeMessage response = inQueueMessages.get(msg.getMessageId());
		return response != null && response.retrieveIsResourceResponse() ? response : null;
	}
	finally {
		inQueueMessages.remove(msg.getMessageId());
	}
}

@OnMessage
public void onMessage(InputStream is, Session session) throws IOException {
	String message = GZipUtil.decompress(is);
	if (logger.isDebugEnabled()) {
		logger.debug("received message from client <" + this + ">: " + message);
	}
	try {
		ExchangeMessage resp = gson.fromJson(message, ExchangeMessage.class);
		if (resp.retrieveIsResourceResponse()) {
			ExchangeMessage request = inQueueMessages.put(resp.getMessageId(), resp);
			if (request != null && request.retrieveIsResourceRequest()) {
				synchronized (request) {
					request.notify();
				}
			}
			else {
				logger.warn("expect a request message object, but encountered: " + request);
			}
		}
		else {
			logger.warn("Ignore message from client <" + this + ">: " + message);
		}
	}
	catch (Exception e) {
		logger.error("cannot handling message from client<" + this + ">: " + message);
	}
}

@OnError
public void onError(Session session, Throwable e) {
	logger.error("exception when handing websocket message between client<" + this + ">", e);
}

public String getAccessKey() {
	return accessKey;
}

private TunnelClientConnMaintainService getMaintainService() {
	return ServiceFactory.getInstance().getTunnelClientConnMaintainService();
}

@Override
public String toString() {
	return customCode + "@" + estabishTime;
}
}
