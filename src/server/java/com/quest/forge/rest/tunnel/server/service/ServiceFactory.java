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

package com.quest.forge.rest.tunnel.server.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.quest.forge.rest.tunnel.server.service.impl.AccessKeyManagerImpl;
import com.quest.forge.rest.tunnel.server.service.impl.TunnelClientConnMaintainServiceImpl;

/**
 * Service factory
 * 
 * @author bliu
 * 
 */
public class ServiceFactory {

private static ServiceFactory instance = new ServiceFactory();

private TunnelClientConnMaintainService tunnelClientConnMaintainService = new TunnelClientConnMaintainServiceImpl();

private AccessKeyManager accessKeyManager = new AccessKeyManagerImpl();

public static ServiceFactory getInstance() {
	return instance;
}

@SuppressWarnings("unchecked")
private static <T> T getDelegateProxy(Class<T> intf, final T delegate) {
	return (T) Proxy.newProxyInstance(delegate.getClass().getClassLoader(), new Class[] { intf },
			new RestServiceInvocationHandler<T>(delegate));
}

private static class RestServiceInvocationHandler<T> implements InvocationHandler {
private final T delegate;

public RestServiceInvocationHandler(T delegate) {
	this.delegate = delegate;
}

@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	return method.invoke(delegate, args);
}
}

public TunnelClientConnMaintainService getTunnelClientConnMaintainService() {
	return getDelegateProxy(TunnelClientConnMaintainService.class, tunnelClientConnMaintainService);
}

public AccessKeyManager getAccessKeyManager() {
	return getDelegateProxy(AccessKeyManager.class, accessKeyManager);
}

}
