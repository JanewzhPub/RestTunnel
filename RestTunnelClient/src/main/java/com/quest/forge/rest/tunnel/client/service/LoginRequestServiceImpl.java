package com.quest.forge.rest.tunnel.client.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quest.forge.rest.tunnel.client.config.RestTunnelClientConfig;

@Service("LoginRequestService")
public class LoginRequestServiceImpl implements LoginRequestService {

	@Autowired
	private RestTunnelClientConfig clientConfig;
	
	@Override
	public String requestAccessToken(String foglightUrl, String authToken) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client
				.target(clientConfig.getFoglightConnectionProtocol() + "://" + foglightUrl + clientConfig.getFoglightConnectionPath())
				.path("security/login");
		Form form = new Form();
		form.param("authToken", new String(authToken));
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		String responseText = response.readEntity(String.class);if (response.getStatus() != 200) {
			//TODO handle failed request
		}
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(responseText);
		return obj.getAsJsonObject("data").get("access-token").getAsString();
	}

}
