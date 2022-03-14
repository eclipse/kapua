/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.authentication;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.client.security.bean.AuthAcl.Action;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;

import org.eclipse.kapua.client.security.bean.AuthContext;

/**
 * Admin profile authentication logic implementation
 *
 * @since 1.0
 */
public class AdminAuthenticationLogic extends AuthenticationLogic {

    /**
     * Default constructor
     *
     */
    public AdminAuthenticationLogic() {
    }

    @Override
    public List<AuthAcl> connect(AuthContext authContext) throws KapuaException {
        authContext.setAdmin(true);
        DeviceConnection deviceConnection = upsertDeviceConnection(authContext, KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(
                KapuaEid.parseCompactId(authContext.getScopeId()), authContext.getClientId())));
        if (deviceConnection!=null && deviceConnection.getId()!=null) {
            authContext.setKapuaConnectionId(deviceConnection.getId());
        }
        //no need to have permissions since is admin profile
        return buildAuthorizationMap(null, authContext);
    }

    @Override
    public boolean disconnect(AuthContext authContext) {
        return !authContext.isIllegalState() && !authContext.isMissing();
    }

    protected List<AuthAcl> buildAuthorizationMap(UserPermissions userPermissions, AuthContext authContext) {
        ArrayList<AuthAcl> ael = new ArrayList<AuthAcl>();
        StringBuilder aclDestinationsLog = new StringBuilder();
        ael.add(createAuthorizationEntry(Action.all, aclHash, aclDestinationsLog));
        logger.info("{}", aclDestinationsLog);
        return ael;
    }

}
