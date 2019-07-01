/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
     * Clear GWT state and remove SSO token when set
     */
    public static void cleanToken() {
        final String url = Window.Location.createUrlBuilder()
                .removeParameter(KapuaCloudConsole.PARAMETER_ACCESS_TOKEN)
                .removeParameter(KapuaCloudConsole.PARAMETER_ERROR)
                .removeParameter(KapuaCloudConsole.PARAMETER_ERROR_DESC)
                .buildString();
        Window.Location.assign(url);
    }
}
