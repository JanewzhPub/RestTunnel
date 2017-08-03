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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Keystore helper.
 * 
 * @author bliu
 * 
 */
public class KeystoreUtil {

private final static Log logger = LogFactory.getLog(KeystoreUtil.class.getName());

private final static String ALIAS = "FoglightRestTunnel";

public static void generateKeyPair(final String keystorePath, final String pwd) throws Exception {
	nativeCall("keytool", "-genkey", "-alias", ALIAS, "-keysize", "1024", "-validity", "36500",
			"-keyalg", "RSA", "-dname", "cn=FoglightRestTunnel, ou=Foglight, o=Quest", "-keypass",
			pwd, "-storepass", pwd, "-keystore", keystorePath);

}

public static KeyPair getKeyPair(final InputStream keystoreIS, final char[] pwd) throws Exception {
	KeyStore keystore = KeyStore.getInstance("jks");
	keystore.load(keystoreIS, pwd);
	Certificate cert = keystore.getCertificate(ALIAS);
	PublicKey publicKey = cert.getPublicKey();
	PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) keystore.getEntry(ALIAS,
			new KeyStore.PasswordProtection(pwd));
	return new KeyPair(publicKey, privateKeyEntry.getPrivateKey());
}

private static String nativeCall(final String... commands) {
	logger.info("Running " + Arrays.asList(commands));
	final ProcessBuilder pb = new ProcessBuilder(commands);
	try {
		final Process process = pb.start();
		final InputStream is = process.getInputStream();
		final String data = IOUtils.toString(is);
		logger.info("Completed native call: " + Arrays.asList(commands) + "\nResponse: " + data);
		return data;
	}
	catch (final IOException e) {
		logger.error("Error running commands: " + Arrays.asList(commands), e);
		return "";
	}
}

public static byte[] sha1(byte[] content) {
	try {
		MessageDigest m = MessageDigest.getInstance("SHA1");
		m.reset();
		m.update(content);
		return m.digest();
	}
	catch (NoSuchAlgorithmException e) {
		throw new RuntimeException(e);
	}
}
}
