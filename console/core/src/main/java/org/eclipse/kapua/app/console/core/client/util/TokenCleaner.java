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
package org.eclipse.kapua.app.console.core.client.util;

import com.google.gwt.user.client.Window;
import org.eclipse.kapua.app.console.core.client.KapuaCloudConsole;

public final class TokenCleaner {

    private TokenCleaner() {
    }

    /**
     * Clear GWT state and remove OpenID tokens when set (invalidates URL parameters)
     */
    public static void cleanToken() {
        final String url = Window.Location.createUrlBuilder()
                .removeParameter(KapuaCloudConsole.PARAMETER_ACCESS_TOKEN)
                .removeParameter(KapuaCloudConsole.PARAMETER_ID_TOKEN)
                .removeParameter(KapuaCloudConsole.PARAMETER_ERROR)
                .removeParameter(KapuaCloudConsole.PARAMETER_ERROR_DESC)
                .buildString();

        // side note: Window.Location.assign and reload methods force the page to reload
        // see also here: https://stackoverflow.com/questions/20036194/remove-url-parameters-in-gwt
        Window.Location.assign(url);
    }
}
