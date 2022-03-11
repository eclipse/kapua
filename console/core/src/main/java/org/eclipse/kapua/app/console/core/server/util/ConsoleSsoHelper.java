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
package org.eclipse.kapua.app.console.core.server.util;

import com.google.common.base.Strings;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDIllegalUriException;

import java.net.URI;

public final class ConsoleSsoHelper {

    private static final String ILLEGAL_STATE_MESSAGE = "Unable to lookup OpenID redirect URL";

    private ConsoleSsoHelper() {
    }

    private static ConsoleSetting getSettings() {
        return ConsoleSetting.getInstance();
    }

    public static String getHomeUri() throws OpenIDIllegalUriException {
        String homeUri = getSettings().getString(ConsoleSettingKeys.SSO_OPENID_CONSOLE_HOME_URI);
        if (Strings.isNullOrEmpty(homeUri)) {
            throw new OpenIDIllegalUriException(ConsoleSettingKeys.SSO_OPENID_CONSOLE_HOME_URI.key(), null);
        }
        return homeUri;
    }

    public static URI getRedirectUri() {
        String result = getSettings().getString(ConsoleSettingKeys.SSO_OPENID_REDIRECT_URI);

        if (!Strings.isNullOrEmpty(result)) {
            return URI.create(result);
        }

        try {
            result = getHomeUri();
            return URI.create(result + "/openid/callback");
        } catch (OpenIDIllegalUriException e) {
            throw new IllegalStateException(ILLEGAL_STATE_MESSAGE, e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(ILLEGAL_STATE_MESSAGE, e);
        }
    }
}
