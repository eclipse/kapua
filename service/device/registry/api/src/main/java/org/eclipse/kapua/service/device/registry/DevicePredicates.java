/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
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
    String GROUP_ID = "groupId";

    /**
     * Tag id.
     */
    String TAG_IDS = "tagIds";

    /**
     * Client identifier
     */
    String CLIENT_ID = "clientId";
    /**
     * Display name
     */
    String DISPLAY_NAME = "displayName";

    /**
     * Connection id.
     */
    String CONNECTION_ID = "connectionId";

    /**
     * Connection
     */
    String CONNECTION = "connection";

    /**
     * Connection status
     */
    String CONNECTION_STATUS = CONNECTION + ".status";

    /**
     * Connection protocol
     */
    String CONNECTION_PROTOCOL = CONNECTION + ".protocol";

    /**
     * Connection clientId
     */
    String CONNECTION_CLIENT_ID = CONNECTION + ".clientId";

    /**
     * Last event
     */
    String LAST_EVENT = "lastEvent";

    /**
     * Last event on
     */
    String LAST_EVENT_ON = LAST_EVENT + ".receivedOn";

    /**
     * Last event resource
     */
    String LAST_EVENT_RESOURCE = LAST_EVENT + ".resource";

    /**
     * Serial number
     */
    String SERIAL_NUMBER = "serialNumber";
    /**
     * Imei
     */
    String IMEI = "imei";
    /**
     * Imsi
     */
    String IMSI = "imsi";
    /**
     * Iccd
     */
    String ICCID = "iccid";
    /**
     * Model identifier
     */
    String MODEL_ID = "modelId";
    /**
     * Bios version
     */
    String BIOS_VERSION = "biosVersion";
    /**
     * Firmware version
     */
    String FIRMWARE_VERSION = "firmwareVersion";
    /**
     * Operating system version
     */
    String OS_VERSION = "osVersion";
    /**
     * Jvm version
     */
    String JVM_VERSION = "jvmVersion";
    /**
     * Osgi framework version
     */
    String OSGI_FRAMEWORK_VERSION = "osgiFrameworkVersion";
    /**
     * Application framework version
     */
    String APPLICATION_FRAMEWORK_VERSION = "applicationFrameworkVersion";
    /**
     * Application identifier
     */
    String APPLICATION_IDENTIFIERS = "applicationIdentifiers";
    /**
     * Custom attribute 1
     */
    String CUSTOM_ATTRIBUTE_1 = "customAttribute1";
    /**
     * Custom attribute 2
     */
    String CUSTOM_ATTRIBUTE_2 = "customAttribute2";
    /**
     * Custom attribute 3
     */
    String CUSTOM_ATTRIBUTE_3 = "customAttribute3";
    /**
     * Custom attribute 4
     */
    String CUSTOM_ATTRIBUTE_4 = "customAttribute4";
    /**
     * Custom attribute 5
     */
    String CUSTOM_ATTRIBUTE_5 = "customAttribute5";
    /**
     * Accept encoding
     */
    String ACCEPT_ENCODING = "acceptEncoding";
    /**
     * Gps longitude
     */
    String GPS_LONGITUDE = "gpsLongitude";
    /**
     * Gps latitude
     */
    String GPS_LATITUDE = "gpsLatitude";

    /**
     * Device status
     */
    String STATUS = "status";
    /**
     * Credentials mode
     */
    String CREDENTIALS_MODE = "credentialsMode";
    /**
     * Preferred user identifier
     */
    String PREFERRED_USER_ID = "preferredUserId";
}
