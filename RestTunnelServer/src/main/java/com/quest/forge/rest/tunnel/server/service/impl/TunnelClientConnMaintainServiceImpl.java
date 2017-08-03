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

package com.quest.forge.rest.tunnel.server.service.impl;

import static com.quest.forge.rest.tunnel.server.util.KeystoreUtil.sha1;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.quest.forge.rest.tunnel.server.TunnelWebsocket;
import com.quest.forge.rest.tunnel.server.service.TunnelClientConnMaintainService;

/**
 * 
 * @author bliu
 * 
 */
public class TunnelClientConnMaintainServiceImpl implements TunnelClientConnMaintainService {

// key is the accessKey SHA1 hash
private static Map<String, TunnelWebsocket> tunnelClients = new HashMap<String, TunnelWebsocket>();

@Override
public void clientConnected(TunnelWebsocket connection) {
	tunnelClients.put(sha1AndBase64(connection.getAccessKey()), connection);
}

@Override
public void clientDisconnected(TunnelWebsocket tunnelWebsocket) {
	tunnelClients.remove(sha1AndBase64(tunnelWebsocket.getAccessKey()));
}

private String sha1AndBase64(String str) {
	return Base64.getEncoder().encodeToString(sha1(str.getBytes()));
}

@Override
public TunnelWebsocket getClient(String customerAccessKeySHA1Hash) {
	return tunnelClients.get(customerAccessKeySHA1Hash);
}

}
