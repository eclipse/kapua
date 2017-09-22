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
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.server.util;

import java.net.URI;

import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;

import javax.servlet.http.HttpServletRequest;

public final class SsoHelper {

    private SsoHelper() {
    }

    private static ConsoleSetting getSettings() {
        return ConsoleSetting.getInstance();
    }

    public static String getHomeUri(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
    }

    public static URI getRedirectUri(HttpServletRequest request) {
        String result = getSettings().getString(ConsoleSettingKeys.SSO_REDIRECT_URI);
        if (result != null && !result.isEmpty()) {
            return URI.create(result);
        }

        result = getHomeUri(request);
        if (result != null && !result.isEmpty()) {
            return URI.create(result + "/sso/callback");
        }

        throw new IllegalStateException("Unable to lookup SSO redirect URL");
    }
}
