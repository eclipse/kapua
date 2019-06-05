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

import java.util.Set;

import org.apache.activemq.command.ActiveMQDestination;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;

/**
 * Default authorizer implementation.
 *
 */
public class DefaultAuthorizer implements Authorizer {

    @Override
    public boolean isAllowed(ActionType actionType, KapuaSecurityContext kapuaSecurityContext, ActiveMQDestination destination) throws KapuaException {
        switch (actionType) {
        case READ:
            return isConsumeAllowed(kapuaSecurityContext, destination);
        case WRITE:
            return isSendAllowed(kapuaSecurityContext, destination);
        case ADMIN:
            return isAdminAllowed(kapuaSecurityContext, destination);
        default:
            return false;
        }
    }

    protected boolean isSendAllowed(KapuaSecurityContext kapuaSecurityContext, ActiveMQDestination destination) throws KapuaException {
        Set<?> allowedACLs = kapuaSecurityContext.getAuthorizationMap().getWriteACLs(destination);
        if (allowedACLs != null && !kapuaSecurityContext.isInOneOf(allowedACLs)) {
            return false;
        }
        return true;
    }

    protected boolean isConsumeAllowed(KapuaSecurityContext kapuaSecurityContext, ActiveMQDestination destination) throws KapuaException {
        Set<?> allowedACLs = kapuaSecurityContext.getAuthorizationMap().getReadACLs(destination);
        if (allowedACLs != null && !kapuaSecurityContext.isInOneOf(allowedACLs)) {
            return false;
        }
        return true;
    }

    protected boolean isAdminAllowed(KapuaSecurityContext kapuaSecurityContext, ActiveMQDestination destination) throws KapuaException {
        Set<?> allowedACLs = kapuaSecurityContext.getAuthorizationMap().getAdminACLs(destination);
        if (allowedACLs != null && !kapuaSecurityContext.isInOneOf(allowedACLs)) {
            return false;
        }
        return true;
    }
}
