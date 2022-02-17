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
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaSortCriteria;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;

/**
 * {@link DeviceManagementOperationQuery} implementation.
 *
 * @since 1.1.0
 */
public class DeviceManagementOperationQueryImpl extends AbstractKapuaQuery implements DeviceManagementOperationQuery {

    /**
     * Constructor.
     *
     * @since 1.1.0
     */
    private DeviceManagementOperationQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.1.0
     */
    public DeviceManagementOperationQueryImpl(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

    @Override
    public KapuaSortCriteria getDefaultSortCriteria() {
        return fieldSortCriteria(DeviceManagementOperationAttributes.STARTED_ON, SortOrder.ASCENDING);
    }
}
