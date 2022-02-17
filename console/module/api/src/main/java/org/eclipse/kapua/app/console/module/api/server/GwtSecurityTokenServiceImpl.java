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
package org.eclipse.kapua.app.console.module.api.server;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenService;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the security token service, a concrete implementation to fix the XSFR security problem.
 */
public class GwtSecurityTokenServiceImpl extends RemoteServiceServlet implements
        GwtSecurityTokenService {

    private static final long serialVersionUID = -6876999298300071273L;

    public static final Logger logger = LoggerFactory.getLogger(GwtSecurityTokenServiceImpl.class);
    public static final String XSRF_TOKEN_KEY = "XSRF_TOKEN";

    @Override
    public String processCall(String payload)
            throws SerializationException {
        try {
            perThreadRequest.set(getThreadLocalRequest());
            return super.processCall(payload);
        } finally {
            perThreadRequest.set(null);
        }
    }

    @Override
    public GwtXSRFToken generateSecurityToken() {
        GwtXSRFToken token = null;

        // Before to generate a token we must to check if the user is correctly authenticated
        HttpSession session = getHttpSession();
        if (session != null) {
            token = new GwtXSRFToken(UUID.randomUUID().toString());
            session.setAttribute(XSRF_TOKEN_KEY, token);

            logger.debug("Generated XSRF token: {} for HTTP session: {}", token.getToken(), session.getId());
        }
        return token;
    }

    private HttpSession getHttpSession() {
        return perThreadRequest.get().getSession();
    }

}
