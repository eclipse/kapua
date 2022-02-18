/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway;

import java.nio.ByteBuffer;

/**
 * A codec for BLOB based payload transports
 */
public interface BinaryPayloadCodec {

    /**
     * Encode a {@link Payload} structure into a BLOB
     * <p>
     * The payload information gets encoded into a blob and appended at the end of the
     * provided buffer. If the provided buffer is {@code null} a new buffer will be allocated.
     * </p>
     * <p>
     * <b>Note:</b> The returning buffer may not be the same as the provided buffer. If the
     * provided buffer has less remaining space than required a new buffer is allocated and
     * returned, which will contain both the existing content as well as the newly appended.
     * </p>
     *
     * @param payload
     *            The payload to encode, must not be {@code null}
     * @param buffer
     *            An optional buffer to append the output to, may be {@code null}
     * @return A buffer with the appended payload output, must never be {@code null}
     * @throws Exception
     *             if anything goes wrong
     */
    public ByteBuffer encode(Payload payload, ByteBuffer buffer) throws Exception;

    /**
     * Decode a {@link Payload} structure from the provided BLOB
     *
     * @param buffer
     *            The buffer to read from, must not be {@code null}
     * @return the decoded payload structure, may be {@code null}
     * @throws Exception
     *             if anything goes wrong
     */
    public Payload decode(ByteBuffer buffer) throws Exception;
}
