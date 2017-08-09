package com.quest.forge.rest.tunnel.server.util;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {

	public static ServletResponse getResponse(ServletResponse response) {
		if (response instanceof HttpServletResponse) {
			((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
			((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
			((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, token");
		}
		return response;
	}
	
	public static HttpServletResponse getResponse(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, token");
		return response;
	}
	
}
