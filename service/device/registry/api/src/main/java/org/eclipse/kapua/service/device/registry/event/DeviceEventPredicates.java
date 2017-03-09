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

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

/**
 * Device event predicates.
 * 
 * @since 1.0
 *
 */
public interface DeviceEventPredicates extends KapuaUpdatableEntityPredicates {

    /**
     * Device identifier
     */
    public static final String DEVICE_ID = "deviceId";
    /**
     * Received on
     */
    public static final String RECEIVED_ON = "receivedOn";
    /**
     * Sent on
     */
    public static final String SENT_ON = "sentOn";
    /**
     * Event type
     */
    public static final String RESOURCE = "resource";
}
