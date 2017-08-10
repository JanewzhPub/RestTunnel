package com.quest.forge.rest.tunnel.server.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.forge.rest.tunnel.server.pojo.AbstractResponseData;
import com.quest.forge.rest.tunnel.server.pojo.CommonResponse;

public class ResponseUtil {

	private final static Log logger = LogFactory.getLog(ResponseUtil.class.getName());
	
	public static <T extends CommonResponse<? extends AbstractResponseData>> String parseResponse(T response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		return null;
	}
}
