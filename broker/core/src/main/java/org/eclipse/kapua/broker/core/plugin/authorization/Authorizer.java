/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
