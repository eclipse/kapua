/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin.authorization;

import org.apache.activemq.command.ActiveMQDestination;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;

/**
 * Broker authorizer definition.<br>
 * This class is meant to allow custom authorization pluggability.
 *
 */
public interface Authorizer {

    public enum ActionType {
        READ,
        WRITE,
        ADMIN
    }

    boolean isAllowed(ActionType actionType, KapuaSecurityContext kapuaSecurityContext, ActiveMQDestination destination) throws KapuaException;

}
