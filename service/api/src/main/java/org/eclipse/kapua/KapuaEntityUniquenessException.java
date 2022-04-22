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

import java.util.List;
import java.util.Map;

/**
 * {@link KapuaEntityUniquenessException} is thrown when an operation cannot be completed because an unique constraint has been violated.
 *
 * @since 1.0.0
 */
public class KapuaEntityUniquenessException extends KapuaException {

    private final String entityType;
    private final List<Map.Entry<String, Object>> uniquesFieldValues;

    /**
     * Constructor for the {@link KapuaEntityUniquenessException} taking in values that cannot be used
     *
     * @param entityType         The {@link KapuaEntity#getType()}
     * @param uniquesFieldValues The field value pairs that breaks a unique constraint.
     * @since 1.0.0
     */
    public KapuaEntityUniquenessException(String entityType, List<Map.Entry<String, Object>> uniquesFieldValues) {
        super(KapuaErrorCodes.ENTITY_UNIQUENESS, uniquesFieldValues);

        this.entityType = entityType;
        this.uniquesFieldValues = uniquesFieldValues;
    }

    /**
     * Gets the {@link KapuaEntity#getType()}
     *
     * @return The {@link KapuaEntity#getType()}
     * @since 1.0.0
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Gets the field value pairs that breaks a unique constraint.
     *
     * @return The field value pairs that breaks a unique constraint.
     * @since 1.0.0
     */
    public List<Map.Entry<String, Object>> getUniquesFieldValues() {
        return uniquesFieldValues;
    }
}
