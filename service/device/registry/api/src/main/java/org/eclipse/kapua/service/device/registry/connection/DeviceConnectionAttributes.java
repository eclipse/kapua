/*******************************************************************************
 * Copyright (c) 2011, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * Device connection query predicates.
 * 
 * @since 1.0
 */
public class DeviceConnectionAttributes extends KapuaUpdatableEntityAttributes {

    /**
     * Client identifier
     */
    public static final String CLIENT_ID = "clientId";

    /**
     * Connection status
     */
    public static final String STATUS = "status";

    /**
     * Credentials mode
     */
    public static final String CREDENTIALS_MODE = "credentialsMode";

    /**
     * Reserved user identifier
     */
    public static final String RESERVED_USER_ID = "reservedUserId";

    /**
     * Last user identifier
     */
    public static final String USER_ID = "userId";

    /**
     * Connection user coupling mode
     */
    public static final String USER_COUPLING_MODE = "userCouplingMode";

    /**
     * Client IP
     */
    public static final String CLIENT_IP = "clientIp";

    /**
     * Protocol
     */
    public static final String PROTOCOL = "protocol";
}
