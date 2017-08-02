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

package com.quest.forge.rest.tunnel.server.filter;

import static com.quest.forge.rest.tunnel.common.Constants.HTTP_HEADER_ACCESS_KEY;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quest.forge.rest.tunnel.server.service.ServiceFactory;

/**
 * Check the access key for those normal http requests
 * 
 * @author bliu
 *
 */
public class HttpRequestTokenFilter implements Filter {

@Override
public void destroy() {}

@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

	if (request instanceof HttpServletRequest) {
		/*
		 * Won't do anything for the /tunnel websocket, leave it to
		 * WebsocketHandshakeFilter have the checking
		 */
		if (((HttpServletRequest) request).getRequestURI().endsWith("/tunnel")) {
			chain.doFilter(request, response);
			return;
		}
		String customerAccessKeySHA1Hash = ((HttpServletRequest) request)
				.getHeader(HTTP_HEADER_ACCESS_KEY);
		/*
		 * Find the websocket connection
		 */
		if (customerAccessKeySHA1Hash != null) {
			// check the FMS is connected
			if (checkTunnelClient(customerAccessKeySHA1Hash)) {
				chain.doFilter(request, response);
			}
			else {
				response.setContentType("application/json");
				response.getWriter().write("{\"status\":0,\"code\":\"foglightNotConnected\"}");
			}
		}
		else {
			((HttpServletResponse) response).sendError(403);
		}
	}
}

private boolean checkTunnelClient(String accessKeySHA1Hash) {
	return ServiceFactory.getInstance().getTunnelClientConnMaintainService()
			.getClient(accessKeySHA1Hash) != null;

}

@Override
public void init(FilterConfig conf) throws ServletException {}

}
