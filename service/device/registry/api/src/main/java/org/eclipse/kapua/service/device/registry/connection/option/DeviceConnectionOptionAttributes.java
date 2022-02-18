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
package org.eclipse.kapua.service.device.registry.connection.option;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * Device connection options query predicates.
 *
 * @since 1.0.0
 */
public class DeviceConnectionOptionAttributes extends KapuaUpdatableEntityAttributes {

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
     * Client identifier
     */
    public static final String CLIENT_ID = "clientId";
}
