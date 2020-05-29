/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.server.util;

import org.eclipse.kapua.app.console.core.server.GwtAuthorizationServiceImpl;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * Listens to the session invalidation, in order to logout also from the sso when the session is invalidated.
 */
public class OpenIDLogoutListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(OpenIDLogoutListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        // do nothing
    }

    /**
     * Handles the session invalidation by triggering the invalidation on the OpenID Connect Provider for the current user.
     * Only used when the session on the Kapua Console is unexpectedly invalidated or the session timeout is expired (the normal logout follows a different
     * path).
     *
     * @param httpSessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        GwtSession gwtSession = (GwtSession) session.getAttribute(GwtAuthorizationServiceImpl.SESSION_CURRENT);

        // TODO: are you sure that you don't need the shiro session? Is the Gwt one sufficient?
        //Subject shiroSubject = SecurityUtils.getSubject();
        //KapuaSession kapuaSession = (KapuaSession) shiroSubject.getSession().getAttribute(KapuaSession.KAPUA_SESSION_KEY);

        // perform the OpenID Logout only if it is enabled
        if (ConsoleSetting.getInstance().getBoolean(ConsoleSettingKeys.SSO_OPENID_LOGOUT_ENABLED, true)) {
            if (gwtSession != null && gwtSession.getSsoIdToken() != null) {
                try {
                    String logoutUri = SsoLocator.getLocator(httpSessionEvent.getSession().getServletContext()).getService().getLogoutUri(
                            gwtSession.getSsoIdToken(), URI.create(SsoHelper.getHomeUri()), UUID.randomUUID().toString());
                    if (!logoutUri.isEmpty()) {
                        URL url = new URL(logoutUri);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setDoOutput(true);

                        int httpRespCode = conn.getResponseCode();
                        if (httpRespCode == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                        }
                    }
                } catch (Exception e) {
                    // It is not possible to throw exceptions in the HttpSessionListener, so I log them
                    logger.warn("Remote session has not been invalidated", e);
                }
            }
        }
    }
}
