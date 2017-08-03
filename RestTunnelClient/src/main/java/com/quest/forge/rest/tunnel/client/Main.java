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

package com.quest.forge.rest.tunnel.client;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Tunnel client main
 * 
 * @author bliu
 * 
 */
// TODO: only SSL connections is allow for security concerns.
public class Main {

public static void main(String[] args) throws Exception {
	if (args == null || args.length != 4) {
		System.out
				.println("--Usage: \n java com.quest.forge.rest.tunnel.client.Main tunnelServerWebsocketUri restServerBaseUri customCode tunnelServerAccessKey");
		return;
	}

	final String tunnelServerWebsocketUri = args[0];
	final String restServerBaseUri = args[1];
	final String customCode = args[2];
	final String tunnelServerAccessKey = args[3];

	ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
	TunnelClient client = new TunnelClient(tunnelServerWebsocketUri, restServerBaseUri, customCode,
			tunnelServerAccessKey);
	client.start();
	new TunnelWebsocketWatcher(executor).startWatch(client);
}

}
