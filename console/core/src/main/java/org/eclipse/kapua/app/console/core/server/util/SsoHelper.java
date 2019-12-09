/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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

import java.net.URI;

import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDIllegalUriException;

public final class SsoHelper {

    private static final String ILLEGAL_STATE_MESSAGE = "Unable to lookup SSO redirect URL";

    private SsoHelper() {
    }

    private static ConsoleSetting getSettings() {
        return ConsoleSetting.getInstance();
    }

    public static String getHomeUri() throws OpenIDIllegalUriException {
        String homeUri = getSettings().getString(ConsoleSettingKeys.SSO_CONSOLE_HOME_URI);
        if (homeUri == null || homeUri.isEmpty()) {
            throw new OpenIDIllegalUriException(ConsoleSettingKeys.SSO_CONSOLE_HOME_URI.key(), null);
        }
        return homeUri;
    }

    public static URI getRedirectUri() {
        String result = getSettings().getString(ConsoleSettingKeys.SSO_REDIRECT_URI);
        if (result != null && !result.isEmpty()) {
            return URI.create(result);
        }

        try {
            result = getHomeUri();
            return URI.create(result + "/sso/callback");
        } catch (OpenIDIllegalUriException e) {
            throw new IllegalStateException(ILLEGAL_STATE_MESSAGE, e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(ILLEGAL_STATE_MESSAGE, e);
        }
    }
}
