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
package org.eclipse.kapua.client.gateway.spi.util;

public final class Strings {

    private Strings() {
    }

    public static String nonEmptyText(final String string, final String fieldName) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException(String.format("'%s' must not be null or empty", fieldName));
        }
        return string;
    }

}
