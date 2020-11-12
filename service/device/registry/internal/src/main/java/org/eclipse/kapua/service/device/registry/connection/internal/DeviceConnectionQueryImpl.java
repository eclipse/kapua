/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;

/**
 * Device connection query.
 *
 * @since 1.0
 */
public class DeviceConnectionQueryImpl extends AbstractKapuaQuery implements DeviceConnectionQuery {

    /**
     * Constructor
     */
    private DeviceConnectionQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceConnectionQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
