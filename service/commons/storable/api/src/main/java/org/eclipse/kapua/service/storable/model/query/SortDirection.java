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
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query;

import org.eclipse.kapua.KapuaIllegalArgumentException;

/**
 * {@link SortDirection} definition.
 * <p>
 * It define which sorting direction to use when processing {@link StorableQuery}.
 *
 * @since 1.0.0
 */
public enum SortDirection {

    /**
     * Ascending.
     *
     * @since 1.0.0
     */
    ASC,

    /**
     * Descending.
     *
     * @since 1.0.0
     */
    DESC;

    /**
     * It returns a {@link SortDirection} from the given {@link String}
     * <p>
     * It is case insensitive.
     *
     * @param value The {@link String} to convert.
     * @return The converted {@link SortDirection}.
     * @throws KapuaIllegalArgumentException If given {@link String} cannot be converted.
     * @since 1.0.0
     */
    public static SortDirection fromString(String value) throws KapuaIllegalArgumentException {
        String ucValue = value.toUpperCase();
        try {
            return valueOf(ucValue);
        } catch (Exception e) {
            throw new KapuaIllegalArgumentException("sortDirection", value);
        }
    }
}
