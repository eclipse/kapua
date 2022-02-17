/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model;

import org.eclipse.kapua.commons.util.PropertiesUtils;
import org.eclipse.kapua.entity.EntityPropertiesReadException;
import org.eclipse.kapua.entity.EntityPropertiesWriteException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

import java.io.IOException;
import java.util.Properties;

/**
 * {@link KapuaUpdatableEntityCreator} {@code abstract} implementation.
 *
 * @param <E> the {@link KapuaEntity} for which this {@link AbstractKapuaEntityCreator} is for.
 * @since 1.0.0
 */
public abstract class AbstractKapuaUpdatableEntityCreator<E extends KapuaEntity> extends AbstractKapuaEntityCreator<E> implements KapuaUpdatableEntityCreator<E> {

    protected String attributes;

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set to in the {@link KapuaUpdatableEntityCreator}
     * @since 1.0.0
     */
    public AbstractKapuaUpdatableEntityCreator(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public Properties getEntityAttributes() {
        try {
            return PropertiesUtils.readPropertiesFromString(attributes);
        } catch (IOException e) {
            throw new EntityPropertiesReadException(e, "attributes", attributes);
        }
    }

    @Override
    public void setEntityAttributes(Properties attributes) {
        try {
            this.attributes = PropertiesUtils.writePropertiesToString(attributes);
        } catch (IOException e) {
            throw new EntityPropertiesWriteException(e, "attributes", attributes);
        }
    }
}
