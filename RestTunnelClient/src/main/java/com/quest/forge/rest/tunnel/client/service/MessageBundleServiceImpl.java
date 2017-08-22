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
package com.quest.forge.rest.tunnel.client.service;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

/**
 * Message bundle read from Messsages.properties/Messages_<lang>.properties
 * @author jwang7
 *
 */
@Service("MessageBundleService")
public class MessageBundleServiceImpl implements MessageBundleService {

	private ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	
	public String getMessage(String messageKey) {
		return bundle.getString(messageKey);
	}
	
	public String getMessage(String messageKey, Object ... params) {
		try {
            return MessageFormat.format(bundle.getString(messageKey), params);
        } catch (MissingResourceException e) {
            return '!' + messageKey + '!';
        }
	}
}
