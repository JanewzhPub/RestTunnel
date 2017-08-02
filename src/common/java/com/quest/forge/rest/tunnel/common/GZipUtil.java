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

package com.quest.forge.rest.tunnel.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * A helper used to compress and decompress string content and byte content.
 * 
 * @author bliu
 * 
 */
public class GZipUtil {

public static byte[] compress(final String text) throws IOException {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	GZIPOutputStream gzip = new GZIPOutputStream(out);
	gzip.write(text.getBytes("utf-8"));
	gzip.close();
	return out.toByteArray();
}

public static String decompress(final InputStream is) throws IOException {
	GZIPInputStream gs = new GZIPInputStream(is);
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	IOUtils.copy(gs, os);
	return new String(os.toByteArray(), "utf-8");
}

public static String decompress(final byte[] content, final int offset, final int length)
		throws IOException {
	GZIPInputStream gs = new GZIPInputStream(new ByteArrayInputStream(content, offset, length));
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	IOUtils.copy(gs, os);
	return new String(os.toByteArray(), "utf-8");
}
}
