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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

/**
 * Device connection query predicates.
 * 
 * @since 1.0
 */
public interface DeviceConnectionPredicates extends KapuaUpdatableEntityPredicates {

    /**
     * Client identifier
     */
    String CLIENT_ID = "clientId";

    /**
     * Connection status
     */
    String STATUS = "status";

    /**
     * Credentials mode
     */
    String CREDENTIALS_MODE = "credentialsMode";

    /**
     * Reserved user identifier
     */
    String RESERVED_USER_ID = "reservedUserId";

    /**
     * Last user identifier
     */
    String USER_ID = "userId";

    /**
     * Connection user coupling mode
     */
    String USER_COUPLING_MODE = "userCouplingMode";
}
