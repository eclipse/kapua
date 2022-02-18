/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.utils;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Gzip utilities.
 *
 * @since 1.0.0
 */
public class GZIPUtils {

    private GZIPUtils() {
    }

    /**
     * Check if the byte array represents compressed data.
     *
     * @param bytes The input data to check.
     * @return {@code true} if the data is compressed, {@code false} otherwise.
     * @since 1.0.0
     */
    public static boolean isCompressed(byte[] bytes) {
        if (bytes == null || bytes.length < 2) {
            return false;
        } else {
            return bytes[0] == (byte) GZIPInputStream.GZIP_MAGIC && bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8);
        }
    }

    /**
     * Compress provided data with GZIP.
     *
     * @param source the input data to compress.
     * @return the compressed output data, returns {@code null} if the input was {@code null}.
     * @throws IOException in case of an I/O error.
     * @since 1.0.0
     */
    public static byte[] compress(byte[] source) throws IOException {
        if (source == null) {
            return new byte[0];
        }

        try (
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                OutputStream out = new GZIPOutputStream(result)
        ) {
            ByteSource.wrap(source).copyTo(out);
            return result.toByteArray();
        }
    }

    /**
     * Uncompress GZIP compressed data.
     *
     * @param source the data to uncompress.
     * @return the uncompressed data, returns {@code null} if the input was {@code null}.
     * @throws IOException in case of an I/O error.
     * @since 1.0.0
     */
    public static byte[] decompress(byte[] source) throws IOException {
        if (source == null) {
            return new byte[0];
        }

        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(source);
                GZIPInputStream inputStream = new GZIPInputStream(byteArrayInputStream)
        ) {
            return ByteStreams.toByteArray(inputStream);
        }
    }
}
