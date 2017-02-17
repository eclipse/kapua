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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialQuery;
import org.eclipse.kapua.app.console.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class GwtCredentialServiceImpl extends KapuaRemoteServiceServlet implements GwtCredentialService {

    @Override
    public PagingLoadResult<GwtCredential> query(PagingLoadConfig loadConfig, GwtCredentialQuery gwtCredentialQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtCredential> gwtCredentials = new ArrayList<>();

        Map<KapuaId, User> usersCache = new HashMap<>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);
            UserService userService = locator.getService(UserService.class);

            // Convert from GWT entity
            CredentialQuery credentialQuery = GwtKapuaModelConverter.convertCredentialQuery(loadConfig, gwtCredentialQuery);

            // query
            CredentialListResult credentials = credentialService.query(credentialQuery);

            // If there are results
            if (!credentials.isEmpty()) {
                // count
                if (credentials.getSize() >= loadConfig.getLimit()) {
                    totalLength = new Long(credentialService.count(credentialQuery)).intValue();
                } else {
                    totalLength = credentials.getSize();
                }

                // Converto to GWT entity
                for (Credential credential : credentials.getItems()) {
                    User user;
                    if (!usersCache.containsKey(credential.getUserId())) {
                        user = userService.find(credential.getScopeId(), credential.getUserId());
                        usersCache.put(credential.getUserId(), user);
                    } else {
                        user = usersCache.get(credential.getUserId());
                    }
                    gwtCredentials.add(KapuaGwtModelConverter.convert(credential, user));
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<>(gwtCredentials, loadConfig.getOffset(), totalLength);
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String stringAccountId, String gwtCredentialId) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            CredentialService credentialService = locator.getService(CredentialService.class);
            KapuaId scopeId = GwtKapuaModelConverter.convert(stringAccountId);
            KapuaId credentialId = GwtKapuaModelConverter.convert(gwtCredentialId);
            credentialService.delete(scopeId, credentialId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }
}
