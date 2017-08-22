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

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Watch the websocket connection with 15 seconds interval, retry to connect
 * websocket if it is disconnected.
 * 
 * @author bliu
 * 
 */
public class TunnelWebsocketWatcher {

private final static Logger logger = LoggerFactory.getLogger(TunnelWebsocketWatcher.class);

private final ScheduledThreadPoolExecutor executor;

private Future<?> watcher;

public TunnelWebsocketWatcher(ScheduledThreadPoolExecutor executor) {
	this.executor = executor;
}

public void startWatch(final TunnelClient client) {
	watcher = executor.scheduleAtFixedRate(new Runnable() {
		@Override
		public void run() {
			if (!client.isConnected()) {
				logger.warn("websocket is disconnected, try to start it");
				try {
					client.stop();
					client.start();
				}
				catch (Exception e) {
					logger.error("failed to restart tunnel client", e);
				}
			}
			else {
				logger.debug("websocket state is fine, still connected.");
			}
		}
	}, 15, 15, TimeUnit.SECONDS);
}

public void stop() {
	if (watcher != null && !watcher.isCancelled()) {
		watcher.cancel(true);
	}
	watcher = null;
}
}
