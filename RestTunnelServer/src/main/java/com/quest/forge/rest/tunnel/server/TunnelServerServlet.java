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
import static com.quest.forge.rest.tunnel.common.Constants.HTTP_HEADER_AUTH_TOKEN;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quest.forge.rest.tunnel.common.ExchangeMessage;
import com.quest.forge.rest.tunnel.common.ResourceRequest;
import com.quest.forge.rest.tunnel.common.ResourceResponse;
import com.quest.forge.rest.tunnel.server.service.ServiceFactory;

/**
 * Consumed all http request from client, transform it to message to
 * corresponding tunnel client to consume the real rest service and then
 * response the http request using the result from tunnel client.
 * 
 * @author bliu
 *
 */
public class TunnelServerServlet extends HttpServlet {

private final static long serialVersionUID = 8093892394757892213L;

private final static Log logger = LogFactory.getLog(TunnelServerServlet.class.getName());

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
		IOException {
	service(req, resp);
}

// TODO: need carefully handling the response of charset encoding
@Override
protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	try {
		String token = req.getHeader(HTTP_HEADER_AUTH_TOKEN);
		String customerAccessKeySHA1Hash = req.getHeader(HTTP_HEADER_ACCESS_KEY);
		TunnelWebsocket ws = ServiceFactory.getInstance().getTunnelClientConnMaintainService()
				.getClient(customerAccessKeySHA1Hash);
		if (ws == null) {
			/*
			 * although there is already a check in the filter, it is still
			 * possible the ws disconnected during the time between filter check
			 * and here.
			 */
			resp.setContentType("application/json");
			resp.getWriter().write(getFailureResponse("foglightNotConnected"));
			return;
		}
		// too many pending requests
		if (ws.isReachMaxConcurrentRequest()) {
			logger.warn("Too many pending requests, reject request handling.");
			resp.setContentType("application/json");
			resp.getWriter().write(getFailureResponse("tooManyPendingRequests"));
			return;
		}
		String resource = req.getPathInfo();
		String queryStr = req.getQueryString();
		if (queryStr != null && !queryStr.isEmpty()) {
			resource += "?" + queryStr;
		}
		ResourceRequest requestPayload = new ResourceRequest(resource);
		requestPayload.setAuthToken(token);
		requestPayload.setHttpMethod(req.getMethod());
		String data = getRequestRawData(req);
		if (data != null && !data.trim().isEmpty()) {
			requestPayload.setData(data);
		}
		ExchangeMessage reqMsg = new ExchangeMessage();
		reqMsg.setData(requestPayload);
		ExchangeMessage respMsg = ws.sendAndWaitForResponse(reqMsg);
		if (respMsg == null) {
			resp.setContentType("application/json");
			resp.getWriter().write(getFailureResponse("timeout"));
		}
		else {
			ResourceResponse resouceResp = (ResourceResponse) respMsg.getData();
			resp.setStatus(resouceResp.getResponseCode());
			resp.setContentType(resouceResp.getContentType());
			resp.getWriter().write(resouceResp.getData());
		}
	}
	catch (Exception e) {
		logger.error("fail to handle request", e);
		resp.setContentType("application/json");
		resp.getWriter().write(getFailureResponse("tunnelServerError"));
	}
}

private String getFailureResponse(String errorCode) {
	return "{\"status\":0,\"code\":\"" + errorCode + "\"}";
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
		IOException {
	service(req, resp);
}

@Override
protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
		IOException {
	service(req, resp);
}

@Override
protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
		IOException {
	service(req, resp);
}

private String getRequestRawData(HttpServletRequest req) throws IOException {
	InputStream body = req.getInputStream();
	// TODO: please try to limit the max size from input stream
	byte[] content = IOUtils.toByteArray(body);
	return new String(content, "utf-8");
}
}
