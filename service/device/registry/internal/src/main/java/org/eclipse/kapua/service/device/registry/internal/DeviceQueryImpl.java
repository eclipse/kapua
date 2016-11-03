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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceQuery;

/**
 * Device query implementation.
 * 
 * @since 1.0
 *
 */
public class DeviceQueryImpl extends AbstractKapuaQuery<Device> implements DeviceQuery
{

    /**
     * Constructor
     */
    private DeviceQueryImpl()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public DeviceQueryImpl(KapuaId scopeId)
    {
        this();
        setScopeId(scopeId);
    }
}
