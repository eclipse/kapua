/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.server.util;

import java.net.URI;

import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;

public final class SsoHelper {

    private SsoHelper() {
    }

    private static ConsoleSetting getSettings() {
        return ConsoleSetting.getInstance();
    }

    public static String getHomeUri() {
        return getSettings().getString(ConsoleSettingKeys.SITE_HOME_URI);
    }

    public static URI getRedirectUri() {
        String result = getSettings().getString(ConsoleSettingKeys.SSO_REDIRECT_URI);
        if (result != null && !result.isEmpty()) {
            return URI.create(result);
        }

        result = getHomeUri();
        if (result != null && !result.isEmpty()) {
            return URI.create(result + "/sso/callback");
        }

        throw new IllegalStateException("Unable to lookup SSO redirect URL");
    }
}
