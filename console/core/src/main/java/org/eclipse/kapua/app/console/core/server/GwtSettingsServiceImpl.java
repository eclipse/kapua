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
package org.eclipse.kapua.app.console.core.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.app.console.core.server.util.SsoHelper;
import org.eclipse.kapua.app.console.core.server.util.SsoLocator;
import org.eclipse.kapua.app.console.core.shared.model.GwtProductInformation;
import org.eclipse.kapua.app.console.core.shared.service.GwtSettingsService;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;

import java.net.URI;
import java.util.UUID;

public class GwtSettingsServiceImpl extends RemoteServiceServlet implements GwtSettingsService {

    private static final long serialVersionUID = -6876999298300071273L;
    private static final ConsoleSetting SETTINGS = ConsoleSetting.getInstance();

    @Override
    public GwtProductInformation getProductInformation() {
        GwtProductInformation result = new GwtProductInformation();
        result.setBackgroundCredits(SETTINGS.getString(ConsoleSettingKeys.LOGIN_BACKGROUND_CREDITS, ""));
        result.setInformationSnippet(SETTINGS.getString(ConsoleSettingKeys.LOGIN_GENERIC_SNIPPET, ""));
        result.setProductName(SETTINGS.getString(ConsoleSettingKeys.PRODUCT_NAME, ""));
        result.setCopyright(SETTINGS.getString(ConsoleSettingKeys.PRODUCT_COPYRIGHT, ""));
        return result;
    }

    @Override
    public String getOpenIDLoginUri() throws GwtKapuaException {
        try {
            return SsoLocator.getLocator(this).getService().getLoginUri(UUID.randomUUID().toString(), SsoHelper.getRedirectUri());
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
            return null;
        }
    }

    @Override
    public String getOpenIDLogoutUri(String idToken) throws GwtKapuaException {
        try {
            if (SETTINGS.getBoolean(ConsoleSettingKeys.SSO_OPENID_USER_LOGOUT_ENABLED, true)) {
                if (idToken.isEmpty()) {
                    throw new KapuaIllegalArgumentException("ssoIdToken", idToken);
                }
                return SsoLocator.getLocator(this).getService().getLogoutUri(
                        idToken, URI.create(SsoHelper.getHomeUri()), UUID.randomUUID().toString());}
            return "";  // return empty string instead of using a dedicated callback just to check if the logout is enabled
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
            return null;
        }
    }

    @Override
    public boolean getOpenIDEnabled() {
        return SsoLocator.getLocator(this).getService().isEnabled();
    }

    @Override
    public String getHomeUri() throws GwtKapuaException {
        try {
            return SsoHelper.getHomeUri();
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
            return null;
        }
    }
}
