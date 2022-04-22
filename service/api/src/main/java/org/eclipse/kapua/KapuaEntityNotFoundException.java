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
package org.eclipse.kapua;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link KapuaEntityNotFoundException} is thrown when an entity could not be loaded from the database.
 *
 * @since 1.0.0
 */
public class KapuaEntityNotFoundException extends KapuaException {

    private static final long serialVersionUID = -4903038247732490215L;

    private final String entityType;
    private KapuaId entityId;
    private String entityName;

    /**
     * Constructor.
     *
     * @param entityType The {@link KapuaEntity#getType()}.
     * @param entityName The {@link KapuaNamedEntity#getName()}.
     * @since 1.0.0
     */
    public KapuaEntityNotFoundException(String entityType, String entityName) {
        super(KapuaErrorCodes.ENTITY_NOT_FOUND, entityType, entityName);

        this.entityType = entityType;
        this.entityName = entityName;
    }

    /**
     * Constructor.
     *
     * @param entityType The {@link KapuaEntity#getType()}.
     * @param entityId   The {@link KapuaEntity#getId()}.
     * @since 1.0.0
     */
    public KapuaEntityNotFoundException(String entityType, KapuaId entityId) {
        super(KapuaErrorCodes.ENTITY_NOT_FOUND, entityType, entityId.getId());

        this.entityType = entityType;
        this.entityId = entityId;
    }

    /**
     * Gets the {@link KapuaEntity#getType()}.
     *
     * @return The {@link KapuaEntity#getType()}.
     * @since 1.0.0
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Gets the {@link KapuaEntity#getId()}.
     *
     * @return The {@link KapuaEntity#getId()}.
     * @since 1.0.0
     */
    public KapuaId getEntityId() {
        return entityId;
    }

    /**
     * Gets the {@link KapuaNamedEntity#getName()}.
     *
     * @return The {@link KapuaNamedEntity#getName()}.
     * @since 1.0.0
     */
    public String getEntityName() {
        return entityName;
    }
}
