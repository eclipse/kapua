/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    public KapuaEntityCloneException(String entityType, KapuaEntity entity) {
        this(null, entityType, entity);
    }

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
