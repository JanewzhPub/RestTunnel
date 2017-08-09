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
import static com.quest.forge.rest.tunnel.common.Constants.HTTP_HEADER_CUSTOM_CODE;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quest.forge.rest.tunnel.server.service.ServiceFactory;
import com.quest.forge.rest.tunnel.server.util.KeystoreUtil;
import com.quest.forge.rest.tunnel.server.util.ResponseUtil;

/**
 * Check whther it is valid websocket client, reject it if the Custom-Code and
 * Access-Key don't match.
 * 
 * @author bliu
 * 
 */
public class WebsocketHandshakeFilter implements Filter {

private final static Log logger = LogFactory.getLog(WebsocketHandshakeFilter.class.getName());

@Override
public void destroy() {}

@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
	if (request instanceof HttpServletRequest) {
		String accessKey = ((HttpServletRequest) request).getHeader(HTTP_HEADER_ACCESS_KEY);
		String customCode = ((HttpServletRequest) request).getHeader(HTTP_HEADER_CUSTOM_CODE);
		/*
		 * TODO: handle the case there is already an active connection for the
		 * same customer, it may due to different FMS input the same information
		 * or some retry logic leading to this issue.
		 */
		response = ResponseUtil.getResponse(response);
		if (isValid(customCode, accessKey)) {
			chain.doFilter(request, response);
		}
		else {
			((HttpServletResponse) response).sendError(403);
		}
	}
}

private boolean isValid(String customCode, String accessKey) {
	if (customCode == null || accessKey == null) {
		return false;
	}
	try {
		return ServiceFactory.getInstance().getAccessKeyManager()
				.verify(customCode.getBytes(), Base64.getDecoder().decode(accessKey));
	}
	catch (Exception e) {
		logger.warn("fail to verify custom code<" + customCode + "> with access key<" + accessKey
				+ ">", e);
		return false;
	}
}

@Override
public void init(FilterConfig conf) throws ServletException {
	String keystoreFileName = conf.getInitParameter("KEYSTORE");
	String keystorePwd = conf.getInitParameter("KEYSTORE_PWD");
	InputStream is = null;
	try {
		String path = conf.getServletContext().getRealPath("/");
		is = new FileInputStream(path + "/WEB-INF/classes/" + keystoreFileName);
		ServiceFactory.getInstance().getAccessKeyManager()
				.init(KeystoreUtil.getKeyPair(is, keystorePwd.toCharArray()));
	}
	catch (Exception e) {
		logger.error("fail to access key manager", e);
		throw new ServletException(e);
	}
	finally {
		IOUtils.closeQuietly(is);
	}
}
}
