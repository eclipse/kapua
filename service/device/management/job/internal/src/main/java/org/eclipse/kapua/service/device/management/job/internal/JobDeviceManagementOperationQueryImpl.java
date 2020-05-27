/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.job.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationQuery;

/**
 * {@link JobDeviceManagementOperationQuery} definition.
 *
 * @since 1.1.0
 */
public class JobDeviceManagementOperationQueryImpl extends AbstractKapuaQuery implements JobDeviceManagementOperationQuery {

    /**
     * Constructor.
     *
     * @param scopeId
     * @since 1.1.0
     */
    public JobDeviceManagementOperationQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
