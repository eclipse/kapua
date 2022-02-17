/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
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
package org.eclipse.kapua.kura.simulator.util;

import com.google.common.io.BaseEncoding;

public final class Hex {

    private Hex() {
    }

    /**
     * Render a byte array as hex string
     *
     * @param payload
     *            the data to render
     * @param maxLength
     *            the maximum number of byes to render
     * @return the string
     */
    public static String toHex(final byte[] payload, final int maxLength) {
        return BaseEncoding.base16().encode(payload, 0, payload.length > maxLength ? maxLength : payload.length)
                + (payload.length > maxLength ? "..." : "");
    }
}
