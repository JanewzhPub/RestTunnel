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
package com.quest.forge.rest.tunnel.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Read and maintain configuration from client.properties
 * @author jwang7
 *
 */
@Configuration
@PropertySource("classpath:client.properties")
public class RestTunnelClientConfig {
	
	@Value("${client.websocket.protocol}")
	private String websocketProtocol;
	
	@Value("${client.server.url}")
	private String serverUrl;
	
	@Value("${client.server.tunnel.path}")
	private String serverTunnelPath;
	
	@Value("${client.foglight.connection.protocol}")
	private String foglightConnectionProtocol;
	
	@Value("${client.foglight.connection.path}")
	private String foglightConnectionPath;
	
	@Value("${mobile.web.protocol}")
	private String mobileWebProtocol;
	
	@Value("${mobile.web.url}")
	private String mobileWebUrl;
	
	@Value("${mobile.web.path}")
	private String mobileWebPath;

	public String getWebsocketProtocol() {
		return websocketProtocol;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getServerTunnelPath() {
		return serverTunnelPath;
	}

	public String getFoglightConnectionProtocol() {
		return foglightConnectionProtocol;
	}

	public String getFoglightConnectionPath() {
		return foglightConnectionPath;
	}

	public String getMobileWebProtocol() {
		return mobileWebProtocol;
	}

	public String getMobileWebUrl() {
		return mobileWebUrl;
	}

	public String getMobileWebPath() {
		return mobileWebPath;
	}	
}
