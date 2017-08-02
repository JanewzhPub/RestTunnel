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
 * Rest resource response content.
 * 
 * @author bliu
 * 
 */
public class ResourceResponse extends SimplePayload {

private int responseCode;

private String contentType;

public ResourceResponse() {
	super(MessageType.RESOURCE_RESPONSE);
}

public ResourceResponse(int responseCode, String contentType, String responseBody) {
	super(MessageType.RESOURCE_RESPONSE, responseBody);
	this.responseCode = responseCode;
	this.contentType = contentType;
}

public int getResponseCode() {
	return responseCode;
}

public void setResponseCode(int responseCode) {
	this.responseCode = responseCode;
}

public String getContentType() {
	return contentType;
}

public void setContentType(String contentType) {
	this.contentType = contentType;
}

}
