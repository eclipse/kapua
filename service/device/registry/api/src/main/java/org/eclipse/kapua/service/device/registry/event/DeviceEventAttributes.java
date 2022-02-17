/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.event;

import org.eclipse.kapua.model.KapuaEntityAttributes;

/**
 * Device event predicates.
 *
 * @since 1.0
 */
public class DeviceEventAttributes extends KapuaEntityAttributes {

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

    /**
     * Event action
     */
    public static final String ACTION = "action";
}
