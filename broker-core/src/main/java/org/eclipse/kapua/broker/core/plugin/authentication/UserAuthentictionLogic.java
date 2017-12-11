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

public class UserAuthentictionLogic extends AuthenticationLogic {

    public List<AuthorizationEntry> buildAuthorizationMap(KapuaConnectionContext kcc) {
        ArrayList<AuthorizationEntry> ael = new ArrayList<AuthorizationEntry>();
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE_ADMIN, AclConstants.ACL_AMQ_ADVISORY));

        // addConnection checks BROKER_CONNECT_IDX permission before call this method
        // then here user has BROKER_CONNECT_IDX permission and if check isn't needed
        // if (hasPermissions[BROKER_CONNECT_IDX]) {
        if (kcc.getHasPermissions()[AclConstants.DEVICE_MANAGE_IDX]) {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAcl(AclConstants.ACL_CTRL_ACC, kcc)));
        } else {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAclFull(AclConstants.ACL_CTRL_ACC_CLI, kcc)));
        }
        if (kcc.getHasPermissions()[AclConstants.DATA_MANAGE_IDX]) {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAcl(AclConstants.ACL_DATA_ACC, kcc)));
        } else if (kcc.getHasPermissions()[AclConstants.DATA_VIEW_IDX]) {
            ael.add(createAuthorizationEntry(kcc, Acl.READ_ADMIN, formatAcl(AclConstants.ACL_DATA_ACC, kcc)));
            ael.add(createAuthorizationEntry(kcc, Acl.WRITE, formatAclFull(AclConstants.ACL_DATA_ACC_CLI, kcc)));
        } else {
            ael.add(createAuthorizationEntry(kcc, Acl.ALL, formatAclFull(AclConstants.ACL_DATA_ACC_CLI, kcc)));
        }
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE_ADMIN, formatAcl(AclConstants.ACL_CTRL_ACC_REPLY, kcc)));

        // Write notify to any client Id and any application and operation
        ael.add(createAuthorizationEntry(kcc, Acl.WRITE, formatAclFull(AclConstants.ACL_CTRL_ACC_NOTIFY, kcc)));

        kcc.logAuthDestinationToLog();

        return ael;
    }
}
