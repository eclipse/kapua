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
package org.eclipse.kapua.service.device.management.job.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationQuery;

/**
 * {@link JobDeviceManagementOperationQuery} definition.
 *
 * @since 1.1.0
 */
public class JobDeviceManagementOperationQueryImpl extends AbstractKapuaQuery<JobDeviceManagementOperation> implements JobDeviceManagementOperationQuery {

    /**
     * Constructor
     *
     * @param scopeId
     */
    public JobDeviceManagementOperationQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
