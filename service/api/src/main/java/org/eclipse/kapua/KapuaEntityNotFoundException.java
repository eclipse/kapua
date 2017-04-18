/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link KapuaEntityNotFoundException} is thrown when an entity could not be loaded from the database.
 * 
 * @since 1.0
 *
 */
public class KapuaEntityNotFoundException extends KapuaException {

    private static final long serialVersionUID = -4903038247732490215L;

    /**
     * Constructor with entity name not found
     * 
     * @param entityType
     * @param entityName
     */
    public KapuaEntityNotFoundException(String entityType, String entityName) {
        super(KapuaErrorCodes.ENTITY_NOT_FOUND, new Object[] { entityType, entityName });
    }

    /**
     * Constructor with entity identifier not found.
     * 
     * @param entityType
     * @param entityId
     */
    public KapuaEntityNotFoundException(String entityType, KapuaId entityId) {
        super(KapuaErrorCodes.ENTITY_NOT_FOUND, new Object[] { entityType, entityId.getId() });
    }
}
