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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quest.forge.rest.tunnel.common.ResourceRequest;
import com.quest.forge.rest.tunnel.common.ResourceResponse;

/**
 * Client that will consume rest services, and wrap the response of rest
 * services.
 * 
 * @author bliu
 *
 */
public class RestClient {

private final static Logger logger = LoggerFactory.getLogger(TunnelClient.class);

private final String baseURL;

public RestClient(String baseURL) {
	this.baseURL = baseURL;
}

public void testConnection() throws IOException {
	try {
		ResourceResponse response = request(new ResourceRequest("", "GET"));
		if (!isOK(response.getResponseCode()) || response.getData() == null) {
			throw new IllegalStateException("The base URL " + baseURL
					+ " is not accessable, response code: " + response.getResponseCode()
					+ ", responseBody:" + response.getData() + ", incorrect configuration?");
		}
		if (response.getContentType() != null
				&& !response.getContentType().toLowerCase().contains("json")) {
			throw new IllegalStateException("The base URL " + baseURL
					+ " is accessable, but expected a json response, but encountered "
					+ response.getContentType());
		}
		JsonElement root = new JsonParser().parse(response.getData());
		if (!(root instanceof JsonObject) || ((JsonObject) root).get("data") != null
				&& ((JsonObject) root).get("data").getAsJsonObject().get("restVersion") == null) {
			throw new IllegalStateException("The base URL " + baseURL
					+ " is accessable, but it doesn't look like a correct rest service.");
		}
	}
	catch (Exception e) {
		logger.warn("cannot verify rest server: " + baseURL + ", please check your configuration.");
		throw new IOException(e);
	}
}

private String parseRequestedUrl(String baseUrl, String requestedUrl) {
	if (requestedUrl.startsWith("/api/v1")) {
		return baseUrl + requestedUrl.replace("/api/v1", "");
	} else if (requestedUrl.startsWith("/v1")) {
		return baseUrl + requestedUrl.replace("/v1", "");
	}
	return baseUrl + requestedUrl;
}

public ResourceResponse request(ResourceRequest request) throws IOException {
	OutputStream out = null;
	InputStream is = null;
	try {
		URL url = new URL(parseRequestedUrl(baseURL, request.getResource()));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(request.getHttpMethod());
		con.setRequestProperty("Auth-Token", request.getAuthToken());
		con.setRequestProperty("Content-Type", request.getContentType());
		con.setUseCaches(false);
		if (request.getData() != null) {
			con.setDoOutput(true);
		}
		con.connect();
		if (request.getData() != null) {
			out = con.getOutputStream();
			out.write(request.getData().getBytes(StandardCharsets.UTF_8));
		}
		if (isOK(con.getResponseCode())) {
			is = con.getInputStream();
		}
		else {
			is = con.getErrorStream();
		}
		String responseBody = IOUtils.toString(is, StandardCharsets.UTF_8.name());
		logger.debug("requested resource " + url.toString() + ", and response body is: "
				+ responseBody);
		return new ResourceResponse(con.getResponseCode(), con.getContentType(), responseBody);
	}
	finally {
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(out);
	}
}

private boolean isOK(int httpCode) {
	return httpCode >= 200 && httpCode <= 299;
}

public String getBaseURL() {
	return this.baseURL;
}
}
