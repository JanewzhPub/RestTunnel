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

package com.quest.forge.rest.tunnel.server.util;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

/**
 * Use RSA keypair to sign and verify contents.
 * 
 * @author bliu
 * 
 */
public class RSAUtil {

private final PrivateKey privateKey;

private final PublicKey publicKey;

public RSAUtil(KeyPair keyPair) {
	this.privateKey = keyPair.getPrivate();
	this.publicKey = keyPair.getPublic();
}

public byte[] decrypt(byte[] data) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.DECRYPT_MODE, privateKey);
	return cipher.doFinal(data);
}

public byte[] encryptWithPublicKey(byte[] data) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	return cipher.doFinal(data);
}

public byte[] signWithPrivateKey(byte[] data) throws Exception {
	Signature signature = Signature.getInstance("SHA1withRSA");
	signature.initSign(privateKey);
	signature.update(data);
	return signature.sign();
}

public boolean verifyWithPublicKey(byte[] data, byte[] signature) throws Exception {
	Signature sig = Signature.getInstance("SHA1withRSA");
	sig.initVerify(publicKey);
	sig.update(data);
	return sig.verify(signature);
}
}
