/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import java.util.UUID;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.shared.model.GwtLoginInformation;
import org.eclipse.kapua.app.console.shared.service.GwtSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This is the security token service, a concrete implementation to fix the XSFR security problem.
 */
public class GwtSettingsServiceImpl extends RemoteServiceServlet implements
        GwtSettingsService {

    private static final Logger logger = LoggerFactory.getLogger(GwtSettingsServiceImpl.class);

    private static final long serialVersionUID = -6876999298300071273L;
    private static final ConsoleSetting settings = ConsoleSetting.getInstance();

    @Override
    public GwtLoginInformation getLoginInformation() {
        final GwtLoginInformation result = new GwtLoginInformation();
        result.setBackgroundCredits(settings.getString(ConsoleSettingKeys.LOGIN_BACKGROUND_CREDITS));
        result.setInformationSnippet(settings.getString(ConsoleSettingKeys.LOGIN_GENERIC_SNIPPET));
        return result;
    }

    @Override
    public String getSsoLoginUri() {
        try {
            final URIBuilder uri = new URIBuilder(settings.getString(ConsoleSettingKeys.SSO_OPENID_SERVER_ENDPOINT_AUTH));

            uri.addParameter("scope", "openid");
            uri.addParameter("response_type", "code");
            uri.addParameter("client_id", settings.getString(ConsoleSettingKeys.SSO_OPENID_CLIENT_ID));
            uri.addParameter("state", UUID.randomUUID().toString());
            uri.addParameter("redirect_uri", settings.getString(ConsoleSettingKeys.SSO_OPENID_REDIRECT_URI));

            return uri.toString();
        } catch (Exception e) {
            logger.warn("Failed to construct SSO URI", e);
        }
        return null;
    }

    @Override
    public boolean getSsoEnabled() {
        return settings.getBoolean(ConsoleSettingKeys.SSO_ENABLE);
    }

    @Override
    public String getHomeUri() {
        return settings.getString(ConsoleSettingKeys.SITE_HOME_URI);
    }
}
