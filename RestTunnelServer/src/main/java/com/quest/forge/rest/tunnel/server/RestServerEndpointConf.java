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

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Endpoint configuration, store the Custom-Code and Access-Key which are
 * available when initializing the websocket connection.
 * 
 * @author bliu
 * 
 */
public class RestServerEndpointConf extends ServerEndpointConfig.Configurator {

public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request,
		HandshakeResponse response) {
	super.modifyHandshake(config, request, response);
	// the access key should be represent in the header, as
	// WebsocketHandshakeFilter will have a validation before handshake
	config.getUserProperties().put(HTTP_HEADER_ACCESS_KEY,
			request.getHeaders().get(HTTP_HEADER_ACCESS_KEY).get(0));
	config.getUserProperties().put(HTTP_HEADER_CUSTOM_CODE,
			request.getHeaders().get(HTTP_HEADER_CUSTOM_CODE).get(0));
}
}
