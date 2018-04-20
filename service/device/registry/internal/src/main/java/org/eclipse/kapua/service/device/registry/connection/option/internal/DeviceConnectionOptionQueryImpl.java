/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionQuery;

/**
 * Device connection options query.
 *
 * @since 1.0.0
 */
public class DeviceConnectionOptionQueryImpl extends AbstractKapuaQuery<DeviceConnectionOption> implements DeviceConnectionOptionQuery {

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
