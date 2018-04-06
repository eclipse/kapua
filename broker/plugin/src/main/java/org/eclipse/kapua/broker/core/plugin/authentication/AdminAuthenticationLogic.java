/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.broker.core.plugin.KapuaConnectionContext;

/**
 * Admin profile authentication logic implementation
 * 
 * @since 1.0
 */
public class AdminAuthenticationLogic extends AuthenticationLogic {

    /**
     * Default constuctor
     * 
     * @param options
     */
    public AdminAuthenticationLogic(Map<String, Object> options) {
        super((String) options.get(Authenticator.ADDRESS_PREFIX_KEY), (String) options.get(Authenticator.ADDRESS_CLASSIFIER_KEY), (String) options.get(Authenticator.ADDRESS_ADVISORY_PREFIX_KEY));
    }

    @Override
    public List<AuthorizationEntry> connect(KapuaConnectionContext kcc) throws KapuaException {
        return buildAuthorizationMap(kcc);
    }

    @Override
    public boolean disconnect(KapuaConnectionContext kcc, Throwable error) {
        boolean stealingLinkDetected = isStealingLink(kcc, error);
        logger.debug("Old connection id: {} - new connection id: {} - error: {} - error cause: {}", kcc.getOldConnectionId(), kcc.getConnectionId(), error, (error!=null ? error.getCause() : "null"), error);
        if (stealingLinkDetected) {
            loginMetric.getAdminStealingLinkDisconnect().inc();
        }
        return !stealingLinkDetected && !kcc.isMissing();
    }

    protected List<AuthorizationEntry> buildAuthorizationMap(KapuaConnectionContext kcc) {
        ArrayList<AuthorizationEntry> ael = new ArrayList<AuthorizationEntry>();
        ael.add(createAuthorizationEntry(kcc, Acl.ALL, aclHash));
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE_ADMIN, aclAdvisory));
        kcc.logAuthDestinationToLog();
        return ael;
    }

}