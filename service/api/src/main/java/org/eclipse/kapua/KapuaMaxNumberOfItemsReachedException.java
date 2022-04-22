/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

import org.eclipse.kapua.model.KapuaEntity;

/**
 * @since 1.0.0
 */
public class KapuaMaxNumberOfItemsReachedException extends KapuaException {

    private static final long serialVersionUID = 8651132350411186861L;

    private final String entityType;

    /**
     * Constructor.
     *
     * @param entityType The {@link KapuaEntity#getType()} that has reached the maximum number allowed.
     * @since 1.0.0
     */
    public KapuaMaxNumberOfItemsReachedException(String entityType) {
        super(KapuaErrorCodes.MAX_NUMBER_OF_ITEMS_REACHED, entityType);

        this.entityType = entityType;
    }

    /**
     * Gets the {@link KapuaEntity#getType()} that has reached the maximum number allowed.
     *
     * @return The {@link KapuaEntity#getType()} that has reached the maximum number allowed.
     * @since 1.0.0
     */
    public String getEntityType() {
        return entityType;
    }
}
