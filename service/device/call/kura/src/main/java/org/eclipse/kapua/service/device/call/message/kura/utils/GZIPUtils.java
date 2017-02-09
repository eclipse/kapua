/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

/**
 * Gzip utilities.
 *
 * @since 1.0
 *
 */
public class GZIPUtils {

    private GZIPUtils() {
    }

    /**
     * Check if the byte array represents compressed data
     *
     * @param bytes
     * @return
     */
    public static boolean isCompressed(byte[] bytes) {
        if (bytes == null || bytes.length < 2) {
            return false;
        } else {
            return bytes[0] == (byte) GZIPInputStream.GZIP_MAGIC && bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8);
        }
    }

    /**
     * Compress provided data with GZIP
     *
     * @param source
     *            the input data to compress
     * @return
     *         the compressed output data, returns {@code null} if the input was {@code null}
     * @throws IOException
     *             in case of an I/O error
     */
    public static byte[] compress(byte[] source) throws IOException {
        if (source == null) {
            return null;
        }

        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        try (final OutputStream out = new GZIPOutputStream(result)) {
            ByteSource.wrap(source).copyTo(out);
        }
        return result.toByteArray();
    }

    /**
     * Uncompress GZIP compressed data
     *
     * @param source
     *            the data to uncompress
     * @return the uncompressed data, returns {@code null} if the input was {@code null}
     * @throws IOException
     *             in case of an I/O error
     */
    public static byte[] decompress(byte[] source) throws IOException {
        if (source == null) {
            return null;
        }

        return ByteStreams.toByteArray(new GZIPInputStream(new ByteArrayInputStream(source)));
    }
}
