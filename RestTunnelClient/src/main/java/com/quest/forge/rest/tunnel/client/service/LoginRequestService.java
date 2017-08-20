package com.quest.forge.rest.tunnel.client.service;

public interface LoginRequestService {

	String requestAccessToken(String customCode, String authToken);
}
