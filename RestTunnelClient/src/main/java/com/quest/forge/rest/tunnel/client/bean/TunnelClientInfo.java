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
package com.quest.forge.rest.tunnel.client.bean;

/**
 * 
 * @author jwang7
 *
 */
public class TunnelClientInfo {

	private String customCode;
	private String connectionToken;
	private String accessKey;
	private String authToken;
	private String foglightUrl;
	
	public TunnelClientInfo () {
		this (null, null, null, null, null);
	}
	
	public TunnelClientInfo (String customCode, String connectionToken, String accessKey, String foglightUrl, String authToken) {
		this.customCode = customCode;
		this.connectionToken = connectionToken;
		this.accessKey = accessKey;
		this.foglightUrl = foglightUrl;
		this.authToken = authToken;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getFoglightUrl() {
		return foglightUrl;
	}

	public void setFoglightUrl(String foglightUrl) {
		this.foglightUrl = foglightUrl;
	}

	public String getConnectionToken() {
		return connectionToken;
	}

	public void setConnectionToken(String connectionToken) {
		this.connectionToken = connectionToken;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
}
