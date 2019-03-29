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
package org.eclipse.kapua.job.engine.queue;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link QueuedJobExecutionService} exposes APIs to manage {@link QueuedJobExecution} objects.<br>
 * It includes APIs to create, update, find, list and delete {@link QueuedJobExecution}s.<br>
 * Instances of the {@link QueuedJobExecutionService} can be acquired through the {@link org.eclipse.kapua.locator.KapuaLocator} object.
 *
 * @since 1.1.0
 */
public interface QueuedJobExecutionService extends KapuaEntityService<QueuedJobExecution, QueuedJobExecutionCreator>,
        KapuaUpdatableEntityService<QueuedJobExecution>,
        KapuaConfigurableService {

    /**
     * Returns the {@link QueuedJobExecutionListResult} with elements matching the provided query.
     *
     * @param query The {@link QueuedJobExecutionQuery} used to filter results.
     * @return The {@link QueuedJobExecutionListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.1.0
     */
    @Override
    QueuedJobExecutionListResult query(KapuaQuery<QueuedJobExecution> query) throws KapuaException;
}
