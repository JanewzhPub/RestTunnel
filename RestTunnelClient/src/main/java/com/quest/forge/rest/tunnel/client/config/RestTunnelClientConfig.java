package com.quest.forge.rest.tunnel.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
