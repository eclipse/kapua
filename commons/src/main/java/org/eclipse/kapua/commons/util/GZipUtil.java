/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public interface GZipUtil {

    public static boolean isCompressed(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length < 2) {
            return false;
        } else {
            return bytes[0] == (byte) GZIPInputStream.GZIP_MAGIC
                    && bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8);
        }
    }

    public static byte[] compress(byte[] source) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipos = null;
        try {
            gzipos = new GZIPOutputStream(baos);
            gzipos.write(source);
        } catch (IOException e) {
            throw e;
        } finally {
            if (gzipos != null) {
                try {
                    gzipos.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return baos.toByteArray();
    }

    public static byte[] decompress(byte[] source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(source);
        GZIPInputStream gzipis = null;

        try {
            gzipis = new GZIPInputStream(bais);

            int n;
            final int maxBuffer = 1024;
            byte[] buf = new byte[maxBuffer];
            while ((n = gzipis.read(buf, 0, maxBuffer)) != -1) {
                baos.write(buf, 0, n);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (gzipis != null) {
                try {
                    gzipis.close();
                } catch (IOException e) {
                    // Ignore
                }
            }

            try {
                baos.close();
            } catch (IOException e) {
                // Ignore
            }
        }

        return baos.toByteArray();
    }
}
