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

import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.broker.core.plugin.AclConstants;
import org.eclipse.kapua.broker.core.plugin.KapuaConnectionContext;

public class AdminAuthenticationLogic extends AuthenticationLogic {

    public List<AuthorizationEntry> buildAuthorizationMap(KapuaConnectionContext kcc) {
        ArrayList<AuthorizationEntry> ael = new ArrayList<AuthorizationEntry>();
        ael.add(createAuthorizationEntry(kcc, Acl.ALL, AclConstants.ACL_HASH));
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE_ADMIN, AclConstants.ACL_AMQ_ADVISORY));

        kcc.logAuthDestinationToLog();
        return ael;
    }
}
