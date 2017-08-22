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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quest.forge.rest.tunnel.client.config.RestTunnelClientConfig;

/**
 * Manage login through client to foglight using http/https
 * @author jwang7
 *
 */
@Service("LoginRequestService")
public class LoginRequestServiceImpl implements LoginRequestService {

	private static final Logger logger = LoggerFactory.getLogger(LoginRequestServiceImpl.class);
	
	@Autowired
	private MessageBundleService message;
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
			logger.error(message.getMessage("client.login.request.failed.debug", "status: " + response.getStatus()));
			return null;
		}
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(responseText);
		return obj.getAsJsonObject("data").get("access-token").getAsString();
	}

}
