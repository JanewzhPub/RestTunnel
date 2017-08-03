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

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quest.forge.rest.tunnel.common.SimplePayload.MessageType;

/**
 * Schema for messages exchanged between tunnel server and tunnel client.
 * 
 * @author bliu
 * 
 */
public class ExchangeMessage {

private int schemaVersion = 1;

private SimplePayload data;

private String messageId;

public static final Gson gson = new GsonBuilder().registerTypeAdapter(SimplePayload.class,
		new PayloadClassAdapter()).create();

public ExchangeMessage() {
	messageId = UUID.randomUUID().toString();
}

public ExchangeMessage(ExchangeMessage request) {
	messageId = request.getMessageId();
}

public String getMessageId() {
	return messageId;
}

public SimplePayload getData() {
	return data;
}

public void setData(SimplePayload data) {
	this.data = data;
}

public int getSchemaVersion() {
	return schemaVersion;
}

public void setSchemaVersion(int schemaVersion) {
	this.schemaVersion = schemaVersion;
}

public boolean retrieveIsResourceRequest() {
	return getData() != null && getData().getMessageType() == MessageType.RESOURCE_REQUEST;
}

public boolean retrieveIsResourceResponse() {
	return getData() != null && getData().getMessageType() == MessageType.RESOURCE_RESPONSE;
}

@Override
public String toString() {
	return gson.toJson(this);
}
}
