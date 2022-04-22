/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
 * {@link KapuaEntityCloneException} definition.
 * <p>
 * This {@link KapuaRuntimeException} is thrown whenever invoking a {@link org.eclipse.kapua.model.KapuaEntityFactory#clone(KapuaEntity)} has a severe error while cloning the {@link KapuaEntity}
 *
 * @since 1.1.0
 */
public class KapuaEntityCloneException extends KapuaRuntimeException {

    private final String entityType;
    private final KapuaEntity entity;

    /**
     * Constructor.
     *
     * @param entityType The source {@link KapuaEntity#getType()}
     * @param entity     The source {@link KapuaEntity}.
     * @since 1.1.0
     */
    public KapuaEntityCloneException(String entityType, KapuaEntity entity) {
        this(null, entityType, entity);
    }

    /**
     * Constructor.
     *
     * @param entityType The source {@link KapuaEntity#getType()}
     * @param entity     The source {@link KapuaEntity}.
     * @param t          The original {@link Throwable} that caused this exception.
     * @since 1.1.0
     */
    public KapuaEntityCloneException(Throwable t, String entityType, KapuaEntity entity) {
        super(KapuaRuntimeErrorCodes.ENTITY_CLONE_ERROR, t, entityType, entity);

        this.entityType = entityType;
        this.entity = entity;
    }

    /**
     * Gets the source {@link KapuaEntity#getType()}
     *
     * @return the source {@link KapuaEntity#getType()}
     * @since 1.1.0
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Gets the source {@link KapuaEntity}
     *
     * @return The source {@link KapuaEntity}
     * @since 1.1.0
     */
    public KapuaEntity getEntity() {
        return entity;
    }
}
