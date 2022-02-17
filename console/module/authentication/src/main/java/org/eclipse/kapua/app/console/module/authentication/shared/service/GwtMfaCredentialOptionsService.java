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
package org.eclipse.kapua.app.console.module.authentication.shared.service;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptions;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptionsCreator;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mfaOptions")
public interface GwtMfaCredentialOptionsService extends RemoteService {

    /**
     * Delete the supplied {@link GwtMfaCredentialOptions}.
     *
     * @param gwtMfaCredentialOptionsId
     * @throws GwtKapuaException
     */
    void delete(GwtXSRFToken xsfrToken, String stringScopeId, String gwtMfaCredentialOptionsId, boolean selfManagement)
            throws GwtKapuaException;

    GwtMfaCredentialOptions find(String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement)
            throws GwtKapuaException;

    GwtMfaCredentialOptions findByUserId(String scopeIdStr, String userIdStr, boolean selfManagement)
            throws GwtKapuaException;

    GwtMfaCredentialOptions create(GwtXSRFToken gwtXsrfToken, GwtMfaCredentialOptionsCreator gwtRoleCreator, boolean selfManagement)
            throws GwtKapuaException;

    void disableTrust(GwtXSRFToken gwtXsrfToken, String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement)
            throws GwtKapuaException;

}
