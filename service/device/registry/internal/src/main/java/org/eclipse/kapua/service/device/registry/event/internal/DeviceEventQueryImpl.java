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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.event.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;

/**
 * Device event query.
 *
 * @since 1.0
 */
public class DeviceEventQueryImpl extends AbstractKapuaQuery<DeviceEvent> implements DeviceEventQuery {

    /**
     * Constructor
     */
    private DeviceEventQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceEventQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
