/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
 * {@link KapuaException} to {@code throw} when creating a resource in accounts that are not first level Accounts is forbidden
 *
 * @since 2.0.0
 */
public class ResourceRestrictedToFirstLevelAccountException extends KapuaException {

    private final String entityType;

    /**
     * Constructor.
     *
     * @param entityType The {@link KapuaEntity#getType()} whose creation is restricted only to first level Account.
     * @since 2.0.0
     */
    public ResourceRestrictedToFirstLevelAccountException(String entityType) {
        super(KapuaErrorCodes.RESOURCE_RESTRICTED_TO_FIRST_LEVEL_ACCOUNTS, entityType);

        this.entityType = entityType;
    }

    /**
     * Gets the {@link KapuaEntity#getType()} whose creation is restricted only to first level Account.
     *
     * @return The {@link KapuaEntity#getType()} whose creation is restricted only to first level Account.
     * @since 2.0.0
     */
    public String getEntityType() {
        return entityType;
    }
}
