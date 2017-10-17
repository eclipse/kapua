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
package org.eclipse.kapua.service.device.management.registry.operation.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;

/**
 * {@link DeviceManagementOperationQueryImpl} definition.
 *
 * @since 1.0
 */
public class DeviceManagementOperationQueryImpl extends AbstractKapuaQuery<DeviceManagementOperation> implements DeviceManagementOperationQuery {

    private DeviceManagementOperationQueryImpl() {
        super();
    }

    public DeviceManagementOperationQueryImpl(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

}
