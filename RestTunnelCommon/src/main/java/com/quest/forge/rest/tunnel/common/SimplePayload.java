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

/**
 * Payload of the {@link ExchangeMessage}
 * 
 * @author bliu
 * 
 */
public class SimplePayload {

private MessageType messageType;

private String data;

public SimplePayload() {}

public SimplePayload(MessageType messageType) {
	this.messageType = messageType;
}

public SimplePayload(MessageType messageType, String data) {
	this.messageType = messageType;
	this.data = data;
}

public String getData() {
	return data;
}

public void setData(String data) {
	this.data = data;
}

public MessageType getMessageType() {
	return messageType;
}

public void setMessageType(MessageType messageType) {
	this.messageType = messageType;
}

public static enum MessageType {
	RESOURCE_REQUEST, RESOURCE_RESPONSE
}
}
