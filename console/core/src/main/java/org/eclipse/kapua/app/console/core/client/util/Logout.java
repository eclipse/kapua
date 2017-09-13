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
package org.eclipse.kapua.app.console.core.client.util;

import com.google.gwt.user.client.Window;

public final class Logout {

    public static final String PARAMETER_ACCESS_TOKEN = "access_token";

    private Logout() {
    }

    /**
     * Clear GWT state and remove SSO token when set
     */
    public static void logout() {
        final String url = Window.Location.createUrlBuilder()
                .removeParameter(PARAMETER_ACCESS_TOKEN)
                .buildString();
        Window.Location.assign(url);
    }
}
