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

import static com.quest.forge.rest.tunnel.common.Constants.*;
import static com.quest.forge.rest.tunnel.server.util.KeystoreUtil.sha1;

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
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quest.forge.rest.tunnel.server.service.ServiceFactory;
import com.quest.forge.rest.tunnel.server.util.KeystoreUtil;
import com.quest.forge.rest.tunnel.server.util.ResponseUtil;

/**
 * Assign access key for custom code.
 * 
 * @author jwang7
 *
 */
public class RequestAccessKeyFilter implements Filter {

	private final static Log logger = LogFactory.getLog(RequestAccessKeyFilter.class.getName());

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			String customCode = ((HttpServletRequest) request).getHeader(HTTP_HEADER_CUSTOM_CODE);
			String method = ((HttpServletRequest) request).getMethod();
			response = ResponseUtil.getResponse(response);
			response.setContentType("application/json");
			if (method != null && "GET".equalsIgnoreCase(method)) {
				if (customCode != null) {
					try {
						String accessKey = Base64.getEncoder().encodeToString(sha1(ServiceFactory.getInstance()
								.getAccessKeyManager().generateAccessKey(customCode).getBytes()));
						response.getWriter().write("{\"status\":1,\"data\":{\"accessKey\":\"" + accessKey + "\"}}");
					} catch (Exception e) {
						response.getWriter()
								.write("{\"status\":0,\"code\":\"findKeyPairFailed\"}");
					}
				} else {
					response.getWriter().write("{\"status\":0,\"code\":\"customCodeNotFound\"}");
				}
			} else {
				response.getWriter().write("{\"status\":0,\"code\":\"invalidMethodFound\"}");
			}
		}
	}

	@Override
	public void init(FilterConfig conf) throws ServletException {
		InputStream is = null;
		try {
			String path = conf.getServletContext().getRealPath("/");
			is = new FileInputStream(path + "/WEB-INF/classes/" + conf.getInitParameter("KEYSTORE"));
			ServiceFactory.getInstance().getAccessKeyManager()
					.init(KeystoreUtil.getKeyPair(is, conf.getInitParameter("KEYSTORE_PWD").toCharArray()));
		} catch (Exception e) {
			logger.error("fail to access key manager", e);
			throw new ServletException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
