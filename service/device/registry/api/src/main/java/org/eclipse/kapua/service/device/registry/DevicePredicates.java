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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

/**
 * {@link DeviceQuery} predicates.
 * 
 * @since 1.0.0
 */
public interface DevicePredicates extends KapuaUpdatableEntityPredicates {

    /**
     * Group id.
     */
    public static final String GROUP_ID = "groupId";

    /**
     * Client identifier
     */
    public static final String CLIENT_ID = "clientId";
    /**
     * Display name
     */
    public static final String DISPLAY_NAME = "displayName";

    /**
     * Connection
     */
    public static final String CONNECTION = "connection";

    /**
     * Connection status
     */
    public static final String CONNECTION_STATUS = CONNECTION + ".status";

    /**
     * Connection protocol
     */
    public static final String CONNECTION_PROTOCOL = CONNECTION + ".protocol";

    /**
     * Connection clientId
     */
    public static final String CONNECTION_CLIENT_ID = CONNECTION + ".clientId";

    /**
     * Last event
     */
    public static final String LAST_EVENT = "lastEvent";

    /**
     * Last event on
     */
    public static final String LAST_EVENT_ON = LAST_EVENT + ".receivedOn";

    /**
     * Last event resource
     */
    public static final String LAST_EVENT_RESOURCE = LAST_EVENT + ".resource";

    /**
     * Serial number
     */
    public static final String SERIAL_NUMBER = "serialNumber";
    /**
     * Imei
     */
    public static final String IMEI = "imei";
    /**
     * Imsi
     */
    public static final String IMSI = "imsi";
    /**
     * Iccd
     */
    public static final String ICCID = "iccid";
    /**
     * Model identifier
     */
    public static final String MODEL_ID = "modelId";
    /**
     * Bios version
     */
    public static final String BIOS_VERSION = "biosVersion";
    /**
     * Firmware version
     */
    public static final String FIRMWARE_VERSION = "firmwareVersion";
    /**
     * Operating system version
     */
    public static final String OS_VERSION = "osVersion";
    /**
     * Jvm version
     */
    public static final String JVM_VERSION = "jvmVersion";
    /**
     * Osgi framework version
     */
    public static final String OSGI_FRAMEWORK_VERSION = "osgiFrameworkVersion";
    /**
     * Application framework version
     */
    public static final String APPLICATION_FRAMEWORK_VERSION = "applicationFrameworkVersion";
    /**
     * Application identifier
     */
    public static final String APPLICATION_IDENTIFIERS = "applicationIdentifiers";
    /**
     * Custom attribute 1
     */
    public static final String CUSTOM_ATTRIBUTE_1 = "customAttribute1";
    /**
     * Custom attribute 2
     */
    public static final String CUSTOM_ATTRIBUTE_2 = "customAttribute2";
    /**
     * Custom attribute 3
     */
    public static final String CUSTOM_ATTRIBUTE_3 = "customAttribute3";
    /**
     * Custom attribute 4
     */
    public static final String CUSTOM_ATTRIBUTE_4 = "customAttribute4";
    /**
     * Custom attribute 5
     */
    public static final String CUSTOM_ATTRIBUTE_5 = "customAttribute5";
    /**
     * Accept encoding
     */
    public static final String ACCEPT_ENCODING = "acceptEncoding";
    /**
     * Gps longitude
     */
    public static final String GPS_LONGITUDE = "gpsLongitude";
    /**
     * Gps latitude
     */
    public static final String GPS_LATITUDE = "gpsLatitude";

    /**
     * Device status
     */
    public static final String STATUS = "status";
    /**
     * Credentials mode
     */
    public static final String CREDENTIALS_MODE = "credentialsMode";
    /**
     * Preferred user identifier
     */
    public static final String PREFERRED_USER_ID = "preferredUserId";
}
