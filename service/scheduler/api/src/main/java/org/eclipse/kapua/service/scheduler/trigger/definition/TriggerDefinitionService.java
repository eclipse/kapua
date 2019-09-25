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
package org.eclipse.kapua.service.scheduler.trigger.definition;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * {@link TriggerDefinitionService} exposes APIs to manage {@link TriggerDefinition} objects.
 * <p>
 * It includes APIs to create, update, find, list and delete {@link TriggerDefinition}s.
 *
 * @since 1.1.0
 */
public interface TriggerDefinitionService extends KapuaEntityService<TriggerDefinition, TriggerDefinitionCreator>,
        KapuaUpdatableEntityService<TriggerDefinition> {


    /**
     * Finds the {@link TriggerDefinition} by its {@link TriggerDefinition#getId()}
     *
     * @param entityId The {@link TriggerDefinition#getId()}
     * @return The {@link TriggerDefinition} by its {@link TriggerDefinition#getId()}, or {@code null} if does not exists.
     * @throws KapuaException
     * @since 1.1.0
     */
    TriggerDefinition find(KapuaId entityId) throws KapuaException;

    /**
     * Returns the {@link TriggerDefinitionListResult} with elements matching the provided query.
     *
     * @param query The {@link TriggerDefinitionQuery} used to filter results.
     * @return The {@link TriggerDefinitionListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.1.0
     */
    @Override
    TriggerDefinitionListResult query(KapuaQuery<TriggerDefinition> query) throws KapuaException;
}
