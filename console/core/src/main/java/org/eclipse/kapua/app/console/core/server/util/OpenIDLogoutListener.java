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
package org.eclipse.kapua.app.console.core.server.util;

import com.google.common.net.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.commons.security.KapuaSession;
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
     * Only used when the session on the Kapua Console is unexpectedly invalidated or when the session timeout is expired (the normal logout follows a
     * different path).
     *
     * @param httpSessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // Perform the OpenID Logout only if it is enabled
        if (ConsoleSetting.getInstance().getBoolean(ConsoleSettingKeys.SSO_OPENID_SESSION_LISTENER_LOGOUT_ENABLED, true)) {
            HttpSession session = httpSessionEvent.getSession();
            KapuaSession kapuaSession = (KapuaSession) session.getAttribute(KapuaSession.KAPUA_SESSION_KEY);

            if (kapuaSession != null && kapuaSession.getOpenIDidToken() != null && !kapuaSession.isUserInitiatedLogout()) {
                try {
                    String logoutUri = ConsoleSsoLocator
                            .getLocator(httpSessionEvent.getSession().getServletContext())
                            .getService()
                            .getLogoutUri(
                                    kapuaSession.getOpenIDidToken(),
                                    URI.create(ConsoleSsoHelper.getHomeUri()),
                                    UUID.randomUUID().toString()
                            );

                    URL url = new URL(logoutUri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(HttpMethod.GET.toString());
                    conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");
                    conn.setDoOutput(true);

                    int httpRespCode = conn.getResponseCode();
                    if (httpRespCode == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    }
                } catch (Exception e) {
                    // It is not possible to throw exceptions in the HttpSessionListener, so I log them
                    logger.warn("Remote session has not been invalidated", e);
                }
            }
        }
    }
}
