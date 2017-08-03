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
 * Rest resource request content
 * 
 * @author bliu
 * 
 */
public class ResourceRequest extends SimplePayload {

private String resource;

private String httpMethod;

private String authToken;

public ResourceRequest(String resource, String httpMethod, String authToken, String data) {
	super(MessageType.RESOURCE_REQUEST, data);
	this.resource = resource;
	this.httpMethod = httpMethod;
	this.authToken = authToken;
}

public ResourceRequest(String resource) {
	super(MessageType.RESOURCE_REQUEST);
	this.resource = resource;
}

public ResourceRequest(String resource, String method) {
	this(resource);
	this.httpMethod = method;
}

public ResourceRequest() {
	super(MessageType.RESOURCE_REQUEST);
}

public void setResource(String resource) {
	this.resource = resource;
}

public void setHttpMethod(String httpMethod) {
	this.httpMethod = httpMethod;
}

public void setAuthToken(String authToken) {
	this.authToken = authToken;
}

public String getResource() {
	return resource;
}

public String getHttpMethod() {
	return httpMethod;
}

public String getAuthToken() {
	return authToken;
}

}
