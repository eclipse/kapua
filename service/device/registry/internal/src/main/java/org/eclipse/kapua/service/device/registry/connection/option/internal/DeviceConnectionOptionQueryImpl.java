/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionQuery;

/**
 * Device connection options query.
 *
 * @since 1.0.0
 */
public class DeviceConnectionOptionQueryImpl extends AbstractKapuaQuery implements DeviceConnectionOptionQuery {

    /**
     * Constructor
     */
    private DeviceConnectionOptionQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceConnectionOptionQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
