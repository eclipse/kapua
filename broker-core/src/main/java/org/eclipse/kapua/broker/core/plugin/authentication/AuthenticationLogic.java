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

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.broker.core.plugin.AclConstants;
import org.eclipse.kapua.broker.core.plugin.KapuaConnectionContext;

public abstract class AuthenticationLogic {

    public abstract List<AuthorizationEntry> buildAuthorizationMap(KapuaConnectionContext kcc);

    protected String formatAcl(String pattern, KapuaConnectionContext kcc) {
        return MessageFormat.format(pattern, kcc.getAccountName());
    }

    protected String formatAclFull(String pattern, KapuaConnectionContext kcc) {
        return MessageFormat.format(pattern, kcc.getAccountName(), kcc.getClientId());
    }

    protected AuthorizationEntry createAuthorizationEntry(KapuaConnectionContext kcc, Acl acl, String address) {
        AuthorizationEntry entry = new AuthorizationEntry(address, acl);
        kcc.addAuthDestinationToLog(MessageFormat.format(AclConstants.PERMISSION_LOG,
                acl.isRead() ? "r" : "_",
                acl.isWrite() ? "w" : "_",
                acl.isAdmin() ? "a" : "_",
                address));
        return entry;
    }

}
