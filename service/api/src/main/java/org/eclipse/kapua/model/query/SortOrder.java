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
 * Sort order
 */
public enum SortOrder {
    /**
     * Ascending
     */
    ASCENDING,
    /**
     * Descending
     */
    DESCENDING;

    public static SortOrder fromString(String value) throws KapuaIllegalArgumentException {
        String ucValue = value.toUpperCase();
        try {
            return valueOf(ucValue);
        } catch (Exception e) {
            throw new KapuaIllegalArgumentException("sortOrder", value);
        }
    }
}
