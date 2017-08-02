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

package com.quest.forge.rest.tunnel.common;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Tell Gson engine how to serialize and deserialize {@link SimplePayload}
 * object.
 * 
 * @author bliu
 * 
 */
public class PayloadClassAdapter implements JsonDeserializer<SimplePayload>,
		JsonSerializer<SimplePayload> {

@Override
public SimplePayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		throws JsonParseException {
	JsonObject jsonObject = json.getAsJsonObject();
	String type = jsonObject.get("messageType").getAsString();
	try {
		if ("RESOURCE_REQUEST".equals(type)) {
			return context.deserialize(json,
					Class.forName("com.quest.forge.rest.tunnel.common.ResourceRequest"));
		}
		else if ("RESOURCE_RESPONSE".equals(type)) {
			return context.deserialize(json,
					Class.forName("com.quest.forge.rest.tunnel.common.ResourceResponse"));
		}
		else {
			return context.deserialize(json,
					Class.forName("com.quest.forge.rest.tunnel.common.SimplePayload"));
		}
	}
	catch (ClassNotFoundException cnfe) {
		throw new JsonParseException("Unknown element type: " + typeOfT, cnfe);
	}
}

@Override
public JsonElement serialize(SimplePayload payload, Type typeOfT, JsonSerializationContext context) {
	if (payload instanceof ResourceRequest) {
		return context.serialize(payload, ResourceRequest.class);
	}
	else if (payload instanceof ResourceResponse) {
		return context.serialize(payload, ResourceResponse.class);
	}
	else {
		return context.serialize(payload, SimplePayload.class);
	}
}

}
