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
package org.eclipse.kapua.service.device.management.job;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;

/**
 * {@link JobDeviceManagementOperationService} exposes APIs to manage {@link JobDeviceManagementOperation} objects.<br>
 *
 * @since 1.1.0
 */
public interface JobDeviceManagementOperationService extends KapuaEntityService<JobDeviceManagementOperation, JobDeviceManagementOperationCreator> {

    /**
     * Returns the {@link JobDeviceManagementOperationListResult} with elements matching the provided query.
     *
     * @param query The {@link JobDeviceManagementOperationQuery} used to filter results.
     * @return The {@link JobDeviceManagementOperationListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.1.0
     */
    @Override
    JobDeviceManagementOperationListResult query(KapuaQuery<JobDeviceManagementOperation> query) throws KapuaException;
}
