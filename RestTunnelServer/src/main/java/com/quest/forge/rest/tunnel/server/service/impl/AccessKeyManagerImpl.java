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

import java.io.FileInputStream;
import java.security.KeyPair;
import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quest.forge.rest.tunnel.server.service.AccessKeyManager;
import com.quest.forge.rest.tunnel.server.util.KeystoreUtil;
import com.quest.forge.rest.tunnel.server.util.RSAUtil;

/**
 * 
 * @author bliu
 * 
 */
public class AccessKeyManagerImpl implements AccessKeyManager {

private final static Log logger = LogFactory.getLog(AccessKeyManagerImpl.class.getName());

private RSAUtil rsa;

@Override
public String generateAccessKey(String customCode) {
	try {
		return Base64.getEncoder().encodeToString(rsa.signWithPrivateKey(customCode.getBytes()));
	}
	catch (Exception e) {
		logger.error("fail to generate access key for " + customCode, e);
		return null;
	}
}

@Override
public void init(KeyPair keyPair) {
	this.rsa = new RSAUtil(keyPair);
}

@Override
public boolean verify(byte[] data, byte[] signature) throws Exception {
	return rsa.verifyWithPublicKey(data, signature);
}

public static void main(String[] args) throws Exception {
	KeyPair pair = KeystoreUtil.getKeyPair(new FileInputStream(
			"src/main/resources/FoglightRestTunnel.jks"), "nitrogen".toCharArray());
	AccessKeyManagerImpl manager = new AccessKeyManagerImpl();
	manager.init(pair);
	String customCode = "Quest";
	String signature = manager.generateAccessKey("Quest");
	System.out.println("Custom Code: " + customCode);
	System.out.println("Tunnel Client Access Key: " + signature);
	System.out.println("Mobile Client Access Key: "
			+ Base64.getEncoder().encodeToString(sha1(signature.getBytes())));
}
}
