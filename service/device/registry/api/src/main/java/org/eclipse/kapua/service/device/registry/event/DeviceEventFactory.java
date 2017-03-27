/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.event;

import java.util.Date;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device event factory service definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceEventFactory extends KapuaEntityFactory<DeviceEvent, DeviceEventCreator, DeviceEventQuery, DeviceEventListResult> {

    /**
     * Creates a new device event creator
     * 
     * @param scopeId
     * @param deviceId
     * @param receivedOn
     * @param resource
     * @return
     */
    DeviceEventCreator newCreator(KapuaId scopeId, KapuaId deviceId, Date receivedOn, String resource);

}
