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

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;

import com.codahale.metrics.Timer.Context;

import org.eclipse.kapua.client.security.bean.AuthContext;

/**
 * Admin profile authentication logic implementation
 *
 * @since 1.0
 */
public class AdminAuthenticationLogic extends AuthenticationLogic {

    @Override
    public List<AuthAcl> connect(AuthContext authContext) throws KapuaException {
        Context timeAdminTotal = loginMetric.getExternalAddConnectionTimeAdminTotal().time();
        authContext.setAdmin(true);
        DeviceConnection deviceConnection = KapuaSecurityUtils.doPrivileged(() -> deviceConnectionService.findByClientId(
                KapuaEid.parseCompactId(authContext.getScopeId()), authContext.getClientId()));
        deviceConnection = deviceConnection!=null ? updateDeviceConnection(authContext, deviceConnection) : createDeviceConnection(authContext);
        if (deviceConnection!=null && deviceConnection.getId()!=null) {
            authContext.setKapuaConnectionId(deviceConnection.getId());
        }
        //no need to have permissions since is admin profile
        List<AuthAcl> authorizationEntries = buildAuthorizationMap(null, authContext);
        timeAdminTotal.stop();

        return authorizationEntries;
    }

    @Override
    public boolean disconnect(AuthContext authContext) {
        return !authContext.isIllegalState() && !authContext.isMissing();
    }

    protected List<AuthAcl> buildAuthorizationMap(UserPermissions userPermissions, AuthContext authContext) {
        StringBuilder aclDestinationsLog = new StringBuilder();
        List<AuthAcl> ael = aclCreator.buildAdminAcls(authContext.getAccountName(), authContext.getClientId(), aclDestinationsLog);
        logger.info("Admin ACLs: {}", aclDestinationsLog);
        return ael;
    }

}
