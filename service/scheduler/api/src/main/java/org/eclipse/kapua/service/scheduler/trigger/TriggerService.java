/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * TriggerService exposes APIs to manage Trigger objects.<br>
 * It includes APIs to create, update, find, list and delete Triggers.<br>
 * Instances of the TriggerService can be acquired through the ServiceLocator object.
 * 
 * @since 1.0
 * 
 */
public interface TriggerService extends KapuaEntityService<Trigger, TriggerCreator>,
        KapuaUpdatableEntityService<Trigger>,
        KapuaConfigurableService {

    /**
     * Returns the {@link TriggerListResult} with elements matching the provided query.
     * 
     * @param query
     *            The {@link TriggerQuery} used to filter results.
     * @return The {@link TriggerListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    public TriggerListResult query(KapuaQuery<Trigger> query)
            throws KapuaException;
}
