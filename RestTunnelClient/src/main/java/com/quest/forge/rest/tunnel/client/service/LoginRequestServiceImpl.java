package com.quest.forge.rest.tunnel.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LoginRequestService")
public class LoginRequestServiceImpl implements LoginRequestService {

	@Autowired
	private TunnelClientService tunnelClientService;
	
	@Override
	public String requestAccessToken(String customCode, String authToken) {
		String foglightUrl = tunnelClientService.getFoglightUrl(customCode);
		//TODO implement it.
		return null;
	}

}
