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
package org.eclipse.kapua.service.device.registry.lifecycle;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Device life cycle service definition.<br>
 * This service handles the life cycle messages coming from the device.
 * 
 * @since 1.0
 *
 */
public interface DeviceLifeCycleService extends KapuaService
{

    /**
     * Processes a birth certificate for a device, creating or updating the device footprint with the information supplied.
     * 
     * @param connectionId
     * @param message
     * @throws KapuaException
     */
    public <M extends KapuaBirthMessage> void birth(KapuaId connectionId, M message)
        throws KapuaException;

    /**
     * Processes a death certificate for a device, updating the device footprint with the information supplied.
     * 
     * @param connectionId
     * @param message
     * @throws KapuaException
     */
    public <M extends KapuaDisconnectMessage> void death(KapuaId connectionId, M message)
        throws KapuaException;

    /**
     * Processes a last-will testament for a device, updating the device footprint with the information supplied.
     * 
     * @param connectionId
     * @param message
     * @throws KapuaException
     */
    public <M extends KapuaMissingMessage> void missing(KapuaId connectionId, M message)
        throws KapuaException;

    /**
     * Processes a birth certificate for a device, creating or updating the device footprint with the information supplied.
     * 
     * @param connectionId
     * @param message
     * @throws KapuaException
     */
    public <M extends KapuaAppsMessage> void applications(KapuaId connectionId, M message)
        throws KapuaException;
}
