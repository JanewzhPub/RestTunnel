package com.quest.forge.rest.tunnel.client.controller;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class AbstractController {

	private ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	
	protected String getMessage(String messageKey) {
		return bundle.getString(messageKey);
	}
	
	protected String getMessage(String messageKey, Object ... params) {
		try {
            return MessageFormat.format(bundle.getString(messageKey), params);
        } catch (MissingResourceException e) {
            return '!' + messageKey + '!';
        }
	}
}
