/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model.query;

import org.eclipse.kapua.KapuaIllegalArgumentException;

/**
 * Sort orderdefinition.
 * <p>
 * It define which sorting direction to use when processing {@link KapuaQuery}.
 *
 * @since 1.0.0
 */
public enum SortOrder {

    /**
     * Ascending.
     *
     * @since 1.0.0
     */
    ASCENDING,

    /**
     * Descending.
     *
     * @since 1.0.0
     */
    DESCENDING;

    /**
     * It returns a {@link SortOrder} from the given {@link String}
     * <p>
     * It is case insensitive.
     *
     * @param value The {@link String} to convert.
     * @return The converted {@link SortOrder}.
     * @throws KapuaIllegalArgumentException If given {@link String} cannot be converted.
     * @since 1.0.0
     */
    public static SortOrder fromString(String value) throws KapuaIllegalArgumentException {
        String ucValue = value.toUpperCase();
        try {
            return valueOf(ucValue);
        } catch (Exception e) {
            throw new KapuaIllegalArgumentException("sortOrder", value);
        }
    }
}
