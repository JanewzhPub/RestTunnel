package com.quest.forge.rest.tunnel.server.util;

import org.junit.Test;
import com.quest.forge.rest.tunnel.server.pojo.CommonResponse;
import com.quest.forge.rest.tunnel.server.pojo.MTokenResponseData;
import com.quest.forge.rest.tunnel.server.pojo.NormalResponse;
import com.quest.forge.rest.tunnel.server.pojo.TTokenResponseData;

import junit.framework.Assert;

public class TestResponseUtil {

	@Test
	public void testCommonResponse() {
		Assert.assertEquals(
				"{\"status\":0,\"code\":\"authFailed\"}", 
				ResponseUtil.parseResponse(new CommonResponse(0, "authFailed")));
	}
	
	@Test
	public void testMTokenResponse() {
		Assert.assertEquals(
				"{\"status\":1,\"data\":{\"mtoken\":\"fjidjfioeredf=938kkofd*fkdo\"}}", 
				ResponseUtil.parseResponse(
						new NormalResponse(
								new MTokenResponseData("fjidjfioeredf=938kkofd*fkdo"))));
	}
	
	@Test
	public void testTTokenResponse() {
		Assert.assertEquals(
				"{\"status\":1,\"data\":{\"ttoken\":\"fjidjfioeredf=938kkofd*fkdo\"}}", 
				ResponseUtil.parseResponse(
						new NormalResponse(
								new TTokenResponseData("fjidjfioeredf=938kkofd*fkdo"))));
	}
}
