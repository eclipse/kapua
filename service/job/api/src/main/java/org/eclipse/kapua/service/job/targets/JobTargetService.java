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
package org.eclipse.kapua.service.job.targets;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link JobTargetService} exposes APIs to manage JobTarget objects.<br>
 * It includes APIs to create, update, find, list and delete Jobs.<br>
 * Instances of the JobTargetService can be acquired through the ServiceLocator object.
 * 
 * @since 1.0
 * 
 */
public interface JobTargetService extends KapuaEntityService<JobTarget, JobTargetCreator>,
        KapuaUpdatableEntityService<JobTarget>,
        KapuaConfigurableService {

    /**
     * Returns the {@link JobTargetListResult} with elements matching the provided query.
     * 
     * @param query
     *            The {@link JobTargetQuery} used to filter results.
     * @return The {@link JobTargetListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    public JobTargetListResult query(KapuaQuery<JobTarget> query)
            throws KapuaException;
}
